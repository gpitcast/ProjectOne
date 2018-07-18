package com.youyi.YWL.activity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youyi.YWL.R;
import com.youyi.YWL.adapter.FragmentPagerAdapter;
import com.youyi.YWL.fragment.CourseCatalogFragment;
import com.youyi.YWL.fragment.CourseEvaluateFragment;
import com.youyi.YWL.fragment.CourseIntroduceFragment;
import com.youyi.YWL.util.ToastUtil;
import com.youyi.YWL.view.BasePopupWindow;

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
 * Created by Administrator on 2018/7/16.
 * 精品课程 - 详情界面
 */

public class ExcellentCourseDetailActivity extends BaseActivity {
    @BindView(R.id.magicIndicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private List<String> titleList;
    private List<Fragment> fragmentList;
    private CourseIntroduceFragment courseIntroduceFragment;
    private CourseCatalogFragment courseCatalogFragment;
    private CourseEvaluateFragment courseEvaluateFragment;
    private FragmentPagerAdapter fragmentPagerAdapter;


    @Override
    protected void Response(String code, String msg, String url, Object result) {
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_excellent_course_detail);
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
        titleList.add("课程介绍");
        titleList.add("课程目录");
        titleList.add("课程评价");

        fragmentList = new ArrayList<>();
        for (int i = 0; i < titleList.size(); i++) {
            switch (i) {
                case 0:
                    //课程介绍fragment
                    if (courseIntroduceFragment == null) {
                        courseIntroduceFragment = new CourseIntroduceFragment();
                        fragmentList.add(courseIntroduceFragment);
                    }
                    break;
                case 1:
                    //课程目录fragment
                    if (courseCatalogFragment == null) {
                        courseCatalogFragment = new CourseCatalogFragment();
                        fragmentList.add(courseCatalogFragment);
                    }
                    break;
                case 2:
                    //课程评价fragment
                    if (courseEvaluateFragment == null) {
                        courseEvaluateFragment = new CourseEvaluateFragment();
                        fragmentList.add(courseEvaluateFragment);
                    }
                    break;
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
                clipPagerTitleView.setTextSize(32);
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
                linePagerIndicator.setLineHeight(2);
                linePagerIndicator.setLineWidth(132);
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

    @OnClick({R.id.iv_back, R.id.iv_share})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_share:
                showSharePop();
                break;
        }
    }

    private View popwindowView;
    private BasePopupWindow basePopupWindow;

    private void showSharePop() {
        if (popwindowView == null) {
            popwindowView = LayoutInflater.from(this).inflate(R.layout.layout_share_pop, null);
            popwindowView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (basePopupWindow != null) {
                        basePopupWindow.dismiss();
                    }
                }
            });
            //微信朋友圈
            popwindowView.findViewById(R.id.iv_wechat_friends_circle).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show(ExcellentCourseDetailActivity.this, "点击了微信朋友圈分享", 0);
                }
            });
            //微信
            popwindowView.findViewById(R.id.iv_wechat).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show(ExcellentCourseDetailActivity.this, "点击了微信分享", 0);
                }
            });
            //QQ
            popwindowView.findViewById(R.id.iv_qq).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show(ExcellentCourseDetailActivity.this, "点击了QQ分享", 0);
                }
            });
            //QQ空间
            popwindowView.findViewById(R.id.iv_qzone).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show(ExcellentCourseDetailActivity.this, "点击了QQ空间分享", 0);
                }
            });
            //微博
            popwindowView.findViewById(R.id.iv_weibo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show(ExcellentCourseDetailActivity.this, "点击了微博分享", 0);
                }
            });
            //复制链接
            popwindowView.findViewById(R.id.iv_copy_link).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show(ExcellentCourseDetailActivity.this, "点击了复制链接", 0);
                }
            });
        }

        if (basePopupWindow == null) {
            basePopupWindow = new BasePopupWindow(this);
            basePopupWindow.setContentView(popwindowView);
            basePopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            basePopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            basePopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
            basePopupWindow.setOutsideTouchable(true);
            basePopupWindow.setFocusable(true);
        }

        basePopupWindow.showAtLocation(findViewById(R.id.ll_base), Gravity.BOTTOM, 0, 0);
    }
}
