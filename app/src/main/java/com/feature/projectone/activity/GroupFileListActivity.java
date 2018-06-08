//package com.feature.projectone.activity;
//
//import android.content.Intent;
//import android.support.v7.widget.LinearLayoutManager;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.feature.projectone.R;
//import com.feature.projectone.adapter.GroupFileListAdapter;
//import com.feature.projectone.inter.RecyclerViewOnItemClickListener;
//import com.feature.projectone.util.ToastUtil;
//import com.jcodecraeer.xrecyclerview.ProgressStyle;
//import com.jcodecraeer.xrecyclerview.XRecyclerView;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//
///**
// * Created by Administrator on 2018/5/25.
// * 群文件列表界面
// */
//
//public class GroupFileListActivity extends BaseActivity {
//    @BindView(R.id.tvTitle)
//    TextView tvTitle;
//    @BindView(R.id.xRecyclerView)
//    XRecyclerView xRecyclerView;
//    private GroupFileListAdapter groupFileListAdapter;
//
//    @Override
//    protected void Response(String code, String msg, String url, Object result) {
//
//    }
//
//    @Override
//    public void setContentLayout() {
//        setContentView(R.layout.activity_group_file_list);
//    }
//
//    @Override
//    public void beforeInitView() {
//    }
//
//    @Override
//    public void initView() {
//        tvTitle.setText("2019 春学练优五年级课外知识阅读理解");
//        initRecyclerView();
//    }
//
//    private void initRecyclerView() {
//        LinearLayoutManager manager = new LinearLayoutManager(this);
//        xRecyclerView.setLayoutManager(manager);
//        groupFileListAdapter = new GroupFileListAdapter(this);
//        xRecyclerView.setAdapter(groupFileListAdapter);
//        View headerView1 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
//        View headerView2 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
//        xRecyclerView.addHeaderView(headerView1);
//        xRecyclerView.addHeaderView(headerView2);
//        xRecyclerView.setPullRefreshEnabled(false);
//        xRecyclerView.setLoadingMoreEnabled(false);
//        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
//        //刷新和加载监听
//        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
//            @Override
//            public void onRefresh() {
//            }
//
//            @Override
//            public void onLoadMore() {
//
//            }
//        });
//
//        View footerView = LayoutInflater.from(this).inflate(R.layout.layout_recyclerview_footer, null);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
//        layoutParams.gravity = Gravity.CENTER;
//        footerView.setLayoutParams(layoutParams);
//        footerView.setBackgroundColor(getResources().getColor(R.color.white));
//        xRecyclerView.setFootView(footerView);
//        xRecyclerView.loadMoreComplete();
//
//        groupFileListAdapter.setOnItemClickListener(new RecyclerViewOnItemClickListener() {
//            @Override
//            public void OnItemClick(View view, int position) {
//                ToastUtil.show(GroupFileListActivity.this, "点击了第" + position + "个条目",0);
//                startActivity(new Intent(GroupFileListActivity.this,GroupFileSingleActivity.class));
//            }
//        });
//    }
//
//    @Override
//    public void afterInitView() {
//    }
//
//    @OnClick({R.id.tvBack})
//    public void OnClick(View view) {
//        switch (view.getId()) {
//            case R.id.tvBack:
//                finish();
//                break;
//        }
//    }
//}
