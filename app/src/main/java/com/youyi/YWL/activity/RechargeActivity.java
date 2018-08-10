package com.youyi.YWL.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.youyi.YWL.R;
import com.youyi.YWL.adapter.RechargeAdapter;
import com.youyi.YWL.adapter.RechargeRecyclerAdapter;
import com.youyi.YWL.inter.RecyclerViewOnItemClickListener;
import com.youyi.YWL.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/12.
 * 充值界面
 */

public class RechargeActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private RechargeRecyclerAdapter rechargeRecyclerAdapter;

    @Override
    protected void Response(String code, String msg, String url, Object result) {

    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_recharge);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tv_title.setText("充值");
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("充值须知");
        tv_right.setTextColor(getResources().getColor(R.color.dark_black2));
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(manager);

        rechargeRecyclerAdapter = new RechargeRecyclerAdapter(this);
        recyclerView.setAdapter(rechargeRecyclerAdapter);
        rechargeRecyclerAdapter.setOnItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                ToastUtil.show(RechargeActivity.this, "点击了第" + position + "个条目", 0);
                Intent intent = new Intent(RechargeActivity.this, RechargePaymentActivity.class);
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
