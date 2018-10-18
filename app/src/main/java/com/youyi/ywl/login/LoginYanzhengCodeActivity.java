package com.youyi.ywl.login;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.youyi.ywl.R;
import com.youyi.ywl.activity.BaseActivity;
import com.youyi.ywl.activity.MainActivity;
import com.youyi.ywl.other.Constanst;
import com.youyi.ywl.util.HttpUtils;
import com.youyi.ywl.util.SoftUtil;
import com.youyi.ywl.util.ToastUtil;
import com.youyi.ywl.view.VerifyCodeView_White;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/9/29.
 * 登录 验证码界面
 */

public class LoginYanzhengCodeActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.verifyCodeView)
    VerifyCodeView_White verifyCodeView;
    @BindView(R.id.tv_send_phone_num)
    TextView tv_send_phone_num;
    @BindView(R.id.tv_send_sms_seconds)
    TextView tv_send_sms_seconds;

    private static final String CODE_URL = HttpUtils.Host + "/user2/send_hsms";//获取验证码接口
    private static final String CODE_LOGIN_URL = HttpUtils.Host + "/user2/smsLogin";//验证码登录接口
    private String type;
    private String phoneNum;
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

                        tv_send_sms_seconds.setTextColor(getResources().getColor(R.color.normal_blue5));
                        tv_send_sms_seconds.setText(secondCounts + "s后重新发送");

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                secondCounts--;
                                tv_send_sms_seconds.setText(secondCounts + "s后重新发送");
                                if (secondCounts > 0) {
                                    handler.postDelayed(this, 1000);
                                } else {
                                    secondCounts = 60;
                                    tv_send_sms_seconds.setText("重新发送验证码");
                                    tv_send_sms_seconds.setTextColor(getResources().getColor(R.color.light_blue20));
                                    tv_send_sms_seconds.setClickable(true);
                                }
                            }
                        }, 1000);
                    } else {
                        tv_send_sms_seconds.setClickable(true);
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                    }
                } else {
                    tv_send_sms_seconds.setClickable(true);
                    ToastUtil.show(this, msg, 0);
                }
                break;
            case CODE_LOGIN_URL:
                dismissLoadingDialog();
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        HashMap dataMap = (HashMap) resultMap.get("data");
                        Constanst.userNickName = dataMap.get("nickname") + "";
                        Constanst.userRealName = dataMap.get("realname") + "";
                        Constanst.userPhoneNum = dataMap.get("username") + "";
                        Constanst.userHeadImg = dataMap.get("img") + "";
                        Constanst.userSex = dataMap.get("sex") + "";
                        Constanst.userBirthday = dataMap.get("birthday") + "";
                        Constanst.userAddress = dataMap.get("address") + "";
                        Constanst.user_role_id = dataMap.get("role_id") + "";

                        String step = resultMap.get("step") + "";//1 跳转到设置身份界面 2跳转到设置科目(学段年级)界面,0跳转到首页
                        if ("1".equals(step)) {
                            Intent intent = new Intent(this, LoginSelectIdentityActivity.class);
                            startActivity(intent);
                        } else if ("2".equals(step)) {
                            Intent intent = new Intent(this, LoginSelectGradeActivity.class);
                            startActivity(intent);
                        } else if ("0".equals(step)) {
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                        }
                        finish();
                    } else {
                        SoftUtil.hideSoft(this);
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                    }
                } else {
                    SoftUtil.hideSoft(this);
                    ToastUtil.show(this, msg, 0);
                }
                break;
        }
    }

    @Override
    protected void handleIntent(Intent intent) {
        if (intent != null) {
            type = intent.getStringExtra("type");//区分跳转的类型, 0代表是 验证码登录  1代表的是验证码注册
            phoneNum = intent.getStringExtra("phone");
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_login_yanzheng_code);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tv_send_phone_num.setText("验证码已发送至  " + phoneNum);
        tv_send_sms_seconds.setClickable(false);

        if (type != null) {
            if (type.equals("0")) {
                tv_title.setText("验证码登录");
            }
            if (type.equals("1")) {
                tv_title.setText("验证注册手机");
            }
        }

        if ("0".equals(type)) {
            PostLoginYanzhengCodeList();
        } else if ("1".equals(type)) {
            PostRegistYanzhengCodeList();
        }


        verifyCodeView.setInputCompleteListener(new VerifyCodeView_White.InputCompleteListener() {
            @Override
            public void inputComplete() {
                String editContent = verifyCodeView.getEditContent();
                if (editContent != null && editContent.length() >= 6 && editContent.equals(yanzhengCode)) {
                    SoftUtil.hideSoft(LoginYanzhengCodeActivity.this);
                    if ("0".equals(type)) {
                        //验证码验证成功,请求验证码登录接口
                        PostYanzhengCodeLoginList();
                    } else {
                        //验证码验证成功,跳转到设置密码界面
                        Intent intent = new Intent(LoginYanzhengCodeActivity.this, LoginSetPswActivity.class);
                        intent.putExtra("phone", phoneNum);
                        intent.putExtra("type", "register");
                        intent.putExtra("hms_token", hms_token);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    ToastUtil.show(LoginYanzhengCodeActivity.this, "验证码输入有误,请重新输入", 0);
                    SoftUtil.hideSoft(LoginYanzhengCodeActivity.this);
                    verifyCodeView.clear();
                }
            }

            @Override
            public void invalidContent() {
            }
        });
    }


    /**
     * 验证码登录接口
     */
    private void PostYanzhengCodeLoginList() {
        showLoadingDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user2");
        map.put("action", "smsLogin");
        map.put("code", verifyCodeView.getEditContent());
        map.put("tel", phoneNum);
        map.put("hms_token", hms_token);
        getJsonUtil().PostJson(this, map);
    }

    /**
     * 请求登录的验证码接口
     */
    private void PostLoginYanzhengCodeList() {
        showLoadingDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user2");
        map.put("action", "send_hsms");
        map.put("tel", phoneNum);
        map.put("type", "login");//登录的短信验证码
        getJsonUtil().PostJson(this, map);
    }

    /**
     * 请求注册的验证码接口
     */
    private void PostRegistYanzhengCodeList() {
        showLoadingDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user2");
        map.put("action", "send_hsms");
        map.put("tel", phoneNum);
        map.put("type", "register");//注册的短信验证码
        getJsonUtil().PostJson(this, map);
    }

    @Override
    public void afterInitView() {

    }

    @OnClick({R.id.ll_back, R.id.tv_send_sms_seconds})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                SoftUtil.hideSoft(this);
                finish();
                break;
            case R.id.tv_send_sms_seconds:
                tv_send_sms_seconds.setClickable(false);
                if ("0".equals(type)) {
                    PostLoginYanzhengCodeList();
                } else if ("1".equals(type)) {
                    PostRegistYanzhengCodeList();
                }
                break;
        }
    }
}
