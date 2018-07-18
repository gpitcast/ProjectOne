package com.youyi.YWL.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.youyi.YWL.R;
import com.youyi.YWL.adapter.CreditMallAdapter;
import com.youyi.YWL.inter.RecyclerViewOnItemClickListener;
import com.youyi.YWL.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/9.
 * 积分商城界面
 */

public class CreditMallActivity extends BaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvTitleRight)
    TextView tvTitleRight;
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;

    private CreditMallAdapter creditMallAdapter;
    private TextView tv_status;

    @Override
    protected void Response(String code, String msg, String url, Object result) {

    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_credit_mall);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tvTitle.setText("积分商城");
        tvTitleRight.setText("说明");
        initRecyclerView();
    }


    private void initRecyclerView() {
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        xRecyclerView.setLayoutManager(manager);
        creditMallAdapter = new CreditMallAdapter(this);
        xRecyclerView.setAdapter(creditMallAdapter);
        View headerView1 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        View headerView2 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        xRecyclerView.addHeaderView(headerView1);
        xRecyclerView.addHeaderView(headerView2);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {

            }
        });

        creditMallAdapter.setOnItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                ToastUtil.show(CreditMallActivity.this, "点击了第" + position + "个条目", 0);
            }
        });

        //自定义加载footer
        View footerView = LayoutInflater.from(this).inflate(R.layout.layout_recyclerview_footer, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
        layoutParams.gravity = Gravity.CENTER;
        footerView.setLayoutParams(layoutParams);
        xRecyclerView.setFootView(footerView);
        footerView.setBackgroundColor(getResources().getColor(R.color.light_gray5));
        tv_status = (TextView) footerView.findViewById(R.id.tv_status);
        tv_status.setText("没有更多数据");
        tv_status.setTextColor(getResources().getColor(R.color.grayone));
    }


    @Override
    public void afterInitView() {

    }

    @OnClick({R.id.tvBack, R.id.ll_exchange_history})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                finish();
                break;
            case R.id.ll_exchange_history:
                //兑换记录
                startActivity(new Intent(this, CreditExchangeRecordActivity.class));
                break;
        }
    }
}
