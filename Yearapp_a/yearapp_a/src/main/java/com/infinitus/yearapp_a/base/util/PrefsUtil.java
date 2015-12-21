package com.infinitus.yearapp_a.base.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.androidquery.util.AQUtility;

public class PrefsUtil {

	private static final String __MASK_KEY = "1234567812345678";

	private final SharedPreferences prefs;

	private static PrefsUtil __prefs ;

	public static PrefsUtil prefs() {
		return __prefs = new PrefsUtil();
	}
	
	public static PrefsUtil prefs(String name) {
		return __prefs = new PrefsUtil(AQUtility.getContext(), name); 
	}

	public PrefsUtil(Context context, String name, int mode) {
		prefs = context.getSharedPreferences(name, mode);
	}

	public PrefsUtil(Context context, String name) {
		this(context, name, Context.MODE_PRIVATE);
	}

	public PrefsUtil() {
		this(AQUtility.getContext(), "prefs");
	}

	public int getInt(String key, int defValue) {
		return prefs.getInt(key, defValue);
	}

	public String getString(String key, String defValue) {
		return prefs.getString(key, defValue);
	}

	public boolean getBoolean(String key, boolean defValue) {
		return prefs.getBoolean(key, defValue);
	}

	public String getDecryptedString(String key, String defValue) {
		String value = getString(key, defValue);
//		String newValue = AESUtils.decrypt(__MASK_KEY, value);
		CryptUtils mcrypt = new CryptUtils();
		String newValue = null;
		try {
			newValue = new String(mcrypt.decrypt(value));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newValue;
	}

	public boolean putInt(String key, int value) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(key, value);
		return editor.commit();
	}

	public boolean putString(String key, String value) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(key, value);
		return editor.commit();
	}

	public boolean putEncryptedString(String key, String value) {
//		String newValue = AESUtils.encrypt(__MASK_KEY, value);
		CryptUtils mcrypt = new CryptUtils();
		String newValue = null;
		try {
			newValue = CryptUtils.bytesToHex(mcrypt.encrypt(value));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return putString(key, newValue);
	}

	public boolean putBoolean(String key, boolean value) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(key, value);
		return editor.commit();
	}

	public boolean removeKey(String key) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.remove(key);
		return editor.commit();
	}

	public boolean removeAll(String key) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.clear();
		return editor.commit();
	}
}
