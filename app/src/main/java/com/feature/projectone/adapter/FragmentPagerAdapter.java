package com.feature.projectone.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/3/16.
 */

public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private ArrayList<Fragment> fragmentList;

    public FragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
