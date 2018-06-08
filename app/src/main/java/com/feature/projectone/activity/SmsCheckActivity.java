package com.feature.projectone.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.util.ToastUtil;
import com.feature.projectone.view.VerifyCodeView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/12.
 * 短信验证界面
 */

public class SmsCheckActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.verifyCodeView)
    VerifyCodeView verifyCodeView;

    @Override
    protected void Response(String code, String msg, String url, Object result) {

    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_sms_check);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tv_title.setText("短信验证");
        verifyCodeView.setInputCompleteListener(new VerifyCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {
                ToastUtil.show(SmsCheckActivity.this, "inputComplete: " + verifyCodeView.getEditContent(), 0);
                startActivity(new Intent(SmsCheckActivity.this, BindNewPhoneNumActivity.class));
            }

            @Override
            public void invalidContent() {
                ToastUtil.show(SmsCheckActivity.this, "invalidContent: " + verifyCodeView.getEditContent(), 0);
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
