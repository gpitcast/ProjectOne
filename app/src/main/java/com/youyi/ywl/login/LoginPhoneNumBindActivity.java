package com.youyi.ywl.login;

import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youyi.ywl.R;
import com.youyi.ywl.activity.BaseActivity;
import com.youyi.ywl.activity.MainActivity;
import com.youyi.ywl.other.Constanst;
import com.youyi.ywl.util.HttpUtils;
import com.youyi.ywl.util.PhoneNumberCheckedUtil;
import com.youyi.ywl.util.SoftUtil;
import com.youyi.ywl.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/9/30.
 * 登录 手机号绑定界面
 */

public class LoginPhoneNumBindActivity extends BaseActivity {
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.et_phone_number)
    EditText et_phone_number;
    @BindView(R.id.et_code)
    EditText et_code;
    @BindView(R.id.tv_next_step)
    TextView tv_next_step;
    @BindView(R.id.tv_get_code)
    TextView tv_get_code;

    private static final String CODE_URL = HttpUtils.Host + "/user2/send_hsms";//获取验证码
    private static final String BIND_URL = HttpUtils.Host + "/user2/bind";//绑定手机号
    private String yanzhengCode;
    private String hms_token;
    private Handler handler = new Handler();
    private int secondCounts = 60;//倒计时60秒
    private String type;
    private String openId;

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
            case BIND_URL:
                dismissLoadingDialog();
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        String flag = resultMap.get("flag") + "";
                        if ("1".equals(flag)) {
                            Intent intent = new Intent(this, LoginSetPswActivity.class);
                            intent.putExtra("phone", et_phone_number.getText().toString().trim());
                            intent.putExtra("type", type);
                            intent.putExtra("openId", openId);
                            startActivity(intent);
                            finish();
                        } else if ("2".equals(flag)) {
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
                            EventBus.getDefault().post("关闭输入手机号码界面");
                            finish();
                        }
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
    protected void handleIntent(Intent intent) {
        if (intent != null) {
            type = intent.getStringExtra("type");
            openId = intent.getStringExtra("openId");
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_phone_num_bind);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        ll_back.setVisibility(View.GONE);
        tv_title.setText("手机号绑定");
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("关闭");
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
    }

    private void checkedText() {
        String phoneNumStr = et_phone_number.getText().toString().trim();
        String codeStr = et_code.getText().toString().trim();
        if (phoneNumStr != null && PhoneNumberCheckedUtil.checkNumber(phoneNumStr) && codeStr != null && codeStr.length() >= 6) {
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

    @OnClick({R.id.tv_right, R.id.tv_get_code, R.id.tv_next_step})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_right:
                SoftUtil.hideSoft(this);
                finish();
                break;
            case R.id.tv_get_code:
                //获取验证码
                if (PhoneNumberCheckedUtil.checkNumber(et_phone_number.getText().toString().trim())) {
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
                    PostBindPhoneNumList();
                } else {
                    ToastUtil.show(this, "请输入正确的验证码", 0);
                }
                break;
        }
    }

    /**
     * 绑定手机号码
     */
    private void PostBindPhoneNumList() {
        showLoadingDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user2");
        map.put("action", "bind");
        map.put("type", type);
        map.put("type_id", openId);
        map.put("tel", et_phone_number.getText().toString().trim());
        map.put("hms_token", hms_token);
        getJsonUtil().PostJson(this, map);
    }

    /**
     * 获取验证码
     */
    private void PostYanzhengCodeList() {
        showLoadingDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user2");
        map.put("action", "send_hsms");
        map.put("tel", et_phone_number.getText().toString().trim());
        map.put("type", "bind");//登录的短信验证码
        getJsonUtil().PostJson(this, map);
    }
}
