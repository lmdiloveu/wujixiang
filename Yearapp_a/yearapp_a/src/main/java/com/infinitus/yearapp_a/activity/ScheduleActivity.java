package com.infinitus.yearapp_a.activity;

import com.infinitus.yearapp_a.R;
import com.infinitus.yearapp_a.utils.DialogUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

/**
 * 会议日程页面
 * @author Administrator
 *
 */
public class ScheduleActivity extends BaseActivity  {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setLeftButtonDrawable(R.drawable.icon_back);
		super.setRightButtonDrawable(R.drawable.icon_setting);
		setContentView(R.layout.activity_schedule);
	}

	@Override
	protected void initLayout(View view) {
		ImageView iv_logo_bg = aq.id(R.id.iv_logo_background).getImageView();
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.logo_bg_in);
		iv_logo_bg.startAnimation(animation);
	}

	@Override
	protected void loadData() {
		setNetworkResult(true);
	}

	@Override
	protected void onRightButtonClick(View v) {
		Intent intent = new Intent(this, SettingActivity.class);
		startActivity(intent);
	}

}
