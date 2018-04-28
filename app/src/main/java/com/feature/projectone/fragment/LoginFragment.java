package com.feature.projectone.fragment;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.activity.ForgetPswActivity;
import com.feature.projectone.activity.MainActivity;
import com.feature.projectone.activity.SourceDownloadActivity;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.EtDrawableLeftUtil;
import com.feature.projectone.util.HttpUtils;
import com.feature.projectone.util.PhoneNumberCheckedUtil;
import com.feature.projectone.util.ShareUtil;
import com.feature.projectone.util.ToastUtil;


import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/3.
 * 登陆fragment
 */

public class LoginFragment extends BaseFragment {
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_psw)
    EditText et_psw;
    @BindView(R.id.ll_login)
    LinearLayout ll_login;
    @BindView(R.id.tv_login)
    TextView tv_login;

    private String phoneText;
    private String pswText;
    private static final String loginUrl = HttpUtils.Host + "/user/login";//登录接口
    private static final String isLoginUrl = HttpUtils.Host + "/user/is_login";//查看用户是否登陆

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            //是否登录接口
            case isLoginUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap<String, Object> resultMap = (HashMap<String, Object>) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        //该账号处于登录状态，弹出吐司提醒用户
                        ToastUtil.show(getActivity(), resultMap.get("msg") + "", 0);
                        dismissLoadingDialog();
                    } else {
                        //该账号处于未登录状态，执行登录操作
                        Login();
                    }
                } else {
                    dismissLoadingDialog();
                    ToastUtil.show(getActivity(), msg, 0);
                }
                break;
            //登录接口
            case loginUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = (Integer) resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        //登录成功
                        //存储登录的用户名和用户密码
                        ShareUtil.putString(getActivity(), "username", et_phone.getText().toString().trim());
                        ShareUtil.putString(getActivity(), "psw", et_psw.getText().toString().trim());
                        dismissLoadingDialog();
                        //跳转界面
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                    } else {
                        //登录失败
                        String message = (String) resultMap.get("msg");
                        ToastUtil.show(getActivity(), message, 0);
                        dismissLoadingDialog();
                    }
                } else {
                    dismissLoadingDialog();
                    ToastUtil.show(getActivity(), msg, 0);
                }
                break;

        }
    }

    //登录
    private void Login() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user");
        map.put("action", "login");
        map.put("type", "1");
        map.put("username", et_phone.getText().toString().trim());
        map.put("password", et_psw.getText().toString().trim());
        getJsonUtil().PostJson(getActivity(), map, tv_login);
    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        listenEtChanged();
        EtDrawableLeftUtil.setEtImgSize(et_phone);
        EtDrawableLeftUtil.setEtImgSize(et_psw);
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
     * 判断当手机号输入框和密码输入框都不为空的时候改变登录的背景
     */
    private void checkedText() {
        phoneText = et_phone.getText().toString().trim();
        pswText = et_psw.getText().toString().trim();
        if (phoneText != null && phoneText.trim().length() > 0 && pswText != null && pswText.trim().length() > 0) {
            ll_login.setBackgroundResource(R.drawable.bg_login_dark_btn);
            tv_login.setEnabled(true);
        } else {
            ll_login.setBackgroundResource(R.drawable.bg_login_light_btn);
            tv_login.setEnabled(false);
        }
    }

    @OnClick({R.id.tv_login, R.id.tv_forget_psw})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                if (et_phone.getText().toString().trim() == null || et_phone.getText().toString().trim().length() == 0) {
                    ToastUtil.show(getActivity(), getString(R.string.write_phone_number), 0);
                    return;
                }
                if (!PhoneNumberCheckedUtil.checkNumber(et_phone.getText().toString().trim())) {
                    ToastUtil.show(getActivity(), getString(R.string.write_right_phone_number), 0);
                    return;
                }
                if (et_psw.getText().toString().trim() == null || et_psw.getText().toString().trim().length() == 0) {
                    ToastUtil.show(getActivity(), getString(R.string.write_right_phone_psw), 0);
                    return;
                }
                showLoadingDialog();
                //请求用户是否登陆接口
                isLogin();
                break;
            case R.id.tv_forget_psw:
                startActivity(new Intent(getActivity(), ForgetPswActivity.class));
                break;
        }
    }

    //用户是否登陆
    private void isLogin() {
        String user_token = ShareUtil.getString(getActivity(), "user_token");
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user");
        map.put("action", "is_login");
        if (user_token != null && user_token.length() > 0) {
            map.put("user_token", user_token);
        }
        getJsonUtil().PostJson(getActivity(), map, tv_login);
    }
}
