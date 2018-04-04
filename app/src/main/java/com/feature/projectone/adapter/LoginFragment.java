package com.feature.projectone.adapter;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feature.projectone.R;
import com.feature.projectone.activity.MainActivity;
import com.feature.projectone.fragment.BaseFragment;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.EtDrawableLeftUtil;
import com.feature.projectone.util.HttpUtils;
import com.feature.projectone.util.NetWorkUtils;
import com.feature.projectone.util.ToastUtil;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.orhanobut.logger.Logger;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

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
    private static final String loginUrl = "http://47.104.128.245/api/data/user/login";

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            //登录接口
            case loginUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    Integer status = (Integer) resultMap.get("status");
                    if (status == 0) {
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    } else {
                        String message = (String) ((HashMap) result).get("msg");
                        ToastUtil.show(getActivity(), message, 0);
                    }
                } else {
                    ToastUtil.show(getActivity(), msg, 0);
                }
                break;
        }
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
            tv_login.setEnabled(true);
        } else {
            ll_login.setBackgroundResource(R.drawable.bg_login_light_btn);
            tv_login.setEnabled(false);
        }
    }

    @OnClick({R.id.tv_login})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                HashMap<String, Object> map = new HashMap<>();
                map.put("controller", "user");
                map.put("action", "login");
                map.put("type", "1");
                map.put("username", et_phone.getText().toString().trim());
                map.put("password", et_psw.getText().toString().trim());
                getJsonUtil().PostJson(getActivity(), map, tv_login);
                break;
        }
    }
}
