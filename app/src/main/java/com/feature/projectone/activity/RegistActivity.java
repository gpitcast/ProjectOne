package com.feature.projectone.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.util.EtDrawableLeftUtil;
import com.feature.projectone.util.TvUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/29.
 * 注册界面
 */

public class RegistActivity extends BaseActivity {
    @BindView(R.id.tv_login)
    TextView tv_login; //登录控件
    @BindView(R.id.tv_get_number)
    TextView tv_get_number;//获取验证码TextView
    @BindView(R.id.tv_regist_now)
    TextView tv_regist_now;//立即注册TextView
    @BindView(R.id.et_phone)
    EditText et_phone;//手机号输入框
    @BindView(R.id.et_code)
    EditText et_code;//验证码输入框
    @BindView(R.id.et_psw)
    EditText et_psw;//密码输入框

    @Override
    protected void Response(String code, String msg, String url, Object result) {

    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_regist);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        TvUtil.addUnderLine(tv_login);
        TvUtil.addUnderLine(tv_get_number);
        TvUtil.addUnderLine(tv_regist_now);
        EtDrawableLeftUtil.setEtImgSize(et_phone);
        EtDrawableLeftUtil.setEtImgSize(et_code);
        EtDrawableLeftUtil.setEtImgSize(et_psw);
    }

    @OnClick({R.id.tv_login, R.id.ll_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                EventBus.getDefault().post("关闭登录界面");
                break;
            case R.id.ll_back:
                finish();
                break;
        }
    }


    @Override
    public void afterInitView() {
    }
}
