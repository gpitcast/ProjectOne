package com.youyi.YWL.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youyi.YWL.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/11.
 * 确认订单界面
 */

public class OrderConfirmActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @Override
    protected void Response(String code, String msg, String url, Object result) {
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_order_confirm);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        tv_title.setText("确认订单");
    }

    @Override
    public void afterInitView() {

    }

    @OnClick({R.id.ll_back,R.id.ll_goto_payment})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_goto_payment:
                //前往支付
                startActivity(new Intent(this,OrderPaymentActivity.class));
                break;
        }
    }
}
