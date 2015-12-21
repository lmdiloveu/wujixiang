package com.infinitus.yearapp_a.activity;

import com.infinitus.yearapp_a.Application;
import com.infinitus.yearapp_a.R;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * 闪光灯页面
 * 
 * @author Administrator
 * 
 */
@SuppressWarnings("deprecation")
public class FlashlightActivity extends BaseActivity implements
		SensorEventListener {
	private Camera mCamera;
	/* 为true时停止响应传感器数据 */
//	private boolean mQuit;

	private SensorManager mSensorManager;
	private Sensor mSensor;

//	private float lastRad;

	@Override
	protected void initLayout(View view) {
		ImageView iv_logo_bg = aq.id(R.id.iv_logo_background).getImageView();
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.logo_bg_in);
		iv_logo_bg.startAnimation(animation);
	}

	@Override
	protected void loadData() {
		setNetworkResult(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flashlight);
		setLeftButtonDrawable(R.drawable.icon_back);
		setRightButtonDrawable(R.drawable.icon_setting);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (!Application.isFlashlightAvailable()) {
			return;
		}
		mSensorManager.registerListener(this, mSensor,
				SensorManager.SENSOR_DELAY_FASTEST);
		if (mCamera == null) {
			mCamera = Camera.open();
		}

		// light();

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (!Application.isFlashlightAvailable()) {
			return;
		}
		mSensorManager.unregisterListener(this);
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

	/**
	 * 闪光
	 */
	private void light() {
		if (!Application.isFlashlightAvailable()) {
			return;
		}

		if (mCamera == null) {
			mCamera = Camera.open();
		}

		Parameters parameters = mCamera.getParameters();
		parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
		mCamera.setParameters(parameters);
		mCamera.startPreview();

		parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
		mCamera.setParameters(parameters);
		mCamera.startPreview();

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {// 精度发生变化

	}

	private double lastCos = 0;
	private boolean directionChange;
	private float lastAy = -9.8f;
	private float lastAx = 0.0f;

	// 两次检测的时间间隔
	private static final int UPTATE_INTERVAL_TIME = 150;
	// 上次检测时间
	private long lastUpdateTime;

	@Override
	public void onSensorChanged(SensorEvent event) {
		long currentUpdateTime = System.currentTimeMillis();
		// 两次检测的时间间隔
		long timeInterval = currentUpdateTime - lastUpdateTime;
		// 判断是否达到了检测时间间隔
		if (timeInterval < UPTATE_INTERVAL_TIME)
			return;
		lastUpdateTime = currentUpdateTime;

		if (Sensor.TYPE_ACCELEROMETER != event.sensor.getType()) {
			return;
		}

		float[] values = event.values;
		float ax = values[0];
		float ay = values[1];

		double g = Math.sqrt(ax * ax + ay * ay);
		double cos = ay / g;
		if (cos > 1) {
			cos = 1;
		} else if (cos < -1) {
			cos = -1;
		}
		System.out.println("cos>>>" + cos);// 手机横屏时，cos为接近0，竖屏时，cos为接近1

		if ((ax / lastAx) > 0) {// 两次变化均发生在同一象限内
			if(directionChange && (Math.abs(cos) - lastCos) > 0.15){
				light();
				directionChange = false;
			}
		} else if ((ax / lastAx) < 0) {// 两次变化不是发生在同一象限内    TODO ax
			directionChange = true;
		}
		lastCos = Math.abs(cos);
		lastAy = ay;
		lastAx = ax;

		// double rad = Math.acos(cos);
		// if (ax < 0) {
		// rad = 2 * Math.PI - rad;
		// }

		// int uiRot = getWindowManager().getDefaultDisplay().getRotation();
		// double uiRad = Math.PI / 2 * uiRot;
		// rad -= uiRad;

		// System.out.println("rad>>>" + rad);
		
//		if ((ay / lastAy) > 0) {// 两次变化均发生在同一象限内
//			if(change && (Math.abs(cos) - lastCos) > 0.2){
//				light();
//				change = false;
//			}
//			lastCos = Math.abs(cos);
//		} else if ((ay / lastAy) < 0) {// 两次变化不是发生在同一象限内
//			change = true;
//			lastCos = Math.abs(cos);
//		}
//		lastAy = ay;
	}

}
