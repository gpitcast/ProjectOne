package com.youyi.ywl.login;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.youyi.ywl.R;
import com.youyi.ywl.activity.BaseActivity;
import com.youyi.ywl.activity.MainActivity;
import com.youyi.ywl.other.Constanst;
import com.youyi.ywl.util.HttpUtils;
import com.youyi.ywl.util.PswCheckedUtil;
import com.youyi.ywl.util.SoftUtil;
import com.youyi.ywl.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/9/29.
 * 登录 输入密码界面
 */

public class LoginEnterPswActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.et_psw)
    EditText et_psw;
    @BindView(R.id.tv_next_step)
    TextView tv_next_step;
    private String phoneNum;
    private String type;

    private static final String PSW_URL = HttpUtils.Host + "/user2/setpass";

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case PSW_URL:
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
                        EventBus.getDefault().post("关闭输入手机号码界面");
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
    protected void handleIntent(Intent intent) {
        if (intent != null) {
            phoneNum = intent.getStringExtra("phone");
            type = intent.getStringExtra("type");
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_login_enter_psw);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tv_title.setText("输入密码");
        tv_next_step.setClickable(false);
        listenEtChanged();//添加输入密码EditText的监听
    }

    private void listenEtChanged() {
        et_psw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pswStr = et_psw.getText().toString().trim();
                if (pswStr != null && pswStr.length() >= 6) {
                    tv_next_step.setClickable(true);
                    tv_next_step.setBackgroundResource(R.drawable.bg_login_next_step_white);
                } else {
                    tv_next_step.setClickable(false);
                    tv_next_step.setBackgroundResource(R.drawable.bg_login_next_step_blue);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void afterInitView() {
    }

    @OnClick({R.id.ll_back, R.id.tv_next_step, R.id.tv_forget_psw, R.id.tv_code_fast_login})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                SoftUtil.hideSoft(this);
                finish();
                break;
            case R.id.tv_next_step:
                SoftUtil.hideSoft(this);
                //下一步
                String pswStr = et_psw.getText().toString().trim();
                if (pswStr.length() >= 6 && pswStr.length() <= 20 && PswCheckedUtil.isLetterAndDigit(pswStr)) {
                    PostSetPswList();
                } else {
                    ToastUtil.show(this, "请输入正确的密码", 0);
                }
                break;
            case R.id.tv_forget_psw:
                //忘记密码
                Intent intent = new Intent(this, LoginForgetPswActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_code_fast_login:
                //手机验证码快速登录
                Intent intent1 = new Intent(this, LoginYanzhengCodeActivity.class);
                intent1.putExtra("type", "0");
                intent1.putExtra("phone", phoneNum);
                startActivity(intent1);
                finish();
                break;
        }
    }

    /**
     * 请求验证和设置密码接口
     */
    private void PostSetPswList() {
        showLoadingDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user2");
        map.put("action", "setpass");
        map.put("tel", phoneNum);
        map.put("type", type);
        map.put("hms_token", "");
        map.put("passwd", et_psw.getText().toString().trim());
        getJsonUtil().PostJson(this, map, tv_next_step);
    }
}
