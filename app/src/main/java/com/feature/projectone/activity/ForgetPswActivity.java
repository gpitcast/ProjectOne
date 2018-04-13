package com.feature.projectone.activity;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.EtDrawableLeftUtil;
import com.feature.projectone.util.MsgCodeUtil;
import com.feature.projectone.util.PhoneNumberCheckedUtil;
import com.feature.projectone.util.ToastUtil;
import com.feature.projectone.util.TvUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/8.
 * 忘记密码界面
 */

public class ForgetPswActivity extends BaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_code)
    EditText et_code;
    @BindView(R.id.et_psw)
    EditText et_psw;
    @BindView(R.id.tv_get_number)
    TextView tv_get_number;
    @BindView(R.id.ll_get_number)
    LinearLayout ll_get_number;
    @BindView(R.id.tv_commit)
    TextView tv_commit;
    @BindView(R.id.ll_commit)
    LinearLayout ll_commit;

    private String phoneText;
    private String pswText;
    private String coedText;
    private boolean isCodeWork;//验证码是否超时
    private int restSeconds = 60;//验证码倒计时总时长
    private static final String updatePswUrl = "http://47.104.128.245/api/data/user/password_back";//找回密码接口
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            restSeconds = restSeconds - 1;
            if (restSeconds != 0) {
                tv_get_number.setText(restSeconds + "s后重试");
                handler.postDelayed(this, 1000);
            } else {
                isCodeWork = false;
                ll_get_number.setBackgroundResource(R.drawable.bg_get_number_orange);
                tv_get_number.setTextColor(getResources().getColor(R.color.normal_orange));
                TvUtil.addUnderLine(tv_get_number);
                tv_get_number.setText(getString(R.string.get_number));
                tv_get_number.setEnabled(true);
                handler.removeCallbacks(this);
                restSeconds = 60;
            }
        }
    };

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case updatePswUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap<String, Object> resultMap = (HashMap<String, Object>) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        //找回成功
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                        finish();
                    } else {
                        //找回失败
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                    }
                } else {
                    ToastUtil.show(this, msg, 0);
                }
                break;
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_forget_psw);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        tvTitle.setText(getString(R.string.forget_psw));
        EtDrawableLeftUtil.setEtImgSize(et_phone);
        EtDrawableLeftUtil.setEtImgSize(et_code);
        EtDrawableLeftUtil.setEtImgSize(et_psw);
        TvUtil.addUnderLine(tv_get_number);
        listenEtChanged();
    }

    /**
     * 监听手机输入框，验证码和密码输入框，当三个输入框都有内容时改变登录的背景
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

        et_code.addTextChangedListener(new TextWatcher() {
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

    /**
     * 判断当手机号输入框,验证码输入框和密码输入框都不为空的时候改变登录的背景
     */
    private void checkedText() {
        phoneText = et_phone.getText().toString().trim();
        coedText = et_code.getText().toString().trim();
        pswText = et_psw.getText().toString().trim();

        if (phoneText != null && phoneText.trim().length() > 0 && coedText != null && coedText.length() > 0 && pswText != null && pswText.trim().length() > 0) {
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

    @OnClick({R.id.tvBack, R.id.tv_commit, R.id.tv_get_number})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                finish();
                break;
            case R.id.tv_commit:
                if (et_phone.getText().toString().trim() == null || et_phone.getText().toString().trim().length() == 0) {
                    ToastUtil.show(this, getString(R.string.write_phone_number), 0);
                    return;
                }
                if (!PhoneNumberCheckedUtil.checkNumber(et_phone.getText().toString().trim())) {
                    ToastUtil.show(this, getString(R.string.write_right_phone_number), 0);
                    return;
                }
                if (et_code.getText().toString().trim() == null || et_code.getText().toString().trim().length() != 6) {
                    ToastUtil.show(this, getString(R.string.write_right_phone_code), 0);
                    return;
                }
                if (!et_code.getText().toString().trim().equals(Constanst.phoneCode)) {
                    ToastUtil.show(this, getString(R.string.phone_code_error), 0);
                    return;
                }
                if (et_psw.getText().toString().trim() == null || et_psw.getText().toString().trim().length() == 0) {
                    ToastUtil.show(this, getString(R.string.write_right_phone_psw), 0);
                    return;
                }
                if (et_psw.getText().toString().trim().length() < 6) {
                    ToastUtil.show(this, getString(R.string.write_six_phone_psw), 0);
                    return;
                }
                if (!isCodeWork) {
                    ToastUtil.show(this, getString(R.string.phone_code_time_out), 0);
                    return;
                }
                reSetPsw();//请求重新设置密码接口
                break;
            case R.id.tv_get_number:
                if (PhoneNumberCheckedUtil.checkNumber(et_phone.getText().toString().trim())) {
                    isCodeWork = true;
                    MsgCodeUtil.sendMsg(this, et_phone.getText().toString().trim(), Constanst.registSignature, Constanst.registTemplate);
                    TvUtil.removeUnderLine(tv_get_number);
                    tv_get_number.setText(restSeconds + "s后重试");
                    tv_get_number.setTextColor(getResources().getColor(R.color.light_gray9));
                    ll_get_number.setBackgroundResource(R.drawable.bg_get_number_gray);
                    tv_get_number.setEnabled(false);
                    handler.postDelayed(runnable, 1000);
                } else {
                    ToastUtil.show(this, getString(R.string.write_right_phone_number), 0);
                }
                break;
        }
    }

    //重新设置密码接口
    private void reSetPsw() {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("controller", "user");
        map.put("action", "password_back");
        map.put("tel", et_phone.getText().toString().trim());
        map.put("password", et_psw.getText().toString().trim());
        getJsonUtil().PostJson(this, map, tv_commit);
    }
}
