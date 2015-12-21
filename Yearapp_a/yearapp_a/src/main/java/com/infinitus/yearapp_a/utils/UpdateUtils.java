package com.infinitus.yearapp_a.utils;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.infinitus.yearapp_a.R;
import com.infinitus.yearapp_a.utils.update.download.services.DownloadService;
import com.infinitus.yearapp_a.utils.update.download.utils.MyIntents;

public class UpdateUtils {

	/**
	 * 获取软件版本号
	 * 
	 * @param context
	 * @return
	 */
	public static int getVerCode(Context context) {
		int verCode = -1;
		try {
			// 注意："com.example.try_downloadfile_progress"对应AndroidManifest.xml里的package="……"部分
			verCode = context.getPackageManager().getPackageInfo("com.youledong.fitness", 0).versionCode;
		} catch (NameNotFoundException e) {
			Log.e("msg", e.getMessage());
		}
		return verCode;
	}

	/**
	 * 获取版本名称
	 * 
	 * @param context
	 * @return
	 */
	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo("com.youledong.fitness", 0).versionName;
		} catch (NameNotFoundException e) {
			Log.e("msg", e.getMessage());
		}
		return verName;
	}

	/**
	 * Show Notification
	 * 
	 */
	public static void showNotification(Context context, String content, String apkUrl) {
		Notification noti;
		Intent myIntent = new Intent(context, DownloadService.class);
		myIntent.putExtra(MyIntents.URL, apkUrl);
		myIntent.putExtra(MyIntents.TYPE, MyIntents.Types.ADD);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		int smallIcon = context.getApplicationInfo().icon;
		noti = new NotificationCompat.Builder(context).setTicker(context.getString(R.string.newUpdateAvailable)).setContentTitle(context.getString(R.string.newUpdateAvailable))
				.setContentText(content).setSmallIcon(smallIcon).setContentIntent(pendingIntent).build();

		noti.flags = Notification.FLAG_AUTO_CANCEL;
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(0, noti);
	}
	
	
}
