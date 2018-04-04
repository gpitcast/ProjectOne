package com.feature.projectone.fragment;

import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.feature.projectone.R;
import com.feature.projectone.activity.NewsHeadlinesDetailsActivity;
import com.feature.projectone.adapter.NewsAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/21.
 */

public class NewsFragment extends BaseFragment {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerNews)
    RecyclerView recyclerNews;

    private ArrayList<String> newsList = new ArrayList<>();
    private NewsAdapter newsAdapter;

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
        return R.layout.fragment_news;
    }

    @Override
    protected void initViewsAndEvents(View view) {

        for (int i = 0; i < 4; i++) {
            newsList.add(i + "");
        }
        newsAdapter = new NewsAdapter(getActivity(), newsList);
        recyclerNews.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_gray));
        recyclerNews.addItemDecoration(dividerItemDecoration);
        recyclerNews.setAdapter(newsAdapter);
        newsAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                startActivity(new Intent(getActivity(), NewsHeadlinesDetailsActivity.class));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }
}
