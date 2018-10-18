package com.youyi.ywl.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.youyi.ywl.R;
import com.youyi.ywl.adapter.ExampleExplainMainListAdapter;
import com.youyi.ywl.adapter.ExampleExplainMainPopAdapter;
import com.youyi.ywl.adapter.ExampleExplainMainTitleAdapter;
import com.youyi.ywl.inter.RecyclerViewOnItemClickListener;
import com.youyi.ywl.other.Constanst;
import com.youyi.ywl.util.CheckLoginUtil;
import com.youyi.ywl.util.HttpUtils;
import com.youyi.ywl.util.LearningPathDao1;
import com.youyi.ywl.util.ToastUtil;
import com.youyi.ywl.view.BasePopupWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/8/24.
 * 习题讲解 - 首页
 */

public class ExampleExplainMainActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.recyclerView_title)
    RecyclerView recyclerView_title;
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;
    @BindView(R.id.tv_version)
    TextView tv_version;
    @BindView(R.id.tv_subject)
    TextView tv_subject;
    @BindView(R.id.tv_grade)
    TextView tv_grade;

    private static final String CATE_TYPE_URL = HttpUtils.Host + "/xtjj/getCateAndType";//分类和类型数据接口
    private static final String DATA_URL = HttpUtils.Host + "/xtjj/index";//列表数据接口
    private HashMap mDataMap = new HashMap();//所有数据map集合
    private ArrayList<Map<String, Object>> titleList = new ArrayList<>();//分类栏数据
    private ArrayList<HashMap<String, Object>> dataList = new ArrayList<>();//列表数据
    private ExampleExplainMainTitleAdapter exampleExplainMainTitleAdapter;
    private ExampleExplainMainListAdapter exampleExplainMainListAdapter;
    private TextView tv_status;
    private ExampleExplainMainPopAdapter exampleExplainMainPopAdapter;
    private boolean isLoadMore;

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case CATE_TYPE_URL:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        HashMap dataMap = (HashMap) resultMap.get("data");
                        //所有数据
                        mDataMap.clear();
                        mDataMap.putAll(dataMap);

                        //分类标题栏数据
                        ArrayList<HashMap<String, Object>> cateList = (ArrayList<HashMap<String, Object>>) dataMap.get("cate");
                        titleList.clear();
                        titleList.addAll(cateList);
                        for (int i = 0; i < titleList.size(); i++) {
                            Map<String, Object> map = titleList.get(i);
                            if (i == 0) {
                                map.put("isSelected", true);
                                titleList.set(i, map);
                            } else {
                                map.put("isSelected", false);
                                titleList.set(i, map);
                            }
                        }
                        exampleExplainMainTitleAdapter.notifyDataSetChanged();

                        //如果在登录状态,后台会返回学习年级字段,需要自动设置到请求数据的条件中去,
                        //并且要在请求数据之前
                        if (CheckLoginUtil.isLogin(this)) {
                            //登录
                            String userClass = dataMap.get("userClass") + "";
                            if (userClass != null && userClass.length() > 0) {
                                grade_shaixuan = userClass;
                                tv_grade.setText(userClass);

                                ArrayList<String> aClassList = (ArrayList<String>) dataMap.get("class");
                                if (aClassList != null) {
                                    for (int i = 0; i < aClassList.size(); i++) {
                                        String className = aClassList.get(i);
                                        if (userClass.equals(className)) {
                                            classPosition = i;
                                        }
                                    }
                                }
                            }
                        }

                        //请求列表数据,初始化首次默认选中第一个分类
                        if (titleList != null && titleList.size() > 0) {
                            cate_id = ((Map<String, Object>) titleList.get(0)).get("id") + "";
                            PostList();
                        }

                    } else {
                        ToastUtil.show(this, ((HashMap) result).get("msg") + "", 0);
                    }
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
                        exampleExplainMainListAdapter.notifyDataSetChanged();

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
                            exampleExplainMainListAdapter.notifyDataSetChanged();
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
                        exampleExplainMainListAdapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_example_explain_main);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tv_title.setText("习题讲解");
        PostCateAndTypeList();//请求分类和类型接口
        initTitleRecyclerView();//初始化分类标题的recyclerview
        initXRecyclerView();//初始化列表的recyclerview
    }

    //请求分类和类型接口
    private void PostCateAndTypeList() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("controller", "xtjj");
        map.put("action", "getCateAndType");
        getJsonUtil().PostJson(this, map);
    }

    private int pageno = 1;//分页页数
    private String cate_id = "";//分类类型的id
    private String version_shaixuan = "";//版本筛选的条目
    private String subject_shaixuan = "";//版本筛选的条目
    private String grade_shaixuan = "";//版本筛选的条目

    //请求列表数据接口
    public void PostList() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("controller", "xtjj");
        map.put("action", "index");
        map.put("page", pageno + "");
        map.put("cate_id", cate_id);
        map.put("version", version_shaixuan);
        map.put("subject", subject_shaixuan);
        map.put("grade", grade_shaixuan);
        getJsonUtil().PostJson(this, map);
    }

    private String DB_NAME = Constanst.userPhoneNum + "_example_explain_learning_info.db";
    private String TABLE_NAME = "exampleExplainLearning";

    private void initXRecyclerView() {
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        xRecyclerView.setLayoutManager(manager);
        exampleExplainMainListAdapter = new ExampleExplainMainListAdapter(this, dataList);
        xRecyclerView.setAdapter(exampleExplainMainListAdapter);
        View headerView1 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        View headerView2 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        xRecyclerView.addHeaderView(headerView1);
        xRecyclerView.addHeaderView(headerView2);
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

        exampleExplainMainListAdapter.setonItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                //点击条目记录到学习轨迹数据库表格中
                LearningPathDao1 learningPathDao1 = new LearningPathDao1(ExampleExplainMainActivity.this, TABLE_NAME, DB_NAME);
                learningPathDao1.insert(dataList.get(position));

                //跳转界面
                HashMap<String, Object> map = dataList.get(position);
                Intent intent = new Intent(ExampleExplainMainActivity.this, ExampleExplainSelectPageActivity.class);
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
        footerView.setBackgroundColor(getResources().getColor(R.color.white));
        tv_status = (TextView) footerView.findViewById(R.id.tv_status);
        tv_status.setTextColor(getResources().getColor(R.color.grayone));
    }

    private void initTitleRecyclerView() {
        final LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView_title.setLayoutManager(manager);
        exampleExplainMainTitleAdapter = new ExampleExplainMainTitleAdapter(this, titleList);
        recyclerView_title.setAdapter(exampleExplainMainTitleAdapter);
        exampleExplainMainTitleAdapter.setOnItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                for (int i = 0; i < titleList.size(); i++) {
                    if (position == i) {
                        Map<String, Object> map = titleList.get(i);
                        boolean isSelected = (boolean) map.get("isSelected");
                        if (!isSelected) {
                            map.put("isSelected", true);
                            titleList.set(i, map);
                        }
                    } else {
                        Map<String, Object> map = titleList.get(i);
                        map.put("isSelected", false);
                        titleList.set(i, map);
                    }
                }
                cate_id = ((Map<String, Object>) titleList.get(position)).get("id") + "";
                pageno = 1;
                PostList();
                exampleExplainMainTitleAdapter.notifyDataSetChanged();
                exampleExplainMainListAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void afterInitView() {
    }

    //记录上一次选中的筛选弹出popwindow的类型
    private boolean isVersionChecked;
    private boolean isSubjectChecked;
    private boolean isClassChecked;
    //记录每一个对应的选中筛选的popwindow的position
    private int versionPosition = -1;
    private int subjectPosition = -1;
    private int classPosition = -1;

    @OnClick({R.id.ll_back, R.id.ll_shaixuan_banben, R.id.ll_shaixuan_kemu, R.id.ll_shaixuan_nianji})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_shaixuan_banben:
                //版本筛选
                if (!isVersionChecked) {
                    isVersionChecked = true;
                    isSubjectChecked = false;
                    isClassChecked = false;
                    shaixuanList.clear();
                    ArrayList versionList = (ArrayList) mDataMap.get("version");
                    if (versionList != null) {
                        for (int i = 0; i < versionList.size(); i++) {
                            if (versionPosition == i) {
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("name", versionList.get(i) + "");
                                map.put("isSelected", true);
                                shaixuanList.add(map);
                            } else {
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("name", versionList.get(i) + "");
                                map.put("isSelected", false);
                                shaixuanList.add(map);
                            }
                        }
                    }
                }
                showShaixuanPop();
                break;
            case R.id.ll_shaixuan_kemu:
                //科目筛选
                if (!isSubjectChecked) {
                    isVersionChecked = false;
                    isSubjectChecked = true;
                    isClassChecked = false;
                    shaixuanList.clear();
                    ArrayList subjectList = (ArrayList) mDataMap.get("subject");
                    if (subjectList != null) {
                        for (int i = 0; i < subjectList.size(); i++) {
                            if (subjectPosition == i) {
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("name", subjectList.get(i) + "");
                                map.put("isSelected", true);
                                shaixuanList.add(map);
                            } else {
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("name", subjectList.get(i) + "");
                                map.put("isSelected", false);
                                shaixuanList.add(map);
                            }
                        }
                    }
                }
                showShaixuanPop();

                break;
            case R.id.ll_shaixuan_nianji:
                //年级筛选
                if (!isClassChecked) {
                    isVersionChecked = false;
                    isSubjectChecked = false;
                    isClassChecked = true;
                    shaixuanList.clear();
                    ArrayList classList = (ArrayList) mDataMap.get("class");
                    if (classList != null) {
                        for (int i = 0; i < classList.size(); i++) {
                            if (classPosition == i) {
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("name", classList.get(i) + "");
                                map.put("isSelected", true);
                                shaixuanList.add(map);
                            } else {
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("name", classList.get(i) + "");
                                map.put("isSelected", false);
                                shaixuanList.add(map);
                            }
                        }
                    }
                }
                showShaixuanPop();
                break;
        }
    }

    private PopupWindow popupWindow;
    private View popWindowView;
    private List<HashMap<String, Object>> shaixuanList = new ArrayList();//用来存储shaixuan的popwindow里的数据

    //显示筛选的popwindow
    private void showShaixuanPop() {
        if (popupWindow == null) {
            popupWindow = new BasePopupWindow(this);
            popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        }

        if (popWindowView == null) {
            popWindowView = LayoutInflater.from(this).inflate(R.layout.layout_shaixuan_pop_select_page, null);
            //取消
            popWindowView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                }
            });
            popupWindow.setContentView(popWindowView);
            popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
            popupWindow.setOutsideTouchable(false);
            popupWindow.setFocusable(true);
        }
        initPopWindowViewAdapter();

        popupWindow.showAtLocation(findViewById(R.id.ll_base), Gravity.RIGHT, 0, 0);
    }

    private void initPopWindowViewAdapter() {
        if (exampleExplainMainPopAdapter == null) {
            RecyclerView recyclerView = (RecyclerView) popWindowView.findViewById(R.id.recyclerView);
            GridLayoutManager manager = new GridLayoutManager(this, 3);
            recyclerView.setLayoutManager(manager);
            exampleExplainMainPopAdapter = new ExampleExplainMainPopAdapter(this, shaixuanList);
            recyclerView.setAdapter(exampleExplainMainPopAdapter);
            exampleExplainMainPopAdapter.setOnItemClickListener(new RecyclerViewOnItemClickListener() {
                @Override
                public void OnItemClick(View view, int position) {
                    boolean isSelected = (boolean) ((HashMap<String, Object>) shaixuanList.get(position)).get("isSelected");
                    //当前未选中点击的条目才做高边
                    if (!isSelected) {
                        for (int i = 0; i < shaixuanList.size(); i++) {
                            if (position == i) {
                                HashMap<String, Object> map = shaixuanList.get(i);
                                map.put("isSelected", true);
                                shaixuanList.set(i, map);
                            } else {
                                HashMap<String, Object> map = shaixuanList.get(i);
                                map.put("isSelected", false);
                                shaixuanList.set(i, map);
                            }
                        }
                        exampleExplainMainPopAdapter.notifyDataSetChanged();
                    }

                    //根据选中的条目请求数据
                    if (isVersionChecked) {
                        version_shaixuan = ((HashMap<String, Object>) shaixuanList.get(position)).get("name") + "";
                        versionPosition = position;
                        tv_version.setText(version_shaixuan);
                    } else if (isSubjectChecked) {
                        subject_shaixuan = ((HashMap<String, Object>) shaixuanList.get(position)).get("name") + "";
                        subjectPosition = position;
                        tv_subject.setText(subject_shaixuan);
                    } else if (isClassChecked) {
                        grade_shaixuan = ((HashMap<String, Object>) shaixuanList.get(position)).get("name") + "";
                        classPosition = position;
                        tv_grade.setText(grade_shaixuan);
                    }
                    pageno = 1;
                    PostList();
                    //隐藏popwindow
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                }
            });
        } else {
            exampleExplainMainPopAdapter.notifyDataSetChanged();
        }

    }
}
