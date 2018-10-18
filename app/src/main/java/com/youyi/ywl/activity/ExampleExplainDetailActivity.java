package com.youyi.ywl.activity;

import android.content.Intent;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.youyi.ywl.R;
import com.youyi.ywl.util.WebViewUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/8/28.
 * 习题讲解 - 详情界面
 */

public class ExampleExplainDetailActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.webView)
    WebView webView;

    private String id;
    private String name;

    @Override
    protected void Response(String code, String msg, String url, Object result) {
    }

    @Override
    protected void handleIntent(Intent intent) {
        if (intent != null) {
            id = intent.getStringExtra("id");
            name = intent.getStringExtra("name");
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_example_explain_detail);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tv_title.setText(name);
        WebViewUtil.setWebViewSettings(webView);
        String url = "http://47.104.128.245/youyi/detailH5/showXt/id/" + id + ".html";
        webView.loadUrl(url);
    }

    @Override
    public void afterInitView() {
    }

    @OnClick({R.id.ll_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
                break;
        }
    }
}
