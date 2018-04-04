package com.feature.projectone.activity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.adapter.FragmentPagerAdapter;
import com.feature.projectone.fragment.NewsFragment;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/2.
 * 资讯头条首页界面
 */

public class NewsHeadlinesMainActivity extends BaseActivity {
    @BindView(R.id.magicIndicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private ArrayList<String> navigationList = new ArrayList<>();
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private FragmentPagerAdapter pagerAdapter;

    @Override
    protected void Response(String code, String msg, String url, Object result) {

    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_news_headlines_main);
    }

    @Override
    public void beforeInitView() {

        //初始化title的文本信息和fragment
        navigationList.add("热点推荐");
        navigationList.add("教育资讯");
        navigationList.add("热点推荐");
        navigationList.add("教育视频");
        navigationList.add("学习资讯");

        for (int i = 0; i < navigationList.size(); i++) {
            NewsFragment newsFragment = new NewsFragment();
            fragmentList.add(newsFragment);
        }
    }

    @Override
    public void initView() {
        initPager();
    }

    /**
     * 初始化ViewPager和MagicIndicator，并关联它们
     */
    private void initPager() {
        //实力化viewPager
        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), this, fragmentList, navigationList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(fragmentList.size());
        //实例化MagicIndicator
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return navigationList == null ? 0 : navigationList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                clipPagerTitleView.setText(navigationList.get(index));
                clipPagerTitleView.setTextColor(getResources().getColor(R.color.normal_black));
                clipPagerTitleView.setClipColor(getResources().getColor(R.color.orangeone));
                clipPagerTitleView.setTextSize(40);
                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return clipPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;// 没有指示器，因为title的指示作用已经很明显了
            }
        });

        magicIndicator.setNavigator(commonNavigator);//将标题适配器和指示器关联

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

    @OnClick({R.id.tvBack})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                finish();
                break;
        }
    }
}
