package com.feature.projectone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.adapter.GroupFolderListAdapter;
import com.feature.projectone.inter.RecyclerViewOnItemClickListener;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.HttpUtils;
import com.feature.projectone.util.ToastUtil;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/25.
 * 群文件夹列表界面
 */

public class GroupFolderListActivity extends BaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;
    @BindView(R.id.tv_files_count)
    TextView tv_files_count;

    private static final String dataUrl = HttpUtils.Host + "/qunzhu/get_file";
    private GroupFolderListAdapter groupFolderListAdapter;
    private String id;
    private int pageno = 1;
    private boolean isLoadMore;
    private List<HashMap<String, Object>> dataList = new ArrayList<>();

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case dataUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    ArrayList<HashMap<String, Object>> arrayList = (ArrayList<HashMap<String, Object>>) resultMap.get("data");
                    if (arrayList != null && arrayList.size() > 0) {
                        if (isLoadMore) {
                            isLoadMore = false;
                            xRecyclerView.loadMoreComplete();
                        }
                        dataList.addAll(arrayList);
                        tv_files_count.setText("共 " + resultMap.get("nums") + " 个文件");//文件总数量
                        groupFolderListAdapter.notifyDataSetChanged();
                    } else {
                        if (isLoadMore) {
                            pageno--;
                            isLoadMore = false;
                            xRecyclerView.loadMoreComplete();
                        }
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
        setContentView(R.layout.activity_group_folder_list);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tvTitle.setText("群文件");
        PostList();
        initRecyclerView();
    }

    private void PostList() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "qunzhu");
        map.put("action", "get_file");
        map.put("id", id);
        map.put("p", pageno + "");
        getJsonUtil().PostJson(this, map);
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        xRecyclerView.setLayoutManager(manager);
        groupFolderListAdapter = new GroupFolderListAdapter(this, dataList);
        xRecyclerView.setAdapter(groupFolderListAdapter);
        View headView1 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        View headView2 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        xRecyclerView.addHeaderView(headView1);
        xRecyclerView.addHeaderView(headView2);
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
                isLoadMore = true;
                pageno++;
                PostList();
            }
        });

        View footerView = LayoutInflater.from(this).inflate(R.layout.layout_recyclerview_footer, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
        layoutParams.gravity = Gravity.CENTER;
        footerView.setLayoutParams(layoutParams);
        footerView.setBackgroundColor(getResources().getColor(R.color.white));
        xRecyclerView.setFootView(footerView);
        xRecyclerView.loadMoreComplete();

        groupFolderListAdapter.setOnItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                HashMap<String, Object> map = dataList.get(position);
                Intent intent = new Intent(GroupFolderListActivity.this, GroupFileSingleActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("dataMap", ((Serializable) map));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
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
