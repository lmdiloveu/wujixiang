package com.infinitus.yearapp_a.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.infinitus.yearapp_a.R;
import com.infinitus.yearapp_a.base.util.PrefsUtil;
import com.infinitus.yearapp_a.utils.Constants;
import com.infinitus.yearapp_a.utils.update.UpdateChecker;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 版本升级页面
 * @author Administrator
 *
 */
public class UpdateActivity extends BaseActivity implements OnClickListener {

	private Button btn_sure, btn_cancel;

	private TextView tv_new_version, tv_version_description;

	private PrefsUtil prefsUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setLeftButtonDrawable(R.drawable.icon_back);
		setContentView(R.layout.activity_update);

		prefsUtil = new PrefsUtil(getApplicationContext(), Constants.PREF_NAME_UPDATE);

		if(checkVersion()) {//有新版本
			String name = prefsUtil.getString(Constants.KEY_VERSION_NAME, Constants.VALUE_DEFAULT_STRING);
			String description = prefsUtil.getString(Constants.KEY_VERSION_DESCRIPTION, Constants.VALUE_DEFAULT_STRING);
			tv_new_version.setVisibility(View.VISIBLE);
			tv_new_version.setText("发现新版本：" + name);
			tv_version_description.setVisibility(View.VISIBLE);
			tv_version_description.setText(description);

			btn_sure.setVisibility(View.VISIBLE);
			btn_cancel.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void initLayout(View view) {
		btn_sure = aq.id(R.id.btn_sure).getButton();
		btn_sure.setOnClickListener(this);
		btn_cancel = aq.id(R.id.btn_cancel).getButton();
		btn_cancel.setOnClickListener(this);

		tv_new_version = aq.id(R.id.tv_new_version).getTextView();
		tv_version_description = aq.id(R.id.tv_version_description).getTextView();
	}

	@Override
	protected void loadData() {
		setNetworkResult(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_sure:
//			DialogUtils.showShortToast(this, "已经是最新版本！");
			startUpdate();
			break;
		case R.id.btn_cancel:
			this.finish();
			break;
		}
	}

	/**
	 * 获取版本信息
	 * @return true为有新版本
	 */
	private boolean checkVersion() {
		//根据接口文档  ，提交json参数，获取返回json数据判断是否要更新
		String url = Constants.BASE_URL + "/base.php";

		JSONObject params = new JSONObject();
		try {
//			params.putOpt("edition", UpdateUtils.getVerName(UpdateActivity.this));
			params.putOpt("edition", "0.1");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		aq.post(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				// TODO: 2015/11/27  还需要根据接口返回的json格式，调用UpdateChecker类调用内部修改解析json的代码，用于显示对话框和通知栏的提示内容
				int succ = object.optInt("SUCC");
				if(succ == 0){//需要更新
					UpdateChecker.setUpdateServerJsonObject(object);
					UpdateChecker.checkForDialog(UpdateActivity.this);
				}else if(succ == 1){//无需更新

				}else if(succ == 2){//请求参数edition有误

				}
//				dialog.dismiss();
//				String error = JSONUtils.error(object, status);
//				if (error == null) {
//					int version = object.optJSONObject("data").optInt("version");
//					int verCode = UpdateUtils.getVerCode(MainActivity.this);
//					if (verCode >= version) {
//						Toast.makeText(MainActivity.this, "已经是最新版本", Toast.LENGTH_SHORT).show();
//					} else if (verCode < version) {
//						UpdateChecker.setUpdateServerUrl("http://nat.nat123.net:14313/oa/UpdateChecker.jsp");
//						UpdateChecker.setUpdateServerJsonObject(object.optJSONObject("data"));
//						UpdateChecker.checkForDialog(MainActivity.this);
//					}
//				} else {
//					DialogUtils.showShortToast(MainActivity.this, error);
//				}
			}
		});

		int newVersion = prefsUtil.getInt(Constants.KEY_VERSION_CODE, 0);
		PackageManager pm = getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			int currentVersion = packageInfo.versionCode;
			return currentVersion < newVersion;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 启动更新
	 */
	private void startUpdate() {

	}

}
