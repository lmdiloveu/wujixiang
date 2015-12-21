/*
 * Copyright (c) 2014, 青岛司通科技有限公司 All rights reserved.
 * File Name：LoadingDialog.java
 * Version：V1.0
 * Author：zhaokaiqiang
 * Date：2014-10-24
 */

package com.infinitus.yearapp_a.view;

import java.security.acl.Group;

import com.infinitus.yearapp_a.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


/**
 * 
 * @Description LoadingDialog
 * @author xuzh
 *
 */
public class LoadingDialog extends Dialog {
	private Context context;
	private static int loadingWidth = 0;
	private static int loadingHeigth= 0;

	public LoadingDialog(Context context) {
		super(context, R.style.dialog);
		this.context = context;
		
		setCanceledOnTouchOutside(false);
		this.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				LoadingDialog.this.dismiss();
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_dialog_loading);
		
		if (loadingWidth == 0) {
			Drawable drawable = context.getResources().getDrawable(R.drawable.loading_1);
			loadingWidth = drawable.getMinimumWidth();
			loadingHeigth = drawable.getMinimumHeight();
		}
		
		ProgressBar progress = (ProgressBar)findViewById(R.id.pb_loading);
		ViewGroup.LayoutParams params = progress.getLayoutParams();
		params.width = loadingWidth;
		params.height = loadingHeigth;
		progress.setLayoutParams(params);
	}
}