package com.infinitus.yearapp_a.activity;

import com.infinitus.yearapp_a.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

/**
 * 设置页面
 * @author Administrator
 *
 */
public class SettingActivity extends BaseActivity implements OnClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setLeftButtonDrawable(R.drawable.icon_back);
		setContentView(R.layout.activity_setting);
	}

	@Override
	protected void initLayout(View view) {
		Button btn_select_dept = aq.id(R.id.btn_select_dept).getButton();
		btn_select_dept.setOnClickListener(this);
		Button btn_use_guide = aq.id(R.id.btn_use_guide).getButton();
		btn_use_guide.setOnClickListener(this);
		Button btn_update = aq.id(R.id.btn_update).getButton();
		btn_update.setOnClickListener(this);

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
		case R.id.btn_select_dept:
			intent = new Intent(this, SelectDepartmentActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_use_guide:
			intent = new Intent(this, UseGuideActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_update:
			intent = new Intent(this, UpdateActivity.class);
			startActivity(intent);
			break;
		}
	}

}
