package com.feature.projectone.activity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.adapter.FragmentPagerAdapter;
import com.feature.projectone.fragment.RedianTuijianFragment;
import com.feature.projectone.fragment.StudyConsultFragment;
import com.feature.projectone.fragment.TeachConsultFragment;
import com.feature.projectone.fragment.TeachHeadlinesFragment;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.HttpUtils;
import com.feature.projectone.util.ToastUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;

import java.util.ArrayList;
import java.util.HashMap;

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
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    private static final String newsIndexUrl = HttpUtils.Host + "/news/index";//新闻列表接口
    private ArrayList<String> navigationList = new ArrayList<>();
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private FragmentPagerAdapter pagerAdapter;
    TeachHeadlinesFragment teachHeadlinesFragment;//教育头条fragment
    RedianTuijianFragment redianTuijianFragment;//热点推荐fragment
    TeachConsultFragment teachConsultFragment;//教育咨询fragment
    StudyConsultFragment studyConsultFragment;//学习咨询fragment
    private int pageno = 1;//分页参数，默认第一页

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case newsIndexUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap<String, Object> resultMap = (HashMap<String, Object>) result;
                    ArrayList<HashMap<String, Object>> navList = (ArrayList<HashMap<String, Object>>) resultMap.get("nav");
                    if (navList != null) {
                        navigationList.clear();
                        initData(navList);
                        initPager();
                    }
                } else {
                    ToastUtil.show(this, msg, 0);
                }
                break;
        }
    }

    private void initData(ArrayList<HashMap<String, Object>> navList) {
        for (int i = 0; i < navList.size(); i++) {
            HashMap<String, Object> map = navList.get(i);
            navigationList.add(map.get("title") + "");
        }

        for (int i = 0; i < navigationList.size(); i++) {
            switch (i) {
                case 0:
                    //教育头条
                    if (teachHeadlinesFragment == null) {
                        teachHeadlinesFragment = new TeachHeadlinesFragment(navList.get(0));
                    }
                    fragmentList.add(teachHeadlinesFragment);
                    break;
                case 1:
                    //热点推荐
                    if (redianTuijianFragment == null) {
                        redianTuijianFragment = new RedianTuijianFragment(navList.get(1));
                    }
                    fragmentList.add(redianTuijianFragment);
                    break;
                case 2:
                    //教育咨询
                    if (teachConsultFragment == null) {
                        teachConsultFragment = new TeachConsultFragment(navList.get(2));
                    }
                    fragmentList.add(teachConsultFragment);
                    break;
                case 3:
                    //学习咨询
                    if (studyConsultFragment == null) {
                        studyConsultFragment = new StudyConsultFragment(navList.get(3));
                    }
                    fragmentList.add(studyConsultFragment);
                    break;
            }
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_news_headlines_main);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tvTitle.setText(getString(R.string.news_headlines));
        PostList();
    }

    private void PostList() {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("controller", "news");
        map.put("action", "index");
        map.put("page", null);//分页参数  请求横向title数据暂时传null
        map.put("type", null);//新闻类型(1:教育头条 2:热点推荐 3:教育咨询 4: 学习咨询) 请求横向title数据暂时传null
        map.put("keywords", null);//关键词(格式：关键词1|关键词2) 关键字暂时传null
        getJsonUtil().PostJson(this, map);
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
