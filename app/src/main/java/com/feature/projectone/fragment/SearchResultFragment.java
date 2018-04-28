package com.feature.projectone.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;

import com.feature.projectone.R;
import com.feature.projectone.adapter.FragmentPagerAdapter;
import com.feature.projectone.inter.MyPassViewListener;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.HttpUtils;
import com.feature.projectone.util.ShareUtil;
import com.feature.projectone.util.ToastUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/19.
 * 搜索结果fragment
 */

public class SearchResultFragment extends BaseFragment {
    @BindView(R.id.magicIndicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private FragmentPagerAdapter pagerAdapter;
    private List<String> navigationList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();
    TeachHeadlinesSearchFragment teachHeadlinesFragment;//教育头条fragment
    RedianTuijianSearchFragment redianTuijianFragment;//热点推荐fragment
    TeachConsultSearchFragment teachConsultFragment;//教育咨询fragment
    StudyConsultSearchFragment studyConsultFragment;//学习咨询fragment
    ZongHeFragment zongHeFragment;//综合fragment
    private static final String SearchUrl = HttpUtils.Host + "/news/search";//新闻搜索接口
    private EditText etSearch;//从activity传递过来的EditText
    private String searchKey;

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case SearchUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap<String, Object> resultMap = (HashMap<String, Object>) result;
                    ArrayList<HashMap<String, Object>> navList = (ArrayList<HashMap<String, Object>>) resultMap.get("nav");
                    if (navList != null) {
                        navigationList.clear();
                        initData(navList);
                        initPager();
                    }
                } else {
                    ToastUtil.show(getActivity(), msg, 0);
                }
                break;
        }
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
        return R.layout.fragment_search_result;
    }

    @Override
    protected void initViewsAndEvents(View view) {
    }


    private void initData(ArrayList<HashMap<String, Object>> navList) {

        if (navigationList != null) {
            navigationList.clear();
        }

        for (int i = 0; i < navList.size(); i++) {
            HashMap<String, Object> map = navList.get(i);
            navigationList.add(map.get("title") + "");
        }

        for (int i = 0; i < navigationList.size(); i++) {
            switch (i) {
                case 0:
                    if (zongHeFragment == null) {
                        zongHeFragment = new ZongHeFragment(navList.get(0));
                        fragmentList.add(zongHeFragment);
                    }
                    break;
                case 1:
                    //教育头条
                    if (teachHeadlinesFragment == null) {
                        teachHeadlinesFragment = new TeachHeadlinesSearchFragment(navList.get(1));
                        fragmentList.add(teachHeadlinesFragment);
                    }
                    break;
                case 2:
                    //热点推荐
                    if (redianTuijianFragment == null) {
                        redianTuijianFragment = new RedianTuijianSearchFragment(navList.get(2));
                        fragmentList.add(redianTuijianFragment);
                    }

                    break;
                case 3:
                    //教育咨询
                    if (teachConsultFragment == null) {
                        teachConsultFragment = new TeachConsultSearchFragment(navList.get(3));
                        fragmentList.add(teachConsultFragment);
                    }

                    break;
                case 4:
                    //学习咨询
                    if (studyConsultFragment == null) {
                        studyConsultFragment = new StudyConsultSearchFragment(navList.get(4));
                        fragmentList.add(studyConsultFragment);
                    }
                    break;
            }
        }
    }

    /**
     * 初始化ViewPager和MagicIndicator，并关联它们
     */
    private void initPager() {
        //实例化viewPager
        pagerAdapter = new FragmentPagerAdapter(getActivity().getSupportFragmentManager(), getActivity(), fragmentList, navigationList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(fragmentList.size());
        //实例化MagicIndicator
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
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
                clipPagerTitleView.setTextSize(32);
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
        commonNavigator.setAdjustMode(true);
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
    public void onAttach(Context context) {
        super.onAttach(context);
        etSearch = ((MyPassViewListener) context).getEtSearch();

    }

    //fragment在show和hide过程中不走任何生命周期方法，只会调用onHiddenChanged
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            // 不在最前端显示
        } else {
            // 在最前端显示 相当于调用了onResume();相当于调用了onPause();在每次显示的时候，也就是点下了搜索按钮的时候 请求接口刷新数据
            PostSearchList();
        }
    }

    public void PostSearchList() {
        searchKey = etSearch.getText().toString().trim();
        if (searchKey == null || searchKey.length() == 0) {
            ToastUtil.show(getActivity(), "搜索关键字不能为空", 0);
            return;
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("controller", "news");
        map.put("action", "search");
        map.put("page", "1");
        map.put("keywords", searchKey);
        map.put("tags", ShareUtil.getString(getActivity(), Constanst.TAGS));
        getJsonUtil().PostJson(getActivity(), map);
    }
}
