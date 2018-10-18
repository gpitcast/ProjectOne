package com.youyi.ywl.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youyi.ywl.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/10.
 * 订单详情界面
 */

public class OrderDetailActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.ll_tobe_pay_bottom)
    LinearLayout ll_tobe_pay_bottom;
    @BindView(R.id.ll_already_complete)
    LinearLayout ll_already_complete;
    private String state;

    @Override
    protected void Response(String code, String msg, String url, Object result) {

    }

    @Override
    protected void handleIntent(Intent intent) {
        if (intent != null) {
            state = intent.getStringExtra("state");
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_order_detail);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        tv_title.setText("订单详情");
        if (state != null) {
            if ("0".equals(state)) {
                //已支付
                ll_tobe_pay_bottom.setVisibility(View.GONE);
                ll_already_complete.setVisibility(View.VISIBLE);
            }
            if ("1".equals(state)) {
                //待支付
                //已支付
                ll_tobe_pay_bottom.setVisibility(View.VISIBLE);
                ll_already_complete.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void afterInitView() {
    }

    @OnClick({R.id.ll_back, R.id.tv_pay_right_now, R.id.tv_cancel_order})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_pay_right_now:
                //立即支付
                startActivity(new Intent(this, OrderConfirmActivity.class));
                break;
            case R.id.tv_cancel_order:
                //取消订单
                startActivity(new Intent(this, OrderCancelActivity.class));
                break;
        }
    }
}
