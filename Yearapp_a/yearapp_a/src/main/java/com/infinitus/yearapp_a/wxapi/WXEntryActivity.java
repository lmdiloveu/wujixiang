package com.infinitus.yearapp_a.wxapi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.infinitus.yearapp_a.Application;
import com.infinitus.yearapp_a.R;
import com.infinitus.yearapp_a.base.auth.TokenManager;
import com.infinitus.yearapp_a.base.util.JSONUtils;
import com.infinitus.yearapp_a.utils.Constants;
import com.infinitus.yearapp_a.utils.DialogUtils;
import com.infinitus.yearapp_a.utils.LogUtils;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

import org.json.JSONObject;

public class WXEntryActivity extends WXCallbackActivity implements IWXAPIEventHandler {
	
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_result);
		Application.api.handleIntent(getIntent(), this);
	}
//	
//	@Override
//	protected void onNewIntent(Intent intent) {
//		super.onNewIntent(intent);
//		setIntent(intent);
//		Application.api.handleIntent(intent, this);
//		finish();
//	}

//	@Override
//	public void onReq(BaseReq arg0) {
//		try {
//			Intent intent = new Intent(Application.getInstance(), MainActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			Application.getInstance().startActivity(intent);
////			finish();
//		} catch (Exception e) {
//		
//		}
//	}

	@Override
	public void onResp(BaseResp resp) {
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			if(resp instanceof com.tencent.mm.sdk.modelmsg.SendAuth.Resp){
//				dialog = ProgressDialog.show(this, this.getString(R.string.app_tip), "正在登录...");
//				PrefsUtil.prefs().putInt(TokenHandle., 1);
				DialogUtils.hideLoading();
				String code = ((SendAuth.Resp) resp).code;
				thirdpartyLogin("wx", code);
			}else if (resp instanceof com.tencent.mm.sdk.modelmsg.SendMessageToWX.Resp) {
//				WXEntryActivity.this.finish();
				super.onResp(resp);
			}
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			DialogUtils.hideLoading();
			finish();
			break;
		}

	}
	
	/**
	 * type: 第三方类型。wx：微信 qq：QQ code：第三方登录授权码
	 * 
	 * @param type
	 */
	private void thirdpartyLogin(String type, String code) {
		DialogUtils.showLoading(this);
		Application.aq.ajax(Constants.BASE_URL + "/shop/member!thirdpartyLogin.do?type=" + type + "&code=" + code, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				DialogUtils.hideLoading();
				String error = JSONUtils.error(object, status);
				if (error != null) {
					DialogUtils.showShortToast(Application.getInstance(), error);
					WXEntryActivity.this.finish();
				} else {
					TokenManager.instance().getTokenHandle().saveLoginType(1);
					LogUtils.i("登录获取的数据"+object.toString());
					JSONObject data = object.optJSONObject("data");
					String token = data.optString("token");
					String member_id = data.optString("member_id");
					String mobile = data.optString("mobile");
					boolean has_bind = data.optBoolean("has_bind");
					if(has_bind){//已经绑定了手机，但是并未没有返回绑定了哪个手机
                        TokenManager.instance().thirdpartyLogin(mobile,token);
						
//			        	Intent intent = new Intent(WXEntryActivity.this, MainActivity.class);
//			    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			    		intent.putExtra("main_position", 3);//跳到首页
//			    		WXEntryActivity.this.startActivity(intent);
                        
                        Intent loginIntent = new Intent();
//                        loginIntent.setAction(RegisterUserActivity.WX_LOGIN);
                        WXEntryActivity.this.sendBroadcast(loginIntent);
        				
			    		WXEntryActivity.this.finish();
					}else{
						bindingCellphone(token,member_id);
						WXEntryActivity.this.finish();
					}
				}
				
				if (dialog != null) {
					dialog.dismiss();
				}
				
				DialogUtils.hideLoading();
			}
		});
	}
	
	private void bindingCellphone(String token, String member_id) {
//		Intent bindingIntent = new Intent(this, BindingPhoneStepOneActivity.class);
////		bindingIntent.putExtra("register_or_not", 2);// 已注册
//		bindingIntent.putExtra("token", token);
//		bindingIntent.putExtra("member_id", member_id);
//		WXEntryActivity.this.startActivityForResult(bindingIntent, 2);
	}
}
