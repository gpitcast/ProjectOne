package com.youyi.ywl.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.youyi.ywl.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/11.
 *  支付订单界面
 */

public class OrderPaymentActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @Override
    protected void Response(String code, String msg, String url, Object result) {

    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_order_payment);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        tv_title.setText("支付订单");
    }

    @Override
    public void afterInitView() {

    }

    @OnClick({R.id.ll_back,R.id.tv_goto_recharge})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_goto_recharge:
                //余额不足,去充值
                Intent intent = new Intent(this, RechargeActivity.class);
                startActivity(intent);
                break;
        }
    }
}
