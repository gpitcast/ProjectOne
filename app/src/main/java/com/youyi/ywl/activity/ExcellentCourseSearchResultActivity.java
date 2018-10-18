package com.youyi.ywl.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.youyi.ywl.R;
import com.youyi.ywl.adapter.PopGridViewAdapter;
import com.youyi.ywl.adapter.SubjectAdapter_Weike;
import com.youyi.ywl.inter.RecyclerViewOnItemClickListener;
import com.youyi.ywl.other.Constanst;
import com.youyi.ywl.util.HttpUtils;
import com.youyi.ywl.util.ToastUtil;
import com.youyi.ywl.view.MyGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/13.
 * 精品课程 - 搜索界面
 */

public class ExcellentCourseSearchResultActivity extends BaseActivity {
    @BindView(R.id.tv_search_layout)
    TextView tv_search_layout;
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;
    @BindView(R.id.tv_click_rate)
    TextView tv_click_rate;
    @BindView(R.id.tv_time_sort)
    TextView tv_time_sort;
    @BindView(R.id.iv_time_sort)
    ImageView iv_time_sort;

    private static final String RESULT_URL = HttpUtils.Host + "/video/index";//搜索结果列表接口
    private List<HashMap<String, Object>> dataList = new ArrayList<>();//列表数据
    private static final String SHAIXUAN_URL = HttpUtils.Host + "/video/cate";//筛选接口
    private List<String> mTitlesList = new ArrayList<>();//分类标题数据
    private List<Object> otherList = new ArrayList<>();//其他级的数据

    private String searchContent = "";//搜索内容
    private int pageno = 1;//分页页数
    private String time = "";//时间排序(0代表降序 1代表升序, 默认传空字符串)
    private String hits = "";//点击量(1代表有这个条件)
    private String price = "";//价格排序(0代表降序 1代表升序 ,默认传空字符串)
    private String shaixuanId = "";//筛选对应的筛选ID

