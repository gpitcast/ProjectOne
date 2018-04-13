package com.feature.projectone.activity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.adapter.FragmentPagerAdapter;
import com.feature.projectone.adapter.PopGridViewAdapter;
import com.feature.projectone.fragment.HearingDownLoadFragment;
import com.feature.projectone.fragment.PeiTaoSourceFragment;
import com.feature.projectone.fragment.ReadingBookFragment;
import com.feature.projectone.fragment.WordDictationFragment;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.HttpUtils;
import com.feature.projectone.util.ToastUtil;
import com.feature.projectone.util.TvUtil;
import com.feature.projectone.util.ViewMeasureUtil;
import com.feature.projectone.view.MyGridView;

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
    @BindView(R.id.rl_magicIndicator)
    RelativeLayout rl_magicIndicator;
    @BindView(R.id.view_line)
    View view_line;

    private List<Fragment> fragmentList;
    private static final String shaixuanUrl = HttpUtils.Host + "/source/cate";//筛选接口
    private ArrayList<HashMap<String, Object>> fragmentTitleList = new ArrayList<>();//存储nav集合的数据，里面有fragment的title数据
    private FragmentPagerAdapter fragmentPagerAdapter;
    private List<String> titleList;//将fragmentTitleList里的title数据取出来存到该集合
    private ArrayList<Object> shaixuanDataList = new ArrayList<>();//所有fragment对应的筛选数据，按照index来取，index 0 的就是对应的navList的0的fragment数据
    private List<HashMap<String, Object>> sonTitleList = new ArrayList<>();//儿子级用来存储条目textview数据的集合
    private List<HashMap<String, Object>> sunziTitleList = new ArrayList<>();//孙子级用来存储条目textview数据的集合
    private List<HashMap<String, Object>> zenSunziTitleList = new ArrayList<>();//曾孙子级用来存储条目textview数据的集合
    private List<HashMap<String, Object>> chongSunziTitleList = new ArrayList<>();//重孙子级用来存储条目textview数据的集合
    private List<Object> smallTitleList = new ArrayList<>();//用来存储图书，学科等title数据的集合
    private LinearLayout ll_book;
    private LinearLayout ll_xueke;
    private LinearLayout ll_version;
    private LinearLayout ll_grade;
    private TextView tv_title_book;
    private TextView tv_title_xueke;
    private TextView tv_title_version;
    private TextView tv_title_grade;
    private MyGridView gridView_book;
    private MyGridView gridView_xueke;
    private MyGridView gridView_version;
    private MyGridView gridView_grade;
    private ArrayList<Object> firstList;
    private PeiTaoSourceFragment peiTaoSourceFragment;
    private HearingDownLoadFragment hearingDownLoadFragment;
    private ReadingBookFragment readingBookFragment;
    private WordDictationFragment wordDictationFragment;


    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case shaixuanUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    //横向滑动一级分类的数据
                    ArrayList<HashMap<String, Object>> navList = (ArrayList<HashMap<String, Object>>) resultMap.get("nav");
                    fragmentTitleList.addAll(navList);
                    //筛选框每一级的title(图书，学科，版本，年级)
                    ArrayList<Object> titlesList = (ArrayList<Object>) resultMap.get("titles");
                    smallTitleList.addAll(titlesList);
                    //筛选框每一级的分类
                    ArrayList<Object> s_navList = (ArrayList<Object>) resultMap.get("s_nav");
                    shaixuanDataList.addAll(s_navList);

                    initData();//初始化fragment和title
                } else {
                    ToastUtil.show(this, msg, 0);
                }
                break;
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_resource_download);
    }

    private int rlTitleHeight;
    private int rlMagicIndicatorHeght;
    private int viewLineHeght;


    @Override
    public void beforeInitView() {
    }


    private void initData() {
        if (titleList == null) {
            titleList = new ArrayList<>();
        }
        for (int i = 0; i < fragmentTitleList.size(); i++) {
            String title = ((HashMap<String, Object>) fragmentTitleList.get(i)).get("title") + "";
            titleList.add(title);
        }
        fragmentList = new ArrayList<>();
        for (int i = 0; i < titleList.size(); i++) {
            switch (i) {
                case 0:
                    //配套资源fragment
                    if (peiTaoSourceFragment == null) {
                        peiTaoSourceFragment = new PeiTaoSourceFragment(((HashMap<String, Object>) fragmentTitleList.get(i)).get("cate_id") + "");
                    }
                    fragmentList.add(peiTaoSourceFragment);
                    break;
                case 1:
                    //听力下载fragment
                    if (hearingDownLoadFragment == null) {
                        hearingDownLoadFragment = new HearingDownLoadFragment(((HashMap<String, Object>) fragmentTitleList.get(i)).get("cate_id") + "");
                    }
                    fragmentList.add(hearingDownLoadFragment);
                    break;
                case 2:
                    //教材朗读fragment
                    if (readingBookFragment == null) {
                        readingBookFragment = new ReadingBookFragment(((HashMap<String, Object>) fragmentTitleList.get(i)).get("cate_id") + "");
                    }
                    fragmentList.add(readingBookFragment);
                    break;
                case 3:
                    //单词听写fragment
                    if (wordDictationFragment == null) {
                        wordDictationFragment = new WordDictationFragment(((HashMap<String, Object>) fragmentTitleList.get(i)).get("cate_id") + "");
                    }
                    fragmentList.add(wordDictationFragment);
                    break;
            }

        }
        initPager();//初始化界面
    }

    @Override
    public void initView() {
        tvTitle.setText("资源下载");
        TvUtil.addUnderLine(tvTitleRight);
        tvTitleRight.getPaint().setFakeBoldText(true);
        tvTitleRight.setText("已下载");
        PostShaixuanList();
    }

    /**
     * 请求筛选接口
     */
    private void PostShaixuanList() {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("controller", "source");
        map.put("action", "cate");
        getJsonUtil().PostJson(this, map);
    }

    private void initPager() {

        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), this, fragmentList, titleList);
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(fragmentList.size());

        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return fragmentTitleList == null ? 0 : fragmentTitleList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                clipPagerTitleView.setText((((HashMap<String, Object>) fragmentTitleList.get(index)).get("title")) + "");
                clipPagerTitleView.setTextSize(32);
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

    private PopupWindow popupWindow;
    private View popView;

    /**
     * 弹出筛选popwindow
     */
    private void initShaiXuanPopWindow() {
        if (popupWindow == null) {
            popupWindow = new PopupWindow(this);
            popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        }
        if (popView == null) {
            popView = LayoutInflater.from(this).inflate(R.layout.layout_shaixuan_pop, null);
        }
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

    private PopGridViewAdapter popGridViewAdapter_book;
    private PopGridViewAdapter popGridViewAdapter_xueke;
    private PopGridViewAdapter popGridViewAdapter_version;
    private PopGridViewAdapter popGridViewAdapter_grade;
    private int selectPosition_book;//记录儿子级选中的条目position
    private int selectPosition_xueke;//记录孙子级选中条目的position
    private int selectPosition_version;//记录曾孙子级选中条目的position
    private int selectPosition_grade;//记录重孙子级选中条目的position

    /**
     * 初始化popwindow里面的view
     *
     * @param popView
     * @param popupWindow
     */
    private void initPopView(final View popView, final PopupWindow popupWindow) {

        //外面阴影部分点击隐藏popwindow
        LinearLayout ll_outside = (LinearLayout) popView.findViewById(R.id.ll_outside);
        ll_outside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        //图书模块布局
        ll_book = (LinearLayout) popView.findViewById(R.id.ll_book);
        //学科模块布局
        ll_xueke = (LinearLayout) popView.findViewById(R.id.ll_xueke);
        //版本模块布局
        ll_version = (LinearLayout) popView.findViewById(R.id.ll_version);
        //年级模块布局
        ll_grade = (LinearLayout) popView.findViewById(R.id.ll_grade);

        //图书的标题
        tv_title_book = (TextView) popView.findViewById(R.id.tv_title_book);
        //学科的标题
        tv_title_xueke = (TextView) popView.findViewById(R.id.tv_title_xueke);
        //版本的标题
        tv_title_version = (TextView) popView.findViewById(R.id.tv_title_version);
        //年级的标题
        tv_title_grade = (TextView) popView.findViewById(R.id.tv_title_grade);
        tv_title_book.setText(smallTitleList.get(0) + "");
        tv_title_xueke.setText(smallTitleList.get(1) + "");
        tv_title_version.setText(smallTitleList.get(2) + "");
        tv_title_grade.setText(smallTitleList.get(3) + "");

        //图书的gridview
        gridView_book = (MyGridView) popView.findViewById(R.id.gridView_book);
        //学科的gridview
        gridView_xueke = (MyGridView) popView.findViewById(R.id.gridView_xueke);
        //版本的gridview
        gridView_version = (MyGridView) popView.findViewById(R.id.gridView_version);
        //年级的gridview
        gridView_grade = (MyGridView) popView.findViewById(R.id.gridView_grade);

        //每一次弹出popupwindow都先隐藏最后三级的筛选布局
        ll_xueke.setVisibility(View.GONE);
        ll_version.setVisibility(View.GONE);
        ll_grade.setVisibility(View.GONE);

        int currentItem = viewPager.getCurrentItem();
        String cate_id1 = ((HashMap<String, Object>) fragmentTitleList.get(currentItem)).get("cate_id") + "";//拿到fragment级的cate_id

        //对应的每一个fragment的筛选数据
        firstList = (ArrayList<Object>) shaixuanDataList.get(currentItem);
        if (firstList == null || firstList.size() == 0) {
            ll_book.setVisibility(View.GONE);
            ToastUtil.show(this, "该版块暂时无数据", 0);
            return;
        } else {
            ll_book.setVisibility(View.VISIBLE);
        }
        ArrayList<HashMap<String, Object>> sonList = (ArrayList<HashMap<String, Object>>) firstList.get(0);//儿子级
        sonTitleList.clear();//添加数据前清空上一次的筛选数据
        for (int i = 0; i < sonList.size(); i++) {
            String pid2 = ((HashMap<String, Object>) sonList.get(i)).get("pid") + "";
            if (cate_id1 != null && cate_id1.equals(pid2)) {
                HashMap<String, Object> cataList = (HashMap<String, Object>) sonList.get(i);
                sonTitleList.add(cataList);
            }
        }
        if (sonTitleList != null && sonTitleList.size() > 0) {
            if (popGridViewAdapter_book == null) {
                popGridViewAdapter_book = new PopGridViewAdapter(this, sonTitleList);
                gridView_book.setAdapter(popGridViewAdapter_book);
                popGridViewAdapter_book.changeDefault();//第一次初始化默认未选中状态
                gridView_book.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        popGridViewAdapter_book.changeState(position);
                        selectPosition_book = position;
                        ll_version.setVisibility(View.GONE);
                        ll_grade.setVisibility(View.GONE);
                        initSunzi(((HashMap<String, Object>) sonTitleList.get(position)), ((ArrayList<HashMap<String, Object>>) firstList.get(1)), position);
                    }
                });
            } else {
                //在不是第一次初始化adapter的时候还原初始化之前的选择状态
                selectPosition_book = -1;//初始化记录被选中的position
                popGridViewAdapter_book.changeDefault();//还原默认未选中状态
                popGridViewAdapter_book.notifyDataSetChanged();
            }
        }


    }

    private void initSunzi(HashMap<String, Object> sonTitleMap, ArrayList<HashMap<String, Object>> sunziList, int positionSon) {
        sunziTitleList.clear();//添加数据前清空上一次的筛选数据
        String cate_id = sonTitleMap.get("cate_id") + "";
        for (int i = 0; i < sunziList.size(); i++) {
            String pid = ((HashMap<String, Object>) sunziList.get(i)).get("pid") + "";
            if (cate_id != null && cate_id.equals(pid)) {
                sunziTitleList.add(sunziList.get(i));
            }
        }
        if (sunziTitleList != null && sunziTitleList.size() > 0) {
            ll_xueke.setVisibility(View.VISIBLE);
            if (popGridViewAdapter_xueke == null) {
                popGridViewAdapter_xueke = new PopGridViewAdapter(this, sunziTitleList);
                gridView_xueke.setAdapter(popGridViewAdapter_xueke);
                popGridViewAdapter_xueke.changeDefault();//第一次初始化默认未选中状态
                gridView_xueke.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        popGridViewAdapter_xueke.changeState(position);
                        selectPosition_xueke = position;
                        ll_grade.setVisibility(View.GONE);
                        initZengSunzi(((HashMap<String, Object>) sunziTitleList.get(position)), ((ArrayList<HashMap<String, Object>>) firstList.get(2)), position);
                    }
                });
            } else {
                //在不是第一次初始化adapter的时候还原初始化之前的选择状态
                selectPosition_xueke = -1;//初始化记录被选中的position
                popGridViewAdapter_xueke.changeDefault();//还原默认未选中状态
                popGridViewAdapter_xueke.notifyDataSetChanged();
            }
        } else {
            ll_xueke.setVisibility(View.GONE);
            //刷新数据
            int currentItem = viewPager.getCurrentItem();
            switch (currentItem) {
                case 0:
                    peiTaoSourceFragment.shaiXuanRefresh(((HashMap<String, Object>) sonTitleList.get(positionSon)).get("cate_id") + "");
                    break;
                case 1:

                    break;
                case 2:

                    break;
                case 3:

                    break;
            }
            popupWindow.dismiss();//关闭筛选popwindow
        }
    }

    private void initZengSunzi(HashMap<String, Object> sunziTitleMap, ArrayList<HashMap<String, Object>> zengSunziList, int sunziPosition) {
        zenSunziTitleList.clear();//添加数据前清空上一次的筛选数据
        String cate_id = sunziTitleMap.get("cate_id") + "";
        for (int i = 0; i < zengSunziList.size(); i++) {
            String pid = ((HashMap<String, Object>) zengSunziList.get(i)).get("pid") + "";
            if (cate_id != null && cate_id.equals(pid)) {
                zenSunziTitleList.add(zengSunziList.get(i));
            }
        }
        if (zenSunziTitleList != null && zenSunziTitleList.size() > 0) {
            ll_version.setVisibility(View.VISIBLE);
            if (popGridViewAdapter_version == null) {
                popGridViewAdapter_version = new PopGridViewAdapter(this, zenSunziTitleList);
                gridView_version.setAdapter(popGridViewAdapter_version);
                popGridViewAdapter_version.changeDefault();//第一次初始化默认未选中状态
                gridView_version.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        popGridViewAdapter_version.changeState(position);
                        selectPosition_version = position;

                        initChongSunzi(((HashMap<String, Object>) zenSunziTitleList.get(position)), ((ArrayList<HashMap<String, Object>>) firstList.get(3)), position);
                    }
                });

            } else {
                //在不是第一次初始化adapter的时候还原初始化之前的选择状态
                selectPosition_version = -1;//初始化记录被选中的position
                popGridViewAdapter_version.changeDefault();//还原默认未选中状态
                popGridViewAdapter_version.notifyDataSetChanged();
            }
        } else {
            ll_version.setVisibility(View.GONE);
            //刷新数据
            int currentItem = viewPager.getCurrentItem();
            switch (currentItem) {
                case 0:
                    peiTaoSourceFragment.shaiXuanRefresh(((HashMap<String, Object>) sunziTitleList.get(sunziPosition)).get("cate_id") + "");
                    break;
                case 1:

                    break;
                case 2:

                    break;
                case 3:

                    break;
            }
            popupWindow.dismiss();//关闭筛选popwindow
        }
    }

    private void initChongSunzi(HashMap<String, Object> zengSunziTitleMap, ArrayList<HashMap<String, Object>> chongSunziList, int chongSunziPosition) {
        chongSunziTitleList.clear();//添加数据前清空上一次的筛选数据
        String cate_id = zengSunziTitleMap.get("cate_id") + "";
        for (int i = 0; i < chongSunziList.size(); i++) {
            String pid = ((HashMap<String, Object>) chongSunziList.get(i)).get("pid") + "";
            if (cate_id != null && cate_id.equals(pid)) {
                chongSunziTitleList.add(chongSunziList.get(i));
            }
        }
        if (chongSunziTitleList != null && chongSunziTitleList.size() > 0) {
            ll_grade.setVisibility(View.VISIBLE);
            if (popGridViewAdapter_grade == null) {
                popGridViewAdapter_grade = new PopGridViewAdapter(this, chongSunziTitleList);
                gridView_grade.setAdapter(popGridViewAdapter_grade);
                popGridViewAdapter_grade.changeDefault();//第一次初始化默认未选中状态
                gridView_grade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        popGridViewAdapter_grade.changeState(position);
                        selectPosition_grade = position;
                        //最后一级不用再创建了，直接刷新数据
                        int currentItem = viewPager.getCurrentItem();
                        switch (currentItem) {
                            case 0:
                                peiTaoSourceFragment.shaiXuanRefresh(((HashMap<String, Object>) chongSunziTitleList.get(position)).get("cate_id") + "");
                                break;
                            case 1:

                                break;
                            case 2:

                                break;
                            case 3:

                                break;
                        }
                        popupWindow.dismiss();//关闭筛选popwindow
                    }
                });
            } else {
                //在不是第一次初始化adapter的时候还原初始化之前的选择状态
                selectPosition_grade = -1;//初始化记录被选中的position
                popGridViewAdapter_grade.changeDefault();//还原默认未选中状态
                popGridViewAdapter_grade.notifyDataSetChanged();
            }
        } else {
            ll_grade.setVisibility(View.GONE);
            //刷新数据
            int currentItem = viewPager.getCurrentItem();
            switch (currentItem) {
                case 0:
                    peiTaoSourceFragment.shaiXuanRefresh(((HashMap<String, Object>) zenSunziTitleList.get(chongSunziPosition)).get("cate_id") + "");
                    break;
                case 1:

                    break;
                case 2:

                    break;
                case 3:

                    break;
            }
            popupWindow.dismiss();//关闭筛选popwindow
        }
    }
}
