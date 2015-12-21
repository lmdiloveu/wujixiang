package com.infinitus.yearapp_a.base.util;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.text.TextUtils;

import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.AQUtility;

public class JSONUtils {

	public static String error(JSONObject object, AjaxStatus status) {
		String error = null;
		switch (status.getCode()) {
		case AjaxStatus.NETWORK_ERROR:
			error = "网络失败";
			break;
		case AjaxStatus.AUTH_ERROR:
			error = "鉴权失败";
			break;
		case AjaxStatus.TRANSFORM_ERROR:
			error = "服务器数据错误";
			break;
		default:
			if (object != null) {
				if (object.optInt("result") != 1)
					error = object.optString("message");
			} else {
				error = status.getMessage();
				if (TextUtils.isEmpty(error)) {
					error = "未知错误";
				}
			}
			break;
		}
		return error;
	}
	
	public static void copyValue(JSONObject src, JSONObject dest, String key) throws JSONException {
		dest.put(key, src.opt(key));
	}
	

	public static JSONObject toJSON(byte[] data) {
		JSONObject result = null;
		String str = null;
		try {
			str = new String(data, "UTF-8");
			result = (JSONObject) new JSONTokener(str).nextValue();
		} catch (Exception e) {
			AQUtility.debug(e);
			AQUtility.debug(str);
		}
		return result;
	}
}
