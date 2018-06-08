package com.feature.projectone.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.adapter.GroupListAdapter;
import com.feature.projectone.inter.RecyclerViewOnItemClickListener;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.HttpUtils;
import com.feature.projectone.util.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * Created by Administrator on 2018/5/29.
 */

public class GroupListActivity extends BaseActivity {
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;
    @BindView(R.id.tvTitle)
    TextView tvTitle;

    private static final String dataUrl = HttpUtils.Host + "/qunzhu/get_list";
    private int pageno = 1;
    private GroupListAdapter groupListAdapter;
    private List<HashMap> dataList = new ArrayList<>();

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case dataUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        ArrayList<HashMap<String, Object>> arrayList = (ArrayList<HashMap<String, Object>>) resultMap.get("data");
                        if (arrayList != null && arrayList.size() > 0) {
                            dataList.addAll(arrayList);
                            groupListAdapter.notifyDataSetChanged();
                        }
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
        setContentView(R.layout.activity_group_list);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tvTitle.setText("群组列表");
        PostList();
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        xRecyclerView.setLayoutManager(manager);
        groupListAdapter = new GroupListAdapter(this, dataList);
        xRecyclerView.setAdapter(groupListAdapter);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(false);
        View headerView1 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        View headerView2 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        xRecyclerView.addHeaderView(headerView1);
        xRecyclerView.addHeaderView(headerView2);
        groupListAdapter.setOnItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                ToastUtil.show(GroupListActivity.this, "点击了第" + position + "个条目", 0);
                HashMap map = dataList.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("id", map.get("id") + "");
                RongIM.getInstance().startConversation(GroupListActivity.this, Conversation.ConversationType.GROUP, map.get("groupId") + "", map.get("name") + "", bundle);
//                RongIM.getInstance().startGroupChat(GroupListActivity.this, map.get("groupId") + "", map.get("name") + "");
            }
        });
    }

    private void PostList() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "qunzhu");
        map.put("action", "get_list");
        map.put("p", pageno + "");
        getJsonUtil().PostJson(this, map);
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
