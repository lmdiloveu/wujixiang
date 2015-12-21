package com.infinitus.yearapp_a.activity;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.infinitus.yearapp_a.Application;
import com.infinitus.yearapp_a.R;
import com.infinitus.yearapp_a.base.auth.TokenManager;

public abstract class BaseActivity extends FragmentActivity {

	protected LayoutInflater mInflater;
	protected Button bar_left_btn;
	protected ImageButton bar_left_img_btn;
	protected TextView bar_title;
	protected Button bar_right_btn;
	protected ImageButton bar_right_img_btn;
	protected View customView;
	protected ActionBar bar;
	protected AQuery aq;
	protected View icl_bar;
	protected View v_shelter;

	private boolean isShowBar = true;

	/** ContentView */
	private View inflate;
	private FrameLayout frame_content;
	private LinearLayout ll_loading_fail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Application.getInstance().add(this);// 添加当前activity到管理集合中
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		mInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		aq = new AQuery(this);
		aq.auth(TokenManager.instance().getTokenHandle());

		super.setContentView(R.layout.fragment_base);
		initBar();

		frame_content = (FrameLayout) findViewById(R.id.frameLayout);
		v_shelter = findViewById(R.id.v_shelter);
		ll_loading_fail = (LinearLayout) findViewById(R.id.ll_loading_fail);
		ll_loading_fail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseActivity.this.loadData();
			}
		});
	}

	@Override
	public void setContentView(int layoutResID) {
		if (inflate != null) {
			frame_content.removeView(inflate);
		}

		inflate = createViewFromResource(layoutResID);
		inflate.setVisibility(View.GONE);
		frame_content.addView(inflate);

		initLayout(inflate);
		loadData();
	}

	/**
	 * 初始化布局
	 * 
	 * @param view
	 */
	protected abstract void initLayout(View view);

	/**
	 * 加载数据
	 */
	protected abstract void loadData();

	protected void setNetworkResult(boolean ok) {
		if (ok) {
			ll_loading_fail.setVisibility(View.GONE);
			inflate.setVisibility(View.VISIBLE);
		} else {
			ll_loading_fail.setVisibility(View.VISIBLE);
			inflate.setVisibility(View.GONE);
		}
	}

	protected void setShowBar(Boolean isShow) {
		isShowBar = isShow;
	}

	private void initBar() {
		icl_bar = findViewById(R.id.nav_bar);
		if (isShowBar) {
			icl_bar.setVisibility(View.VISIBLE);

			TypedArray actionbarSizeTypedArray = this
					.obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
			int h = 0;
			if (actionbarSizeTypedArray != null) {
				h = (int) actionbarSizeTypedArray.getDimension(0, 0);
			}
			if (h == 0) {
				h = getResources().getDimensionPixelSize(R.dimen.y180);
			}
			

			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) icl_bar
					.getLayoutParams();
			linearParams.height = h;
			icl_bar.setLayoutParams(linearParams);
		} else {
			icl_bar.setVisibility(View.GONE);
			return;
		}

		bar_left_btn = (Button) findViewById(R.id.bar_left_button);
		bar_left_img_btn = (ImageButton) findViewById(R.id.bar_left_img_button);
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_right_btn = (Button) findViewById(R.id.bar_right_button);
		bar_right_img_btn = (ImageButton) findViewById(R.id.bar_right_img_button);
		View.OnClickListener lcl = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onLeftButtonClick();
			}
		};
		View.OnClickListener rcl = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onRightButtonClick(v);
			}
		};
		bar_left_btn.setOnClickListener(lcl);
		bar_left_img_btn.setOnClickListener(lcl);
		bar_right_btn.setOnClickListener(rcl);
		bar_right_img_btn.setOnClickListener(rcl);
	}

	protected View createViewFromResource(int resource) {
		return mInflater.inflate(resource, null, false);
	}

	protected void setNavTitle(String title) {
		if (bar_title != null) {
			bar_title.setText(title);
		}
	}

	protected void setNavTitle(int resid) {
		if (bar_title != null) {
			bar_title.setText(resid);
		}
	}

	protected void setLeftButtonText(String text) {
		if (bar_left_btn != null) {
			bar_left_btn.setText(text);
			bar_left_btn.setVisibility(View.VISIBLE);
		}
	}

	protected void setLeftButtonText(int resid) {
		if (bar_left_btn != null) {
			bar_left_btn.setText(resid);
			bar_left_btn.setVisibility(View.VISIBLE);
		}
	}

	protected void setRightButtonText(String text) {
		if (bar_right_btn != null) {
			bar_right_btn.setText(text);
			bar_right_btn.setVisibility(View.VISIBLE);
		}
	}

	protected void setRightButtonText(int resid) {
		if (bar_right_btn != null) {
			bar_right_btn.setText(resid);
			bar_right_btn.setVisibility(View.VISIBLE);
		}
	}

	protected void setLeftButtonDrawable(int resid) {
		if (bar_left_img_btn != null) {
			bar_left_img_btn.setImageResource(resid);
			bar_left_img_btn.setVisibility(View.VISIBLE);
		}
	}

	protected void setRightButtonDrawable(int resid) {
		if (bar_right_img_btn != null) {
			bar_right_img_btn.setImageResource(resid);
			bar_right_img_btn.setVisibility(View.VISIBLE);
		}
	}

	protected void onLeftButtonClick() {
		this.finishActivity();
	}

	protected void onRightButtonClick(View v) {
		// 子类实现
	}

	/**
	 * 移除当前管理集合中当前的activity，并且销毁
	 */
	protected void finishActivity() {
		Application.getInstance().remove(this);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		this.finishActivity();
	}

	@Override
	protected void onDestroy() {
		Application.getInstance().remove(this);
		super.onDestroy();
	}

}
