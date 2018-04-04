package com.feature.projectone.util;

import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Administrator on 2018/3/30.
 */

public class WebViewUtil {

    public static void setWebViewSettings(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);     //缩放至屏幕的大小
        webSettings.setAllowFileAccess(true);      //设置可以访问文件
        webSettings.setSupportZoom(true);    //设置支持缩放
        webSettings.setLoadsImagesAutomatically(true);    //设置自动加载图片
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);   //设置缓存模式
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.setFocusable(false);
    }
}
