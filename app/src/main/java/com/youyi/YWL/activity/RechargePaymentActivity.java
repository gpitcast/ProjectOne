package com.youyi.YWL.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.youyi.YWL.R;
import com.youyi.YWL.adapter.RechargePaymentRecyclerAdapter;
import com.youyi.YWL.inter.ReplyIconClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/25.
 * 充值支付界面
 */

public class RechargePaymentActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<Boolean> checkedList;
    private RechargePaymentRecyclerAdapter rechargePaymentRecyclerAdapter;

    @Override
    protected void Response(String code, String msg, String url, Object result) {

    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_recharge_payment);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        tv_title.setText("充值支付");
        initCheckedList();
        initRecyclerView();
    }

    private void initCheckedList() {
        if (checkedList == null) {
            checkedList = new ArrayList<>();
            checkedList.add(false);
            checkedList.add(false);
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        rechargePaymentRecyclerAdapter = new RechargePaymentRecyclerAdapter(this, checkedList);
        recyclerView.setAdapter(rechargePaymentRecyclerAdapter);
        rechargePaymentRecyclerAdapter.setOnCheckedClickListener(new ReplyIconClickListener() {
            @Override
            public void OnReplyIconClick(View view, int position) {
                for (int i = 0; i < checkedList.size(); i++) {
                    if (position==i){
                        checkedList.set(i,true);
                    }else {
                        checkedList.set(i,false);
                    }
                }
                rechargePaymentRecyclerAdapter.notifyDataSetChanged();
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
