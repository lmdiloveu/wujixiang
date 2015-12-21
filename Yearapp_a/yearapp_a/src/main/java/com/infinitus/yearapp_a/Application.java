package com.infinitus.yearapp_a;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import com.androidquery.AQuery;
import com.androidquery.util.AQUtility;
import com.infinitus.yearapp_a.activity.MainActivity;
import com.infinitus.yearapp_a.utils.CrashHandler;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.File;
import java.util.ArrayList;


public class Application extends android.app.Application {
	private ArrayList<Activity> activities = new ArrayList<Activity>();// 用于管理所有activity

	public static String TAG = "MyApplication";

	private static Application mInstance = null;

	private DisplayImageOptions options;
	
	public static IWXAPI api;
	
	public static AQuery aq;
	
	/**
	 * 当前设备是否支持闪光灯
	 */
	private static boolean flashlightAvailable;

	public DisplayImageOptions getOptions() {
		return options;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		initAQ();
		initCrashHandler();
//		initWXLogin();
		// tryStartService(this);
		// 定位相关
		mInstance = this;
//		mLocationClient = new LocationClient(this.getApplicationContext());
//		mLocationListener = new MyLocationListenner();
//		mLocationClient.registerLocationListener(mLocationListener);
		
		initImageLoader();
		
		flashlightAvailable = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
	}
	
	private void initWXLogin() {
		api = WXAPIFactory.createWXAPI(this, net.sourceforge.simcpux.Constants.APP_ID, true);
		api.registerApp(net.sourceforge.simcpux.Constants.APP_ID);
	}

	/**初始化自定义奔溃异常处理器*/
	public void initCrashHandler() {
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(this);
	}

	public void initAQ() {
		aq = new AQuery(this);
		AQUtility.setContext(this);
//		AQUtility.setDebug(true);
//		 AQUtility.setExceptionHandler(new UncaughtExceptionHandler() {
//		 public void uncaughtException(Thread thread, Throwable ex) {
//		 // TODO Auto-generated method stub
//		 // TODO save to file
//		 }
//		 });
	}

	private void initImageLoader() {
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.details_defualt_big) // 设置图片下载期间显示的图�?
				.showImageForEmptyUri(R.drawable.details_defualt_big) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.products_def_broken) // 设置图片加载或解码过程中发生错误显示的图�?
				.bitmapConfig(Bitmap.Config.ARGB_8888)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED) 
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存�?
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图�?
				.build();
				
		File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽  
				.threadPoolSize(3)//线程池内加载的数量  
				.threadPriority(Thread.NORM_PRIORITY - 2)  
				.denyCacheImageMultipleSizesInMemory()  
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现  
				.memoryCacheSize(2 * 1024 * 1024)    
				.discCacheSize(50 * 1024 * 1024)    
				.discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密  
				.tasksProcessingOrder(QueueProcessingType.LIFO)  
				.discCacheFileCount(100) //缓存的文件数量  
				.discCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径  
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) 
				.imageDownloader(new BaseImageDownloader(getApplicationContext(), 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间  
				.writeDebugLogs() // Remove for release app  
				.build();//开始构建  
		ImageLoader.getInstance().init(config);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();

		AQUtility.cleanCacheAsync(this);
	}

	// private void tryStartService(Context context) {
	// boolean isServiceRunning = false;
	// String serviceName = CoreService.class.getName();
	// // �?��Service状�?
	// ActivityManager manager = (ActivityManager) context
	// .getSystemService(Context.ACTIVITY_SERVICE);
	// for (RunningServiceInfo service : manager
	// .getRunningServices(Integer.MAX_VALUE)) {
	// if (serviceName.equals(service.service.getClassName())) {
	// isServiceRunning = true;
	// }
	// }
	//
	// if (!isServiceRunning) {
	// Intent i = new Intent(context, CoreService.class);
	// i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// context.startService(i);
	// // context.startService(new Intent("com.yx.service"));
	// }
	// }

	public static Application getInstance() {
		return mInstance;
	}


	public void add(Activity activity) {
		activities.add(activity);
	}

	public void remove(Activity activity) {
		activities.remove(activity);
	}
	
	/**
	 * 清空关闭所有activity，并重启程序
	 */
	public void ExitApp() {
		for (Activity activity : activities) {
			if (activity != null) {
				activity.finish();
			}
		}
		activities.clear();
		// 退出程序
		// System.exit(0);
		// 重启程序
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		getApplicationContext().startActivity(intent);
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	/**
	 * 系统闪光灯是否可用
	 * @return
	 */
	public static boolean isFlashlightAvailable() {
		return flashlightAvailable;
	}

}
