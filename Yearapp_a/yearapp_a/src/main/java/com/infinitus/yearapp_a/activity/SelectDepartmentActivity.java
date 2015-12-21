package com.infinitus.yearapp_a.activity;

import com.infinitus.yearapp_a.R;
import com.infinitus.yearapp_a.base.util.PrefsUtil;
import com.infinitus.yearapp_a.utils.Constants;
import com.infinitus.yearapp_a.view.SelectDepartmentPopupWindow;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

/**
 * 选择部门
 * @author Administrator
 *
 */
public class SelectDepartmentActivity extends BaseActivity implements OnClickListener {
	private PrefsUtil prefsUtil;
	private SelectDepartmentPopupWindow pop;
	private Button btn_select_dept;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.setShowBar(false);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_dept);
		
		prefsUtil = new PrefsUtil(getApplicationContext(), Constants.PREF_NAME_SELECT_DEPT);
	}
	
	@Override
	protected void initLayout(View view) {
		btn_select_dept = aq.id(R.id.btn_select_dept).getButton();
		btn_select_dept.setOnClickListener(this);
		Button btn_select_sure = aq.id(R.id.btn_select_sure).getButton();
		btn_select_sure.setOnClickListener(this);

		ImageView iv_logo_bg = aq.id(R.id.iv_logo_background).getImageView();
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.logo_bg_in);
		iv_logo_bg.startAnimation(animation);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		String dept_status = prefsUtil.getString(Constants.KEY_DEPT, Constants.VALUE_DEFAULT_STRING);
		switch (dept_status) {
		case Constants.VALUE_BEIJING:
			//TODO
			break;
		case Constants.VALUE_GUANGZHOU:
			//TODO
			break;
		default://R.string.selelct_your_department
			//TODO
			break;
		}
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
			if(pop == null) {
				pop = new SelectDepartmentPopupWindow(this, new OnClickListener() {
					@Override
					public void onClick(View v) {
						switch (v.getId()) {
							case R.id.tv_beijing:
								prefsUtil.putString(Constants.KEY_DEPT, Constants.VALUE_BEIJING);
								pop.dismiss();
								btn_select_dept.setText("北京部门");
								break;
							case R.id.tv_guangzhou:
								prefsUtil.putString(Constants.KEY_DEPT, Constants.VALUE_GUANGZHOU);
								pop.dismiss();
								btn_select_dept.setText("广州部门");
								break;
							default:
								break;
						}
					}
				});
			}
			pop.showAtLocation(findViewById(R.id.root_select_dept), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.btn_select_sure:
			intent = new Intent(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		}
	}

}
