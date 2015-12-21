package com.infinitus.yearapp_a.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.infinitus.yearapp_a.R;
import com.infinitus.yearapp_a.utils.Constants;

/**
 * 显示Web页面
 * @author Administrator
 *
 */
public class WebActivity extends BaseActivity {
	private WebView mWebView;

	private String mLoadUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mLoadUrl = getIntent().getStringExtra(Constants.EXTRA_KEY_WEBVIEW_URL);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
	}

	@Override
	protected void initLayout(View view) {
		mWebView = aq.id(R.id.webView).getWebView();

		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setUseWideViewPort(true);

		mWebView.loadUrl(mLoadUrl);
	}

	@Override
	protected void loadData() {
		setNetworkResult(true);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