    private TextView tv_status;
    private SubjectAdapter_Weike subjectAdapter;
    private boolean isLoadMore;
    private LinearLayout ll_first;
    private LinearLayout ll_second;
    private LinearLayout ll_threed;
    private LinearLayout ll_fourth;
    private MyGridView gridView_first;
    private MyGridView gridView_second;
    private MyGridView gridView_threed;
    private MyGridView gridView_fourth;


    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case RESULT_URL:
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
                        subjectAdapter.notifyDataSetChanged();
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
                            subjectAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    if (isLoadMore) {
                        isLoadMore = false;
                        pageno--;
                        xRecyclerView.loadMoreComplete();
                        tv_status.setText(msg);
                    } else {
                        ToastUtil.show(this, msg, 0);
                        subjectAdapter.notifyDataSetChanged();
                    }
                }

                break;
            case SHAIXUAN_URL:
                PostList();//请求列表数据
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    //标题数据
                    ArrayList<String> titlesList = (ArrayList<String>) resultMap.get("titles");
                    mTitlesList.addAll(titlesList);
                    //第一级数据
                    ArrayList<HashMap<String, Object>> navList = (ArrayList<HashMap<String, Object>>) resultMap.get("nav");
                    firstList.addAll(navList);
                    //其他级数据
                    ArrayList<Object> s_navList = (ArrayList<Object>) resultMap.get("s_nav");
                    otherList.addAll(s_navList);

                } else {
                    ToastUtil.show(this, msg, 0);
                }
                break;
        }
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
        setContentView(R.layout.activity_excellent_weike_search_result);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        PostShaixuanList();
        initRecyclerView();
    }

    //请求搜索结果数据
    private void PostList() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "weike");
        map.put("action", "index");
        map.put("page", pageno);//分页页数
        map.put("keywords", searchContent);//搜索关键词
        map.put("time", time);//时间排序(0代表降序 1代表升序, 默认是升序)
        map.put("hits", hits);//点击率
        map.put("price", price);//价格排序(0代表降序 1代表升序 )
        map.put("cate_id", shaixuanId);//分类id(就是筛选的cate_id)
        getJsonUtil().PostJson(this, map);
    }

    //请求筛选数据
    private void PostShaixuanList() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("controller", "weike");
        map.put("action", "cate");
        getJsonUtil().PostJson(this, map);
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        xRecyclerView.setLayoutManager(manager);
        subjectAdapter = new SubjectAdapter_Weike(this, dataList);
        xRecyclerView.setAdapter(subjectAdapter);

        View headView1 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        View headView2 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        xRecyclerView.addHeaderView(headView1);
        xRecyclerView.addHeaderView(headView2);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                pageno++;
                isLoadMore = true;
                tv_status.setText("正在加载...");
                PostList();
            }
        });

        subjectAdapter.setOnItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Intent intent = new Intent(ExcellentCourseSearchResultActivity.this, ExcellentCourseDetailActivity.class);
                HashMap<String, Object> map = dataList.get(position);
                intent.putExtra("id", map.get("id") + "");
                startActivity(intent);
            }
        });

        //自定义加载footer
        View footerView = LayoutInflater.from(this).inflate(R.layout.layout_recyclerview_footer, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        footerView.setLayoutParams(layoutParams);
        xRecyclerView.setFootView(footerView);
        tv_status = (TextView) footerView.findViewById(R.id.tv_status);
        tv_status.setTextColor(getResources().getColor(R.color.grayone));
    }

    @Override
    public void afterInitView() {
    }

    @OnClick({R.id.iv_back, R.id.tv_search_layout, R.id.tv_click_rate, R.id.ll_time_sort})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_search_layout:
                //搜索框布局,点击跳转到搜索界面
                Intent intent1 = new Intent(this, ExcellentWeikeSearchActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.tv_click_rate:
                //点击量
                if (!"1".equals(hits)) {
                    //当hits字段为1的时候,代表是在点击率的时候点击了,不做任何改变,只有当hits不为1的时候,代表没有选中点击率,点击了才去做改变
                    tv_click_rate.setTextColor(getResources().getColor(R.color.orangeone));
                    hits = "1";
                    pageno = 1;
                    PostList();
                }
                break;
            case R.id.ll_time_sort:
                //时间排序(0代表降序 1代表升序, 默认是升序)
                tv_time_sort.setTextColor(getResources().getColor(R.color.orangeone));
                if ("0".equals(time)) {
                    //原始为降序,点击改变为升序
                    iv_time_sort.setImageResource(R.mipmap.icon_arrow_up_half_blue);
                    time = "1";
                    pageno = 1;
                    PostList();
                } else if ("1".equals(time)) {
                    //原始为升序,改变为降序
                    iv_time_sort.setImageResource(R.mipmap.icon_arrow_down_half_blue);
                    time = "0";
                    pageno = 1;
                    PostList();
                }

                //点击时间排序同时改变价格排序
                break;
//            case R.id.ll_price_sort:
//                //价格排序(0代表降序 1代表升序 )
//
//                break;
//            case R.id.ll_course_classification:
//                //科目分类
//                showShaixuanPop();
//                break;
        }
    }

    private PopupWindow popupWindow;
    private View popWindowView;

    //显示筛选条件的pop
    private void showShaixuanPop() {
        if (popupWindow == null) {
            popupWindow = new PopupWindow(this);
            popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        }

        if (popWindowView == null) {
            popWindowView = LayoutInflater.from(this).inflate(R.layout.layout_shaixuan_pop_weike_search, null);
        }
        initPopWindowView(popWindowView, popupWindow);
        popupWindow.setContentView(popWindowView);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(findViewById(R.id.ll_search_title), 0, 0, Gravity.RIGHT);
    }

    private List<HashMap<String, Object>> firstList = new ArrayList<>();//第一级数据
    private PopGridViewAdapter popGridViewAdapter_first;
    private int isWhatLevelChecked = -1;//几率是哪一级被选中的(3代表第三级被选中,4代表第四季被选中)
    private int selectPosition_all = -1;//记录每一个条目选中的position,用来取出对应数据集合的cate_id
    private int firstSelectPosition = -1;

    //初始化筛选pop
    private void initPopWindowView(View popWindowView, final PopupWindow popupWindow) {
        //外面阴影部分点击隐藏popwindow
        popWindowView.findViewById(R.id.ll_outside).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            }
        });

        ll_first = (LinearLayout) popWindowView.findViewById(R.id.ll_first);
        ll_second = (LinearLayout) popWindowView.findViewById(R.id.ll_second);
        ll_threed = (LinearLayout) popWindowView.findViewById(R.id.ll_threed);
        ll_fourth = (LinearLayout) popWindowView.findViewById(R.id.ll_fourth);

        ll_first.setVisibility(View.GONE);
        ll_second.setVisibility(View.GONE);
        ll_threed.setVisibility(View.GONE);
        ll_fourth.setVisibility(View.GONE);

        TextView tv_title_first = (TextView) popWindowView.findViewById(R.id.tv_title_first);
        TextView tv_title_second = (TextView) popWindowView.findViewById(R.id.tv_title_second);
        TextView tv_title_threed = (TextView) popWindowView.findViewById(R.id.tv_title_threed);
        TextView tv_title_fourth = (TextView) popWindowView.findViewById(R.id.tv_title_fourth);

        gridView_first = (MyGridView) popWindowView.findViewById(R.id.gridView_first);
        gridView_second = (MyGridView) popWindowView.findViewById(R.id.gridView_second);
        gridView_threed = (MyGridView) popWindowView.findViewById(R.id.gridView_threed);
        gridView_fourth = (MyGridView) popWindowView.findViewById(R.id.gridView_fourth);

        //重置
        popWindowView.findViewById(R.id.tv_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_second.getVisibility() == View.VISIBLE) {
                    ll_second.setVisibility(View.GONE);
                }
                if (ll_threed.getVisibility() == View.VISIBLE) {
                    ll_threed.setVisibility(View.GONE);
                }
                if (ll_fourth.getVisibility() == View.VISIBLE) {
                    ll_fourth.setVisibility(View.GONE);
                }
                if (popGridViewAdapter_first != null) {
                    popGridViewAdapter_first.changeDefault();
                    selectPosition_all = -1;
                    isWhatLevelChecked = -1;
                }
            }
        });

        //完成
        popWindowView.findViewById(R.id.tv_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isWhatLevelChecked == 1) {
                    //第一层被选中
                    shaixuanId = ((HashMap<String, Object>) firstList.get(selectPosition_all)).get("cate_id") + "";
                    pageno = 1;
                    PostList();
                } else if (isWhatLevelChecked == 2) {
                    //第二层被选中
                    shaixuanId = ((HashMap<String, Object>) secondList.get(selectPosition_all)).get("cate_id") + "";
                    pageno = 1;
                    PostList();
                } else if (isWhatLevelChecked == 3) {
                    //第三层被选中
                    shaixuanId = ((HashMap<String, Object>) threedList.get(selectPosition_all)).get("cate_id") + "";
                    pageno = 1;
                    PostList();
                } else if (isWhatLevelChecked == 4) {
                    //第四层被选中
                    shaixuanId = ((HashMap<String, Object>) fourthList.get(selectPosition_all)).get("cate_id") + "";
                    pageno = 1;
                    PostList();
                } else {
                    //没有被选中
                    shaixuanId = "";
                    pageno = 1;
                    PostList();
                }

                isWhatLevelChecked = -1;
                selectPosition_all = -1;

                if (popupWindow != null) {
                    popupWindow.dismiss();
                }

            }
        });

        //标题
        if (mTitlesList != null && mTitlesList.size() == 4) {
            tv_title_first.setText(mTitlesList.get(0));
            tv_title_second.setText(mTitlesList.get(1));
            tv_title_threed.setText(mTitlesList.get(2));
            tv_title_fourth.setText(mTitlesList.get(3));
        }

        if (firstList != null && firstList.size() > 0) {
            ll_first.setVisibility(View.VISIBLE);
            if (popGridViewAdapter_first == null) {
                popGridViewAdapter_first = new PopGridViewAdapter(this, firstList);
                gridView_first.setAdapter(popGridViewAdapter_first);
                popGridViewAdapter_first.changeDefault();//第一次初始化默认未选中状态
                gridView_first.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        popGridViewAdapter_first.changeState(position);
                        isWhatLevelChecked = 1;//第一级被选中
                        firstSelectPosition = position;//记录第一级被选中的position
                        selectPosition_all = position;//记录选中的position
                        ll_threed.setVisibility(View.GONE);
                        ll_fourth.setVisibility(View.GONE);
                        initSecondGridView();
                    }
                });
            } else {
                //在不是第一次初始化adapter的时候还原初始化之前的选择状态
                selectPosition_all = -1;//初始化记录被选中的position
                popGridViewAdapter_first.changeDefault();//还原默认未选中状态
                popGridViewAdapter_first.notifyDataSetChanged();
            }
        } else {
            ToastUtil.show(this, "暂无筛选数据", 0);
        }
    }

    private PopGridViewAdapter popGridViewAdapter_second;
    private List<HashMap<String, Object>> secondList = new ArrayList<>();//第二级数据集合

    //初始化第二级数据
    private void initSecondGridView() {
        //第一级选中的cate_id
        String cate_id = ((HashMap<String, Object>) firstList.get(selectPosition_all)).get("cate_id") + "";
        //第二级数据
        ArrayList<HashMap<String, Object>> arrayList = (ArrayList<HashMap<String, Object>>) ((ArrayList) otherList.get(firstSelectPosition)).get(0);
        if (arrayList == null || arrayList.size() == 0) {
            ll_second.setVisibility(View.GONE);
            return;
        }

        //根据cate_id匹配pid整理出对应的数据
        secondList.clear();
        for (int i = 0; i < arrayList.size(); i++) {
            HashMap<String, Object> hashMap = arrayList.get(i);
            String pid = hashMap.get("pid") + "";
            if (cate_id != null && cate_id.equals(pid)) {
                secondList.add(hashMap);
            }
        }

        if (secondList != null && secondList.size() > 0) {
            ll_second.setVisibility(View.VISIBLE);
            if (popGridViewAdapter_second == null) {
                popGridViewAdapter_second = new PopGridViewAdapter(this, secondList);
                gridView_second.setAdapter(popGridViewAdapter_second);
                popGridViewAdapter_second.changeDefault();//第一次初始化默认未选中状态
                gridView_second.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        popGridViewAdapter_second.changeState(position);
                        isWhatLevelChecked = 2;//第三级被选中
                        selectPosition_all = position;//记录选中的position
                        ll_fourth.setVisibility(View.GONE);
                        initThreedGridView();
                    }
                });
            } else {
                //在不是第一次初始化adapter的时候还原初始化之前的选择状态
                popGridViewAdapter_second.changeDefault();//还原默认未选中状态
                popGridViewAdapter_second.notifyDataSetChanged();
            }
        } else {
            ll_second.setVisibility(View.GONE);
        }
    }

    private PopGridViewAdapter popGridViewAdapter_threed;
    private List<HashMap<String, Object>> threedList = new ArrayList<>();//第三级数据集合

    //初始化第三级数据
    private void initThreedGridView() {
        //第二级选中的cate_id
        String cate_id = ((HashMap<String, Object>) secondList.get(selectPosition_all)).get("cate_id") + "";
        //第三级数据
        ArrayList<HashMap<String, Object>> arrayList = (ArrayList<HashMap<String, Object>>) ((ArrayList) otherList.get(firstSelectPosition)).get(1);
        if (arrayList == null || arrayList.size() == 0) {
            ll_threed.setVisibility(View.GONE);
            return;
        }

        //根据cate_id匹配pid整理出对应的数据
        threedList.clear();
        for (int i = 0; i < arrayList.size(); i++) {
            HashMap<String, Object> hashMap = arrayList.get(i);
            String pid = hashMap.get("pid") + "";
            if (cate_id != null && cate_id.equals(pid)) {
                threedList.add(hashMap);
            }
        }

        if (threedList != null && threedList.size() > 0) {
            ll_threed.setVisibility(View.VISIBLE);
            if (popGridViewAdapter_threed == null) {
                popGridViewAdapter_threed = new PopGridViewAdapter(this, threedList);
                gridView_threed.setAdapter(popGridViewAdapter_threed);
                popGridViewAdapter_threed.changeDefault();//第一次初始化默认未选中状态
                gridView_threed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        popGridViewAdapter_threed.changeState(position);
                        isWhatLevelChecked = 3;//第三级被选中
                        selectPosition_all = position;//记录选中的position
                        initFourthGridView();
                    }
                });
            } else {
                //在不是第一次初始化adapter的时候还原初始化之前的选择状态
                popGridViewAdapter_threed.changeDefault();//还原默认未选中状态
                popGridViewAdapter_threed.notifyDataSetChanged();
            }
        } else {
            ll_threed.setVisibility(View.GONE);
        }
    }

    private PopGridViewAdapter popGridViewAdapter_fourth;
    private List<HashMap<String, Object>> fourthList = new ArrayList<>();//第四级数据集合

    //初始化第四级数据
    private void initFourthGridView() {
        //第三级选中的cate_id
        String cate_id = ((HashMap<String, Object>) threedList.get(selectPosition_all)).get("cate_id") + "";
        //第四级数据
        ArrayList<HashMap<String, Object>> arrayList = (ArrayList<HashMap<String, Object>>) ((ArrayList) otherList.get(firstSelectPosition)).get(2);
        if (arrayList == null || arrayList.size() == 0) {
            ll_fourth.setVisibility(View.GONE);
            return;
        }

        //根据cate_id匹配pid整理出对应的数据
        fourthList.clear();
        for (int i = 0; i < arrayList.size(); i++) {
            HashMap<String, Object> hashMap = arrayList.get(i);
            String pid = hashMap.get("pid") + "";
            if (cate_id != null && cate_id.equals(pid)) {
                fourthList.add(hashMap);
            }
        }

        if (fourthList != null && fourthList.size() > 0) {
            ll_fourth.setVisibility(View.VISIBLE);
            if (popGridViewAdapter_fourth == null) {
                popGridViewAdapter_fourth = new PopGridViewAdapter(this, fourthList);
                gridView_fourth.setAdapter(popGridViewAdapter_fourth);
                popGridViewAdapter_fourth.changeDefault();//第一次初始化默认未选中状态
                gridView_fourth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        popGridViewAdapter_fourth.changeState(position);
                        isWhatLevelChecked = 4;//第三级被选中
                        selectPosition_all = position;//记录选中的position
                    }
                });
            } else {
                //在不是第一次初始化adapter的时候还原初始化之前的选择状态
                popGridViewAdapter_fourth.changeDefault();//还原默认未选中状态
                popGridViewAdapter_fourth.notifyDataSetChanged();
            }
        } else {
            ll_fourth.setVisibility(View.GONE);
        }
    }
}
