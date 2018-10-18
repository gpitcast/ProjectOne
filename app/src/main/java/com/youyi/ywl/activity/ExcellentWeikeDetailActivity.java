package com.youyi.ywl.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.youyi.ywl.R;
import com.youyi.ywl.adapter.FragmentPagerAdapter;
import com.youyi.ywl.fragment.ContentListFragment_Weike;
import com.youyi.ywl.fragment.CourseIntroduceFragment_Weike;
import com.youyi.ywl.other.Constanst;
import com.youyi.ywl.util.GlideUtil;
import com.youyi.ywl.util.HttpUtils;
import com.youyi.ywl.util.ToastUtil;
import com.youyi.ywl.view.BasePopupWindow;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2018/8/13.
 * 精品微课 - 详情界面
 */

public class ExcellentWeikeDetailActivity extends BaseActivity {
    @BindView(R.id.magicIndicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.iv_is_want_learn)
    ImageView iv_is_want_learn;
    @BindView(R.id.ll_is_want_learn)
    LinearLayout ll_is_want_learn;
    @BindView(R.id.imageView)
    ImageView imageView;

    private static final String WANT_LEARN_URL = HttpUtils.Host + "/weike/wantLearn";//想学接口
    private static final String KEFU_URL = HttpUtils.Host + "/qunzhu/online_kefu";//获取客服信息的接口
    private List<String> titleList;
    private List<Fragment> fragmentList;
    private CourseIntroduceFragment_Weike courseIntroduceFragment_weike;
    private ContentListFragment_Weike contentListFragment_weike;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private String id;
    private String isWantLearn;//记录想学的状态

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case WANT_LEARN_URL:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        //0表示添加成功
                        showWantLearnState("1");
                    } else {
                        //其他表示添加失败
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                    }

                } else {
                    ToastUtil.show(this, msg, 0);
                }
                break;
            case KEFU_URL:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) resultMap.get("data");
                    Constanst.kefuId = dataMap.get("userId") + "";
                    Constanst.kefuName = dataMap.get("name") + "";
                } else {
                    ToastUtil.show(this, msg, 0);
                }
        }
    }

    @Override
    protected void handleIntent(Intent intent) {
        if (intent != null) {
            id = intent.getStringExtra("id");
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_excellent_weike_detail);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        PostKeFuList();
        initData();
        initPager();
    }

    private void initData() {
        titleList = new ArrayList<>();
        titleList.add("课程介绍");
        titleList.add("内容列表");

        fragmentList = new ArrayList<>();
        for (int i = 0; i < titleList.size(); i++) {
            switch (i) {
                case 0:
                    //课程介绍fragment
                    if (courseIntroduceFragment_weike == null) {
                        courseIntroduceFragment_weike = new CourseIntroduceFragment_Weike(id);
                        fragmentList.add(courseIntroduceFragment_weike);
                    }
                    break;
                case 1:
                    //课程目录fragment
                    if (contentListFragment_weike == null) {
                        contentListFragment_weike = new ContentListFragment_Weike(id);
                        fragmentList.add(contentListFragment_weike);
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
                clipPagerTitleView.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getApplicationContext().getResources().getDisplayMetrics()));
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
                linePagerIndicator.setLineWidth(152);
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

    public void loadImageView(String url) {
        GlideUtil.loadNetImageView(this,url,imageView);
    }

    @OnClick({R.id.iv_back, R.id.iv_share, R.id.ll_is_want_learn, R.id.ll_zixun})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_share:
                showSharePop();
                break;
            case R.id.ll_is_want_learn:
                //想学按钮
                if ("0".equals(isWantLearn)) {
                    //没有添加想学
                    PostWantLearnList(ll_is_want_learn);
                } else {
                    ToastUtil.show(this, "您已添加了想学,不必重复添加", 0);
                }
                break;
            case R.id.ll_zixun:
                //咨询按钮(跳转到客服聊天界面)
                RongIM.getInstance().startPrivateChat(this, Constanst.kefuId, Constanst.kefuName);
                break;
        }
    }

    //请求想学接口
    private void PostWantLearnList(LinearLayout ll_is_want_learn) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("controller", "weike");
        map.put("action", "wantLearn");
        map.put("id", id);
        getJsonUtil().PostJson(this, map, ll_is_want_learn);
    }

    //获取客服信息的接口
    private void PostKeFuList() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "qunzhu");
        map.put("action", "online_kefu");
        getJsonUtil().PostJson(this, map);
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
                    ToastUtil.show(ExcellentWeikeDetailActivity.this, "点击了微信朋友圈分享", 0);
                }
            });
            //微信
            popwindowView.findViewById(R.id.iv_wechat).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show(ExcellentWeikeDetailActivity.this, "点击了微信分享", 0);
                }
            });
            //QQ
            popwindowView.findViewById(R.id.iv_qq).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show(ExcellentWeikeDetailActivity.this, "点击了QQ分享", 0);
                }
            });
            //QQ空间
            popwindowView.findViewById(R.id.iv_qzone).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show(ExcellentWeikeDetailActivity.this, "点击了QQ空间分享", 0);
                }
            });
            //微博
            popwindowView.findViewById(R.id.iv_weibo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show(ExcellentWeikeDetailActivity.this, "点击了微博分享", 0);
                }
            });
            //复制链接
            popwindowView.findViewById(R.id.iv_copy_link).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show(ExcellentWeikeDetailActivity.this, "点击了复制链接", 0);
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

    //现实是否想学的状态图标(0代表用户没有添加想学 1代表用户添加了想学)
    public void showWantLearnState(String isWantLearn) {
        ll_is_want_learn.setVisibility(View.VISIBLE);
        this.isWantLearn = isWantLearn;
        if ("0".equals(isWantLearn)) {
            //没有添加想学
            iv_is_want_learn.setImageResource(R.mipmap.icon_blue_line_hearts);
        } else if ("1".equals(isWantLearn)) {
            //添加了想学
            iv_is_want_learn.setImageResource(R.mipmap.icon_blue_solid_hearts);
        }
    }
}
