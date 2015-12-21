package com.infinitus.yearapp_a.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.infinitus.yearapp_a.R;
import com.infinitus.yearapp_a.utils.Constants;
import com.infinitus.yearapp_a.utils.Prefs;
import com.infinitus.yearapp_a.utils.imageloader.ImageLoader;
import com.infinitus.yearapp_a.view.ZoomImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 会议相机，为照片添加相框
 *
 * @author Administrator
 */
public class PhotoAddFrameActivity extends BaseActivity {
    //相框小图的容器
    private LinearLayout ll_frames;
    //原图，可缩放拖动
    private ZoomImageView iv_src;
    //相框
    private ImageView iv_frmae;

    private Uri sourceUri;// content://
    private boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sourceUri = getIntent().getParcelableExtra(Constants.EXTRA_KEY_IMAGE_URI);
        isEdit = getIntent().getBooleanExtra(Constants.EXTRA_KEY_PHOTO_EDIT, true);
        super.onCreate(savedInstanceState);
        super.setLeftButtonText(R.string.cancel);
        if (isEdit)//如果是合成图片的，则显示完成按钮
            super.setRightButtonText(R.string.complete);
        setContentView(R.layout.activity_add_frame);

    }

    @Override
    protected void initLayout(View view) {
        ll_frames = (LinearLayout) aq.id(R.id.ll_frames).getView();
        iv_frmae = aq.id(R.id.iv_frame).getImageView();//初始化时，默认使用可用的第一个相框

        iv_src = (ZoomImageView) aq.id(R.id.iv_src).getView();
        iv_src.setImageURI(sourceUri);
        initFrames();
    }

    @Override
    protected void loadData() {
        setNetworkResult(true);
    }

    @Override
    protected void onRightButtonClick(View v) {
        new SaveImageTask().execute();
    }

    /**
     * 初始化可用相框的预览小图
     */
    private void initFrames() {
        File unlockFramesDir = new File(Prefs.getUnlockFramesPath());
        LayoutParams layoutParams = new LayoutParams(getResources().getDimensionPixelSize(R.dimen.frame_preview_width), getResources().getDimensionPixelSize(R.dimen.frame_preview_height));
        layoutParams.leftMargin = getResources().getDimensionPixelSize(R.dimen.frame_preview_margin);
        layoutParams.rightMargin = getResources().getDimensionPixelSize(R.dimen.frame_preview_margin);
        if (!unlockFramesDir.exists() || unlockFramesDir.list().length == 0) {//没有已解锁可用的，直接使用默认的
            unlockFramesDir.mkdirs();
            for (int i = 0; i < 6; i++) {//TODO 此处要使用默认的相框和对应的处理
                ImageView image = new ImageView(this);
                image.setImageResource(R.drawable.item_frame);
                image.setLayoutParams(layoutParams);
            final int tmp = i;
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(PhotoAddFrameActivity.this, "点击了第" + (tmp + 1) + "个相框", Toast.LENGTH_SHORT).show();
                }
            });
                ll_frames.addView(image);
            }
        } else {
            File[] frames = unlockFramesDir.listFiles();
            for (final File frame : frames) {
                ImageView image = new ImageView(this);
                image.setLayoutParams(new LayoutParams(getResources().getDimensionPixelSize(R.dimen.frame_preview_width), getResources().getDimensionPixelSize(R.dimen.frame_preview_height)));
                ll_frames.addView(image);
                ImageLoader.getInstance().loadImage(frame.getAbsolutePath(), image, false);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ImageLoader.getInstance().loadImage(frame.getAbsolutePath(), iv_frmae, false);
                    }
                });
            }
        }
    }

    private class SaveImageTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Bitmap sourceBitmap = clipBitmap();
            Bitmap montageBitmap = montageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.frame_default), sourceBitmap, 0, 0);//TODO
            sourceBitmap.recycle();
            if (saveBitmap(montageBitmap)) {
                montageBitmap.recycle();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            AlertDialog.Builder builder = new AlertDialog.Builder(PhotoAddFrameActivity.this);
            LayoutInflater inflater = LayoutInflater.from(PhotoAddFrameActivity.this);
            View dialogRoot = inflater.inflate(R.layout.dialog_two_button, null);
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.setContentView(dialogRoot);
            TextView btn1 = (TextView) dialogRoot.findViewById(R.id.tv_dialog_btn1);
            TextView btn2 = (TextView) dialogRoot.findViewById(R.id.tv_dialog_btn2);
            TextView tv_message = (TextView) dialogRoot.findViewById(R.id.tv_dialog_message);
            btn1.setText("继续拍照");
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openMeetingCamera();
                }
            });
            btn2.setText("返回首页");
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            tv_message.setText("制作完成");
            dialog.setCancelable(false);
        }

        private boolean saveBitmap(Bitmap bitmap) {
            Date date = new Date(System.currentTimeMillis());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
            String fileName = fmt.format(date) + ".jpg";
            File dir = new File(Prefs.getDCIMPath());
            if (!dir.exists()) {
                //noinspection ResultOfMethodCallIgnored
                dir.mkdirs();
            }
            File f = new File(dir, fileName);
            FileOutputStream out = null;
            try {
                if (!f.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    f.createNewFile();
                }
                out = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return true;
        }

        /**
         * 将相框与 原相片合成一张新的
         *
         * @param frame 相框
         * @param src   内容
         * @param x     在画布上偏移的X量
         * @param y     在画布上偏移的Y量
         * @return 合成后的照片
         */
        private Bitmap montageBitmap(Bitmap frame, Bitmap src, int x, int y) {
            int w = src.getWidth();
            int h = src.getHeight();

            Bitmap newBM = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newBM);
            canvas.drawBitmap(src, x, y, null);
            src.recycle();
            Bitmap sizeFrame = Bitmap.createScaledBitmap(frame, w, h, true);
            canvas.drawBitmap(sizeFrame, x, y, null);
            sizeFrame.recycle();
            return newBM;

//		Bitmap newBitmap = src;
//		if(!newBitmap.isMutable()) {
//			newBitmap = src.copy(Bitmap.Config.RGB_565, true);
//		}
//		Canvas canvas = new Canvas(newBitmap);
//		canvas.drawBitmap(frame, x, y, null);
//		canvas.save(Canvas.ALL_SAVE_FLAG);
//		canvas.restore();
//		return newBitmap;
        }

        /**
         * 裁剪出需要的部分
         */
        private Bitmap clipBitmap() {
            return iv_src.clip();
        }
    }

    /*请求系统照相机*/
    private static final int REQUEST_CAPTURE = 0x1;
    private Uri imageUri;

    private void openMeetingCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File dir = new File(Prefs.getTempImagePath());
        if (!dir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
        }
        String fileName = UUID.randomUUID().toString() + ".jpg";
        File file = new File(dir, fileName);
        if (!file.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imageUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAPTURE && resultCode == RESULT_OK) {
            Intent intent = new Intent(this, MeetingCameraActivity.class);
            intent.putExtra(Constants.EXTRA_KEY_IMAGE_URI, imageUri);
            startActivity(intent);
            finish();
        }
    }

}
