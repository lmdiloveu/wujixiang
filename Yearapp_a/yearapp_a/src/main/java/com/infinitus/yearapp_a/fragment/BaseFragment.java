package com.infinitus.yearapp_a.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.infinitus.yearapp_a.R;

/**
 * 
 * @Description fragment基类， 可实现首次加载数据失败后显示加载失败图标， 点击后重新加载数据
 * @use 子类需实现initView(),initData();
 *      notice:实现的initData()方法需调用setNetworkResult(boolean), true为加载成功，false为加载失败
 * @use 默认显示actionbar，若不显示，重新实现setShowBar()方法，设置为false
 * @author xuzh
 *
 */
public abstract class BaseFragment extends Fragment {
	/** 子类的布局view */
	private View view;
	/** 绑定的Activity */
	protected Activity ct;
	/** 头部左边按钮 */
	protected Button bar_left_btn;
	/** 头部右边按钮 */
	private Button bar_right_btn;
	/** 头部左边图片按钮 */
	private ImageButton bar_left_img_btn;
	/** 头部中间标题 */
	protected TextView bar_title;
	/** 头部右边图片按钮 */
	protected ImageButton bar_right_img_btn;
	/** 网络框架的对象 */
	protected AQuery aQuery;
	/** Fragment当前状态是否可见 */
    protected boolean isVisible;

	/** ContentView */
	private View inflate;
	private FrameLayout frame_content;
	private LinearLayout ll_loading_fail;
	
	private boolean isShowBar;
	private boolean isLoadData;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ct = getActivity();
		aQuery = new AQuery(ct);
		isShowBar = setShowBar();
		isLoadData = loadData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_base, null);
			aQuery = new AQuery(view);
			
			if (isShowBar) {
				initBar(view);
			}

			frame_content = (FrameLayout) view.findViewById(R.id.frameLayout);
			ll_loading_fail = (LinearLayout) view
					.findViewById(R.id.ll_loading_fail);
			ll_loading_fail.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					BaseFragment.this.initData();
				}
			});

			setContentView(inflater);
		}
		return view;
	}
	
	private void initBar(View view){
		View icl_bar = view.findViewById(R.id.nav_bar);
		if (isShowBar) {
			icl_bar.setVisibility(View.VISIBLE);

			TypedArray actionbarSizeTypedArray = ct.obtainStyledAttributes(new int[] { android.R.attr.actionBarSize }); 
			int h = (int)actionbarSizeTypedArray.getDimension(0, 0);
			
			LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) icl_bar.getLayoutParams();
			linearParams.height = h;
			icl_bar.setLayoutParams(linearParams);
		}else {
			icl_bar.setVisibility(View.GONE);
			return ;
		}
		
		bar_left_btn = (Button) view
				.findViewById(R.id.bar_left_button);
		bar_left_img_btn = (ImageButton) view
				.findViewById(R.id.bar_left_img_button);
		bar_title = (TextView) view.findViewById(R.id.bar_title);
		bar_right_btn = (Button) view
				.findViewById(R.id.bar_right_button);
		bar_right_img_btn = (ImageButton) view
				.findViewById(R.id.bar_right_img_button);
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

	public void setContentView(LayoutInflater inflater) {
		if (inflate != null) {
			frame_content.removeView(inflate);
		}

		inflate = initView(inflater);
		inflate.setVisibility(View.GONE);
		frame_content.addView(inflate);

		if (isLoadData) {
			initData();
		}
		
	}

	protected void setNetworkResult(boolean ok) {
		if (ok) {
			ll_loading_fail.setVisibility(View.GONE);
			inflate.setVisibility(View.VISIBLE);
		} else {
			ll_loading_fail.setVisibility(View.VISIBLE);
			inflate.setVisibility(View.GONE);
		}
	}
	
	protected boolean loadData() {
		return true;
	}

	public abstract View initView(LayoutInflater inflater);

	public abstract void initData();
	
	protected boolean setShowBar() {
		return true;
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

	protected void setRighttButtonText(int resid) {
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

	}

	protected void onRightButtonClick(View v) {
		// 子类实现
	}

	protected void HideKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager) v.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
		}
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);

		if (getUserVisibleHint()) {
			isVisible = true;
			onVisible();
		} else {
			isVisible = false;
			onInvisible();
		}
	}

	protected void onVisible() {
		lazyLoad();
	}

	protected void onInvisible() {

	}

	protected abstract void lazyLoad();

}

