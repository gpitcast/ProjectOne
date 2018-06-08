package com.feature.projectone.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
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
 * 绑定手机号界面
 */

public class BindPhoneNumActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_psw)
    EditText et_psw;
    @BindView(R.id.ll_commit)
    LinearLayout ll_commit;
    @BindView(R.id.tv_commit)
    TextView tv_commit;


    @Override
    protected void Response(String code, String msg, String url, Object result) {

    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_bind_phone_num);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        tv_title.setText("绑定手机号");
        EtDrawableLeftUtil.setEtImgSize(et_phone);
        EtDrawableLeftUtil.setEtImgSize(et_psw);
        listenEtChanged();
    }

    /**
     * 监听手机输入框，验证码,旧密码和新密码输入框，当三个输入框都有内容时改变登录的背景
     */
    private void listenEtChanged() {
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
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
                checkedText();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private String phoneText;
    private String pswText;

    /**
     * 判断当手机号输入框,验证码输入框和密码输入框都不为空的时候改变登录的背景
     */
    private void checkedText() {
        phoneText = et_phone.getText().toString().trim();
        pswText = et_psw.getText().toString().trim();
        if (phoneText != null && phoneText.trim().length() > 0 && pswText != null && pswText.length() > 0) {
            ll_commit.setBackgroundResource(R.drawable.bg_login_dark_btn);
            tv_commit.setEnabled(true);
        } else {
            ll_commit.setBackgroundResource(R.drawable.bg_login_light_btn);
            tv_commit.setEnabled(false);
        }
    }


    @Override
    public void afterInitView() {

    }

    @OnClick({R.id.ll_back, R.id.tv_commit})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_commit:
                startActivity(new Intent(this, SmsCheckActivity.class));
                break;
        }
    }
}
