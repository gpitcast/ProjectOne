package com.feature.projectone.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.adapter.CommunityFriendCircleAdapter;
import com.feature.projectone.inter.RecyclerViewOnItemClickListener;
import com.feature.projectone.util.ToastUtil;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/17.
 * 社区集结令-社区详细界面
 */

public class CommunityDetailActivity extends BaseActivity {
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;
    @BindView(R.id.tv_title)
    TextView tv_title;
    private CommunityFriendCircleAdapter communityFriendCircleAdapter;

    @Override
    protected void Response(String code, String msg, String url, Object result) {

    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_community_detail);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        tv_title.setText("高三语文兴趣组");
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        xRecyclerView.setLayoutManager(manager);
        communityFriendCircleAdapter = new CommunityFriendCircleAdapter(this);
        xRecyclerView.setAdapter(communityFriendCircleAdapter);
        View headerView1 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        View headerView2 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        xRecyclerView.addHeaderView(headerView1);
        xRecyclerView.addHeaderView(headerView2);
        xRecyclerView.setPullRefreshEnabled(true);
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

        communityFriendCircleAdapter.setOnItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                ToastUtil.show(CommunityDetailActivity.this, "点击了第" + position + "个条目", 0);
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
