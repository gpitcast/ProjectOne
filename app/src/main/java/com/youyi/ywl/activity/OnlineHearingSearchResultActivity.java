package com.youyi.ywl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.youyi.ywl.R;
import com.youyi.ywl.adapter.ExcellentCourseAdapter;
import com.youyi.ywl.adapter.OnlineHearingAdapter;
import com.youyi.ywl.inter.RecyclerViewOnItemClickListener;
import com.youyi.ywl.other.Constanst;
import com.youyi.ywl.util.HttpUtils;
import com.youyi.ywl.util.ToastUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/8/8.
 * 在线听力搜索结果界面
 */

public class OnlineHearingSearchResultActivity extends BaseActivity {
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;
    //    @BindView(R.id.tv_all)
//    TextView tv_all;
//    @BindView(R.id.tv_hot)
//    TextView tv_hot;
//    @BindView(R.id.iv_time_sort)
//    ImageView iv_time_sort;
//    @BindView(R.id.tv_time_sort)
//    TextView tv_time_sort;
    @BindView(R.id.tv_search_layout)
    TextView tv_search_layout;
    //    @BindView(R.id.tv_shaixuan_short)
//    TextView tv_shaixuan_short;
    @BindView(R.id.ll_first_layout)
    LinearLayout ll_first_layout;
    @BindView(R.id.recyclerView_first)
    RecyclerView recyclerView_first;
    @BindView(R.id.ll_second_layout)
    LinearLayout ll_second_layout;
    @BindView(R.id.recyclerView_second)
    RecyclerView recyclerView_second;
    @BindView(R.id.ll_shaixuan_second)
    LinearLayout ll_shaixuan_second;

    private static final String SHAIXUAN_URL = HttpUtils.Host + "/listening/cate";//筛选数据接口
    private List<HashMap<String, Object>> firstList = new ArrayList<>();//第一级筛选数据
    private int firstPosition = -1;//记录第一级选择的position
    private List<HashMap<String, Object>> secondList = new ArrayList<>();//第二级筛选数据
    private int secondPosition = -1;//记录第二级选择的position
    private List<HashMap<String, Object>> secondList_replace = new ArrayList<>();//第二级用来替换的数据
    private List<Object> mS_navList = new ArrayList<>();//我的s_nav集合,存储除了一级后面所有级数据的集合
    List<String> mTitleList = new ArrayList<>();//标题数据集合

