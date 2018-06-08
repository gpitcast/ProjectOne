package com.feature.projectone.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feature.projectone.R;
import com.feature.projectone.activity.GroupNoticeListActivity;
import com.feature.projectone.adapter.GroupNoticeAdapter;
import com.feature.projectone.util.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/28.
 * 群租信息里面的群公告fragment
 */

@SuppressLint("ValidFragment")
public class GroupNoticeFragment extends BaseFragment {
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;

    private GroupNoticeAdapter groupNoticeAdapter;
    private String id;

    public GroupNoticeFragment(String id) {
        this.id = id;
    }

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
        return R.layout.fragment_group_notice;
    }

    public void notifyDataSetChanged(List<HashMap<String, Object>> mNoticeLists) {
        if (groupNoticeAdapter != null) {
            groupNoticeAdapter.refresh(mNoticeLists);
        }
    }


    @Override
    protected void initViewsAndEvents(View view) {
        initRecycleView();
    }


    private void initRecycleView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        xRecyclerView.setLayoutManager(manager);
        groupNoticeAdapter = new GroupNoticeAdapter(getActivity());
        xRecyclerView.setAdapter(groupNoticeAdapter);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(false);
    }

    @OnClick({R.id.linearLayout})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.linearLayout:
                Intent intent = new Intent(getActivity(), GroupNoticeListActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                break;
        }
    }
}
