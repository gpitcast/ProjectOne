package com.feature.projectone.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.adapter.MyCreditAdapter;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/10.
 * 我的学分界面
 */

public class MyCreditActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;

    private MyCreditAdapter myCreditAdapter;

    @Override
    protected void Response(String code, String msg, String url, Object result) {

    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_my_credit);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        tv_title.setText("我的学分");
        tv_right.setVisibility(View.VISIBLE);
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        xRecyclerView.setLayoutManager(manager);
        myCreditAdapter = new MyCreditAdapter(this);
        xRecyclerView.setAdapter(myCreditAdapter);
        View headerView1 = LayoutInflater.from(this).inflate(R.layout.head_my_credit, null);
        View headerView2 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        xRecyclerView.addHeaderView(headerView1);
        xRecyclerView.addHeaderView(headerView2);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {

            }
        });

        //自定义加载footer
        View footerView = LayoutInflater.from(this).inflate(R.layout.layout_recyclerview_footer, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
        layoutParams.gravity = Gravity.CENTER;
        footerView.setLayoutParams(layoutParams);
//        footerView.setBackgroundColor(getResources().getColor(R.color.white));
        xRecyclerView.setFootView(footerView);
        TextView tv_status = (TextView) footerView.findViewById(R.id.tv_status);
        tv_status.setText("没有更多数据");
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
