package com.infinitus.yearapp_a.activity;

import com.infinitus.yearapp_a.R;
import com.infinitus.yearapp_a.utils.Constants;
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
 * 点击首页【电子相册】后跳转到的页面
 * 【电子相册】主页面
 * @author Administrator
 *
 */
public class XiangceMainActivity extends BaseActivity implements OnClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setLeftButtonDrawable(R.drawable.icon_back2);
//		super.setRightButtonDrawable(R.drawable.icon_setting);
		setContentView(R.layout.activity_xiangce_main);
	}

	@Override
	protected void initLayout(View view) {
		Button btn_make_xiangce = aq.id(R.id.btn_make_xiangce).getButton();
		btn_make_xiangce.setOnClickListener(this);
		Button btn_browse_xiangce = aq.id(R.id.btn_browse_xiangce).getButton();
		btn_browse_xiangce.setOnClickListener(this);

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
		case R.id.btn_make_xiangce:
			DialogUtils.showShortToast(this, "打开制作相册");
//			intent = new Intent(this, ImageGridActivity.class);
			intent = new Intent(this, XiangceMainOneActivity.class);
			startActivityForResult(intent, Constants.MAKE_XIANG_CE);
			break;
		case R.id.btn_browse_xiangce:
			DialogUtils.showShortToast(this, "打开浏览相册");
			intent = new Intent(this, BrowseXiangCeActivity.class);
			startActivity(intent);
			break;
		}
	}

}
