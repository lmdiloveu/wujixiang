package com.infinitus.yearapp_a.utils;

import android.os.Environment;

public class Prefs {

	/** 获取app在sd卡上的根路径 */
	public static String getSDRoot() {
		return Environment.getExternalStorageDirectory().getAbsolutePath() + "/yearsapp";
	}

	/** 获取相册目录 */
	public static String getDCIMPath() {
		return getSDRoot() + "/Camera/";
	}

	/** 获取图片处理目录 */
	public static String getImageFilterPath() {
		return getSDRoot() + "/Images/";
	}
	
	/**
	 * 临时的图片目录，在对此目录下的文件处理完成后，需清理对应文件
	 * @return
	 */
	public static String getTempImagePath() {
		return getSDRoot() + "/temp/images/";
	}
	
	/**
	 * 相框的目录
	 * @return
	 */
	public static String getFramesPath() {
		return getSDRoot() + "/frames";
	}

	/**
	 * 获取可用相框的目录
	 * @return
	 */
	public static String getUnlockFramesPath() {
		return getSDRoot() + "/frames/unlock/";
	}

	/**
	 * 获取不可相框的目录
	 * @return
	 */
	public static String getLockFramesPath() {
		return getSDRoot() + "/frames/lock";
	}
}
