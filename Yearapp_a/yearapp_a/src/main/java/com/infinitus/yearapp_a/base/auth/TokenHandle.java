package com.infinitus.yearapp_a.base.auth;

import java.net.HttpURLConnection;
import java.util.HashMap;

import org.apache.http.HttpRequest;
import org.json.JSONObject;

import android.content.Intent;
import android.text.TextUtils;

import com.androidquery.AQuery;
import com.androidquery.auth.AccountHandle;
import com.androidquery.callback.AbstractAjaxCallback;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.AQUtility;
import com.infinitus.yearapp_a.Application;
import com.infinitus.yearapp_a.base.util.JSONUtils;
//import com.tencent.mm.sdk.modelmsg.SendAuth;
//import com.youzi.base.util.JSONUtils;
//import com.youzi.base.util.PrefsUtil;
//import com.youzi.glassshop.Application;
//import com.youzi.glassshop.activity.RegisterUserActivity;
//import com.youzi.glassshop.utils.Constants;
//import com.youzi.glassshop.utils.DialogUtils;
import com.infinitus.yearapp_a.base.util.PrefsUtil;
import com.infinitus.yearapp_a.utils.Constants;
import com.infinitus.yearapp_a.utils.DialogUtils;

public class TokenHandle extends AccountHandle {
	public static final String KEY_LOGGED_TOKEN = "logged_token";
	public static final String KEY_LOGGED_ACCOUNT = "logged_account";
	public static final String KEY_LOGGED_TYPE = "logged_type";
	private static final String AUTH_URL = Constants.BASE_URL
			+ "/shop/member!login.do";

	public String token;
	private String username;
	private String password;
	private int loginType;

	public TokenHandle() {
		this.token = fetchToken();
		this.username = fetchUserAccount();
		this.password = fectchPassword();
		this.loginType = fectchLoginType();
	}

	public static boolean isAutoLogged() {
		return (fetchToken() != null);
	}

	public static String fetchUserAccount() {
		return PrefsUtil.prefs().getString(KEY_LOGGED_ACCOUNT, "");
	}

	public static String fectchPassword() {
		return PrefsUtil.prefs().getDecryptedString(
				Constants.KEY_LOGGED_PASSWORD, "");
		// return PrefsUtil.prefs().getString(Constants.KEY_LOGGED_PASSWORD,
		// "");
	}

	public static int fectchLoginType() {
		return PrefsUtil.prefs().getInt(KEY_LOGGED_TYPE, 0);
	}

	public static String fetchToken() {
		return PrefsUtil.prefs().getString(KEY_LOGGED_TOKEN, null);
	}

	public void auth(String username, String password) {
		this.username = username;
		this.password = password;

		saveValue(KEY_LOGGED_ACCOUNT, this.username, false);

		auth();
	}

	public void thirdpartyLoginAuth(String cellphone, String token) {
		this.username = cellphone;
		this.token = token;
		if (TextUtils.isEmpty(username)) {
			saveValue(KEY_LOGGED_ACCOUNT, "", false);
		} else {
			saveValue(KEY_LOGGED_ACCOUNT, this.username, false);
		}
		saveValue(KEY_LOGGED_TOKEN, token, false);
	}

	@Override
	public boolean authenticated() {
		return true;
		// return (token != null);
	}

