package com.feature.projectone.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.feature.projectone.R;
import com.feature.projectone.adapter.FragmentPagerAdapter;
import com.feature.projectone.fragment.LoginFragment;
import com.feature.projectone.fragment.RegistFragment;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.ShareUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/3.
 * 登录与注册activity
 */

public class LoginAndRegistActivity extends BaseActivity {
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private List titleList;

    private List<Fragment> fragmentList;

    @Override
    protected void Response(String code, String msg, String url, Object result) {

    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_login_and_regist);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        initData();
        initPager();
    }


    private void initData() {
        titleList = new ArrayList<>();
        titleList.add("登陆");
        titleList.add("注册");

        fragmentList = new ArrayList<>();
        LoginFragment loginFragment = new LoginFragment();
        fragmentList.add(loginFragment);
        RegistFragment registFragment = new RegistFragment();
        fragmentList.add(registFragment);
    }

    private void initPager() {
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), this, fragmentList, titleList);
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public void afterInitView() {

    }
}
