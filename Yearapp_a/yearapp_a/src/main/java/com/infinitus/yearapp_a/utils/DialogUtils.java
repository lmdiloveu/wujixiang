package com.infinitus.yearapp_a.utils;

import com.infinitus.yearapp_a.R;
import com.infinitus.yearapp_a.view.LoadingDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class DialogUtils {
	// private static ProgressDialog __pd = null;
	private static LoadingDialog loadingDialog = null;

	/** 5秒超时时间 */
	// private static final long FIVE_SECOND = 5000;

	// private static TimeoutProgressDialog __pd = null;

	// public static void showLoading(Context context, String title, String
	// message) {
	// __pd = TimeoutProgressDialog.createProgressDialog(context, FIVE_SECOND,
	// new OnTimeOutListener() {
	//
	// @Override
	// public void onTimeOut(Context context, ProgressDialog dialog) {
	// DialogUtils.showShortToast(context,
	// context.getResources().getString(R.string.message_prompt_connecting_timeout));
	// }
	// });
	// __pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	// __pd.setCanceledOnTouchOutside(false);
	// __pd.setCancelable(false);
	// __pd.setTitle(title);
	// __pd.setMessage(message);
	// __pd.show();
	// }

	public static void showLoading(Context context) {
		if (loadingDialog != null && loadingDialog.isShowing())
			return;

		loadingDialog = new LoadingDialog(context);
		loadingDialog.show();
	}

	public static void showLoading(Context context, String message) {
		showLoading(context);
	}

	public static void showLoading(Context context, int messageId) {
		showLoading(context);
	}

	public static void hideLoading() {
		if (loadingDialog != null) {
			loadingDialog.dismiss();
			loadingDialog = null;
		}
	}

	// public static void showLoading(Context context, String title, String
	// message) {
	// if (__pd != null)
	// return ;
	//
	// __pd = new ProgressDialog(context);
	// __pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	// __pd.setCanceledOnTouchOutside(false);
	// __pd.setCancelable(false);
	// __pd.setTitle(title);
	// __pd.setMessage(message);
	// __pd.show();
	// }
	//
	// public static void showLoading(Context context, String message) {
	// showLoading(context, null, message);
	// }
	//
	// public static void showLoading(Context context, int messageId) {
	// showLoading(context, null, context.getString(messageId));
	// }
	//
	// public static void hideLoading() {
	// if (__pd != null) {
	// __pd.dismiss();
	// __pd = null;
	// }
	// }

	public static void showShortToast(Context context, String message) {
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.show();
	}

	public static void showShortToast(Context context, int resid) {
		Toast toast = Toast.makeText(context, resid, Toast.LENGTH_SHORT);
		toast.show();
	}

	/**
	 * 
	 * @param context
	 * @param content
	 *            内容
	 * @param colorResid
	 *            颜色Rid，若不修改传0
	 * @param sureString
	 *            确定按钮的内容
	 * @param cancelString
	 *            取消按钮的内容
	 * @param onClickListener
	 *            确定的响应事件
	 */
	public static void showSureDialog(Context context, String content,
			int colorResid, String sureString, String cancelString,
			final OnClickListener onClickListener) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		final AlertDialog tempDialog = dialog.create();

		View layout = LayoutInflater.from(context).inflate(
				R.layout.dialog_optometry_delete, null);
		TextView tv_content = (TextView) layout.findViewById(R.id.tv_content);
		tv_content.setText(content);

		tempDialog.setView(layout);

		// 确定按钮
		Button btn_sure = (Button) layout.findViewById(R.id.btn_sure);
		btn_sure.setText(sureString);
		if (colorResid != 0) {
			btn_sure.setBackgroundResource(colorResid);
		}
		btn_sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onClickListener != null) {
					onClickListener.onClick(v);
				}
				tempDialog.dismiss();
			}
		});
		// 取消按钮
		Button btn_cancle = (Button) layout.findViewById(R.id.btn_cancel);
		btn_cancle.setText(cancelString);
		btn_cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tempDialog.dismiss();
			}
		});
		tempDialog.setCancelable(false);
		tempDialog.show();
	}

	/**
	 * 
	 * @param context
	 * @param content
	 *            内容
	 * @param colorResid
	 *            颜色Rid，若不修改传0
	 * @param onClickListener
	 *            确定的响应事件
	 */
	public static void showSureDialog(Context context, String content,
			int colorResid, final OnClickListener onClickListener) {
		showSureDialog(context, content, colorResid, "确  定", "取  消",
				onClickListener);
	}

	/**
	 * 
	 * @param context
	 * @param contentResid
	 *            内容id
	 * @param colorResid
	 *            颜色Rid，若不修改传0
	 * @param onClickListener
	 *            确定的响应事件
	 */
	public static void showSureDialog(Context context, int contentResid,
			int colorResid, final OnClickListener onClickListener) {
		showSureDialog(context, context.getString(contentResid), colorResid,
				onClickListener);
	}

	public static void showTwoButtonDialog(Context context, String btn1, String btn2, String message, OnClickListener listener1, OnClickListener listener2) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.MyDialog);
		final AlertDialog tempDialog = dialog.create();
		View layout = LayoutInflater.from(context).inflate(
				R.layout.dialog_two_button, null);

		TextView tv_message = (TextView) layout.findViewById(R.id.tv_dialog_message);
		TextView tv_btn1 = (TextView) layout.findViewById(R.id.tv_dialog_btn1);
		TextView tv_btn2 = (TextView) layout.findViewById(R.id.tv_dialog_btn2);

		tv_message.setText(message == null ? "" : message);
		tv_btn1.setText(btn1 == null ? "" : btn1);
		tv_btn1.setText(btn2 == null ? "" : btn2);

		tv_btn1.setOnClickListener(listener1);
		tv_btn2.setOnClickListener(listener2);

		tempDialog.setView(layout);
		tempDialog.setCancelable(false);
		tempDialog.show();
	}
}
