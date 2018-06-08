package com.feature.projectone.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.adapter.CommonProblemAdapter;
import com.feature.projectone.inter.RecyclerViewOnItemClickListener;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.HttpUtils;
import com.feature.projectone.util.JsonUtils;
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
 * Created by Administrator on 2018/5/18.
 * 常见问题界面
 */

public class CommonProblemActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;

    private static final String dataUrl = HttpUtils.Host + "/faq/index";
    private CommonProblemAdapter commonProblemAdapter;
    private List<HashMap<String, Object>> dataList = new ArrayList<>();//数据集合
    private int pageno = 1;
    private boolean isLoadMore;

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
                        commonProblemAdapter.notifyDataSetChanged();
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
    public void setContentLayout() {
        setContentView(R.layout.activity_common_problem);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        tv_title.setText("常见问题");
        initRecyclerView();
        PostList();
    }

    private void PostList() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "faq");
        map.put("action", "index");
        map.put("p", pageno);//页数
        getJsonUtil().PostJson(this, map);
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        xRecyclerView.setLayoutManager(manager);
        commonProblemAdapter = new CommonProblemAdapter(this, dataList);
        xRecyclerView.setAdapter(commonProblemAdapter);
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
                isLoadMore = true;
                pageno++;
                PostList();
            }
        });

        //自定义加载footer
        View footerView = LayoutInflater.from(this).inflate(R.layout.layout_recyclerview_footer, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
        layoutParams.gravity = Gravity.CENTER;
        footerView.setLayoutParams(layoutParams);
        xRecyclerView.setFootView(footerView);
        TextView tv_status = (TextView) footerView.findViewById(R.id.tv_status);
        tv_status.setText("没有更多数据");

        commonProblemAdapter.setOnItemClickLinstener(new RecyclerViewOnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                ToastUtil.show(CommonProblemActivity.this, "点击了第" + position + "个条目", 0);
                HashMap<String, Object> map = dataList.get(position);
                Intent intent = new Intent(CommonProblemActivity.this, CommonProblemAnswerActivity.class);
                intent.putExtra("title", map.get("title") + "");
                intent.putExtra("content", map.get("info") + "");
                startActivity(intent);
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
