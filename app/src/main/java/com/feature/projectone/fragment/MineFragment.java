package com.feature.projectone.fragment;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.activity.HelpAndFeedBackActivity;
import com.feature.projectone.activity.LoginAndRegistActivity;
import com.feature.projectone.activity.MyCommunityActivity;
import com.feature.projectone.activity.MyCreditActivity;
import com.feature.projectone.activity.MyLearningStageActivity;
import com.feature.projectone.activity.RevelationAndConllectionActivity;
import com.feature.projectone.activity.SettingActivity;
import com.feature.projectone.activity.SignInActivity;
import com.feature.projectone.activity.SourceDownloadListActivity;
import com.feature.projectone.activity.TestActivity;
import com.feature.projectone.adapter.MineGridOneAdapter;
import com.feature.projectone.adapter.MineGridTwoAdapter;
import com.feature.projectone.adapter.MineRecyclerAdapter;
import com.feature.projectone.inter.RecyclerViewOnItemClickListener;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.HttpUtils;
import com.feature.projectone.util.ShareUtil;
import com.feature.projectone.util.ToastUtil;
import com.feature.projectone.view.CircleImageView;
import com.feature.projectone.view.MyGridView;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/4/19.
 * 主界面 我的fragment
 */

public class MineFragment extends BaseFragment {
    @BindView(R.id.gridView_one)
    GridView gridView_one;
    @BindView(R.id.gridView_two)
    MyGridView gridView_two;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_setting)
    TextView tv_setting;
    @BindView(R.id.circleImageView)
    CircleImageView circleImageView;
    @BindView(R.id.tv_name)
    TextView tv_name;

    private String[] strListOne = new String[]{"我的订单", "我的钱包", "优惠券"};
    private int[] imgListOne = new int[]{R.mipmap.iv_my_order, R.mipmap.iv_my_wallet, R.mipmap.iv_my_coupons};
    private String[] strListTwo = new String[]{"我的资源", "学习计划", "学习阶段", "我的社区", "我的收藏", "我的评价", "我的积分", "其他服务"};
    private int[] imgListTwo = new int[]{R.mipmap.iv_my_source, R.mipmap.iv_study_plan, R.mipmap.iv_study_phase,
            R.mipmap.iv_my_community, R.mipmap.iv_my_conllection, R.mipmap.iv_my_write, R.mipmap.iv_my_jifen,
            R.mipmap.iv_other_service};
    private String[] strListThree = new String[]{"分享盈未来APP", "帮助与问题反馈", "关于"};
    private int[] imgListThree = new int[]{R.mipmap.iv_share, R.mipmap.iv_help, R.mipmap.iv_about};

    private MineGridOneAdapter mineGridOneAdapter;
    private MineGridTwoAdapter mineGridTwoAdapter;
    private LinearLayoutManager manager;
    private MineRecyclerAdapter mineRecyclerAdapter;

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
        return R.layout.fragment_mine;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        EventBus.getDefault().register(this);
        initMyView();
        initGridView();
        initRecyclerView();
    }


    public void initMyView() {
        Picasso.with(getActivity()).load(Constanst.userHeadImg).placeholder(R.mipmap.img_loading).error(R.mipmap.img_load_error).into(circleImageView);
        tv_name.setText(Constanst.userNickName);
    }

    private void initGridView() {
        mineGridOneAdapter = new MineGridOneAdapter(strListOne, imgListOne);
        gridView_one.setAdapter(mineGridOneAdapter);

        mineGridTwoAdapter = new MineGridTwoAdapter(strListTwo, imgListTwo);
        gridView_two.setAdapter(mineGridTwoAdapter);
        gridView_two.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        //我的资源
                        startActivity(new Intent(getActivity(), SourceDownloadListActivity.class));
                        break;
                    case 2:
                        //学习阶段
                        startActivity(new Intent(getActivity(), MyLearningStageActivity.class));
                        break;
                    case 3:
                        //我的社区
                        startActivity(new Intent(getActivity(), MyCommunityActivity.class));
                        break;
                    case 6:
                        //我的积分
                        startActivity(new Intent(getActivity(), MyCreditActivity.class));
                        break;
                    case 7:
                        startActivity(new Intent(getActivity(), RevelationAndConllectionActivity.class));
                        break;
                }

            }
        });
    }


    private void initRecyclerView() {

        manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        mineRecyclerAdapter = new MineRecyclerAdapter(strListThree, imgListThree);
        recyclerView.setAdapter(mineRecyclerAdapter);
        recyclerView.setNestedScrollingEnabled(false);//关于滑动冲突的话，一般这种情况下，都是按照ScrollView 去滑动即可。
        mineRecyclerAdapter.setOnItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        //帮助与问题反馈
                        startActivity(new Intent(getActivity(), HelpAndFeedBackActivity.class));
                        break;
                    case 2:
                        break;
                }
            }
        });
    }

    //启动修改设置权限界面
//    private void JumpPermissionActivity() {
//        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
//        intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
//        startActivityForResult(intent, 9527);
//    }

    @OnClick({R.id.ll_setting, R.id.ll_sign_in})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.ll_sign_in:
                startActivity(new Intent(getActivity(), SignInActivity.class));
                break;

        }
    }

    @Subscribe
    public void onEventMainThread(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        switch (str) {
            case "调用initMyView()":
                initMyView();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
