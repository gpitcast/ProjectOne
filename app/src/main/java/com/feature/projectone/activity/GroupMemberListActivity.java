package com.feature.projectone.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.adapter.GroupMemberListAdapter;
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
import retrofit2.http.POST;

/**
 * Created by Administrator on 2018/5/25.
 * 群成员列表界面
 */

public class GroupMemberListActivity extends BaseActivity {
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvTileNum)
    TextView tvTileNum;
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;

    private static final String dataUrl = HttpUtils.Host + "/qunzhu/get_group_user_lists";
    private List<HashMap<String, Object>> dataList = new ArrayList<>();
    private GroupMemberListAdapter groupMemberListAdapter;
    private String id;
    private int pageno = 1;//分页
    private boolean isLoadMore;

    @Override
    protected void handleIntent(Intent intent) {
        if (intent != null) {
            id = intent.getStringExtra("id");
        }
    }

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case dataUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    ArrayList<HashMap<String, Object>> arrayList = (ArrayList<HashMap<String, Object>>) resultMap.get("data");
                    if (arrayList != null) {
                        if (isLoadMore) {
                            isLoadMore = false;
                            xRecyclerView.loadMoreComplete();
                        }
                        dataList.addAll(arrayList);
                        tvTileNum.setText("(" + dataList.size() + ")");
                        groupMemberListAdapter.notifyDataSetChanged();
                    } else {
                        if (isLoadMore) {
                            pageno--;
                            isLoadMore = false;
                            xRecyclerView.loadMoreComplete();
                        }
                    }
                } else {
                    if (isLoadMore) {
                        pageno--;
                        isLoadMore = false;
                        xRecyclerView.loadMoreComplete();
                    }
                    ToastUtil.show(this, msg, 0);
                }
                break;
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_group_member_list);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        tvTitle.setText("群成员");
        tvTileNum.setVisibility(View.VISIBLE);
        PostList();
        initRecyclerView();
    }

    private void PostList() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "qunzhu");
        map.put("action", "get_group_user_lists");
        map.put("id", id);
        map.put("p", pageno + "");
        getJsonUtil().PostJson(this, map);
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        xRecyclerView.setLayoutManager(manager);
        groupMemberListAdapter = new GroupMemberListAdapter(this, dataList);
        xRecyclerView.setAdapter(groupMemberListAdapter);
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
