package com.youyi.ywl.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.youyi.ywl.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/12.
 * 钱包管理界面
 */

public class ManagePurseActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_right)
    ImageView iv_right;

    @Override
    protected void Response(String code, String msg, String url, Object result) {
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_manage_purse);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tv_title.setText("钱包管理");
        iv_right.setVisibility(View.VISIBLE);
        iv_right.setImageResource(R.mipmap.icon_question_mark_gray);
    }

    @Override
    public void afterInitView() {
    }

    @OnClick({R.id.ll_back, R.id.tv_goto_recharge})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_goto_recharge:
                //去充值
                startActivity(new Intent(this, RechargeActivity.class));
                break;
        }
    }
}
