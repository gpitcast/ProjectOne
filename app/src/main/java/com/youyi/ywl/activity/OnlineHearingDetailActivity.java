package com.youyi.ywl.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.youyi.ywl.R;
import com.youyi.ywl.util.ToastUtil;
import com.youyi.ywl.util.WebViewUtil;
import com.youyi.ywl.view.BasePopupWindow;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/8/6.
 * 在线听力详情界面
 */

public class OnlineHearingDetailActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.webView)
    WebView webView;

    private String url;//听力详情的链接

    @Override
    protected void Response(String code, String msg, String url, Object result) {
    }

    @Override
    protected void handleIntent(Intent intent) {
        if (intent != null) {
            url = intent.getStringExtra("url");
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_online_hearing_detail);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tv_title.setText("在线听力");
        WebViewUtil.setWebViewSettings(webView);
        webView.loadUrl(url);
    }

    @Override
    public void afterInitView() {
    }

    @OnClick({R.id.ll_back, R.id.iv_share})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.iv_share:
                //分享图标
                showSharePop();
                break;
        }
    }

    private View popwindowView;
    private BasePopupWindow basePopupWindow;

    private void showSharePop() {
        if (popwindowView == null) {
            popwindowView = LayoutInflater.from(this).inflate(R.layout.layout_share_pop, null);
            popwindowView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (basePopupWindow != null) {
                        basePopupWindow.dismiss();
                    }
                }
            });
            //微信朋友圈
            popwindowView.findViewById(R.id.iv_wechat_friends_circle).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show(OnlineHearingDetailActivity.this, "点击了微信朋友圈分享", 0);
                }
            });
            //微信
            popwindowView.findViewById(R.id.iv_wechat).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show(OnlineHearingDetailActivity.this, "点击了微信分享", 0);
                }
            });
            //QQ
            popwindowView.findViewById(R.id.iv_qq).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show(OnlineHearingDetailActivity.this, "点击了QQ分享", 0);
                }
            });
            //QQ空间
            popwindowView.findViewById(R.id.iv_qzone).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show(OnlineHearingDetailActivity.this, "点击了QQ空间分享", 0);
                }
            });
            //微博
            popwindowView.findViewById(R.id.iv_weibo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show(OnlineHearingDetailActivity.this, "点击了微博分享", 0);
                }
            });
            //复制链接
            popwindowView.findViewById(R.id.iv_copy_link).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show(OnlineHearingDetailActivity.this, "点击了复制链接", 0);
                }
            });
        }

        if (basePopupWindow == null) {
            basePopupWindow = new BasePopupWindow(this);
            basePopupWindow.setContentView(popwindowView);
            basePopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            basePopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            basePopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
            basePopupWindow.setOutsideTouchable(true);
            basePopupWindow.setFocusable(true);
        }

        basePopupWindow.showAtLocation(findViewById(R.id.ll_base), Gravity.BOTTOM, 0, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
        webView.pauseTimers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.resumeTimers();
        webView.onResume();
    }

    @Override
    protected void onDestroy() {
        webView.destroy();
        webView=null;
        super.onDestroy();
    }
}
