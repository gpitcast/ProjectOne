package com.youyi.ywl.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.youyi.ywl.R;
import com.youyi.ywl.adapter.ExcellentCourseAdapter;
import com.youyi.ywl.adapter.ExcellentCourseSubjectAdapter;
import com.youyi.ywl.adapter.PopGridViewAdapter;
import com.youyi.ywl.adapter.SubjectAdapter_Weike;
import com.youyi.ywl.inter.RecyclerViewOnItemClickListener;
import com.youyi.ywl.other.Constanst;
import com.youyi.ywl.util.HttpUtils;
import com.youyi.ywl.util.LearningPathDao3;
import com.youyi.ywl.util.ToastUtil;
import com.youyi.ywl.view.MyGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/8/10.
 * 精品微课界面
 */

public class ExcellentWeikeActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_right)
    ImageView iv_right;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.gridView_subject)
    GridView gridView_subject;
    @BindView(R.id.ll_second_layout)
    LinearLayout ll_second_layout;
    @BindView(R.id.ll_shaixuan_first)
    LinearLayout ll_shaixuan_first;
    @BindView(R.id.ll_shaixuan_second)
    LinearLayout ll_shaixuan_second;
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;

    private static final String SHAIXUAN_URL = HttpUtils.Host + "/weike/cate";//筛选接口
    private List<HashMap<String, Object>> recyclerList = new ArrayList<>();//第一级筛选数据
    private List<HashMap<String, Object>> subjectList = new ArrayList<>();//第二级筛选数据
    private ExcellentCourseAdapter excellentCourseAdapter;
    private ExcellentCourseSubjectAdapter excellentCourseSubjectAdapter;
    private static final String DATA_URL = HttpUtils.Host + "/weike/index";
    private List<HashMap<String, Object>> dataList = new ArrayList<>();//列表数据
    private SubjectAdapter_Weike subjectAdapter_weike;
    private TextView tv_status;
    private boolean isLoadMore = false;
    private List<Object> mS_navList = new ArrayList<>();//我的s_nav集合,存储二三四级数据的集合
    List<HashMap<String, Object>> secondList = new ArrayList<>();
    List<String> mTitleList = new ArrayList<>();//标题数据集合
    private PopGridViewAdapter popGridViewAdapter_threed1;
    private PopGridViewAdapter popGridViewAdapter_threed;
    private LinearLayout ll_fourth2;
    private MyGridView gridView_fourth2;


    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case SHAIXUAN_URL:

                //在筛选数据请求成功后请求列表数据
                PostList();//请求列表数据

                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    //第一级的数据(例如小学,初中)
                    ArrayList<HashMap<String, Object>> navList = (ArrayList<HashMap<String, Object>>) resultMap.get("nav");
                    initRecyclerViewData(navList);
                    //第二,三,四级的数据
                    ArrayList s_navList = (ArrayList) resultMap.get("s_nav");
                    mS_navList.addAll(s_navList);
                    //四级的标题数据
                    mTitleList.addAll(((ArrayList<String>) resultMap.get("titles")));

                } else {
                    ToastUtil.show(this, msg, 0);
                }
                break;
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
                        subjectAdapter_weike.notifyDataSetChanged();
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
                            subjectAdapter_weike.notifyDataSetChanged();
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
                        subjectAdapter_weike.notifyDataSetChanged();
                    }
                }
                break;
        }
    }

    //初始化第一级数据
    private void initRecyclerViewData(ArrayList<HashMap<String, Object>> navList) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cate_id", "");
        map.put("cate_name", "全部");
        map.put("isChecked", true);
        recyclerList.add(0, map);
        for (int i = 0; i < navList.size(); i++) {
            HashMap<String, Object> navMap = navList.get(i);
            navMap.put("isChecked", false);
            recyclerList.add(navMap);
        }
        excellentCourseAdapter.notifyDataSetChanged();
    }


    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_excellent_weike);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tv_title.setText("精品微课");
        iv_right.setVisibility(View.VISIBLE);
        iv_right.setImageResource(R.mipmap.icon_black_search);

        PostShaixuanList();//请求筛选数据
        initRecyclerView();//初始化第一级筛选
        initSubjectRecyclerView();//初始化第二级筛选
        initXRecyclerView();//初始化列表
    }

    private int pageno = 1;//分页页数
    private String shaixuanId = "";//筛选对应的筛选ID

    private String DB_NAME = Constanst.userPhoneNum + "_excellent_weike_learning_info.db";
    private String TABLE_NAME = "excellentWeikeLearning";

    //初始化列表数据
    private void initXRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        xRecyclerView.setLayoutManager(manager);
        subjectAdapter_weike = new SubjectAdapter_Weike(this, dataList);
        xRecyclerView.setAdapter(subjectAdapter_weike);

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

        subjectAdapter_weike.setOnItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                //点击条目记录到学习轨迹数据库表格中
                LearningPathDao3 learningPathDao3 = new LearningPathDao3(ExcellentWeikeActivity.this, TABLE_NAME, DB_NAME);
                learningPathDao3.insert(dataList.get(position));

                //跳转界面
                Intent intent = new Intent(ExcellentWeikeActivity.this, ExcellentWeikeDetailActivity.class);
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

    //请求列表数据
    private void PostList() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("controller", "weike");
        map.put("action", "index");
        map.put("page", pageno + "");
        map.put("cate_id", shaixuanId);
        getJsonUtil().PostJson(this, map);
    }

    //请求筛选数据接口
    private void PostShaixuanList() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("controller", "weike");
        map.put("action", "cate");
        getJsonUtil().PostJson(this, map);
    }


    private void initSubjectRecyclerView() {
        excellentCourseSubjectAdapter = new ExcellentCourseSubjectAdapter(this, subjectList);
        gridView_subject.setAdapter(excellentCourseSubjectAdapter);
        gridView_subject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> map = subjectList.get(position);
                Boolean isChecked = (Boolean) map.get("isChecked");
                if (!isChecked) {
                    //修改当前选中的未ture
                    map.put("isChecked", true);
                    subjectList.set(position, map);
                    //修改其他的未false
                    for (int i = 0; i < subjectList.size(); i++) {
                        if (position != i) {
                            HashMap<String, Object> map1 = subjectList.get(i);
                            map1.put("isChecked", false);
                            subjectList.set(i, map1);
                        }
                    }

                    excellentCourseSubjectAdapter.notifyDataSetChanged();

                    //点击条目请求数据(每次切换视作筛选一次,拿到cate_id,并且把页数初始化成1 再请求数据)
                    HashMap<String, Object> hashMap = subjectList.get(position);
                    shaixuanId = hashMap.get("cate_id") + "";
                    pageno = 1;
                    PostList();

                }
            }
        });
    }


    private int selectPosition_first2 = -1;//记录第一级选中的条目的position

    //初始化第一级筛选数据(全部,小学,初中...)
    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(manager);
        excellentCourseAdapter = new ExcellentCourseAdapter(this, recyclerList);
        recyclerView.setAdapter(excellentCourseAdapter);

        excellentCourseAdapter.setOnItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                HashMap<String, Object> map = recyclerList.get(position);
                Boolean isChecked = (Boolean) map.get("isChecked");
                if (!isChecked) {
                    for (int i = 0; i < recyclerList.size(); i++) {
                        HashMap<String, Object> hashMap = recyclerList.get(i);
                        if (position == i) {
                            hashMap.put("isChecked", true);
                        } else {
                            hashMap.put("isChecked", false);
                        }
                    }
                    excellentCourseAdapter.notifyDataSetChanged();

                    int visibility = ll_second_layout.getVisibility();//获取第二级布局显示状态
                    int visibility1 = ll_shaixuan_first.getVisibility();//获取第一个筛选按钮显示状态
                    //点击第一级分类,选择'全部'的时候隐藏学科分类,选择'小学'或'初中'的时候显示学科分类
                    switch (position) {
                        case 0:
                            //全部
                            if (visibility == View.VISIBLE) {
                                ll_second_layout.setVisibility(View.GONE);
                            }
                            if (visibility1 == View.GONE) {
                                ll_shaixuan_first.setVisibility(View.VISIBLE);
                            }

                            //判断'全部'是否为选中状态,未选中就刷新数据
                            HashMap<String, Object> recyclerMap = recyclerList.get(position);
                            boolean recyclerIsChecked = (boolean) recyclerMap.get("isChecked");
                            if (recyclerIsChecked) {
                                shaixuanId = "";
                                pageno = 1;
                                PostList();
                                selectPosition_first2 = -1;
                            }

                            break;
                        case 1:
                            //初中

                            //判断'初中'是否为选中状态,未选中就刷新数据
                            HashMap<String, Object> recyclerMap1 = recyclerList.get(position);
                            boolean recyclerIsChecked1 = (boolean) recyclerMap1.get("isChecked");
                            if (recyclerIsChecked1) {

                                ArrayList arrayList1 = (ArrayList) mS_navList.get(0);
                                if (arrayList1 == null || arrayList1.size() == 0) {
                                    if (visibility == View.VISIBLE) {
                                        ll_second_layout.setVisibility(View.GONE);
                                    }
                                    if (visibility1 == View.VISIBLE) {
                                        ll_shaixuan_first.setVisibility(View.GONE);
                                    }

                                    if (recyclerList != null && recyclerList.size() > 0) {
                                        HashMap<String, Object> hashMap = recyclerList.get(1);
                                        shaixuanId = hashMap.get("cate_id") + "";
                                        pageno = 1;
                                        PostList();
                                    }

                                    return;
                                }

                                String cate_id = ((HashMap<String, Object>) recyclerList.get(position)).get("cate_id") + "";
                                ArrayList<HashMap<String, Object>> arrayList = (ArrayList<HashMap<String, Object>>) ((ArrayList) mS_navList.get(0)).get(0);
                                if (arrayList == null) {
                                    return;
                                }
                                if (cate_id == null) {
                                    return;
                                }
                                secondList.clear();
                                for (int i = 0; i < arrayList.size(); i++) {
                                    HashMap<String, Object> hashMap = arrayList.get(i);
                                    if (cate_id.equals(hashMap.get("pid") + "")) {
                                        secondList.add(hashMap);
                                    }
                                }
                                subjectList.clear();
                                for (int i = 0; i < secondList.size(); i++) {
                                    HashMap<String, Object> hashMap = secondList.get(i);
                                    if (0 == i) {
                                        hashMap.put("isChecked", true);
                                    } else {
                                        hashMap.put("isChecked", false);
                                    }
                                    subjectList.add(hashMap);
                                }
                                excellentCourseSubjectAdapter.notifyDataSetChanged();

                                if (subjectList != null && subjectList.size() > 0) {
                                    HashMap<String, Object> hashMap = subjectList.get(0);
                                    shaixuanId = hashMap.get("cate_id") + "";
                                    pageno = 1;
                                    PostList();
                                }

                                selectPosition_first2 = 0;
                            }


                            if (visibility == View.GONE) {
                                ll_second_layout.setVisibility(View.VISIBLE);
                            }
                            if (visibility1 == View.VISIBLE) {
                                ll_shaixuan_first.setVisibility(View.GONE);
                            }

                            break;
                        case 2:
                            //小学
                            //判断'小学'是否为选中状态,未选中就刷新数据
                            HashMap<String, Object> recyclerMap2 = recyclerList.get(position);
                            boolean recyclerIsChecked2 = (boolean) recyclerMap2.get("isChecked");
                            if (recyclerIsChecked2) {

                                ArrayList arrayList1 = (ArrayList) mS_navList.get(1);
                                if (arrayList1 == null || arrayList1.size() == 0) {
                                    if (visibility == View.VISIBLE) {
                                        ll_second_layout.setVisibility(View.GONE);
                                    }
                                    if (visibility1 == View.VISIBLE) {
                                        ll_shaixuan_first.setVisibility(View.GONE);
                                    }

                                    if (recyclerList != null && recyclerList.size() > 0) {
                                        HashMap<String, Object> hashMap = recyclerList.get(2);
                                        shaixuanId = hashMap.get("cate_id") + "";
                                        pageno = 1;
                                        PostList();
                                    }

                                    return;
                                }

                                String cate_id = ((HashMap<String, Object>) recyclerList.get(position)).get("cate_id") + "";
                                ArrayList<HashMap<String, Object>> arrayList = (ArrayList<HashMap<String, Object>>) ((ArrayList) mS_navList.get(1)).get(0);
                                if (arrayList == null) {
                                    return;
                                }
                                if (cate_id == null) {
                                    return;
                                }
                                secondList.clear();
                                for (int i = 0; i < arrayList.size(); i++) {
                                    HashMap<String, Object> hashMap = arrayList.get(i);
                                    if (cate_id.equals(hashMap.get("pid"))) {
                                        secondList.add(hashMap);
                                    }
                                }
                                subjectList.clear();
                                for (int i = 0; i < secondList.size(); i++) {
                                    HashMap<String, Object> hashMap = secondList.get(i);
                                    if (0 == i) {
                                        hashMap.put("isChecked", true);
                                    } else {
                                        hashMap.put("isChecked", false);
                                    }
                                    subjectList.add(hashMap);
                                }

                                excellentCourseSubjectAdapter.notifyDataSetChanged();

                                if (subjectList != null && subjectList.size() > 0) {
                                    HashMap<String, Object> hashMap = subjectList.get(0);
                                    shaixuanId = hashMap.get("cate_id") + "";
                                    pageno = 1;
                                    PostList();
                                }

                                selectPosition_first2 = 1;
                            }

                            if (visibility == View.GONE) {
                                ll_second_layout.setVisibility(View.VISIBLE);
                            }
                            if (visibility1 == View.VISIBLE) {
                                ll_shaixuan_first.setVisibility(View.GONE);
                            }

                            break;
                    }

                }
            }
        });
    }

    @Override
    public void afterInitView() {
    }

    @OnClick({R.id.ll_back, R.id.iv_right, R.id.ll_shaixuan_first, R.id.ll_shaixuan_second})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.iv_right:
                //搜索按钮
                Intent intent = new Intent(this, ExcellentWeikeSearchActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_shaixuan_first:
                //当选中'全部'显示的筛选按钮
                showFirstShaixuanPop();
                break;
            case R.id.ll_shaixuan_second:
                //当选中不是'全部'显示的筛选按钮
                showSecondShaixuanPop();
                break;
        }
    }

    private PopupWindow popupWindow2;
    private View popWindowView2;
    private List<HashMap<String, Object>> threedList = new ArrayList<>();//第三级数据集合

    //显示选中不是'全部'的筛选popwindow
    private void showSecondShaixuanPop() {
        if (popupWindow2 == null) {
            popupWindow2 = new PopupWindow(this);
            popupWindow2.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow2.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        }

        if (popWindowView2 == null) {
            popWindowView2 = LayoutInflater.from(this).inflate(R.layout.layout_shaixuan_pop_weike, null);
        }
        initPopWindowView2(popWindowView2, popupWindow2);
        popupWindow2.setContentView(popWindowView2);
        popupWindow2.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow2.setOutsideTouchable(false);
        popupWindow2.setFocusable(true);
        popupWindow2.showAsDropDown(findViewById(R.id.ll_title), 0, 0, Gravity.RIGHT);
        popupWindow2.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
    }

    private int isWhatLevelChecked = -1;//几率是哪一级被选中的(3代表第三级被选中,4代表第四季被选中)
    private int selectPosition_all = -1;//记录每一个条目选中的position,用来取出对应数据集合的cate_id

    //初始化popWindowView2
    private void initPopWindowView2(View popWindowView2, final PopupWindow popupWindow2) {
        //外面阴影部分点击隐藏popwindow
        popWindowView2.findViewById(R.id.ll_outside).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow2 != null) {
                    popupWindow2.dismiss();
                }
            }
        });

        LinearLayout ll_threed2 = (LinearLayout) popWindowView2.findViewById(R.id.ll_threed);
        TextView tv_title_threed2 = (TextView) popWindowView2.findViewById(R.id.tv_title_threed);
        MyGridView gridView_threed2 = (MyGridView) popWindowView2.findViewById(R.id.gridView_threed);
        ll_fourth2 = (LinearLayout) popWindowView2.findViewById(R.id.ll_fourth);
        TextView tv_title_fourth2 = (TextView) popWindowView2.findViewById(R.id.tv_title_fourth);
        gridView_fourth2 = (MyGridView) popWindowView2.findViewById(R.id.gridView_fourth);

        ll_threed2.setVisibility(View.GONE);
        ll_fourth2.setVisibility(View.GONE);

        //重置按钮
        popWindowView2.findViewById(R.id.tv_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_fourth2.getVisibility() == View.VISIBLE) {
                    ll_fourth2.setVisibility(View.GONE);
                }
                if (popGridViewAdapter_threed != null) {
                    popGridViewAdapter_threed.changeDefault();
                    selectPosition_all = -1;
                    isWhatLevelChecked = -1;
                }
            }
        });
        //完成按钮
        popWindowView2.findViewById(R.id.tv_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isWhatLevelChecked == 3) {
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
                    pageno = 1;
                    PostList();
                }

                isWhatLevelChecked = -1;
                selectPosition_all = -1;

                if (popupWindow2 != null) {
                    popupWindow2.dismiss();
                }
            }
        });

        //标题
        tv_title_threed2.setText(mTitleList.get(2));
        tv_title_fourth2.setText(mTitleList.get(3));

        //因为点击完成后shaixuanId会改成最后一个选择的条目,所以在每次初始化popwindow里的数据时候,要把shaixuanId初始化成原来选中的第二级的cate_id
        if (subjectList != null && subjectList.size() > 0) {
            for (int i = 0; i < subjectList.size(); i++) {
                HashMap<String, Object> map = subjectList.get(i);
                boolean isChecked = (boolean) map.get("isChecked");
                if (isChecked == true) {
                    shaixuanId = map.get("cate_id") + "";
                }
            }
        }

        ArrayList arrayList1 = (ArrayList) mS_navList.get(selectPosition_first2);
        if (arrayList1 == null || arrayList1.size() < 2) {
            ToastUtil.show(this, "暂无筛选数据", 0);
            return;
        }

        //第三级数据
        ArrayList<HashMap<String, Object>> arrayList = (ArrayList<HashMap<String, Object>>) ((ArrayList) mS_navList.get(selectPosition_first2)).get(1);
        if (arrayList == null || arrayList.size() == 0) {
            ll_threed2.setVisibility(View.GONE);
            ToastUtil.show(this, "该版块暂时无数据", 0);
            return;
        } else {
            ll_threed2.setVisibility(View.VISIBLE);
        }
        //根据cate_id匹配pid整理出对应的数据
        threedList.clear();
        for (int i = 0; i < arrayList.size(); i++) {
            HashMap<String, Object> hashMap = arrayList.get(i);
            String pid = hashMap.get("pid") + "";
            if (shaixuanId != null && shaixuanId.equals(pid)) {
                threedList.add(hashMap);
            }
        }
        if (threedList != null && threedList.size() > 0) {
            if (popGridViewAdapter_threed == null) {
                popGridViewAdapter_threed = new PopGridViewAdapter(this, threedList);
                gridView_threed2.setAdapter(popGridViewAdapter_threed);
                popGridViewAdapter_threed.changeDefault();//第一次初始化默认未选中状态
                gridView_threed2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        popGridViewAdapter_threed.changeState(position);
                        isWhatLevelChecked = 3;//第三级被选中
                        selectPosition_all = position;//记录选中的position
                        Log.i("selectPosition_all", "    position:     " + position);
                        Log.i("selectPosition_all", "    selectPosition_all:     " + selectPosition_all);
                        initFourthGridView();
                    }
                });
            } else {
                //在不是第一次初始化adapter的时候还原初始化之前的选择状态
                selectPosition_all = -1;//初始化记录被选中的position
                popGridViewAdapter_threed.changeDefault();//还原默认未选中状态
                popGridViewAdapter_threed.notifyDataSetChanged();
            }
        } else {
            ll_threed2.setVisibility(View.GONE);
        }

    }

    private PopGridViewAdapter popGridViewAdapter_fourth;
    private List<HashMap<String, Object>> fourthList = new ArrayList<>();//第三级数据集合

    //初始化第四级数据
    private void initFourthGridView() {
        //第三级选中的cate_id
        String cate_id = ((HashMap<String, Object>) threedList.get(selectPosition_all)).get("cate_id") + "";
        //第四级数据
        ArrayList<HashMap<String, Object>> arrayList = (ArrayList<HashMap<String, Object>>) ((ArrayList) mS_navList.get(selectPosition_first2)).get(2);
        if (arrayList == null || arrayList.size() == 0) {
            ll_fourth2.setVisibility(View.GONE);
            ToastUtil.show(this, "该版块暂时无数据", 0);
            return;
        } else {
            ll_fourth2.setVisibility(View.VISIBLE);
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
            if (popGridViewAdapter_fourth == null) {
                popGridViewAdapter_fourth = new PopGridViewAdapter(this, fourthList);
                gridView_fourth2.setAdapter(popGridViewAdapter_fourth);
                popGridViewAdapter_fourth.changeDefault();//第一次初始化默认未选中状态
                gridView_fourth2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
            ll_fourth2.setVisibility(View.GONE);
        }

    }

    private PopupWindow popupWindow1;
    private View popWindowView1;

    //显示选中'全部'的筛选popwindow
    private void showFirstShaixuanPop() {
        if (popupWindow1 == null) {
            popupWindow1 = new PopupWindow(this);
            popupWindow1.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow1.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        }

        if (popWindowView1 == null) {
            popWindowView1 = LayoutInflater.from(this).inflate(R.layout.layout_shaixuan_pop_weike, null);
        }
        initPopWindowView1(popWindowView1, popupWindow1);
        popupWindow1.setContentView(popWindowView1);
        popupWindow1.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow1.setOutsideTouchable(false);
        popupWindow1.setFocusable(true);
        popupWindow1.showAsDropDown(findViewById(R.id.ll_title), 0, 0, Gravity.RIGHT);
    }

    private int selectPosition_threed1 = -1;//记录'全部'下筛选框中选中的条目的position

    //初始化popWindowView1
    private void initPopWindowView1(View popWindowView1, final PopupWindow popupWindow1) {
        //外面阴影部分点击隐藏popwindow
        popWindowView1.findViewById(R.id.ll_outside).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow1 != null) {
                    popupWindow1.dismiss();
                }
            }
        });

        TextView tv_title_threed = (TextView) popWindowView1.findViewById(R.id.tv_title_threed);//第一个标题
        LinearLayout ll_threed = (LinearLayout) popWindowView1.findViewById(R.id.ll_threed);//第一个模块布局
        MyGridView gridView_threed = (MyGridView) popWindowView1.findViewById(R.id.gridView_threed);//第一个模块的gridview
        //设置title
        if (mTitleList != null && mTitleList.size() > 0) {
            tv_title_threed.setText(mTitleList.get(0));
        }
        //设置gridview
        if (recyclerList != null && recyclerList.size() > 0) {
            ll_threed.setVisibility(View.VISIBLE);
            if (popGridViewAdapter_threed1 == null) {
                popGridViewAdapter_threed1 = new PopGridViewAdapter(this, recyclerList);
                gridView_threed.setAdapter(popGridViewAdapter_threed1);
                popGridViewAdapter_threed1.changeDefault();//第一次初始化默认未选中状态
                gridView_threed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        popGridViewAdapter_threed1.changeState(position);
                        selectPosition_threed1 = position;
                    }
                });
            } else {
                selectPosition_threed1 = -1;
                popGridViewAdapter_threed1.changeDefault();//还原默认未选中状态
                popGridViewAdapter_threed1.notifyDataSetChanged();
            }
        }

        //重置
        ((TextView) popWindowView1.findViewById(R.id.tv_reset)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPosition_threed1 = -1;
                popGridViewAdapter_threed1.changeDefault();
            }
        });

        //完成
        ((TextView) popWindowView1.findViewById(R.id.tv_finish)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow1 != null) {
                    popupWindow1.dismiss();
                }
                if (selectPosition_threed1 == -1) {
                    shaixuanId = "";
                    pageno = 1;
                    PostList();
                } else {
                    HashMap<String, Object> map = recyclerList.get(selectPosition_threed1);
                    shaixuanId = map.get("cate_id") + "";
                    pageno = 1;
                    PostList();
                }
            }
        });
    }

}
