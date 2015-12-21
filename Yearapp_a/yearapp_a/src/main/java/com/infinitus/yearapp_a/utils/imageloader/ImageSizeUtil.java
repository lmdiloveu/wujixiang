package com.infinitus.yearapp_a.utils.imageloader;

import java.lang.reflect.Field;

import android.graphics.BitmapFactory.Options;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
/**
 * 
 * @author pangjb
 *
 */
public class ImageSizeUtil
{
	public static int caculateInSampleSize(Options options, int reqWidth,
			int reqHeight)
	{
		int width = options.outWidth;
		int height = options.outHeight;

		int inSampleSize = 1;

		if (width > reqWidth || height > reqHeight)
		{
			int widthRadio = Math.round(width * 1.0f / reqWidth);
			int heightRadio = Math.round(height * 1.0f / reqHeight);

			inSampleSize = Math.max(widthRadio, heightRadio);
//			inSampleSize =(widthRadio+heightRadio)/2;
		}

		return inSampleSize;
	}
	
	public static ImageSize getImageViewSize(ImageView imageView)
	{

		ImageSize imageSize = new ImageSize();
		DisplayMetrics displayMetrics = imageView.getContext().getResources()
				.getDisplayMetrics();
		

		LayoutParams lp = imageView.getLayoutParams();

		int width = imageView.getWidth();
		if (width <= 0)
		{
			width = lp.width;
		}
		if (width <= 0)
		{
			 //width = imageView.getMaxWidth();
			width = getImageViewFieldValue(imageView, "mMaxWidth");
		}
		if (width <= 0)
		{
			width = displayMetrics.widthPixels;
		}

		int height = imageView.getHeight();
		if (height <= 0)
		{
			height = lp.height;
		}
		if (height <= 0)
		{
			height = getImageViewFieldValue(imageView, "mMaxHeight");
		}
		if (height <= 0)
		{
			height = displayMetrics.heightPixels;
		}
		imageSize.width = width;
		imageSize.height = height;

		return imageSize;
	}

	public static class ImageSize
	{
		int width;
		int height;
	}
	
	
	private static int getImageViewFieldValue(Object object, String fieldName)
	{
		int value = 0;
		try
		{
			Field field = ImageView.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			int fieldValue = field.getInt(object);
			if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE)
			{
				value = fieldValue;
			}
		} catch (Exception e)
		{
		}
		return value;

	}

	
}
