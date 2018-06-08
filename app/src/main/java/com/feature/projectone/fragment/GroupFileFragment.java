package com.feature.projectone.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.feature.projectone.R;
import com.feature.projectone.activity.GroupFolderListActivity;
import com.feature.projectone.adapter.GroupFileAdapter;
import com.feature.projectone.util.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/28.
 * 群组信息里面的群文件fragment
 */

@SuppressLint("ValidFragment")
public class GroupFileFragment extends BaseFragment {
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;

    private GroupFileAdapter groupFileAdapter;
    private String id;

    public GroupFileFragment(String id) {
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

    public void notifyDataSetChanged(List<HashMap<String, Object>> mFileLists) {
        if (groupFileAdapter != null) {
            groupFileAdapter.refresh(mFileLists);
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_group_file;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        initRecyclerView();
    }


    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        xRecyclerView.setLayoutManager(manager);
        groupFileAdapter = new GroupFileAdapter(getActivity());
        xRecyclerView.setAdapter(groupFileAdapter);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(false);
    }

    @OnClick({R.id.linearLayout})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.linearLayout:
                Intent intent = new Intent(getActivity(), GroupFolderListActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                break;
        }
    }
}
