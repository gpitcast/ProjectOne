package com.youyi.ywl.guide;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.youyi.ywl.R;
import com.youyi.ywl.activity.BaseActivity;
import com.youyi.ywl.adapter.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/10.
 * 引导页1
 */

public class GuideActivity extends BaseActivity {
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;

    private List<Fragment> fragmentList = new ArrayList<>();
    private FragmentPagerAdapter fragmentPagerAdapter;

    @Override
    protected void Response(String code, String msg, String url, Object result) {
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_guide);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        initPager();
    }

    private void initPager() {
        Guide1Fragment guide1Fragment = new Guide1Fragment();
        fragmentList.add(guide1Fragment);
        Guide2Fragment guide2Fragment = new Guide2Fragment();
        fragmentList.add(guide2Fragment);
        Guide3Fragment guide3Fragment = new Guide3Fragment();
        fragmentList.add(guide3Fragment);
        Guide4Fragment guide4Fragment = new Guide4Fragment();
        fragmentList.add(guide4Fragment);

        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), this, fragmentList, null);
        mViewPager.setAdapter(fragmentPagerAdapter);
        mViewPager.setCurrentItem(0);
    }

    @Override
    public void afterInitView() {
    }
}
