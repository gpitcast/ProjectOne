package com.feature.projectone.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.adapter.GroupNoticeListAdapter;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.HttpUtils;
import com.feature.projectone.util.ToastUtil;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/6/1.
 * 群公告列表界面
 */

public class GroupNoticeListActivity extends BaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;

    private static final String dataUrl = HttpUtils.Host + "/qunzhu/get_notice";
    private GroupNoticeListAdapter groupNoticeListAdapter;
    private int pageno = 1;
    private String id;
    private List<HashMap<String, Object>> dataList = new ArrayList<>();
    private boolean isLoadMore;
    private TextView tv_status;

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case dataUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    ArrayList<HashMap<String, Object>> arrayList = (ArrayList<HashMap<String, Object>>) resultMap.get("data");
                    if (arrayList != null && arrayList.size() > 0) {
                        dataList.addAll(arrayList);
                        if (isLoadMore) {
                            isLoadMore = false;
                            xRecyclerView.loadMoreComplete();
                        }
                        groupNoticeListAdapter.notifyDataSetChanged();
                        tv_status.setText("加载完成");
                    } else {
                        if (isLoadMore) {
                            isLoadMore = false;
                            pageno--;
                            xRecyclerView.loadMoreComplete();
                        }
                        ToastUtil.show(this, "没有更多数据了", 0);
                    }
                } else {
                    if (isLoadMore) {
                        isLoadMore = false;
                        pageno--;
                        xRecyclerView.loadMoreComplete();
                    }
                    ToastUtil.show(this, msg, 0);
                }
                break;
        }
    }

    @Override
    protected void handleIntent(Intent intent) {
        if (intent != null) {
            id = intent.getStringExtra("id");
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_notice_group_list);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        tvTitle.setText("群公告");
        PostList();
        initRecyclerView();
    }

    private void PostList() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "qunzhu");
        map.put("action", "get_notice");
        map.put("p", pageno + "");
        map.put("id", id);
        getJsonUtil().PostJson(this, map);
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        xRecyclerView.setLayoutManager(manager);
        groupNoticeListAdapter = new GroupNoticeListAdapter(this, dataList);
        xRecyclerView.setAdapter(groupNoticeListAdapter);
        View headerView1 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        View headerView2 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        xRecyclerView.addHeaderView(headerView1);
        xRecyclerView.addHeaderView(headerView2);
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
                pageno++;
                isLoadMore = true;
                tv_status.setText("正在加载中...");
                PostList();
            }
        });

        //自定义加载footer
        View footerView = LayoutInflater.from(this).inflate(R.layout.layout_recyclerview_footer, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
        layoutParams.gravity = Gravity.CENTER;
        footerView.setLayoutParams(layoutParams);
        xRecyclerView.setFootView(footerView);
        tv_status = (TextView) footerView.findViewById(R.id.tv_status);
        tv_status.setText("");
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