    private static final String DATA_URL = HttpUtils.Host + "/listening/index";//在线听力列表数据接口
    private List<HashMap<String, Object>> dataList = new ArrayList<>();//数据集合
    private OnlineHearingAdapter onlineHearingAdapter;
    private TextView tv_status;
    private int pageno = 1;//分页的页数
    private int mRequestCode = 1616;//跳转到筛选界面的请求码
    private boolean isLoadMore = false;//是否加载更多
    private String shaixuanId = "";//筛选条件的分类id
    private String shaixuanName = "";//筛选条件的分类name
    private int timeSort = 0;//时间排序(0是升序,1是降序,默认是升序)
    private String isHot = "";//是否是热点资源(1是热点资源,传""则代表为空)
    private String searchContent = "";//搜索内容
    private ExcellentCourseAdapter excellentCourseAdapter_first;
    private ExcellentCourseAdapter excellentCourseAdapter_second;

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case DATA_URL:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        //0代表数据正常
                        ArrayList<HashMap<String, Object>> arrayList = (ArrayList<HashMap<String, Object>>) resultMap.get("data");
                        if (arrayList != null || arrayList.size() > 0) {
                            if (isLoadMore) {
                                xRecyclerView.loadMoreComplete();
                                isLoadMore = false;
                                dataList.addAll(arrayList);
                                tv_status.setText("加载完毕");
                            } else {
                                dataList.clear();
                                dataList.addAll(arrayList);
                            }
                        } else {
                            if (isLoadMore) {
                                isLoadMore = false;
                                pageno--;
                                xRecyclerView.loadMoreComplete();
                                tv_status.setText(resultMap.get("msg") + "");
                                xRecyclerView.setNoMore(true);
                            } else {
                                dataList.clear();
                                ToastUtil.show(this, msg, 0);
                            }
                        }
                        onlineHearingAdapter.notifyDataSetChanged();
                    } else {
                        //1或其他代表数据不正常
                        if (isLoadMore) {
                            isLoadMore = false;
                            pageno--;
                            xRecyclerView.loadMoreComplete();
                            tv_status.setText(resultMap.get("msg") + "");
                            xRecyclerView.setNoMore(true);
                        } else {
                            dataList.clear();
                            ToastUtil.show(this, resultMap.get("msg") + "", 0);
                            onlineHearingAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    ToastUtil.show(this, msg, 0);
                }
                break;

            case SHAIXUAN_URL:
                if (Constanst.success_net_code.equals(code)) {

//                    PostList();//在请求筛选数据成功后 再请求列表数据接口

                    HashMap<String, Object> resultMap = (HashMap<String, Object>) result;
                    //第一级数据
                    ArrayList<HashMap<String, Object>> navList = (ArrayList<HashMap<String, Object>>) resultMap.get("nav");
                    initFirstRecyclerViewData(navList); //初始化第一级数据
                    //后面所有级数据
                    List s_navList = (List) resultMap.get("s_nav");
                    mS_navList.addAll(s_navList);
                    initSecondRecyclerViewData();//初始化第二级数据
                    //筛选界面四级标题数据
                    mTitleList.addAll(((ArrayList<String>) resultMap.get("titles")));

                } else {
                    ToastUtil.show(this, msg, 0);
                }

        }
    }

    //初始化第二级数据
    private void initSecondRecyclerViewData() {
        if (firstList == null || firstList.size() == 0) {
            ll_first_layout.setVisibility(View.GONE);
            return;
        }
        HashMap<String, Object> map = firstList.get(0);
        firstPosition = 0;
        int visibility = ll_second_layout.getVisibility();//获取第二级布局显示状态
        ArrayList<HashMap<String, Object>> arrayList = (ArrayList<HashMap<String, Object>>) ((ArrayList) mS_navList.get(0)).get(0);
        if (arrayList == null || arrayList.size() == 0) {
            if (visibility == View.VISIBLE) {
                ll_second_layout.setVisibility(View.GONE);
            }

            shaixuanId = map.get("cate_id") + "";
            pageno = 1;
            PostList();

            return;
        }

        String cate_id = map.get("cate_id") + "";
        if (cate_id == null) {
            return;
        }

        secondList_replace.clear();
        for (int i = 0; i < arrayList.size(); i++) {
            HashMap<String, Object> hashMap = arrayList.get(i);
            if (cate_id.equals(hashMap.get("pid") + "")) {
                secondList_replace.add(hashMap);
            }
        }

        secondList.clear();
        for (int i = 0; i < secondList_replace.size(); i++) {
            HashMap<String, Object> hashMap = secondList_replace.get(i);
            if (0 == i) {
                hashMap.put("isChecked", true);
            } else {
                hashMap.put("isChecked", false);
            }
            secondList.add(hashMap);
        }

        excellentCourseAdapter_second.notifyDataSetChanged();

        if (secondList != null && secondList.size() > 0) {
            secondPosition = 0;
            HashMap<String, Object> hashMap = secondList.get(0);
            shaixuanId = hashMap.get("cate_id") + "";
            pageno = 1;
            PostList();
        }

    }

    //初始化第一级数据
    private void initFirstRecyclerViewData(ArrayList<HashMap<String, Object>> navList) {
        if (navList == null) {
            //第一级删选数据为空,隐藏第一级布局
            ll_first_layout.setVisibility(View.GONE);
            return;
        }

        for (int i = 0; i < navList.size(); i++) {
            HashMap<String, Object> map = navList.get(i);
            if (i == 0) {
                map.put("isChecked", true);
                firstList.add(map);
            } else {
                map.put("isChecked", false);
                firstList.add(map);
            }
        }

        excellentCourseAdapter_first.notifyDataSetChanged();
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        if (intent != null) {
            searchContent = intent.getStringExtra("searchContent");
            if (searchContent != null && searchContent.length() > 0) {
                tv_search_layout.setText(searchContent);
            }
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_online_hearing_search_result);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {

        initShaixuanFirstRecyclerView();//初始化筛选第一级的recyclerView
        initShaixuanSecondRecyclerView();//初始化筛选第二级的recyclerView
        PostShaixuanList();//请求筛选数据接口

        initListRecyclerView();//初始化列表recyclerView

//        PostList();
//        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
//
//                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE
//                        || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
////                    if (et_search.getText().toString().trim() != null && et_search.getText().toString().trim().length() > 0) {
//                    SoftUtil.hideSoft(OnlineHearingSearchResultActivity.this);//隐藏键盘
//                    ToastUtil.show(OnlineHearingSearchResultActivity.this, "开始搜索", 0);
//                    searchContent = et_search.getText().toString().trim();
//                    pageno = 1;
//                    PostList();
////                    } else {
////                        ToastUtil.show(OnlineHearingSearchResultActivity.this, "搜索关键字不能为空", 0);
////                    }
//                    return true;
//                }
//                return false;
//            }
//        });
    }

    //初始化筛选第二级的recyclerView
    private void initShaixuanSecondRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView_second.setLayoutManager(manager);
        excellentCourseAdapter_second = new ExcellentCourseAdapter(this, secondList);
        recyclerView_second.setAdapter(excellentCourseAdapter_second);

        excellentCourseAdapter_second.setOnItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                HashMap<String, Object> map = secondList.get(position);
                Boolean isChecked = (Boolean) map.get("isChecked");
                if (!isChecked) {
                    //只有在未选中的状态下在进行选中操作
                    secondPosition = position;

                    for (int i = 0; i < secondList.size(); i++) {
                        HashMap<String, Object> hashMap = secondList.get(i);
                        if (position == i) {
                            hashMap.put("isChecked", true);
                        } else {
                            hashMap.put("isChecked", false);
                        }
                    }
                    excellentCourseAdapter_second.notifyDataSetChanged();

                    shaixuanId = map.get("cate_id") + "";
                    pageno = 1;
                    PostList();

                }
            }
        });
    }

    //初始化筛选第一级的recyclerView
    private void initShaixuanFirstRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView_first.setLayoutManager(manager);
        excellentCourseAdapter_first = new ExcellentCourseAdapter(this, firstList);
        recyclerView_first.setAdapter(excellentCourseAdapter_first);

        excellentCourseAdapter_first.setOnItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                //查看点击的条目是否为选中状态,未选中状态进行操作,选中状态不做任何操作
                HashMap<String, Object> map = firstList.get(position);
                Boolean isChecked = (Boolean) map.get("isChecked");
                if (!isChecked) {
                    //点击之前都是未选中状态的,执行选中操作
                    firstPosition = position;

                    for (int i = 0; i < firstList.size(); i++) {
                        HashMap<String, Object> hashMap = firstList.get(i);
                        if (position == i) {
                            hashMap.put("isChecked", true);
                        } else {
                            hashMap.put("isChecked", false);
                        }
                    }
                    excellentCourseAdapter_first.notifyDataSetChanged();

                    int visibility = ll_second_layout.getVisibility();//获取第二级布局显示状态
                    ArrayList<HashMap<String, Object>> arrayList = (ArrayList<HashMap<String, Object>>) ((ArrayList) mS_navList.get(position)).get(0);
                    if (arrayList == null || arrayList.size() == 0) {
                        if (visibility == View.VISIBLE) {
                            ll_second_layout.setVisibility(View.GONE);
                        }

                        shaixuanId = map.get("cate_id") + "";
                        pageno = 1;
                        PostList();

                        return;
                    }

                    String cate_id = map.get("cate_id") + "";
                    if (cate_id == null) {
                        return;
                    }

                    secondList_replace.clear();
                    for (int i = 0; i < arrayList.size(); i++) {
                        HashMap<String, Object> hashMap = arrayList.get(i);
                        if (cate_id.equals(hashMap.get("pid") + "")) {
                            secondList_replace.add(hashMap);
                        }
                    }

                    secondList.clear();
                    for (int i = 0; i < secondList_replace.size(); i++) {
                        HashMap<String, Object> hashMap = secondList_replace.get(i);
                        if (0 == i) {
                            hashMap.put("isChecked", true);
                        } else {
                            hashMap.put("isChecked", false);
                        }
                        secondList.add(hashMap);
                    }

                    excellentCourseAdapter_second.notifyDataSetChanged();

                    if (secondList != null && secondList.size() > 0) {
                        secondPosition = 0;
                        HashMap<String, Object> hashMap = secondList.get(0);
                        shaixuanId = hashMap.get("cate_id") + "";
                        pageno = 1;
                        PostList();
                    }

                }
            }
        });
    }

    private void initListRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        xRecyclerView.setLayoutManager(manager);
        onlineHearingAdapter = new OnlineHearingAdapter(this, dataList);
        xRecyclerView.setAdapter(onlineHearingAdapter);
        View headView1 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        View headView2 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        xRecyclerView.addHeaderView(headView1);
        xRecyclerView.addHeaderView(headView2);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        //刷新和加载监听
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                isLoadMore = true;
                pageno++;
                PostList();
                tv_status.setText("正在加载...");
            }
        });

        View footerView = LayoutInflater.from(this).inflate(R.layout.layout_recyclerview_footer, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        footerView.setLayoutParams(layoutParams);
        tv_status = ((TextView) footerView.findViewById(R.id.tv_status));
        xRecyclerView.setFootView(footerView);
        xRecyclerView.loadMoreComplete();

        onlineHearingAdapter.setOnItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                HashMap<String, Object> map = dataList.get(position);
                Intent intent = new Intent(OnlineHearingSearchResultActivity.this, OnlineHearingDetailActivity.class);
                intent.putExtra("url", map.get("url") + "");
                startActivity(intent);
            }
        });
    }

    @Override
    public void afterInitView() {
    }

    public void PostList() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("controller", "listening");
        map.put("action", "index");
        map.put("page", pageno + "");//页数
        map.put("time", "");//时间的升降序排序(0是升序,1是降序)
        map.put("hot", "");//是否是热点资源
        map.put("keywords", searchContent + "");//搜索关键词(这个界面没有搜索功能,传""就可以)
        map.put("cate_id", shaixuanId + "");//筛选界面的筛选条件对应的筛选ID
        getJsonUtil().PostJson(this, map);
    }

    //请求筛选数据
    private void PostShaixuanList() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("controller", "listening");
        map.put("action", "cate");
        getJsonUtil().PostJson(this, map);
    }


    @OnClick({R.id.iv_back, R.id.tv_search, R.id.tv_search_layout, R.id.ll_shaixuan_second})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
