package com.infinitus.yearapp_a.utils;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
//import com.infinitus.yearapp_a.MainActivity;
import com.infinitus.yearapp_a.base.auth.TokenManager;
import com.infinitus.yearapp_a.base.util.JSONUtils;
import com.umeng.socialize.bean.HandlerRequestCode;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class QQWXUtils {
	
//	private static final String WX = "wx";
//	private static final String QQ = "qq";

	// 整个平台的Controller, 负责管理整个SDK的配置、操作等处理
	private UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

	private Activity activity;
	private AQuery aq;
	private String type;

	public QQWXUtils(Activity activity, String type) {
		this.activity = activity;
		this.type = type;
		aq = new AQuery(activity);
		addQZoneQQPlatform();
		addWXPlatform();
//		setShareContent();
	}

	private void addQZoneQQPlatform() {
//		String appId = "100424468";
//		String appKey = "c7394704798a158208a74ab60104f0ba";
		String appId = "1104910718";
		String appKey = "E0K0UFaQetm3pEKC";
		
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity, appId, appKey);
		qqSsoHandler.setTargetUrl("http://www.umeng.com");
		qqSsoHandler.addToSocialSDK();

		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity, appId, appKey);
		qZoneSsoHandler.addToSocialSDK();
	}

	private void addWXPlatform() {
		String appId = "wx9b91239c20e84e35";
		String appSecret = "34683c632e4bdcd9e5f13dc986b0533d";
		UMWXHandler wxHandler = new UMWXHandler(activity, appId, appSecret);
		wxHandler.addToSocialSDK();

		UMWXHandler wxCircleHandler = new UMWXHandler(activity, appId, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}
	// 标题   内容  图片地址   点击地址
	public void setShareContent(String title , String content , String imageUrl , String targetUrl) {
//		String url = "https://mmbiz.qlogo.cn/mmbiz/8aK7acF0K0rLoTRrMvdbFfvEDOMkpwg0WgmxOZjTez7lkDia0ZNn4HRISibV9ZqMzYTiccclrsmAjz4953WhaTzkw/0?wx_fmt=png";

		mController.setShareContent("柚子眼镜");

//		UMImage localImage = new UMImage(activity, R.drawable.device);
	    UMImage urlImage = new UMImage(activity, imageUrl);
//		UMImage resImage = new UMImage(activity, R.drawable.ic_launcher);
		// 设置微信分享的内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent.setShareContent(content);
		weixinContent.setTitle(title);
		weixinContent.setTargetUrl(targetUrl);
		weixinContent.setShareMedia(urlImage);
		mController.setShareMedia(weixinContent);
		// 设置微信朋友圈分享的内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(content);
		circleMedia.setTitle(title);
		circleMedia.setShareMedia(urlImage);
		circleMedia.setTargetUrl(targetUrl);
		mController.setShareMedia(circleMedia);
		// 设置QQ空间分享内容
		QZoneShareContent qzone = new QZoneShareContent();
		qzone.setShareContent(content);
		qzone.setTargetUrl(targetUrl);
		qzone.setTitle(title);
		qzone.setShareMedia(urlImage);
		mController.setShareMedia(qzone);
		// 设置QQ分享内容
		QQShareContent qqShareContent = new QQShareContent();
		qqShareContent.setShareContent(content);
		qqShareContent.setTitle(title);
		qqShareContent.setShareMedia(urlImage);
		qqShareContent.setTargetUrl(targetUrl);
		mController.setShareMedia(qqShareContent);

	}

	/**
	 * 授权。如果授权成功，则获取用户信息</br>
	 */
	public void login(final SHARE_MEDIA platform) {
		mController.doOauthVerify(activity, platform, new UMAuthListener() {

			@Override
			public void onStart(SHARE_MEDIA platform) {
				Toast.makeText(activity, "开始授权", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onError(SocializeException e, SHARE_MEDIA platform) {
				Toast.makeText(activity, "授权错误", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onComplete(Bundle value, SHARE_MEDIA platform) {
				Toast.makeText(activity, "完成授权", Toast.LENGTH_SHORT).show();
//				String uid = value.getString("uid");
				String uid = value.getString("openid");
				String access_token = value.getString("access_token");
				LogUtils.i("获取uid：" + uid +"/n-获取access_token："+access_token);
				if (!TextUtils.isEmpty(uid)) {
//					getUserInfo(platform);
					thirdpartyLogin(type, uid);
				} else {
					Toast.makeText(activity, "授权失败...", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onCancel(SHARE_MEDIA platform) {
				Toast.makeText(activity, "授权取消", Toast.LENGTH_SHORT).show();
			}
		});
	}

//	/**
//	 * 获取授权平台的用户信息</br>
//	 */
//	private void getUserInfo(SHARE_MEDIA platform) {
//		mController.getPlatformInfo(activity, platform, new UMDataListener() {
//
//			@Override
//			public void onStart() {
//
//			}
//
//			@Override
//			public void onComplete(int status, Map<String, Object> info) {
//				String showText = "";
//				if (status == StatusCode.ST_CODE_SUCCESSED) {
//					if (info != null) {
//						showText = "用户名：" + info.toString();
//						Toast.makeText(activity, info.toString(), Toast.LENGTH_SHORT).show();
//						LogUtils.i("获取用户的信息：" + info.toString());
//					}
//					thirdpartyLogin("wx", "code");
//				} else {
//					showText = "获取用户信息失败";
//				}
//			}
//		});
//	}

	/**
	 * type: 第三方类型。wx：微信 qq：QQ code：第三方登录授权码
	 * 
	 * @param type
	 */
	private void thirdpartyLogin(String type, String code) {
		aq.ajax(Constants.BASE_URL + "/shop/member!thirdpartyLogin.do?type=" + type + "&code=" + code, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				String error = JSONUtils.error(object, status);
				if (error != null) {
					DialogUtils.showShortToast(activity, error);
				} else {
					TokenManager.instance().getTokenHandle().saveLoginType(1);
					LogUtils.i("登录获取的数据"+object.toString());
					JSONObject data = object.optJSONObject("data");
					String token = data.optString("token");
					String member_id = data.optString("member_id");
					if(TextUtils.isEmpty(member_id)){//已经绑定了手机，但是并未没有返回绑定了哪个手机
                        TokenManager.instance().thirdpartyLogin(null,token);
						
//			        	Intent intent = new Intent(activity, MainActivity.class);
//			    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			    		intent.putExtra("main_position", 3);//跳到首页
//			    		activity.startActivity(intent);
					}else{
						bindingCellphone(token,member_id);
					}
				}
			}
		});
	}

	private void bindingCellphone(String token, String member_id) {
//		Intent bindingIntent = new Intent(activity, BindingPhoneStepOneActivity.class);
//		bindingIntent.putExtra("register_or_not", 2);// 已注册
//		bindingIntent.putExtra("token", token);
//		bindingIntent.putExtra("member_id", member_id);
//		activity.startActivityForResult(bindingIntent, 2);
	}

	protected void loginOut(final SHARE_MEDIA platform) {
		mController.deleteOauth(activity, platform, new SocializeClientListener() {

			@Override
			public void onStart() {

			}

			@Override
			public void onComplete(int status, SocializeEntity entity) {
				String showText = "解除" + platform.toString() + "平台授权成功";
				if (status != StatusCode.ST_CODE_SUCCESSED) {
					showText = "解除" + platform.toString() + "平台授权失败[" + status + "]";
				}
				Toast.makeText(activity, showText, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}
	
	public void share(){
		if (mController.getConfig()
				.getSsoHandler(HandlerRequestCode.QQ_REQUEST_CODE)
				.isClientInstalled()) {
			mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
					SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QZONE,
					SHARE_MEDIA.QQ);
		} else {
			mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
					SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ);
		}
		mController.openShare(activity, false);
	}

}
