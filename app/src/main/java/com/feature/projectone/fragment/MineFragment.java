package com.feature.projectone.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.feature.projectone.R;
import com.feature.projectone.activity.LoginAndRegistActivity;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.CheckLoginUtil;
import com.feature.projectone.util.HttpUtils;
import com.feature.projectone.util.ShareUtil;
import com.feature.projectone.util.ToastUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/19.
 * 主界面 我的fragment
 */

public class MineFragment extends BaseFragment {
    @BindView(R.id.button_logout)
    Button button_logout;

    private static final String logoutUrl = HttpUtils.Host + "/user/logout";//新闻内容接口

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case logoutUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap<String, Object> resultMap = (HashMap<String, Object>) result;
                    ToastUtil.show(getActivity(), resultMap.get("msg") + "", 0);
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        ShareUtil.putString(getActivity(), Constanst.UER_TOKEN, null);
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
        return R.layout.fragment_mine;
    }

    @Override
    protected void initViewsAndEvents(View view) {

    }

    @OnClick({R.id.button_logout, R.id.button_jumpto_login})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.button_logout:
                HashMap<String, Object> map = new HashMap<>();
                map.put("controller", "user");
                map.put("action", "logout");
                getJsonUtil().PostJson(getActivity(), map, button_logout);
                break;
            case R.id.button_jumpto_login:
                if (!ShareUtil.isExist(getActivity(), Constanst.UER_TOKEN)) {
                    startActivity(new Intent(getActivity(), LoginAndRegistActivity.class));
                    getActivity().finish();
                } else {
                    ToastUtil.show(getActivity(), "你已经在登录状态，无需重复登录", 0);
                }
                break;
        }
    }
}
