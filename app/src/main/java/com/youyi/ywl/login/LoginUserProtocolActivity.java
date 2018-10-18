package com.youyi.ywl.login;

import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.youyi.ywl.R;
import com.youyi.ywl.activity.BaseActivity;
import com.youyi.ywl.other.Constanst;
import com.youyi.ywl.util.HttpUtils;
import com.youyi.ywl.util.ToastUtil;
import com.youyi.ywl.util.WebViewUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/10/12.
 * 登录  用户协议界面
 */

public class LoginUserProtocolActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.webView)
    WebView webView;

    private static final String PROTOCOL_URL = HttpUtils.Host + "/user2/protocol";//用户协议接口

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case PROTOCOL_URL:
                dismissLoadingDialog();
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        HashMap<String, Object> dataMap = (HashMap<String, Object>) resultMap.get("data");
                        String protocol_url = dataMap.get("url") + "";
                        WebViewUtil.setWebViewSettings(webView);
                        webView.loadUrl(protocol_url);
                    } else {
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                    }
                } else {
                    ToastUtil.show(this, msg, 0);
                }
                break;
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_login_user_protocol);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tv_title.setText("用户协议");
        PostProtocolList();
    }

    /**
     * 请求用户协议接口
     */
    private void PostProtocolList() {
        showLoadingDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user2");
        map.put("action", "protocol");
        getJsonUtil().PostJson(this, map);
    }

    @Override
    public void afterInitView() {
    }

    @OnClick({R.id.ll_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }
}
