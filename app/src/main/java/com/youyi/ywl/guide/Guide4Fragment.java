package com.youyi.ywl.guide;

import android.content.Intent;
import android.view.View;

import com.youyi.ywl.R;
import com.youyi.ywl.activity.MainActivity;
import com.youyi.ywl.fragment.BaseFragment;
import com.youyi.ywl.login.LoginEnterPhoneNumberActivity;
import com.youyi.ywl.login.LoginSelectGradeActivity;
import com.youyi.ywl.login.LoginSelectIdentityActivity;
import com.youyi.ywl.other.Constanst;
import com.youyi.ywl.util.HttpUtils;
import com.youyi.ywl.util.ShareUtil;
import com.youyi.ywl.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;

/**
 * Created by Administrator on 2018/10/10.
 * 引导图1 fragment
 */

public class Guide4Fragment extends BaseFragment {

    private static final String STEP_URL = HttpUtils.Host + "/user2/step";//查看用户是否设置了身份或科目接口

    @Override
    public void onLazyLoad() {
    }

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case STEP_URL:
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
                            Intent intent = new Intent(getActivity(), LoginSelectIdentityActivity.class);
                            startActivity(intent);
                        } else if ("2".equals(step)) {
                            Intent intent = new Intent(getActivity(), LoginSelectGradeActivity.class);
                            startActivity(intent);
                        } else if ("0".equals(step)) {
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                        }
                        getActivity().finish();
                    } else {
                        ToastUtil.show(getActivity(), resultMap.get("msg") + "", 0);
                    }
                } else {
                    ToastUtil.show(getActivity(), msg, 0);
                }
                break;
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_guide4;
    }

    @Override
    protected void initViewsAndEvents(View view) {
    }

    @OnClick({R.id.tv_start_study_journey})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_start_study_journey:
                //判断token是否为null
                String tokenStr = ShareUtil.getString(getActivity(), Constanst.UER_TOKEN);
                if (tokenStr != null && tokenStr.length() > 0) {
                    //已经登录过,请求'查看用户是否设置了身份或科目'接口该执行哪一步
                    PostWhatStepList();
                } else {
                    //没有登录过
                    startActivity(new Intent(getActivity(), LoginEnterPhoneNumberActivity.class));
                    getActivity().finish();
                }
                break;
        }
    }

    /**
     * 请求'查看用户是否设置了身份或科目'接口
     */
    private void PostWhatStepList() {
        showLoadingDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user2");
        map.put("action", "step");
        getJsonUtil().PostJson(getActivity(), map);
    }
}
