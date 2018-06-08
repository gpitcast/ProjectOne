package com.feature.projectone.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.CheckLoginUtil;
import com.feature.projectone.util.EtDrawableLeftUtil;
import com.feature.projectone.util.HttpUtils;
import com.feature.projectone.util.MsgCodeUtil;
import com.feature.projectone.util.PhoneNumberCheckedUtil;
import com.feature.projectone.util.ShareUtil;
import com.feature.projectone.util.ToastUtil;
import com.feature.projectone.util.TvUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/12.
 * 登入密码修改
 */

public class UpdateLoginPswActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.et_new_psw)
    EditText et_new_psw;
    @BindView(R.id.et_confirm_new_psw)
    EditText et_confirm_new_psw;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_code)
    EditText et_code;
    @BindView(R.id.ll_commit)
    LinearLayout ll_commit;
    @BindView(R.id.tv_commit)
    TextView tv_commit;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.tv_get_number)
    TextView tv_get_number;
    @BindView(R.id.ll_get_number)
    LinearLayout ll_get_number;

    private static final String updatePswUrl = HttpUtils.Host + "/user/update_passwd";
    private boolean isCodeWork;//验证码是否超时
    private int restSeconds = 60;//验证码倒计时总时长
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
                tv_get_number.setText(getString(R.string.get_number));
                TvUtil.addUnderLine(tv_get_number);
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
                dismissLoadingDialog();
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        //请求成功
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                        //跳转到登录界面
                        EventBus.getDefault().post("修改登录密码成功");
                        ShareUtil.removeString(this, Constanst.UER_TOKEN);
                        finish();
                        startActivity(new Intent(this, LoginAndRegistActivity.class));
                    } else {
                        //请求失败
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
        setContentView(R.layout.activity_update_login_psw);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tv_title.setText("登录密码修改");
        EtDrawableLeftUtil.setEtImgSize(et_new_psw);
        EtDrawableLeftUtil.setEtImgSize(et_confirm_new_psw);
        EtDrawableLeftUtil.setEtImgSize(et_phone);
        EtDrawableLeftUtil.setEtImgSize(et_code);
        listenEtChanged();
    }

    /**
     * 监听手机输入框，验证码,旧密码和新密码输入框，当三个输入框都有内容时改变登录的背景
     */
    private void listenEtChanged() {
        et_new_psw.addTextChangedListener(new TextWatcher() {
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

        et_confirm_new_psw.addTextChangedListener(new TextWatcher() {
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
    }

    private String oldPswText;
    private String newPswText;
    private String phoneText;
    private String codeText;

    /**
     * 判断当手机号输入框,验证码输入框和密码输入框都不为空的时候改变登录的背景
     */
    private void checkedText() {
        oldPswText = et_new_psw.getText().toString().trim();
        newPswText = et_confirm_new_psw.getText().toString().trim();
        phoneText = et_phone.getText().toString().trim();
        codeText = et_code.getText().toString().trim();
        if (phoneText != null && phoneText.trim().length() > 0 && codeText != null && codeText.length() > 0
                && oldPswText != null && oldPswText.trim().length() > 0 && newPswText != null && newPswText.trim().length() > 0) {
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

    @OnClick({R.id.ll_back, R.id.tv_commit, R.id.tv_get_number})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
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

                if (!et_phone.getText().toString().trim().equals(Constanst.userPhoneNum)) {
                    ToastUtil.show(this, "请输入该用户绑定的手机号码", 0);
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

                if (!isCodeWork) {
                    ToastUtil.show(this, getString(R.string.phone_code_time_out), 0);
                    return;
                }

                if (et_new_psw.getText().toString().trim() == null || et_new_psw.getText().toString().trim().length() < 6 || et_new_psw.getText().toString().trim().length() > 16) {
                    ToastUtil.show(this, "请输入6-16位新密码", 0);
                    return;
                }

                if (et_confirm_new_psw.getText().toString().trim() == null || et_new_psw.getText().toString().trim().length() < 6 || et_new_psw.getText().toString().trim().length() > 16) {
                    ToastUtil.show(this, "请输入6-16位新密码", 0);
                    return;
                }

                if (et_confirm_new_psw.getText().toString().trim() == null || et_confirm_new_psw.getText().toString().trim().length() == 0) {
                    ToastUtil.show(this, "请确认新密码", 0);
                    return;
                }

                if (!et_new_psw.getText().toString().trim().equals(et_confirm_new_psw.getText().toString().trim())) {
                    ToastUtil.show(this, "两次密码输入不一致", 0);
                }

                showLoadingDialog();
                PostUpdatePswList();
                break;
            case R.id.tv_get_number:

                if (et_phone.getText().toString().trim() == null || et_phone.getText().toString().trim().length() == 0) {
                    ToastUtil.show(this, getString(R.string.write_phone_number), 0);
                    return;
                }

                if (!PhoneNumberCheckedUtil.checkNumber(et_phone.getText().toString().trim())) {
                    ToastUtil.show(this, getString(R.string.write_right_phone_number), 0);
                    return;
                }

                if (!et_phone.getText().toString().trim().equals(Constanst.userPhoneNum)) {
                    ToastUtil.show(this, "请输入该用户绑定的手机号码", 0);
                    return;
                }
                //获取手机验证码
                if (PhoneNumberCheckedUtil.checkNumber(et_phone.getText().toString().trim())) {
                    isCodeWork = true;
                    MsgCodeUtil.sendMsg(this, et_phone.getText().toString().trim(), Constanst.registSignature, Constanst.registTemplate);
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

    //修改密码接口
    private void PostUpdatePswList() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user");
        map.put("action", "update_passwd");
        map.put("password", et_new_psw.getText().toString().trim());
        map.put("repassword", et_confirm_new_psw.getText().toString().trim());
        getJsonUtil().PostJson(this, map);
    }
}
