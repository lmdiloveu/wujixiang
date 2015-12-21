package com.infinitus.yearapp_a.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.infinitus.yearapp_a.R;
/**
 * 选择部门
 * @author Administrator
 *
 */
public class SelectDepartmentPopupWindow extends PopupWindow {
	private View tv_beijing, tv_guangzhou;
	/**
	 * 主体布局
	 */
	private View mMenuView;
	
	/**
	 * 构造器
	 * @param activity 当前页面Activity实例
	 * @param listener 点击监听
	 */
	@SuppressLint("InflateParams") 
	public SelectDepartmentPopupWindow(Activity activity, OnClickListener listener) {
		super(activity);
		LayoutInflater inflater = LayoutInflater.from(activity);
		mMenuView = inflater.inflate(R.layout.widget_select_department, null);
		
		tv_beijing = mMenuView.findViewById(R.id.tv_beijing);
		tv_guangzhou = mMenuView.findViewById(R.id.tv_guangzhou);
		mMenuView.findViewById(R.id.tv_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		tv_beijing.setOnClickListener(listener);
		tv_guangzhou.setOnClickListener(listener);
		
		setContentView(mMenuView);
		setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setTouchable(true);
        
        setAnimationStyle(R.style.PopupAnimation);

        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        setBackgroundDrawable(dw);

        mMenuView.setOnTouchListener(new View.OnTouchListener() {//当点击超出此控件范围，关闭此控件
            @SuppressLint("ClickableViewAccessibility") @Override
            public boolean onTouch(View v, MotionEvent event) {
                int top = mMenuView.findViewById(R.id.rl_select_department).getTop();
				int left = mMenuView.findViewById(R.id.rl_select_department).getLeft();
				int right = mMenuView.findViewById(R.id.rl_select_department).getRight();
                int y = (int) event.getY();
				int x = (int) event.getX();
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(y > top || x < left || x > right)
                        dismiss();
                }
                return true;
            }
        });
	}

}
