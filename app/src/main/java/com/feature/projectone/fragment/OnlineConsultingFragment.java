package com.feature.projectone.fragment;

import android.view.View;

import com.feature.projectone.R;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.ToastUtil;

import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.CSCustomServiceInfo;

/**
 * Created by Administrator on 2018/5/30.
 * 联系人中的在线咨询fragment
 */

public class OnlineConsultingFragment extends BaseFragment {

    @Override
    protected void Response(String code, String msg, String url, Object result) {

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
        return R.layout.fragment_online_consulting;
    }

    @Override
    protected void initViewsAndEvents(View view) {

    }

    @OnClick({R.id.ll_online_consulting})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_online_consulting:
                //在线客服
                CSCustomServiceInfo.Builder csBuilder = new CSCustomServiceInfo.Builder();
                CSCustomServiceInfo csInfo = csBuilder.build();
                //这里的title和csInfo没有启用,客服端显示的头像和昵称是在获取token的时候传入的昵称和头像
                RongIM.getInstance().startCustomerServiceChat(getActivity(), Constanst.kefuId, "在线客服", csInfo);
                break;
            case R.id.ll_my_teacher:
                //我的老师
                break;
        }
    }
}
