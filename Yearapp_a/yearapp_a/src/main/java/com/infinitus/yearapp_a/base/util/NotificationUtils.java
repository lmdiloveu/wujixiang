package com.infinitus.yearapp_a.base.util;


import com.infinitus.yearapp_a.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;


public class NotificationUtils {
	@SuppressWarnings("deprecation")
	public static void post(Context context, int id, String title, String text) {
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();
		notification.icon = R.drawable.ic_launcher; //通知图标
		notification.tickerText = context.getResources().getString(R.string.app_name); //通知的内容
		notification.defaults=Notification.DEFAULT_SOUND; //通知的铃声
		notification.audioStreamType= android.media.AudioManager.ADJUST_LOWER;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		notification.setLatestEventInfo(context, title, text, PendingIntent.getActivity(context, 0, new Intent(), 0));
		manager.notify(id, notification);
	}
}
