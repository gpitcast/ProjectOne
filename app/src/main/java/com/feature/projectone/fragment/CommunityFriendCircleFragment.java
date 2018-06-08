package com.feature.projectone.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.feature.projectone.R;
import com.feature.projectone.adapter.CommunityFriendCircleAdapter;
import com.feature.projectone.inter.RecyclerViewOnItemClickListener;
import com.feature.projectone.util.ToastUtil;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/16.
 * 社区朋友圈fragment
 */

public class CommunityFriendCircleFragment extends BaseFragment {
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;
    private CommunityFriendCircleAdapter communityFriendCircleAdapter;

    @Override
    protected void Response(String code, String msg, String url, Object result) {

    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_community_friend_circle;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        xRecyclerView.setLayoutManager(manager);
        communityFriendCircleAdapter = new CommunityFriendCircleAdapter(getActivity());
        xRecyclerView.setAdapter(communityFriendCircleAdapter);
        View headerView1 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty_head, null);
        View headerView2 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty_head, null);
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

        View footerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_recyclerview_footer, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
        layoutParams.gravity = Gravity.CENTER;
        footerView.setLayoutParams(layoutParams);
        footerView.setBackgroundColor(getResources().getColor(R.color.white));
        xRecyclerView.setFootView(footerView);
        xRecyclerView.loadMoreComplete();

        communityFriendCircleAdapter.setOnItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                ToastUtil.show(getActivity(), "点击了第" + position + "个条目", 0);
            }
        });
    }
}
