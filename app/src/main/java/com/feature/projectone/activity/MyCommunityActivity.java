package com.feature.projectone.activity;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.adapter.FragmentPagerAdapter;
import com.feature.projectone.fragment.CommunityAggregationOrderFragment;
import com.feature.projectone.fragment.CommunityFriendCircleFragment;
import com.feature.projectone.fragment.LoginFragment;
import com.feature.projectone.fragment.RegistFragment;
import com.squareup.picasso.Picasso;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/16.
 * 我的社区界面
 */

public class MyCommunityActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.magicIndicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private List<String> mTitleList = new ArrayList<>();
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private CommunityAggregationOrderFragment communityAggregationOrderFragment;
    private CommunityFriendCircleFragment communityFriendCircleFragment;
    private FragmentPagerAdapter pagerAdapter;

    @Override
    protected void Response(String code, String msg, String url, Object result) {

    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_my_community);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        tv_title.setText("我的社区");
        initMagicIndicator();
    }


    private void initMagicIndicator() {

        mTitleList.add("社区集结令");
        mTitleList.add("社区朋友圈");

        for (int i = 0; i < mTitleList.size(); i++) {
            switch (i) {
                case 0:
                    //社区集结令fragment
                    if (communityAggregationOrderFragment == null) {
                        communityAggregationOrderFragment = new CommunityAggregationOrderFragment();
                    }
                    fragmentList.add(communityAggregationOrderFragment);
                    break;
                case 1:
                    //社区朋友圈fragment
                    if (communityFriendCircleFragment == null) {
                        communityFriendCircleFragment = new CommunityFriendCircleFragment();
                    }
                    fragmentList.add(communityFriendCircleFragment);
                    break;
            }
        }

        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), this, fragmentList, mTitleList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(fragmentList.size());

        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTitleList == null ? 0 : mTitleList.size();
            }

            @Override
            public IPagerTitleView getTitleView(final Context context, final int index) {

                CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(MyCommunityActivity.this);
                View view = LayoutInflater.from(context).inflate(R.layout.layout_community_title, null);
                final ImageView iv_title = (ImageView) view.findViewById(R.id.iv_title);
                final TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                tv_title.setText(mTitleList.get(index));
                commonPagerTitleView.setContentView(view);
                commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {
                    @Override
                    public void onSelected(int index, int i1) {
                        tv_title.setTextColor(getResources().getColor(R.color.orangeone));
                        if (index == 0) {
                            Picasso.with(context).load(R.mipmap.icon_community_blue_user).into(iv_title);
                        } else if (index == 1) {
                            Picasso.with(context).load(R.mipmap.icon_community_blue_chat).into(iv_title);
                        }
                    }

                    @Override
                    public void onDeselected(int index, int i1) {
                        tv_title.setTextColor(getResources().getColor(R.color.dark_black));
                        if (index == 0) {
                            Picasso.with(context).load(R.mipmap.icon_community_black_user).into(iv_title);
                        } else if (index == 1) {
                            Picasso.with(context).load(R.mipmap.icon_community_black_chat).into(iv_title);
                        }
                    }

                    @Override
                    public void onLeave(int i, int i1, float v, boolean b) {
                    }

                    @Override
                    public void onEnter(int i, int i1, float v, boolean b) {
                    }
                });

                commonPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });

                return commonPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setLineHeight(UIUtil.dip2px(context, 2));
                indicator.setColors(getResources().getColor(R.color.orangeone));
                return indicator;
            }
        });


        magicIndicator.setNavigator(commonNavigator);

        //关联
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                magicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                magicIndicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                magicIndicator.onPageScrollStateChanged(state);
            }
        });

        viewPager.setCurrentItem(0);
    }

    @Override
    public void afterInitView() {

    }

    @OnClick({R.id.ll_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }
}
