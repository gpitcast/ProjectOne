package com.feature.projectone.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.adapter.JoinGroupAdapter;
import com.feature.projectone.inter.ReplyIconClickListener;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.HttpUtils;
import com.feature.projectone.util.SoftUtil;
import com.feature.projectone.util.ToastUtil;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/6/1.
 * 加入社区群界面
 */

public class JoinGroupActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;
    @BindView(R.id.et_search)
    EditText et_search;

    private static final String dataUrl = HttpUtils.Host + "/qunzhu/index";
    private static final String joinUrl = HttpUtils.Host + "/qunzhu/join";
    private JoinGroupAdapter joinGroupAdapter;
    private List<HashMap<String, Object>> dataList = new ArrayList<>();
    private int pageno = 1;
    private boolean isLoadMore;
    private boolean isSearch;
    private int joinClickPosition;

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case dataUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    ArrayList<HashMap<String, Object>> arrayList = (ArrayList<HashMap<String, Object>>) resultMap.get("data");
                    if (arrayList != null && arrayList.size() > 0) {
                        if (isLoadMore) {
                            isLoadMore = false;
                            xRecyclerView.loadMoreComplete();
                        }
                        if (isSearch) {
                            isSearch = false;
                            dataList.clear();
                        }
                        dataList.addAll(arrayList);
                        joinGroupAdapter.notifyDataSetChanged();
                    } else {
                        if (isLoadMore) {
                            isLoadMore = false;
                            pageno--;
                            xRecyclerView.loadMoreComplete();
                        }
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                    }
                } else {
                    if (isLoadMore) {
                        isLoadMore = false;
                        pageno--;
                        xRecyclerView.loadMoreComplete();
                    }
                    ToastUtil.show(this, msg, 0);
                }
                break;
            case joinUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        //申请成功(需要改变)
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                        HashMap<String, Object> map = dataList.get(joinClickPosition);
                        map.put("status", "1");
                        dataList.set(joinClickPosition, map);
                        joinGroupAdapter.notifyDataSetChanged();
                    } else {
                        //申请不成功,弹出吐司提醒用户
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                    }
                } else {
                    ToastUtil.show(this, msg, 0);
                }
                break;
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_join_group);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tv_title.setText("加入社区群");
        PostList();
        initRecyclerView();
        initSearchView();
    }

    private void initSearchView() {
        //添加软键盘的监听
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE
                        || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (et_search.getText().toString().trim() != null && et_search.getText().toString().trim().length() > 0) {
                        SoftUtil.hideSoft(JoinGroupActivity.this);//隐藏键盘
                        PostList(textView);
                    } else {
                        ToastUtil.show(JoinGroupActivity.this, "搜索关键字不能为空", 0);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    //请求相关社群列表接口
    private void PostList() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "qunzhu");
        map.put("action", "index");
        map.put("p", pageno + "");
        map.put("keywords", et_search.getText().toString().trim());
        getJsonUtil().PostJson(this, map);
    }

    //专门搜索的接口请求方法
    public void PostList(View view) {
        isSearch = true;
        pageno = 1;
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "qunzhu");
        map.put("action", "index");
        map.put("p", pageno + "");
        map.put("keywords", et_search.getText().toString().trim());
        getJsonUtil().PostJson(JoinGroupActivity.this, map, view);
    }

    //加群接口
    private void PostJoinList(View view, String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "qunzhu");
        map.put("action", "join");
        map.put("id", id);
        getJsonUtil().PostJson(this, map, view);
    }


    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        xRecyclerView.setLayoutManager(manager);
        joinGroupAdapter = new JoinGroupAdapter(this, dataList);
        xRecyclerView.setAdapter(joinGroupAdapter);
        View headerView1 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        View headerView2 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        xRecyclerView.addHeaderView(headerView1);
        xRecyclerView.addHeaderView(headerView2);
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
            }
        });

        View footerView = LayoutInflater.from(this).inflate(R.layout.layout_recyclerview_footer, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
        layoutParams.gravity = Gravity.CENTER;
        footerView.setLayoutParams(layoutParams);
        footerView.setBackgroundColor(getResources().getColor(R.color.white));
        xRecyclerView.setFootView(footerView);
        xRecyclerView.loadMoreComplete();

        joinGroupAdapter.setOnJoinClickListener(new ReplyIconClickListener() {
            @Override
            public void OnReplyIconClick(View view, int position) {
                HashMap<String, Object> map = dataList.get(position);
                joinClickPosition = position;
                PostJoinList(view, map.get("id") + "");
            }
        });
    }

    @Override
    public void afterInitView() {
    }

    @OnClick({R.id.ll_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }
}
