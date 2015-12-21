package com.infinitus.yearapp_a.utils;

public class Constants {
//	public static final String BASE_URL = "http://192.168.2.20:10080/api";
//	public static final String BASE_URL = "http://backend.youziglasses.com/api";
	public static final String BASE_URL = "http://120.25.210.239/api";//测试环境
	
	public static final String ACTION_LOGGED = "com.wintous.o2o.express.action.logged";
	public static final String ACTION_LOGOUTED = "com.wintous.o2o.express.action.logouted";
	public static final String ACTION_LOGGED_ERROR_KEY = "error";
	
//	public static final String ORDER_DATE_FMT = "yyyy年MM月dd日";
//	public static final String BILL_DATE_TIME_FMT = "yyyy.MM.dd HH:mm";
	
	public static final String KEY_LOGGED_PASSWORD = "logged_pwd";
	
//	public static final int REGISTER_CODE = 0;		// 注册
//	public static final int LOGIN_CODE = 1;		    // 登录
//	public static final int GOTO_CODE = 2;		    // 退到首页
//	public static final int BINDING_CODE = 3;		// 绑定手机
//	public static final int SUBMIT_ORDER_CODE = 4;		// 去结算提交订单成功后
	
//	public static final String ACTION_LOCATION = "location_bcr"; // 定位
//
//	public static final String PHONE_NUM = "4000569191"; // 客服电话号码
//
//	public static final String ORDER_DETAIL_ACTION = "order_detail_action";//订单详情广播
//	public static final String PAY_RESULT = "pay_result";//支付结果
//
//	public static final String RECECVER_ACTION_COUNT_DOWN = "CountdownTimer";

	/**
	 * 版本更新信息
	 */
	public static final String PREF_NAME_UPDATE = "version_update";
	public static final String KEY_VERSION_CODE = "key_version_code";
	public static final String KEY_VERSION_NAME = "key_version_name";
	public static final String KEY_VERSION_DESCRIPTION = "key_version_description";
	
	/**
	 * 选择部门的SP文件名称
	 */
	public static final String PREF_NAME_SELECT_DEPT = "select_dept";
	public static final String KEY_DEPT = "key_dept";
	public static final String VALUE_BEIJING = "beijing";
	public static final String VALUE_GUANGZHOU = "guangzhou";
	/**
	 * SP默认String值
	 */
	public static final String VALUE_DEFAULT_STRING = " ";
	
	/**
	 * 点击【制作相册】，跳转界面的请求码requestCode
	 */
	public static final int MAKE_XIANG_CE = 100;
	public static final int KEEP_CURRENT_ACTIVITY = 101;

	/**
	 * 图片uri参数的key
	 */
	public static final String EXTRA_KEY_IMAGE_URI = "extra_key_image_uri";
	/**
	 * 是否美颜参数的key
	 */
	public static final String EXTRA_KEY_BEAUTIFY = "extra_key_beautify";
	/**
	 * 是否为编辑图片
 	 */
	public static final String EXTRA_KEY_PHOTO_EDIT = "extra_key_photo_edit";


	public static final String EXTRA_KEY_WEBVIEW_URL = "extra_key_webview_url";
}
