package com.infinitus.yearapp_a.activity;

import com.infinitus.yearapp_a.R;
import com.infinitus.yearapp_a.utils.Constants;
import com.infinitus.yearapp_a.utils.Prefs;
import com.zcw.togglebutton.ToggleButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import java.util.UUID;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSharpenFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

/**
 * 会议相机，预览拍摄后的照片，选择是否美颜
 * 
 * @author Administrator
 *
 */
public class MeetingCameraActivity extends BaseActivity {
	
	/* 由相机拍摄传递过来的照片 */
	private Uri uri;

	private ToggleButton mBeautifyToggle;

	private GPUImageView mGPUImage;

	/* 滤镜 */
	private GPUImageFilter normalFilter, sharpenFilter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		uri = getIntent().getParcelableExtra(Constants.EXTRA_KEY_IMAGE_URI);
		super.onCreate(savedInstanceState);
		super.setLeftButtonText(R.string.cancel);
		super.setRightButtonText(R.string.complete);
		setContentView(R.layout.activity_meeting_camera);
		
		previewPhoto();
	}
	
	/**
	 * 预览照片
	 */
	private void previewPhoto() {
		mGPUImage.setImage(uri);
		mGPUImage.setFilter(new GPUImageSharpenFilter(0.75f));
	}

	@Override
	protected void initLayout(View view) {
		mGPUImage = (GPUImageView) aq.id(R.id.gpuiv_preview).getView();

		mBeautifyToggle = (ToggleButton) aq.id(R.id.toggle_beautify).getView();
		mBeautifyToggle.setToggleOn();
		mBeautifyToggle.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
			@Override
			public void onToggle(boolean on) {
				if (on) {
					if(sharpenFilter == null)
						sharpenFilter = new GPUImageSharpenFilter(0.75f);
					mGPUImage.setFilter(sharpenFilter);
				} else {
					if(normalFilter == null)
						normalFilter = new GPUImageFilter();
					mGPUImage.setFilter(normalFilter);
				}
			}
		});
	}

	@Override
	protected void loadData() {
		setNetworkResult(true);
	}
	
	@Override
	protected void onRightButtonClick(View v) {
		super.onRightButtonClick(v);
		if(mBeautifyToggle.isToggleOn()) {
			final String fileName = UUID.randomUUID().toString()+".png";
			mGPUImage.saveToPictures(Prefs.getTempImagePath(), fileName, new GPUImageView.OnPictureSavedListener() {
				@Override
				public void onPictureSaved(Uri uri) {
					MeetingCameraActivity.this.uri = uri;
					nextStep();
				}
			});
		} else {
			nextStep();
		}
	}

	/**
	 * 跳转到下一个Activity
	 */
	private void nextStep() {
		Intent intent = new Intent(MeetingCameraActivity.this, PhotoAddFrameActivity.class);
		intent.putExtra(Constants.EXTRA_KEY_IMAGE_URI, uri);
		intent.putExtra(Constants.EXTRA_KEY_BEAUTIFY, mBeautifyToggle.isToggleOn());
		startActivity(intent);
		finish();
	}

}
