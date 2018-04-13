package com.feature.projectone.activity;

import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.util.TvUtil;
import com.feature.projectone.util.WebViewUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/8.
 * 注册协议界面
 */

public class RegistAgreementActivty extends BaseActivity {
    @BindView(R.id.tvTitleRight)
    TextView tvTitleRight;
    @BindView(R.id.webView)
    WebView webView;

    @Override
    protected void Response(String code, String msg, String url, Object result) {
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_regist_agreement);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tvTitleRight.setText(getString(R.string.regist));
        TvUtil.addUnderLine(tvTitleRight);
        tvTitleRight.getPaint().setFakeBoldText(true);
        WebViewUtil.setWebViewSettings(webView);
        webView.loadUrl("https://www.baidu.com/");
    }

    @Override
    public void afterInitView() {
    }

    @OnClick({R.id.tvBack})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                finish();
                break;
        }
    }
}