	@Override
	protected void auth() {
		// //type 0：帐号密码登录 1：微信登录 2：QQ登录
		// if (loginType == 0) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("username", this.username);
		params.put("password", this.password);

		AQuery aq = new AQuery(AQUtility.getContext());
		aq.ajax(AUTH_URL, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject object,
							AjaxStatus status) {
						Intent intent = new Intent();
						intent.setAction(Constants.ACTION_LOGGED);

						String error = JSONUtils.error(object, status);
						if (error == null) {
							token = object.optJSONObject("data").optString(
									"token");
							saveValue(KEY_LOGGED_TOKEN, token, false);
							saveValue(Constants.KEY_LOGGED_PASSWORD,
									TokenHandle.this.password, true);
							// saveValue(Constants.KEY_LOGGED_PASSWORD,
							// TokenHandle.this.password, false);
							success(AQUtility.getContext());
						} else {
							TokenHandle.this.failure(AQUtility.getContext(),
									status.getCode(), error);
							intent.putExtra(Constants.ACTION_LOGGED_ERROR_KEY,
									error);
						}
						AQUtility.getContext().sendBroadcast(intent);
					}

				});
		// }else if(loginType == 1){
		// final SendAuth.Req req = new SendAuth.Req();
		// req.scope = "snsapi_userinfo";
		// req.state = "glassshop_wx_login";
		// Application.api.sendReq(req);
		// }else if(loginType == 2){
		//
		// }
	}

	@Override
	public void applyToken(AbstractAjaxCallback<?, ?> cb, HttpRequest request) {
		// AQUtility.debug("apply token", token);
		// request.addHeader("Authorization", token);
		if (!TextUtils.isEmpty(token)) {
			request.addHeader("Token", token);
		}

	}

	@Override
	public void applyToken(AbstractAjaxCallback<?, ?> cb, HttpURLConnection conn) {
		// conn.setRequestProperty("token", token);
	}

	@Override
	public boolean expired(AbstractAjaxCallback<?, ?> cb, AjaxStatus status) {
		// DialogUtils.hideLoading();
		JSONObject jo = JSONUtils.toJSON(status.getData());
		if (jo != null && jo.optInt("code", 0) == 1000) {

			if (loginType == 0 && !TextUtils.isEmpty(password)) {
				return true;
			}

			DialogUtils.hideLoading();
			token = null;
			removeKey(KEY_LOGGED_TOKEN);

			if (loginType == 1) {
//				SendAuth.Req req = new SendAuth.Req();
//				req.scope = "snsapi_userinfo";
//				req.state = "glassshop_wx_login";
//				Application.api.sendReq(req);
			} else {
//				Intent intent = new Intent(Application.getInstance(),
//						RegisterUserActivity.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				Application.getInstance().startActivity(intent);
			}
			// return true;
		}

		return false;
	}

	@Override
	public boolean reauth(final AbstractAjaxCallback<?, ?> cb) {
		token = null;

		AQUtility.post(new Runnable() {
			@Override
			public void run() {
				auth(cb);
			}
		});

		return false;
	}

	@Override
	public void unauth() {
		token = null;

		AjaxCallback.cancel();

		removeKey(KEY_LOGGED_TOKEN);
		removeKey(Constants.KEY_LOGGED_PASSWORD);

		Intent intent = new Intent();
		intent.setAction(Constants.ACTION_LOGOUTED);
		AQUtility.getContext().sendBroadcast(intent);
	}

	@Override
	public String getNetworkUrl(String url) {
		if (url != null
				&& (url.startsWith("http://") || url.startsWith("https://")))
			return url;
		return Constants.BASE_URL + url;
	}

	public boolean saveValue(String key, String value, boolean encrypted) {
		if (encrypted) {
			return PrefsUtil.prefs().putEncryptedString(key, value);
		} else {
			return PrefsUtil.prefs().putString(key, value);
		}
	}

	private boolean removeKey(String key) {
		return PrefsUtil.prefs().removeKey(key);
	}

	// 保存密码
	public void savePassword(String password) {
		this.password = password;
		saveValue(Constants.KEY_LOGGED_PASSWORD, password, true);
	}

	// 保存token
	public void saveToken(String token) {
		this.token = token;
	}

	// 保存手机号
	public void saveUser(String cellphone) {
		this.username = cellphone;
	}

	public void saveLoginType(int type) {
		this.loginType = type;
		PrefsUtil.prefs().putInt(KEY_LOGGED_TYPE, type);
	}

	public String getToken() {
		return this.token;
	}
}
