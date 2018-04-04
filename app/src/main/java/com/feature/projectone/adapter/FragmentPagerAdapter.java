package com.feature.projectone.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.feature.projectone.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/16.
 * 资讯头条首页里fragment的适配器
 */

public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private List<Fragment> fragmentList;
    private List<String> navigationList;

    public FragmentPagerAdapter(FragmentManager fm, Context context, List<Fragment> fragmentList, List<String> navigationList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.navigationList = navigationList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
    @Override
    public CharSequence getPageTitle(int position) {
        return navigationList == null ? "" : navigationList.get(position);
    }

}
