package com.feature.projectone.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import com.feature.projectone.R;
import com.feature.projectone.adapter.NewsHeadLinesDetailsAdapter;
import com.feature.projectone.util.WebViewUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/30.\
 * 新闻头条详情界面
 */

public class NewsHeadlinesDetailsActivity extends BaseActivity {
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;

    private WebView webView;
    private NewsHeadLinesDetailsAdapter newsHeadLinesDetailsAdapter;

    @Override
    protected void Response(String code, String msg, String url, Object result) {

    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_news_headlines_details);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        initXRecyclerView();
    }

    /**
     * 初始化XRecyclerView
     */
    private void initXRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        xRecyclerView.setLayoutManager(manager);
        newsHeadLinesDetailsAdapter = new NewsHeadLinesDetailsAdapter(this);
        xRecyclerView.setAdapter(newsHeadLinesDetailsAdapter);
        View headView = LayoutInflater.from(this).inflate(R.layout.head_news_headlines_details, null);
        webView = ((WebView) headView.findViewById(R.id.webView));
        WebViewUtil.setWebViewSettings(webView);
        webView.loadUrl("https://www.baidu.com/");
        xRecyclerView.addHeaderView(headView);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(true);
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
