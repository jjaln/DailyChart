package com.jjaln.dailychart.contents.premium;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jjaln.dailychart.R;

public class PremiumActivity extends AppCompatActivity {

    private ImageView ivBack;
    private TextView tvToolbarTitle;
    private WebView mWebView; // 웹뷰 선언
    private WebSettings mWebSettings; //웹뷰세팅
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);

        ivBack = findViewById(R.id.iv_back);

        tvToolbarTitle = findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setText("Premium");
        ivBack.setOnClickListener(v -> {
            finish();
        });

        mWebView = (WebView) findViewById(R.id.webView);

        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setDomStorageEnabled(true);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView webView, String url)
            {
                webView.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('relative lg:sticky lg:top-0 z-40 shadow bg-white dark:bg-black bg-opacity-80 dark:bg-opacity-80 bg-blur')[0].style.display='none'; " +
                        "document.getElementsByClassName('adsbygoogle block')[0].style.display='none'; " +
                        "document.getElementsByClassName('flex mt-4 mb-2 space-x-2 justify-between')[0].style.display='none';" +
                        "document.getElementsByClassName('overflow-x-scroll scroll-hidden my-2 rounded border border-general bg-white dark:bg-gray-900')[0].style.display='none';" +
                        "document.getElementsByClassName('h-72 lg:h-80 relative resize-y overflow-y-auto pb-4 mb-0.5 ')[0].style.display='none';" +
                        "document.getElementsByClassName('lg:hidden block my-2')[0].style.display='none';" +
                        "document.getElementsByClassName('adsbygoogle block my-6')[0].style.display='none';" +
                        "document.getElementsByClassName('mt-8 py-8 pb-24 lg:pb-10 bg-gray-200 dark:bg-gray-800')[0].style.display='none';" +
                        "document.getElementsByClassName('fixed right-4 bottom-4 lg:right-6 lg:bottom-6 pb-safe-0 z-50 inline-block')[0].style.display='none';" +
                        "})()");
            }
        });
        mWebView.loadUrl("https://kimpga.com");
    }
}