package com.youyi.ywl.login;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
 * Created by Administrator on 2018/9/30.
 * 登录 设置密码界面
 */

public class LoginSetPswActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.et_psw)
    EditText et_psw;
    @BindView(R.id.iv_psw_length)
    ImageView iv_psw_length;
    @BindView(R.id.tv_psw_length)
    TextView tv_psw_length;
    @BindView(R.id.iv_psw_contain)
    ImageView iv_psw_contain;
    @BindView(R.id.tv_psw_contain)
    TextView tv_psw_contain;
    @BindView(R.id.tv_next_step)
    TextView tv_next_step;

    private static final String PSW_URL = HttpUtils.Host + "/user2/setpass";//正常短信验证码注册设置密码
    private static final String BIND_PSW_URL = HttpUtils.Host + "/user2/setBindPass";//第三方登录绑定手机号设置密码
    private String phoneNum;
    private String type;
    private String hms_token;
    private String openId;

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
            case BIND_PSW_URL:
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
            hms_token = intent.getStringExtra("hms_token");
            openId = intent.getStringExtra("openId");
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_login_set_psw);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tv_title.setText("设置密码");
        tv_next_step.setClickable(false);

        et_psw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pswStr = et_psw.getText().toString().trim();
                if (pswStr != null) {
                    if (pswStr.length() >= 6 && pswStr.length() <= 20) {
                        iv_psw_length.setImageResource(R.mipmap.icon_already_done_white);
                        tv_psw_length.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        iv_psw_length.setImageResource(R.mipmap.icon_already_done_blue);
                        tv_psw_length.setTextColor(getResources().getColor(R.color.normal_blue4));
                    }

                    if (PswCheckedUtil.isLetterAndDigit(pswStr)) {
                        iv_psw_contain.setImageResource(R.mipmap.icon_already_done_white);
                        tv_psw_contain.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        iv_psw_contain.setImageResource(R.mipmap.icon_already_done_blue);
                        tv_psw_contain.setTextColor(getResources().getColor(R.color.normal_blue4));
                    }

                    if (pswStr.length() >= 6 && pswStr.length() <= 20 && PswCheckedUtil.isLetterAndDigit(pswStr)) {
                        tv_next_step.setClickable(true);
                        tv_next_step.setBackgroundResource(R.drawable.bg_login_next_step_white);
                    } else {
                        tv_next_step.setClickable(false);
                        tv_next_step.setBackgroundResource(R.drawable.bg_login_next_step_blue);
                    }

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

    @OnClick({R.id.ll_back, R.id.tv_next_step})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                SoftUtil.hideSoft(this);
                finish();
                break;
            case R.id.tv_next_step:
                //下一步
                if (type == null) {
                    ToastUtil.show(this, "type为空", 0);
                    return;
                }
                if (type.equals("register")) {
                    //注册设置密码
                    PostRegistList();
                } else if (type.equals("weixin")) {
                    PostWeixinList();
                }
                break;
        }
    }


    private void PostWeixinList() {
        showLoadingDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user2");
        map.put("action", "setBindPass");
        map.put("type", type);
        map.put("type_id", openId);
        map.put("tel", phoneNum);
        map.put("passwd", et_psw.getText().toString());
        getJsonUtil().PostJson(this, map, tv_next_step);
    }

    /**
     * 请求注册接口
     */
    private void PostRegistList() {
        showLoadingDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user2");
        map.put("action", "setpass");
        map.put("tel", phoneNum);
        map.put("type", type);
        map.put("hms_token", hms_token);
        map.put("passwd", et_psw.getText().toString().trim());
        getJsonUtil().PostJson(this, map, tv_next_step);
    }
}
