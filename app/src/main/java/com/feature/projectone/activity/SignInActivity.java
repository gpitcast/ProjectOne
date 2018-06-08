package com.feature.projectone.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.adapter.SignInAdapter;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/9.
 * 签到界面
 */

public class SignInActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;
    private SignInAdapter signInAdapter;
    private TextView tv_sign_in;

    @Override
    protected void Response(String code, String msg, String url, Object result) {

    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_sign_in);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        tv_title.setText("签到");
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        xRecyclerView.setLayoutManager(manager);
        signInAdapter = new SignInAdapter(this);
        xRecyclerView.setAdapter(signInAdapter);
        View headerView1 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        View headerView2 = LayoutInflater.from(this).inflate(R.layout.layout_sign_in_head, null);
        xRecyclerView.addHeaderView(headerView1);
        xRecyclerView.addHeaderView(headerView2);
        intiHeaderView(headerView2);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        //刷新和加载监听
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {

            }
        });

        View footerView = LayoutInflater.from(this).inflate(R.layout.layout_recyclerview_footer, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
        layoutParams.gravity = Gravity.CENTER;
        footerView.setLayoutParams(layoutParams);
        footerView.setBackgroundColor(getResources().getColor(R.color.white));
        xRecyclerView.setFootView(footerView);
        xRecyclerView.loadMoreComplete();

    }

    private void intiHeaderView(View headerView) {
        tv_sign_in = ((TextView) headerView.findViewById(R.id.tv_sign_in));//签到按钮
        tv_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_sign_in.setText("已签到");
                tv_sign_in.setBackgroundResource(R.drawable.bg_dark_gray_btn);
            }
        });
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