//            case R.id.iv_shaixuan:
//                Intent intent = new Intent(this, OnlineHearingShaixuanActivity.class);
//                startActivityForResult(intent, mRequestCode);
//                break;
//            case R.id.tv_all:
//                //全部
//                tv_all.setTextColor(getResources().getColor(R.color.orangeone));
//                tv_hot.setTextColor(getResources().getColor(R.color.normal_black));
//
//                isHot = "";
//                pageno = 1;
//                PostList();
//                break;
//            case R.id.tv_hot:
//                tv_all.setTextColor(getResources().getColor(R.color.normal_black));
//                tv_hot.setTextColor(getResources().getColor(R.color.orangeone));
//
//                //热点资源
//                isHot = "1";
//                pageno = 1;
//                PostList();
//                break;
//            case R.id.ll_time_sort:
//                //时间排序(0是升序,1是降序,默认是升序0)
//                tv_time_sort.setTextColor(getResources().getColor(R.color.orangeone));
//                if (timeSort == 0) {
//                    iv_time_sort.setImageResource(R.mipmap.icon_arrow_down_half_blue);
//                    timeSort = 1;
//                    pageno = 1;
//                    PostList();
//                } else if (timeSort == 1) {
//                    iv_time_sort.setImageResource(R.mipmap.icon_arrow_up_half_blue);
//                    timeSort = 0;
//                    pageno = 1;
//                    PostList();
//                }
//                break;
            case R.id.tv_search:
                //搜索(点击默认从第一页重新搜索该内容)
                pageno = 1;
                PostList();
                break;
            case R.id.tv_search_layout:
                //搜索框布局,点击跳转到搜索界面
                Intent intent1 = new Intent(this, OnlineHearingSearchActivity.class);
                startActivity(intent1);
                finish();
                break;

            case R.id.ll_shaixuan_second:
                //第二个布局的筛选按钮的点击事件(也是唯一一个筛选按钮)
                Intent intent = new Intent(this, OnlineHearingShaixuanActivity.class);
                for (int i = 0; i < secondList.size(); i++) {
                    HashMap<String, Object> hashMap = secondList.get(i);
                    boolean isChecked = (boolean) hashMap.get("isChecked");
                    if (isChecked) {
                        intent.putExtra("shaixuanId", hashMap.get("cate_id") + "");
                    }
                }
                intent.putExtra("firstPosition", firstPosition);
                intent.putExtra("secondPosition", secondPosition);
                Bundle bundle = new Bundle();
                bundle.putSerializable("mS_navList", ((Serializable) mS_navList));
                bundle.putSerializable("mTitleList", ((Serializable) mTitleList));
                intent.putExtras(bundle);
                startActivityForResult(intent, mRequestCode);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == mRequestCode) {
            shaixuanId = data.getStringExtra("shaixuanId");
            shaixuanName = data.getStringExtra("shaixuanName");
//            if (shaixuanName != null && shaixuanName.length() > 0) {
//                if (shaixuanName.length() <= 2) {
//                    tv_shaixuan_short.setText("筛选(" + shaixuanName + ")");
//                } else {
//                    String substring = shaixuanName.substring(0, 2);
//                    tv_shaixuan_short.setText("筛选(" + substring + "...)");
//                }
//            } else {
//                tv_shaixuan_short.setText("筛选()");
//            }
            Log.i("ShaixuanActivity", "    shaixuanId:    " + shaixuanId);
            Log.i("ShaixuanActivity", "    shaixuanName:    " + shaixuanName);
            pageno = 1;
            PostList();
        }
    }
}
