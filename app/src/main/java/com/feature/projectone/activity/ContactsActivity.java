package com.feature.projectone.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.fragment.CommunityGroupsFragment;
import com.feature.projectone.fragment.OnlineConsultingFragment;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/30.
 * 联系人界面
 */

public class ContactsActivity extends BaseActivity {
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_online_consulting)
    ImageView iv_online_consulting;
    @BindView(R.id.tv_online_consulting)
    TextView tv_online_consulting;
    @BindView(R.id.iv_online_consulting_line)
    ImageView iv_online_consulting_line;
    @BindView(R.id.iv_community_groups)
    ImageView iv_community_groups;
    @BindView(R.id.tv_community_groups)
    TextView tv_community_groups;
    @BindView(R.id.iv_community_groups_line)
    ImageView iv_community_groups_line;

    private OnlineConsultingFragment onlineConsultingFragment;
    private CommunityGroupsFragment communityGroupsFragment;

    @Override
    protected void Response(String code, String msg, String url, Object result) {

    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_contacts);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        tv_title.setText("联系人");
        showOnlineConsultingFragment();
    }

    @Override
    public void afterInitView() {
    }

    public void hideFragment(FragmentTransaction transaction) {
        if (onlineConsultingFragment != null) {
            transaction.hide(onlineConsultingFragment);
        }
        if (communityGroupsFragment != null) {
            transaction.hide(communityGroupsFragment);
        }
    }

    public void showOnlineConsultingFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (onlineConsultingFragment == null) {
            onlineConsultingFragment = new OnlineConsultingFragment();
            transaction.add(R.id.framelayout, onlineConsultingFragment);
        }
        hideFragment(transaction);
        transaction.show(onlineConsultingFragment);
        transaction.commit();
    }

    public void showCommunityGroupsFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (communityGroupsFragment == null) {
            communityGroupsFragment = new CommunityGroupsFragment();
            transaction.add(R.id.framelayout, communityGroupsFragment);
        }
        hideFragment(transaction);
        transaction.show(communityGroupsFragment);
        transaction.commit();
    }

    private boolean isLeft = true;//记录是否左边为选中状态

    @OnClick({R.id.ll_back, R.id.rl_online_consulting, R.id.rl_community_groups})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.rl_online_consulting:
                //在线咨询
                if (!isLeft) {
                    //表示右边为选中,切换为左边的群公告
                    showOnlineConsultingFragment();
                    isLeft = true;
                    changeSelectColor(isLeft);
                }
                break;
            case R.id.rl_community_groups:
                //社区群组
                if (isLeft) {
                    //表示左边为选中,切换为右边的群文件
                    showCommunityGroupsFragment();
                    isLeft = false;
                    changeSelectColor(isLeft);
                }
                break;
        }
    }

    public void changeSelectColor(boolean isLeft) {
        if (isLeft) {
            //选中左边
            Picasso.with(this).load(R.mipmap.icon_group_blue_notice).into(iv_online_consulting);
            tv_online_consulting.setTextColor(getResources().getColor(R.color.orangeone));
            Picasso.with(this).load(R.mipmap.icon_group_blue_line).into(iv_online_consulting_line);

            Picasso.with(this).load(R.mipmap.icon_group_gray_files).into(iv_community_groups);
            tv_community_groups.setTextColor(getResources().getColor(R.color.light_gray16));
            Picasso.with(this).load(R.mipmap.icon_group_gray_line).into(iv_community_groups_line);
        } else {
            //选中右边
            Picasso.with(this).load(R.mipmap.icon_group_gray_notice).into(iv_online_consulting);
            tv_online_consulting.setTextColor(getResources().getColor(R.color.light_gray16));
            Picasso.with(this).load(R.mipmap.icon_group_gray_line).into(iv_online_consulting_line);

            Picasso.with(this).load(R.mipmap.icon_group_blue_files).into(iv_community_groups);
            tv_community_groups.setTextColor(getResources().getColor(R.color.orangeone));
            Picasso.with(this).load(R.mipmap.icon_group_blue_line).into(iv_community_groups_line);
        }
    }
}
