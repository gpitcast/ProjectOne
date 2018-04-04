package com.feature.projectone.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.TvUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/30.
 * 登录界面
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.tv_regist)
    TextView tv_regist;//注册的TextView
    @BindView(R.id.et_phone)
    EditText et_phone;//手机输入框
    @BindView(R.id.et_psw)
    EditText et_psw;//密码输入框
    @BindView(R.id.ll_login)
    LinearLayout ll_login;//登录按钮布局

    private String phoneText;
    private String pswText;

    @Override
    protected void Response(String code, String msg, String url, Object result) {

    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        TvUtil.addUnderLine(tv_regist);
        listenEtChanged();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    /**
     * 监听手机输入框和密码输入框，当两个输入框都有内容时改变登录的背景
     */
    private void listenEtChanged() {
        et_phone.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                phoneText = charSequence.toString();
                checkedText();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        et_psw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pswText = charSequence.toString();
                checkedText();
//                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    /**
     * 判断当手机号输入框和密码输入框都不为空的时候改变登录的背景
     */
    private void checkedText() {
        if (phoneText != null && phoneText.trim().length() > 0 && pswText != null && pswText.trim().length() > 0) {
            ll_login.setBackgroundResource(R.drawable.bg_login_dark_btn);
        } else {
            ll_login.setBackgroundResource(R.drawable.bg_login_dark_btn);
        }
    }

    @OnClick({R.id.tv_regist, R.id.ll_back, R.id.ll_login, R.id.tv_forget_psw})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_regist:
                //跳转注册页面
                startActivity(new Intent(this, RegistActivity.class));
                break;
            case R.id.ll_back:
                //退出应用
                finish();
                break;
            case R.id.ll_login:
                //登录
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.tv_forget_psw:
                startActivity(new Intent(this, SourceDownloadActivity.class));
                break;
        }
    }


    @Override
    public void afterInitView() {
    }
}
