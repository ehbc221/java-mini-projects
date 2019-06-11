package com.gyro.webapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MainActivity extends Activity {
	private WebView myWebView;
	private JavaScriptInterface JS;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_main);
        myWebView = (WebView) findViewById(R.id.webView1);
        setProgressBarIndeterminateVisibility(true);
        setProgressBarVisibility(true);
        myWebView.setWebChromeClient(new WebChromeClient() {
      	   public void onProgressChanged(WebView view, int progress) {
      	     // Activities and WebViews measure progress with different scales.
      	     // The progress meter will automatically disappear when we reach 100%
      	     MainActivity.this.setProgress(progress * 1000);
      	   }
      	 });
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setAppCacheEnabled(false);
        myWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        myWebView.clearCache(true);
       // myWebView.setWebViewClient(new WebAppView());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("EXTRA_URL");
            myWebView.loadUrl(value);
        }else{
        	myWebView.loadUrl("file:///android_asset/android.html");
        }
        JS = new JavaScriptInterface(this, myWebView);
        myWebView.addJavascriptInterface(JS, "Android");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }
}
