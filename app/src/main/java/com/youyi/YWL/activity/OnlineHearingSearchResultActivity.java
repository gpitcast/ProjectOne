package com.youyi.YWL.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.youyi.YWL.R;
import com.youyi.YWL.adapter.OnlineHearingAdapter;
import com.youyi.YWL.inter.RecyclerViewOnItemClickListener;
import com.youyi.YWL.other.Constanst;
import com.youyi.YWL.util.HttpUtils;
import com.youyi.YWL.util.SoftUtil;
import com.youyi.YWL.util.ToastUtil;

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
    @BindView(R.id.tv_all)
    TextView tv_all;
    @BindView(R.id.tv_hot)
    TextView tv_hot;
    @BindView(R.id.iv_time_sort)
    ImageView iv_time_sort;
    @BindView(R.id.tv_time_sort)
    TextView tv_time_sort;
    @BindView(R.id.tv_search_layout)
    TextView tv_search_layout;
    @BindView(R.id.tv_shaixuan_short)
    TextView tv_shaixuan_short;

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
        setContentView(R.layout.activity_online_hearing_search_result);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        initRecyclerView();
        PostList();

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

    private void initRecyclerView() {
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
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
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
        map.put("time", timeSort + "");//时间的升降序排序(0是升序,1是降序)
        map.put("hot", isHot + "");//是否是热点资源
        map.put("keywords", searchContent + "");//搜索关键词(这个界面没有搜索功能,传""就可以)
        map.put("cate_id", shaixuanId + "");//筛选界面的筛选条件对应的筛选ID
        getJsonUtil().PostJson(this, map);
    }


    @OnClick({R.id.iv_back, R.id.iv_shaixuan, R.id.tv_all, R.id.tv_hot, R.id.ll_time_sort, R.id.tv_search, R.id.tv_search_layout})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_shaixuan:
                Intent intent = new Intent(this, OnlineHearingShaixuanActivity.class);
                startActivityForResult(intent, mRequestCode);
                break;
            case R.id.tv_all:
                //全部
                tv_all.setTextColor(getResources().getColor(R.color.orangeone));
                tv_hot.setTextColor(getResources().getColor(R.color.normal_black));

                isHot = "";
                pageno = 1;
                PostList();
                break;
            case R.id.tv_hot:
                tv_all.setTextColor(getResources().getColor(R.color.normal_black));
                tv_hot.setTextColor(getResources().getColor(R.color.orangeone));

                //热点资源
                isHot = "1";
                pageno = 1;
                PostList();
                break;
            case R.id.ll_time_sort:
                //时间排序(0是升序,1是降序,默认是升序0)
                tv_time_sort.setTextColor(getResources().getColor(R.color.orangeone));
                if (timeSort == 0) {
                    iv_time_sort.setImageResource(R.mipmap.icon_arrow_down_half_blue);
                    timeSort = 1;
                    pageno = 1;
                    PostList();
                } else if (timeSort == 1) {
                    iv_time_sort.setImageResource(R.mipmap.icon_arrow_up_half_blue);
                    timeSort = 0;
                    pageno = 1;
                    PostList();
                }
                break;
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
            if (shaixuanName != null && shaixuanName.length() > 0) {
                if (shaixuanName.length() <= 2) {
                    tv_shaixuan_short.setText("筛选(" + shaixuanName + ")");
                } else {
                    String substring = shaixuanName.substring(0, 2);
                    tv_shaixuan_short.setText("筛选(" + substring + "...)");
                }
            } else {
                tv_shaixuan_short.setText("筛选()");
            }
            pageno = 1;
            PostList();
        }
    }
}
