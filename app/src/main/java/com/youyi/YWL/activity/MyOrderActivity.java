package com.youyi.YWL.activity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.youyi.YWL.R;
import com.youyi.YWL.adapter.FragmentPagerAdapter;
import com.youyi.YWL.fragment.AllOrdersFragment;
import com.youyi.YWL.fragment.AlreadyPayFragment;
import com.youyi.YWL.fragment.HasBeenCancelledFragment;
import com.youyi.YWL.fragment.ToBePayFragment;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/10.
 * 我的订单界面
 */

public class MyOrderActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.magicIndicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private List<String> titleList;
    private List<Fragment> fragmentList;
    private AllOrdersFragment allOrdersFragment;
    private AlreadyPayFragment alreadyPayFragment;
    private ToBePayFragment toBePayFragment;
    private HasBeenCancelledFragment hasBeenCancelledFragment;
    private FragmentPagerAdapter fragmentPagerAdapter;

    @Override
    protected void Response(String code, String msg, String url, Object result) {
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_my_order);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tv_title.setText("我的订单");
        initDatas();
        initPager();
    }


    private void initDatas() {
        if (titleList == null) {
            titleList = new ArrayList<>();
            titleList.add("全部");
            titleList.add("已支付");
            titleList.add("待支付");
            titleList.add("已取消");
        }

        if (fragmentList == null) {
            fragmentList = new ArrayList<>();

            for (int i = 0; i < titleList.size(); i++) {
                switch (i) {
                    case 0:
                        //全部
                        if (allOrdersFragment == null) {
                            allOrdersFragment = new AllOrdersFragment();
                            fragmentList.add(allOrdersFragment);
                        }
                        break;
                    case 1:
                        //已支付
                        if (alreadyPayFragment == null) {
                            alreadyPayFragment = new AlreadyPayFragment();
                            fragmentList.add(alreadyPayFragment);
                        }
                        break;
                    case 2:
                        //待支付
                        if (toBePayFragment == null) {
                            toBePayFragment = new ToBePayFragment();
                            fragmentList.add(toBePayFragment);
                        }
                        break;
                    case 3:
                        //已取消
                        if (hasBeenCancelledFragment == null) {
                            hasBeenCancelledFragment = new HasBeenCancelledFragment();
                            fragmentList.add(hasBeenCancelledFragment);
                        }
                        break;
                }
            }
        }
    }

    private void initPager() {
        if (fragmentList == null) {
            return;
        }
        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), this, fragmentList, titleList);
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(fragmentList.size());

        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return titleList == null ? 0 : titleList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int i) {
                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                clipPagerTitleView.setText(titleList.get(i));
                clipPagerTitleView.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getApplicationContext().getResources().getDisplayMetrics()));
                clipPagerTitleView.setTextColor(getResources().getColor(R.color.normal_black));
                clipPagerTitleView.setClipColor(getResources().getColor(R.color.orangeone));
                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(i);
                    }
                });
                return clipPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setColors(getResources().getColor(R.color.orangeone));
                linePagerIndicator.setLineHeight(4);
                linePagerIndicator.setLineWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getApplicationContext().getResources().getDisplayMetrics()));
                linePagerIndicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                return linePagerIndicator;
            }
        });

        commonNavigator.setAdjustMode(true);//均分宽度
        magicIndicator.setNavigator(commonNavigator);

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
