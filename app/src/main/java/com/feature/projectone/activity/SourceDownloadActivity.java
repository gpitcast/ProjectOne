package com.feature.projectone.activity;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.adapter.FragmentPagerAdapter;
import com.feature.projectone.adapter.SourceDownloadPopAdapter;
import com.feature.projectone.fragment.PeiTaoSourceFragment;
import com.feature.projectone.util.CommonUtil;
import com.feature.projectone.util.TvUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/2.
 * <p>
 * 资源下载界面
 */

public class SourceDownloadActivity extends BaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.imgTitleRight)
    ImageView imgTitleRight;
    @BindView(R.id.tvTitleRight)
    TextView tvTitleRight;
    @BindView(R.id.magicIndicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.ll_shaixuan)
    LinearLayout ll_shaixuan;
    @BindView(R.id.rl_title)
    RelativeLayout rl_title;

    private List<String> titleList;
    private List<Fragment> fragmentList;
    private List<ArrayList<String>> dataList;

    @Override
    protected void Response(String code, String msg, String url, Object result) {

    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_resource_download);
    }

    @Override
    public void beforeInitView() {
        initData();
    }

    private void initData() {
        titleList = new ArrayList<>();
        titleList.add("配套资源");
        titleList.add("英语听力");
        titleList.add("教材朗读");
        titleList.add("单词听写");
        titleList.add("模拟练习");

        fragmentList = new ArrayList<>();
        for (int i = 0; i < titleList.size(); i++) {
            PeiTaoSourceFragment peiTaoSourceFragment = new PeiTaoSourceFragment();
            fragmentList.add(peiTaoSourceFragment);
        }

        dataList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                ArrayList<String> titleList0 = new ArrayList<>();
                titleList0.add("初中学练优");
                titleList0.add("初中学练优");
                titleList0.add("初中学练优");
                titleList0.add("初中学练优");
                titleList0.add("初中学练优");
                titleList0.add("初中学练优");
                dataList.add(titleList0);
            } else if (i == 1) {
                ArrayList<String> titleList1 = new ArrayList<>();
                titleList1.add("语文");
                titleList1.add("语文");
                titleList1.add("语文");
                titleList1.add("语文");
                titleList1.add("语文");
                titleList1.add("语文");
                titleList1.add("语文");
                titleList1.add("语文");
                titleList1.add("语文");
                titleList1.add("语文");
                dataList.add(titleList1);
            } else if (i == 2) {
                ArrayList<String> titleList2 = new ArrayList<>();
                titleList2.add("人教版");
                titleList2.add("人教版");
                titleList2.add("人教版");
                titleList2.add("人教版");
                titleList2.add("人教版");
                titleList2.add("人教版");
                titleList2.add("人教版");
                titleList2.add("人教版");
                titleList2.add("人教版");
                titleList2.add("人教版");
                titleList2.add("人教版");
                titleList2.add("人教版");
                dataList.add(titleList2);
            } else if (i == 3) {
                ArrayList<String> titleList3 = new ArrayList<>();
                titleList3.add("一年级");
                titleList3.add("一年级");
                titleList3.add("一年级");
                titleList3.add("一年级");
                titleList3.add("一年级");
                titleList3.add("一年级");
                titleList3.add("一年级");
                titleList3.add("一年级");
                titleList3.add("一年级");
                dataList.add(titleList3);
            }
        }
    }

    @Override
    public void initView() {
        tvTitle.setText("资源下载");
        imgTitleRight.setVisibility(View.GONE);
        TvUtil.addUnderLine(tvTitleRight);
        tvTitleRight.setText("已下载");

        initPager();
    }

    private void initPager() {

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), this, fragmentList, titleList);
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(fragmentList.size());

        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return titleList == null ? 0 : titleList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                clipPagerTitleView.setText(titleList.get(index));
                clipPagerTitleView.setTextSize(40);
                clipPagerTitleView.setTextColor(getResources().getColor(R.color.normal_black));
                clipPagerTitleView.setClipColor(getResources().getColor(R.color.orangeone));
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
                return null;//不用指示器
            }
        });

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

    @OnClick({R.id.tvBack, R.id.ll_shaixuan})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                finish();
                break;
            case R.id.ll_shaixuan:
                initShaiXuanPopWindow();
                break;
        }
    }

    /**
     * 弹出筛选popwindow
     */
    private void initShaiXuanPopWindow() {
        PopupWindow popupWindow = new PopupWindow(this);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        View popView = LayoutInflater.from(this).inflate(R.layout.layout_shaixuan_pop, null);
        initPopView(popView, popupWindow);
        popupWindow.setContentView(popView);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(rl_title, 0, 0, Gravity.RIGHT);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
    }


    /**
     * 初始化popwindow里面的view
     *
     * @param popView
     * @param popupWindow
     */
    private void initPopView(final View popView, final PopupWindow popupWindow) {
        TextView tv_reset = (TextView) popView.findViewById(R.id.tv_reset);
        TextView tv_done = (TextView) popView.findViewById(R.id.tv_done);
        TvUtil.addUnderLine(tv_reset);
        TvUtil.addUnderLine(tv_done);
        LinearLayout ll_outside = (LinearLayout) popView.findViewById(R.id.ll_outside);
        ll_outside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        RecyclerView recyclerView = (RecyclerView) popView.findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        SourceDownloadPopAdapter sourceDownloadPopAdapter = new SourceDownloadPopAdapter(this, dataList);
        recyclerView.setAdapter(sourceDownloadPopAdapter);
    }
}
