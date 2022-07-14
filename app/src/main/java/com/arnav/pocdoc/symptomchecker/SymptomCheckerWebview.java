package com.arnav.pocdoc.symptomchecker;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.arnav.pocdoc.R;

import im.delight.android.webview.AdvancedWebView;

public class SymptomCheckerWebview extends Activity implements AdvancedWebView.Listener {
    private AdvancedWebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.symptomcheckerwebview);

        mWebView = (AdvancedWebView) findViewById(R.id.webview);
        mWebView.setListener(this, this);
        mWebView.setMixedContentAllowed(false);
//        mWebView.loadUrl("https://mobileappstarter.com/dashboards/asfarmacia/api/package/openpay_payment?user_id=33&total=1000.0&itemid=4");
        mWebView.loadUrl("https://symptomate.com/diagnosis");

    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    protected void onPause() {
        mWebView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mWebView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
    }

    @Override
    public void onPageFinished(String url) {
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }
}
