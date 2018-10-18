package com.youyi.ywl.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.youyi.ywl.R;
import com.youyi.ywl.activity.HelpAndFeedBackActivity;
import com.youyi.ywl.activity.ManagePurseActivity;
import com.youyi.ywl.activity.MyOrderActivity;
import com.youyi.ywl.activity.SettingActivity;
import com.youyi.ywl.adapter.MeFragmentNewGridViewAdapter;
import com.youyi.ywl.inter.RecyclerViewOnItemClickListener;
import com.youyi.ywl.login.LoginEnterPhoneNumberActivity;
import com.youyi.ywl.other.Constanst;
import com.youyi.ywl.util.CheckLoginUtil;
import com.youyi.ywl.util.GlideUtil;
import com.youyi.ywl.util.LoginInterceptor;
import com.youyi.ywl.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/9/18.
 * 个人中心fragment
 */

public class MineFragmentNew extends BaseFragment {
    @BindView(R.id.circleImageView)
    CircleImageView circleImageView;
    @BindView(R.id.tv_username)
    TextView tv_username;
    @BindView(R.id.gridView)
    GridView gridView;

    private List<HashMap<String, Object>> gridList;//gridView的list
    private MeFragmentNewGridViewAdapter meFragmentNewGridViewAdapter;

    @Override
    public void onLazyLoad() {
    }

    @Override
    protected void Response(String code, String msg, String url, Object result) {
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_mine_new;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        initData();
        initGridView();
        initBaseView();
    }

    private void initGridView() {
        meFragmentNewGridViewAdapter = new MeFragmentNewGridViewAdapter(getActivity(), gridList);
        gridView.setAdapter(meFragmentNewGridViewAdapter);
        meFragmentNewGridViewAdapter.setOnMyItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                switch (position) {
                    case 0:
                        //订单
                        startActivity(new Intent(getActivity(), MyOrderActivity.class));
                        break;
                    case 1:
                        //钱包
                        startActivity(new Intent(getActivity(), ManagePurseActivity.class));
                        break;
                    case 2:
                        //优惠券
                        ToastUtil.show(getActivity(), "优惠券正在努力开发中,敬请期待", 0);
                        break;
                }
            }
        });
    }

    private void initData() {
        if (gridList == null) {
            gridList = new ArrayList<>();
            HashMap<String, Object> map0 = new HashMap<>();
            map0.put("img", R.mipmap.icon_order);
            map0.put("name", "订单");
            gridList.add(map0);

            HashMap<String, Object> map1 = new HashMap<>();
            map1.put("img", R.mipmap.icon_wallet);
            map1.put("name", "钱包");
            gridList.add(map1);

            HashMap<String, Object> map2 = new HashMap<>();
            map2.put("img", R.mipmap.icon_coupon);
            map2.put("name", "优惠券");
            gridList.add(map2);
        }

    }

    private void initBaseView() {
        if (CheckLoginUtil.isLogin(getActivity())) {
            GlideUtil.loadNetImageView(getActivity(), Constanst.userHeadImg, circleImageView);
            tv_username.setText(Constanst.userNickName);
        } else {
            circleImageView.setImageResource(R.mipmap.icon_circle_gray_fox);
            tv_username.setText("立即登入");
        }
    }

    @OnClick({R.id.ll_sign_in, R.id.ll_setting, R.id.tv_username, R.id.ll_file_download, R.id.ll_learning_stage, R.id.ll_my_course, R.id.ll_course_evaluate,
            R.id.ll_app_share, R.id.ll_help_feedback})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_sign_in:
                //通过此方法跳转到登录界面,登录成功以后会跳转回当前意图跳转的界面   target代表意图跳转的界面的action
                Bundle bundle = new Bundle();
                bundle.putString("type", "SignInActivity");
                LoginInterceptor.interceptor(getActivity(), "com.youyi.YWL.activity.SignInActivity", bundle);
                break;
            case R.id.ll_setting:
                //设置
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.tv_username:
                //在未登录情况下点击跳转到登录界面
                if (!CheckLoginUtil.isLogin(getActivity())) {
                    Intent intent = new Intent(getActivity(), LoginEnterPhoneNumberActivity.class);
                    intent.putExtra("type", "立即登入");
                    startActivity(intent);
                }
                break;
            case R.id.ll_file_download:
                //文件下载
                Bundle bundle1 = new Bundle();
                bundle1.putString("type", "SourceDownloadListActivity");
                LoginInterceptor.interceptor(getActivity(), "com.youyi.YWL.activity.SourceDownloadListActivity", bundle1);

                break;
            case R.id.ll_learning_stage:
                //学习阶段
                Bundle bundle2 = new Bundle();
                bundle2.putString("type", "MyLearningStageActivity");
                LoginInterceptor.interceptor(getActivity(), "com.youyi.YWL.activity.MyLearningStageActivity", bundle2);

                break;
            case R.id.ll_my_course:
                //我的课程
                ToastUtil.show(getActivity(), "我的课程正在努力开发中,敬请期待", 0);
                break;
            case R.id.ll_course_evaluate:
                //课程评价
                ToastUtil.show(getActivity(), "课程评价正在努力开发中,敬请期待", 0);
                break;
            case R.id.ll_app_share:
                //APP分享
                ToastUtil.show(getActivity(), "APP分享正在努力开发中,敬请期待", 0);
                break;
            case R.id.ll_help_feedback:
                //帮助和反馈
                startActivity(new Intent(getActivity(), HelpAndFeedBackActivity.class));
                break;
        }
    }
}
