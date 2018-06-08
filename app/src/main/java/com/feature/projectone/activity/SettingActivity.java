package com.feature.projectone.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.HttpUtils;
import com.feature.projectone.util.ShareUtil;
import com.feature.projectone.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/12.
 * 设置界面
 */

public class SettingActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;

    private static final String logoutUrl = HttpUtils.Host + "/user/logout";

    @Override
    protected void Response(String code, String msg, String url, Object result) {

        switch (url) {
            case logoutUrl:
                dismissLoadingDialog();
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        //退出登录成功
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                        EventBus.getDefault().post("关闭MainActivity");
                        ShareUtil.removeString(this, Constanst.UER_TOKEN);
                        String string = ShareUtil.getString(this, Constanst.UER_TOKEN);
                        startActivity(new Intent(this, LoginAndRegistActivity.class));
                        finish();
                    } else {
                        //退出登录失败
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
        setContentView(R.layout.activity_setting);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        tv_title.setText("设置");
    }

    @Override
    public void afterInitView() {

    }

    @OnClick({R.id.ll_back, R.id.ll_update_personal_data, R.id.ll_update_login_psw, R.id.ll_update_phone_num, R.id.ll_bind_on_account,
            R.id.ll_clear_cache, R.id.ll_contact_service, R.id.ll_score, R.id.ll_private_statement, R.id.ll_exit_login})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_update_personal_data:
                //个人资料修改
                startActivity(new Intent(this, UpdatePersonalDataActivity.class));
                break;
            case R.id.ll_update_login_psw:
                //登入密码修改
                startActivity(new Intent(this, UpdateLoginPswActivity.class));
                break;
            case R.id.ll_update_phone_num:
                //更换手机号
//                startActivity(new Intent(this,BindPhoneNumActivity.class));
                startActivity(new Intent(this, ChangePhoneNumActivity.class));
                break;
            case R.id.ll_bind_on_account:
                //社交平台账号绑定
                startActivity(new Intent(this, BindAccountActivity.class));
                break;
            case R.id.ll_clear_cache:
                //清除缓存
                break;
            case R.id.ll_contact_service:
                //联系客服
                break;
            case R.id.ll_score:
                //给软件打分
                break;
            case R.id.ll_private_statement:
                //隐私声明
                break;
            case R.id.ll_exit_login:
                //退出登录按钮
                PostLogoutList();
                showLoadingDialog();
                break;
        }
    }

    //退出登录接口
    private void PostLogoutList() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user");
        map.put("action", "logout");
        getJsonUtil().PostJson(this, map);
    }

    @Subscribe
    public void onEventMainThread(String str){
        if (str == null) {
            return;
        }
        switch (str) {
            case "修改登录密码成功":
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
