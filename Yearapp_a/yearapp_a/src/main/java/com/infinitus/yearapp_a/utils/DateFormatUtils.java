package com.infinitus.yearapp_a.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatUtils {

	public static String format(Date date, String fmt) {
		SimpleDateFormat df = new SimpleDateFormat(fmt, Locale.getDefault());
		return df.format(date);
	}

	public static String format(long ms, String fmt) {
		Date date = new Date(ms * 1000);
		return format(date, fmt);
	}
}
