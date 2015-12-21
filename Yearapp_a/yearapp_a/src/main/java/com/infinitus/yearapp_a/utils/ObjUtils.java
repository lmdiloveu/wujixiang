package com.infinitus.yearapp_a.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import android.content.Context;

/**
 * 用于 处理对象序列化操作
 * @author Pangjb
 * 2015-05-20
 */
public class ObjUtils {
	
	
	/**
	 * 
	 * @param context 上下文
	 * @param obj   需要保存的对象，注意该对象需要实现序列化接口
	 * @param fileName  文件名
	 */
	public static void saveObject(Context context,Object obj,String fileName){
		FileOutputStream stream;
		try {
			stream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(stream);
			oos.writeObject(obj);
			stream.close();
			LogUtils.i("保存对象文件名："+ fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param context 上下文
	 * @param fileName 文件名
	 * @return 具体类型需要自己转型，需要捕获类型转换异常
	 */
	public static Object getObject(Context context,String fileName){
		FileInputStream stream;
		ObjectInputStream ois;
		Object obj;
		try {
			stream = context.openFileInput(fileName);
			ois = new ObjectInputStream(stream);
			obj = ois.readObject();
			stream.close();
			LogUtils.i("获取对象文件名："+ fileName);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
