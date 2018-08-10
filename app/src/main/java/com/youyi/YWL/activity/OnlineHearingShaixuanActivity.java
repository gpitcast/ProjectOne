package com.youyi.YWL.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youyi.YWL.R;
import com.youyi.YWL.adapter.OnlineHearingShaixuanGridAdapter;
import com.youyi.YWL.other.Constanst;
import com.youyi.YWL.util.HttpUtils;
import com.youyi.YWL.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/8/2.
 * 在线听力筛选界面
 */

public class OnlineHearingShaixuanActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.ll_take_up)
    LinearLayout ll_take_up;//收起
    //模块
    @BindView(R.id.ll_jiaocai)
    LinearLayout ll_jiaocai;//对应教材的模块
    @BindView(R.id.ll_banben)
    LinearLayout ll_banben;//对应版本的模块
    @BindView(R.id.ll_nianji)
    LinearLayout ll_nianji;//对应年级的模块
    @BindView(R.id.ll_danyuan)
    LinearLayout ll_danyuan;//对应单元的模块
    //标题
    @BindView(R.id.tv_jiaocai)
    TextView tv_jiaocai;//对应教材的标题
    @BindView(R.id.tv_banben)
    TextView tv_banben;//对应版本的标题
    @BindView(R.id.tv_nianji)
    TextView tv_nianji;//对应年级的标题
    @BindView(R.id.tv_danyuan)
    TextView tv_danyuan;//对应单元的标题
    //gridview
    @BindView(R.id.grdiview_jiaocai)
    GridView gridview_jiaocai;//对应教材的gridView
    @BindView(R.id.gridview_banben)
    GridView gridview_banben;//对应版本的gridView
    @BindView(R.id.gridview_nianji)
    GridView gridview_nianji;//对应年级的gridView
    @BindView(R.id.gridview_danyuan)
    GridView gridview_danyuan;//对应单元的gridView
    //收起和重置按钮
    @BindView(R.id.tv_reset)
    TextView tv_reset;//重置
    @BindView(R.id.tv_confirm)
    TextView tv_confirm;//完成

    private static final String SHAIXUAN_URL = HttpUtils.Host + "/listening/cate";//筛选数据接口

    private List<String> titleList = new ArrayList<>();//模块的标题List


    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case SHAIXUAN_URL:
                if (Constanst.success_net_code.equals(code)) {

                    HashMap<String, Object> resultMap = (HashMap<String, Object>) result;
                    titleList.addAll(((List<String>) resultMap.get("titles")));
                    initTitles();

                    List<HashMap<String, Object>> navList = (List<HashMap<String, Object>>) resultMap.get("nav");
                    firstList.addAll(navList);

                    List s_navList = (List) resultMap.get("s_nav");
                    shaixuanList.addAll(s_navList);

                    initFirstViews();
                } else {
                    ToastUtil.show(this, msg, 0);
                }
                break;
        }
    }

    private List<HashMap<String, Object>> firstList = new ArrayList<>();//第一级gridview的List
    private List shaixuanList = new ArrayList();//第二,三,四级gridview的list
    private OnlineHearingShaixuanGridAdapter onlineHearingShaixuanGridAdapter_jiaocai;
    private ArrayList shaixuanSecondList;//取出的二级数据,原因是因为第一级和第二级数据分开了
    private String shaixuanId = "";//记录选中条目的筛选id
    private String shaixuanName = "";//记录筛选条目中的name

    //初始化第一个View
    private void initFirstViews() {
        //第一次显示时都先隐藏最后的三个布局
        ll_banben.setVisibility(View.GONE);
        ll_nianji.setVisibility(View.GONE);
        ll_danyuan.setVisibility(View.GONE);

        if (firstList == null || firstList.size() == 0) {
            ll_jiaocai.setVisibility(View.GONE);
            ToastUtil.show(this, "暂无筛选数据", 0);
            return;
        } else {
            ll_jiaocai.setVisibility(View.VISIBLE);
            if (onlineHearingShaixuanGridAdapter_jiaocai == null) {
                onlineHearingShaixuanGridAdapter_jiaocai = new OnlineHearingShaixuanGridAdapter(this, firstList);
                gridview_jiaocai.setAdapter(onlineHearingShaixuanGridAdapter_jiaocai);
                onlineHearingShaixuanGridAdapter_jiaocai.changeDefault();
                gridview_jiaocai.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        onlineHearingShaixuanGridAdapter_jiaocai.changeState(position);
                        ll_banben.setVisibility(View.GONE);
                        ll_nianji.setVisibility(View.GONE);
                        ll_danyuan.setVisibility(View.GONE);
                        shaixuanId = ((HashMap<String, Object>) firstList.get(position)).get("cate_id") + "";
                        shaixuanName = ((HashMap<String, Object>) firstList.get(position)).get("cate_name") + "";
                        shaixuanSecondList = (ArrayList) shaixuanList.get(position);
                        if (shaixuanSecondList != null && shaixuanSecondList.size() >= 1) {
                            initSecondViews(((HashMap<String, Object>) firstList.get(position)), ((ArrayList<HashMap<String, Object>>) shaixuanSecondList.get(0)), position);
                        }
                    }
                });
            } else {
                //不是第一次初始化adapter的时候还原初始化之前的选择状态
                onlineHearingShaixuanGridAdapter_jiaocai.changeDefault();
            }
        }
    }

    private List<HashMap<String, Object>> secondList = new ArrayList<>();//用来保存第二级的list
    private OnlineHearingShaixuanGridAdapter onlineHearingShaixuanGridAdapter_banben;

    //初始化第二个View
    private void initSecondViews(HashMap<String, Object> firstMap, ArrayList<HashMap<String, Object>> dataList2, final int lastPosition) {
        secondList.clear();//添加数据前清空上一次的筛选数据
        String cate_id = firstMap.get("cate_id") + "";
        for (int i = 0; i < dataList2.size(); i++) {
            String pid = ((HashMap<String, Object>) dataList2.get(i)).get("pid") + "";
            if (cate_id != null && cate_id.equals(pid)) {
                secondList.add(dataList2.get(i));
            }
        }

        if (secondList != null && secondList.size() > 0) {
            ll_banben.setVisibility(View.VISIBLE);
            if (onlineHearingShaixuanGridAdapter_banben == null) {
                onlineHearingShaixuanGridAdapter_banben = new OnlineHearingShaixuanGridAdapter(this, secondList);
                gridview_banben.setAdapter(onlineHearingShaixuanGridAdapter_banben);
                onlineHearingShaixuanGridAdapter_banben.changeDefault();
                gridview_banben.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        onlineHearingShaixuanGridAdapter_banben.changeState(position);
                        ll_nianji.setVisibility(View.GONE);
                        ll_danyuan.setVisibility(View.GONE);
                        shaixuanId = ((HashMap<String, Object>) secondList.get(position)).get("cate_id") + "";
                        shaixuanName = ((HashMap<String, Object>) secondList.get(position)).get("cate_name") + "";
                        if (shaixuanSecondList != null && shaixuanSecondList.size() >= 2) {
                            initThreedViews(((HashMap<String, Object>) secondList.get(position)), ((ArrayList<HashMap<String, Object>>) shaixuanSecondList.get(1)), lastPosition);
                        }
                    }
                });
            } else {
                //在不是第一次初始化adapter的时候还原初始化之前的选择状态
                onlineHearingShaixuanGridAdapter_banben.changeDefault();//还原默认未选中状态
                onlineHearingShaixuanGridAdapter_banben.notifyDataSetChanged();
            }
        } else {
            ll_banben.setVisibility(View.GONE);
        }
    }

    private List<HashMap<String, Object>> threedList = new ArrayList<>();//用来保存第二级的list
    private OnlineHearingShaixuanGridAdapter onlineHearingShaixuanGridAdapter_nianji;

    //初始化第三个View
    private void initThreedViews(HashMap<String, Object> secondMap, ArrayList<HashMap<String, Object>> dataList3, final int lastPosition) {
        threedList.clear();//添加数据前清空上一次的筛选数据
        String cate_id = secondMap.get("cate_id") + "";
        for (int i = 0; i < dataList3.size(); i++) {
            String pid = ((HashMap<String, Object>) dataList3.get(i)).get("pid") + "";
            if (cate_id != null && cate_id.equals(pid)) {
                threedList.add(dataList3.get(i));
            }
        }


        if (threedList != null && threedList.size() > 0) {
            ll_nianji.setVisibility(View.VISIBLE);
            if (onlineHearingShaixuanGridAdapter_nianji == null) {
                onlineHearingShaixuanGridAdapter_nianji = new OnlineHearingShaixuanGridAdapter(this, threedList);
                gridview_nianji.setAdapter(onlineHearingShaixuanGridAdapter_nianji);
                onlineHearingShaixuanGridAdapter_nianji.changeDefault();
                gridview_nianji.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        onlineHearingShaixuanGridAdapter_nianji.changeState(position);
                        ll_danyuan.setVisibility(View.GONE);
                        shaixuanId = ((HashMap<String, Object>) threedList.get(position)).get("cate_id") + "";
                        shaixuanName = ((HashMap<String, Object>) threedList.get(position)).get("cate_name") + "";
                        if (shaixuanSecondList != null && shaixuanSecondList.size() >= 3) {
                            initFourthViews(((HashMap<String, Object>) threedList.get(position)), ((ArrayList<HashMap<String, Object>>) shaixuanSecondList.get(2)), lastPosition);
                        }
                    }
                });
            } else {
                //在不是第一次初始化adapter的时候还原初始化之前的选择状态
                onlineHearingShaixuanGridAdapter_nianji.changeDefault();//还原默认未选中状态
                onlineHearingShaixuanGridAdapter_nianji.notifyDataSetChanged();
            }
        } else {
            ll_nianji.setVisibility(View.GONE);
        }
    }


    private List<HashMap<String, Object>> fourthList = new ArrayList<>();//用来保存第二级的list
    private OnlineHearingShaixuanGridAdapter onlineHearingShaixuanGridAdapter_danyuan;

    //初始化第三个View
    private void initFourthViews(HashMap<String, Object> threedMap, ArrayList<HashMap<String, Object>> dataList4, int lastPosition) {
        fourthList.clear();//添加数据前清空上一次的筛选数据
        String cate_id = threedMap.get("cate_id") + "";
        for (int i = 0; i < dataList4.size(); i++) {
            String pid = ((HashMap<String, Object>) dataList4.get(i)).get("pid") + "";
            if (cate_id != null && cate_id.equals(pid)) {
                fourthList.add(dataList4.get(i));
            }
        }

        if (fourthList != null && fourthList.size() > 0) {
            ll_danyuan.setVisibility(View.VISIBLE);
            if (onlineHearingShaixuanGridAdapter_danyuan == null) {
                onlineHearingShaixuanGridAdapter_danyuan = new OnlineHearingShaixuanGridAdapter(this, fourthList);
                gridview_danyuan.setAdapter(onlineHearingShaixuanGridAdapter_danyuan);
                onlineHearingShaixuanGridAdapter_danyuan.changeDefault();
                gridview_danyuan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        onlineHearingShaixuanGridAdapter_danyuan.changeState(position);
                        shaixuanId = ((HashMap<String, Object>) fourthList.get(position)).get("cate_id") + "";
                        shaixuanName = ((HashMap<String, Object>) fourthList.get(position)).get("cate_name") + "";
                    }
                });
            } else {
                //在不是第一次初始化adapter的时候还原初始化之前的选择状态
                onlineHearingShaixuanGridAdapter_danyuan.changeDefault();//还原默认未选中状态
                onlineHearingShaixuanGridAdapter_danyuan.notifyDataSetChanged();
            }
        } else {
            ll_danyuan.setVisibility(View.GONE);
        }
    }

    //初始化每个版块的标题
    private void initTitles() {
        tv_jiaocai.setText(titleList.get(0));
        tv_banben.setText(titleList.get(1));
        tv_nianji.setText(titleList.get(2));
        tv_danyuan.setText(titleList.get(3));
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_online_shaixuan);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tv_title.setText("筛选列表");
        PostShaixuanList();
    }

    @Override
    public void afterInitView() {
    }

    @OnClick({R.id.ll_back, R.id.tv_reset, R.id.tv_confirm})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_reset:
                //重置按钮
                shaixuanId = "";
                shaixuanName= "";
                if (onlineHearingShaixuanGridAdapter_jiaocai != null) {
                    onlineHearingShaixuanGridAdapter_jiaocai.changeDefault();
                }

                if (onlineHearingShaixuanGridAdapter_banben != null) {
                    onlineHearingShaixuanGridAdapter_banben.changeDefault();
                }

                if (onlineHearingShaixuanGridAdapter_nianji != null) {
                    onlineHearingShaixuanGridAdapter_nianji.changeDefault();
                }

                if (onlineHearingShaixuanGridAdapter_danyuan != null) {
                    onlineHearingShaixuanGridAdapter_danyuan.changeDefault();
                }

                if (ll_danyuan.isShown()) {
                    ll_danyuan.setVisibility(View.GONE);
                }

                if (ll_nianji.isShown()) {
                    ll_nianji.setVisibility(View.GONE);
                }

                if (ll_banben.isShown()) {
                    ll_banben.setVisibility(View.GONE);
                }
                break;
            case R.id.tv_confirm:
                //完成
                Intent intent = new Intent();
                intent.putExtra("shaixuanId", shaixuanId);
                intent.putExtra("shaixuanName", shaixuanName);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    //获取筛选数据接口
    public void PostShaixuanList() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("controller", "listening");
        map.put("action", "cate");
        getJsonUtil().PostJson(this, map);
    }
}
