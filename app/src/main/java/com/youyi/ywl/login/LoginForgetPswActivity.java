package com.youyi.ywl.login;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.youyi.ywl.R;
import com.youyi.ywl.activity.BaseActivity;
import com.youyi.ywl.other.Constanst;
import com.youyi.ywl.util.HttpUtils;
import com.youyi.ywl.util.PhoneNumberCheckedUtil;
import com.youyi.ywl.util.PswCheckedUtil;
import com.youyi.ywl.util.SoftUtil;
import com.youyi.ywl.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/9/29.
 * 登录 忘记密码界面
 */

public class LoginForgetPswActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.et_phone_number)
    EditText et_phone_number;
    @BindView(R.id.et_code)
    EditText et_code;
    @BindView(R.id.et_psw)
    EditText et_psw;
    @BindView(R.id.tv_next_step)
    TextView tv_next_step;
    @BindView(R.id.tv_get_code)
    TextView tv_get_code;

    private static final String CODE_URL = HttpUtils.Host + "/user2/send_hsms";//获取验证码
    private static final String FORGET_URL = HttpUtils.Host + "/user2/password_back";//找回密码
    private String yanzhengCode;
    private String hms_token;
    private Handler handler = new Handler();
    private int secondCounts = 60;//倒计时60秒


    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case CODE_URL:
                dismissLoadingDialog();
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        HashMap dataMap = (HashMap) resultMap.get("data");
                        yanzhengCode = dataMap.get("code") + "";
                        hms_token = dataMap.get("hms_token") + "";

                        tv_get_code.setText(secondCounts + "s后重试");

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                secondCounts--;
                                tv_get_code.setText(secondCounts + "s");
                                if (secondCounts > 0) {
                                    handler.postDelayed(this, 1000);
                                } else {
                                    secondCounts = 60;
                                    tv_get_code.setText("获取验证码");
                                    tv_get_code.setClickable(true);
                                }
                            }
                        }, 1000);

                    } else {
                        tv_get_code.setClickable(true);
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                    }
                } else {
                    tv_get_code.setClickable(true);
                    ToastUtil.show(this, msg, 0);
                }
                break;
            case FORGET_URL:
                dismissLoadingDialog();
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                        finish();
                    } else {
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
        setContentView(R.layout.activity_login_forget_psw);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tv_title.setText("忘记密码");
        tv_next_step.setClickable(false);
        listenEtChanged();//添加EditText的监听
    }

    private void listenEtChanged() {
        et_phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkedText();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        et_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkedText();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        et_psw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkedText();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    //分别检查
    private void checkedText() {
        String phoneNumStr = et_phone_number.getText().toString().trim();
        String codeStr = et_code.getText().toString().trim();
        String pswStr = et_psw.getText().toString().trim();
        if (phoneNumStr != null && PhoneNumberCheckedUtil.checkNumber(phoneNumStr) && codeStr != null && codeStr.length() == 6 && pswStr != null && pswStr.length() >= 6 && pswStr.length() <= 20 && PswCheckedUtil.isLetterAndDigit(pswStr)) {
            tv_next_step.setBackgroundResource(R.drawable.bg_login_next_step_white);
            tv_next_step.setClickable(true);
        } else {
            tv_next_step.setBackgroundResource(R.drawable.bg_login_next_step_blue);
            tv_next_step.setClickable(false);
        }
    }

    @Override
    public void afterInitView() {
    }

    @OnClick({R.id.ll_back, R.id.tv_get_code, R.id.tv_next_step})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                SoftUtil.hideSoft(this);
                finish();
                break;
            case R.id.tv_get_code:
                //获取验证码
                String phoneNumStr1 = et_phone_number.getText().toString().trim();
                if (PhoneNumberCheckedUtil.checkNumber(phoneNumStr1)) {
                    tv_get_code.setClickable(false);
                    PostYanzhengCodeList();
                } else {
                    ToastUtil.show(this, "请输入正确的手机号", 0);
                }
                break;
            case R.id.tv_next_step:
                SoftUtil.hideSoft(this);
                //提交(已经做过判断)
                String codeStr = et_code.getText().toString().trim();
                if (codeStr != null && codeStr.equals(yanzhengCode)) {
                    PostForgetPswList();
                } else {
                    ToastUtil.show(this, "请输入正确的验证码", 0);
                }
                break;
        }
    }

    /**
     * 请求忘记密码接口
     */
    private void PostForgetPswList() {
        showLoadingDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user2");
        map.put("action", "password_back");
        map.put("hms_token", hms_token);
        map.put("tel", et_phone_number.getText().toString().trim());
        map.put("password", et_psw.getText().toString().trim());
        getJsonUtil().PostJson(this, map, tv_next_step);
    }

    /**
     * 请求手机验证码接口
     */
    private void PostYanzhengCodeList() {
        showLoadingDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user2");
        map.put("action", "send_hsms");
        map.put("tel", et_phone_number.getText().toString().trim());
        map.put("type", "passwk_back");//登录的短信验证码
        getJsonUtil().PostJson(this, map);
    }
}
