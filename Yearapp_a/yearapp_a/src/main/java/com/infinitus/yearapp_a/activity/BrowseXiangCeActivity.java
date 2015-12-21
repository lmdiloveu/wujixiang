package com.infinitus.yearapp_a.activity;

import com.infinitus.yearapp_a.R;
import com.infinitus.yearapp_a.module.ImageGridActivity;
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
 * 点击【浏览相册】后跳转到的页面
 * 【浏览相册】主页面
 * @author Administrator
 *
 */
public class BrowseXiangCeActivity extends BaseActivity implements OnClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setLeftButtonDrawable(R.drawable.icon_back);
		super.setRightButtonDrawable(R.drawable.icon_setting);
		setContentView(R.layout.activity_browse_xiangce);
	}

	@Override
	protected void initLayout(View view) {
		Button btn_dianzi_xiangce = aq.id(R.id.btn_dianzi_xiangce).getButton();
		btn_dianzi_xiangce.setOnClickListener(this);
		Button btn_browse_image = aq.id(R.id.btn_browse_image).getButton();
		btn_browse_image.setOnClickListener(this);

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
		case R.id.btn_dianzi_xiangce:
			DialogUtils.showShortToast(this, "打开电子相册");
			intent = new Intent(this, DianziXiangceGridViewActivity.class);
			intent.putExtra("isComeFromBrowseXiangceActivity", true);
			startActivity(intent);
			break;
		case R.id.btn_browse_image:
			DialogUtils.showShortToast(this, "打开浏览图片");
			intent = new Intent(this, BrowseXiangceOneActivity.class);
			intent.putExtra("isComeFromBrowseXiangceActivity", true);
			startActivity(intent);
			break;
		}
	}

}
