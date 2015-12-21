package com.infinitus.yearapp_a.activity;

import com.infinitus.yearapp_a.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * 使用手册页面
 * @author Administrator
 *
 */
public class UseGuideActivity extends BaseActivity  {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setLeftButtonDrawable(R.drawable.icon_back);
		setContentView(R.layout.activity_use_guide);
	}

	@Override
	protected void initLayout(View view) {
		
	}

	@Override
	protected void loadData() {
		setNetworkResult(true);
	}


}
