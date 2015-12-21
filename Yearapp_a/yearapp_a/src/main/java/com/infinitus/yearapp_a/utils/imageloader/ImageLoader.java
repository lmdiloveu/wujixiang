package com.infinitus.yearapp_a.utils.imageloader;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import com.infinitus.yearapp_a.R;
import com.infinitus.yearapp_a.utils.imageloader.ImageSizeUtil.ImageSize;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

/**
 * @author pangjb
 * 
 */
public class ImageLoader {
	private static ImageLoader mInstance;
	private LruCache<String, Bitmap> mLruCache;
	private ExecutorService mThreadPool;
	private static final int DEAFULT_THREAD_COUNT = 5;
	private Type mType = Type.LIFO;
	private LinkedList<Runnable> mTaskQueue;
	private Thread mPoolThread;
	private Handler mPoolThreadHandler;
	private Handler mUIHandler;

	private Semaphore mSemaphorePoolThreadHandler = new Semaphore(0);
	private Semaphore mSemaphoreThreadPool;

	private boolean isDiskCacheEnable = true;

	private static final String TAG = "ImageLoader";

	public enum Type {
		FIFO, LIFO;
	}

	private ImageLoader(int threadCount, Type type) {
		init(threadCount, type);
	}

	private void init(int threadCount, Type type) {
		initBackThread();

		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheMemory = maxMemory / 8;
		mLruCache = new LruCache<String, Bitmap>(cacheMemory) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}

		};

		mThreadPool = Executors.newFixedThreadPool(threadCount);
		mTaskQueue = new LinkedList<Runnable>();
		mType = type;
		mSemaphoreThreadPool = new Semaphore(threadCount);
	}

	private void initBackThread() {
		mPoolThread = new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				mPoolThreadHandler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						mThreadPool.execute(getTask());
						try {
							mSemaphoreThreadPool.acquire();
						} catch (InterruptedException e) {
						}
					}
				};
				mSemaphorePoolThreadHandler.release();
				Looper.loop();
			};
		};

		mPoolThread.start();
	}

	public static ImageLoader getInstance() {
		if (mInstance == null) {
			synchronized (ImageLoader.class) {
				if (mInstance == null) {
					mInstance = new ImageLoader(DEAFULT_THREAD_COUNT, Type.LIFO);
				}
			}
		}
		return mInstance;
	}

	public static ImageLoader getInstance(int threadCount, Type type) {
		if (mInstance == null) {
			synchronized (ImageLoader.class) {
				if (mInstance == null) {
					mInstance = new ImageLoader(threadCount, type);
				}
			}
		}
		return mInstance;
	}

	public void loadImage(final String path, final ImageView imageView, final boolean isFromNet) {
		imageView.setTag(path);
		if (mUIHandler == null) {
			mUIHandler = new Handler() {
				public void handleMessage(Message msg) {
					ImgBeanHolder holder = (ImgBeanHolder) msg.obj;
					Bitmap bm = holder.bitmap;
					ImageView imageview = holder.imageView;
					String path = holder.path;
					// 将path与getTag存储路径进行比较
					if (imageview.getTag().toString().equals(path)) {
						imageview.setScaleType(ImageView.ScaleType.FIT_XY);
						if(bm == null){
							imageview.setBackgroundResource(R.drawable.products_def_broken);
						}else{
							imageview.setImageBitmap(bm);	
						}
					}
				};
			};
		}

		// 根据path在缓存中获取bitmap
		Bitmap bm = getBitmapFromLruCache(path);

		if (bm != null) {
			refreashBitmap(path, imageView, bm);
		} else {
			addTask(buildTask(path, imageView, isFromNet));
		}

	}

	/**
	 * 根据传入的参数，新建一个任务
	 * 
	 * @param path
	 * @param imageView
	 * @param isFromNet
	 * @return
	 */
	private Runnable buildTask(final String path, final ImageView imageView, final boolean isFromNet) {
		return new Runnable() {
			@Override
			public void run() {
				Bitmap bm = null;
				if (isFromNet) {
					File file = getDiskCacheDir(imageView.getContext(), md5(path));
					if (file.exists()) {
						bm = loadImageFromLocal(file.getAbsolutePath(), imageView);
					} else {
						if (isDiskCacheEnable) {
							boolean downloadState = DownloadImgUtils.downloadImgByUrl(path, file);
							if (downloadState) {
								bm = loadImageFromLocal(file.getAbsolutePath(), imageView);
							}
						} else
						{
							bm = DownloadImgUtils.downloadImgByUrl(path, imageView);
						}
					}
				} else {
					bm = loadImageFromLocal(path, imageView);
				}
				
				addBitmapToLruCache(path, bm);
				refreashBitmap(path, imageView, bm);
				mSemaphoreThreadPool.release();
			}

		};
	}

	private Bitmap loadImageFromLocal(final String path, final ImageView imageView) {
		Bitmap bm;
		ImageSize imageSize = ImageSizeUtil.getImageViewSize(imageView);
		bm = decodeSampledBitmapFromPath(path, imageSize.width, imageSize.height);
		return bm;
	}

	private Runnable getTask() {
		if (mType == Type.FIFO) {
			return mTaskQueue.removeFirst();
		} else if (mType == Type.LIFO) {
			return mTaskQueue.removeLast();
		}
		return null;
	}

	public String md5(String str) {
		byte[] digest = null;
		try {
			MessageDigest md = MessageDigest.getInstance("md5");
			digest = md.digest(str.getBytes());
			return bytes2hex02(digest);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String bytes2hex02(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		String tmp = null;
		for (byte b : bytes) {
			tmp = Integer.toHexString(0xFF & b);
			if (tmp.length() == 1)
			{
				tmp = "0" + tmp;
			}
			sb.append(tmp);
		}

		return sb.toString();

	}

	private void refreashBitmap(final String path, final ImageView imageView, Bitmap bm) {
		Message message = Message.obtain();
		ImgBeanHolder holder = new ImgBeanHolder();
		holder.bitmap = bm;
		holder.path = path;
		holder.imageView = imageView;
		message.obj = holder;
		mUIHandler.sendMessage(message);
	}

	protected void addBitmapToLruCache(String path, Bitmap bm) {
		if (getBitmapFromLruCache(path) == null) {
			if (bm != null)
				mLruCache.put(path, bm);
		}
	}

	protected Bitmap decodeSampledBitmapFromPath(String path, int width, int height) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		options.inSampleSize = ImageSizeUtil.caculateInSampleSize(options, width, height);

		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		return bitmap;
	}

	private synchronized void addTask(Runnable runnable) {
		mTaskQueue.add(runnable);
		// if(mPoolThreadHandler==null)wait();
		try {
			if (mPoolThreadHandler == null)
				mSemaphorePoolThreadHandler.acquire();
		} catch (InterruptedException e) {
		}
		mPoolThreadHandler.sendEmptyMessage(0x110);
	}

	public File getDiskCacheDir(Context context, String uniqueName) {
		String cachePath;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			cachePath = context.getExternalCacheDir().getPath();
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		return new File(cachePath + File.separator + uniqueName);
	}

	private Bitmap getBitmapFromLruCache(String key) {
		return mLruCache.get(key);
	}

	private class ImgBeanHolder {
		Bitmap bitmap;
		ImageView imageView;
		String path;
	}
}
