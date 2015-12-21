package com.infinitus.yearapp_a.activity;

import com.infinitus.yearapp_a.R;
import com.infinitus.yearapp_a.utils.DialogUtils;
import com.zxing.activity.CaptureActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

/**
 * 点击首页扫一扫后跳转到的页面
 * 扫一扫主页面
 * @author Administrator
 *
 */
public class ScanMainActivity extends BaseActivity implements OnClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setLeftButtonDrawable(R.drawable.icon_back);
		super.setRightButtonDrawable(R.drawable.icon_setting);
		setContentView(R.layout.activity_scan_main);
	}

	@Override
	protected void initLayout(View view) {
		Button btn_open_data = aq.id(R.id.btn_open_data).getButton();
		btn_open_data.setOnClickListener(this);
		Button btn_open_mould = aq.id(R.id.btn_open_mould).getButton();
		btn_open_mould.setOnClickListener(this);

		ImageView iv_logo_bg = aq.id(R.id.iv_logo_background).getImageView();
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.logo_bg_in);
		iv_logo_bg.startAnimation(animation);
	}

	@Override
	protected void loadData() {
		setNetworkResult(true);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.btn_open_data:
			DialogUtils.showShortToast(this, "开启会议资料");
			break;
		case R.id.btn_open_mould:
			DialogUtils.showShortToast(this, "开启相机模板");
			intent = new Intent(this, CaptureActivity.class);
			startActivity(intent);
			break;
		}
	}

}
