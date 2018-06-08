package com.feature.projectone.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.util.EtDrawableLeftUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/12.
 * 更换手机号界面
 */

public class ChangePhoneNumActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.et_phone)
    EditText et_phone;

    @Override
    protected void Response(String code, String msg, String url, Object result) {

    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_change_phone_num);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        tv_title.setText("更换手机号");
        EtDrawableLeftUtil.setEtImgSize(et_phone);
    }

    @Override
    public void afterInitView() {

    }

    @OnClick({R.id.ll_back, R.id.tv_next_step})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_next_step:
                //下一步
                startActivity(new Intent(this,SmsCheckActivity.class));
                break;
        }
    }
}
