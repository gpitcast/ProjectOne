package com.youyi.ywl.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youyi.ywl.R;
import com.youyi.ywl.adapter.ExcellentCourseSearchGridAdapter;
import com.youyi.ywl.adapter.ExcellentCourseSearchRecyclerAdapter;
import com.youyi.ywl.inter.RecyclerViewOnItemClickListener;
import com.youyi.ywl.other.Constanst;
import com.youyi.ywl.util.HttpUtils;
import com.youyi.ywl.util.SearchHistoryDao;
import com.youyi.ywl.util.SoftUtil;
import com.youyi.ywl.util.ToastUtil;
import com.youyi.ywl.view.BaseDialog;
import com.youyi.ywl.view.MyGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/8/13.
 * 精品微课搜索界面
 */

public class ExcellentWeikeSearchActivity extends BaseActivity {
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.gridView)
    MyGridView gridView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ll_history_search)
    LinearLayout ll_history_search;

    private static final String HOT_SEARCH_URL = HttpUtils.Host + "/weike/hotSearch";//微课热门搜索接口
    private static final String DB_NAME = "weike_search_history_info.db";//存储精品微课搜索历史的数据库名称
    private static final String TABLE_NAME = "excellentWeikeHistory";//存储精品微课搜索历史的表格名称
    private List<String> hotSearchList = new ArrayList<>();//热点搜索数据
    private List<String> historyRecyclerList = new ArrayList<>();//搜索历史数据
    private ExcellentCourseSearchRecyclerAdapter excellentCourseSearchRecyclerAdapter;
    private ExcellentCourseSearchGridAdapter excellentCourseSearchGridAdapter;

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case HOT_SEARCH_URL:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        //'0'代表数据正常
                        ArrayList<String> dataList = (ArrayList<String>) resultMap.get("data");
                        hotSearchList.addAll(dataList);
                        excellentCourseSearchGridAdapter.notifyDataSetChanged();
                    } else {
                        //不为'0'代表数据有问题
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
        setContentView(R.layout.activity_excellent_weike_search);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {

        PostHotSearchList();
        initGridView();
        initRecyclerView();

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE
                        || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (et_search.getText().toString().trim() != null && et_search.getText().toString().trim().length() > 0) {
                        SoftUtil.hideSoft(ExcellentWeikeSearchActivity.this);//隐藏键盘
                        SearchHistoryDao historyDao = new SearchHistoryDao(ExcellentWeikeSearchActivity.this, TABLE_NAME, DB_NAME);
                        historyDao.add(et_search.getText().toString().trim());
                        Intent intent = new Intent(ExcellentWeikeSearchActivity.this, ExcellentWeikeSearchResultActivity.class);
                        intent.putExtra("searchContent", et_search.getText().toString().trim());
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtil.show(ExcellentWeikeSearchActivity.this, "搜索关键字不能为空", 0);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void initGridView() {
        excellentCourseSearchGridAdapter = new ExcellentCourseSearchGridAdapter(this, hotSearchList);
        gridView.setAdapter(excellentCourseSearchGridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //热点搜索点击事件
                SoftUtil.hideSoft(ExcellentWeikeSearchActivity.this);//隐藏键盘
                SearchHistoryDao historyDao = new SearchHistoryDao(ExcellentWeikeSearchActivity.this, TABLE_NAME, DB_NAME);
                historyDao.add(hotSearchList.get(position));
                Intent intent = new Intent(ExcellentWeikeSearchActivity.this, ExcellentWeikeSearchResultActivity.class);
                intent.putExtra("searchContent", hotSearchList.get(position));
                startActivity(intent);
                finish();
            }
        });
    }

    //请求热门搜索接口
    private void PostHotSearchList() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("controller", "weike");
        map.put("action", "hotSearch");
        getJsonUtil().PostJson(this, map);
    }

    //查询数据库的数据
    private void selectData() {
        SearchHistoryDao historyDao = new SearchHistoryDao(this, TABLE_NAME, DB_NAME);
        List<String> list = historyDao.select();
        historyRecyclerList.clear();
        if (list != null && list.size() != 0) {
            historyRecyclerList.addAll(list);
            ll_history_search.setVisibility(View.VISIBLE);
        } else {
            ll_history_search.setVisibility(View.GONE);
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(manager);
        excellentCourseSearchRecyclerAdapter = new ExcellentCourseSearchRecyclerAdapter(this, historyRecyclerList);
        recyclerView.setAdapter(excellentCourseSearchRecyclerAdapter);
        excellentCourseSearchRecyclerAdapter.setOnItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                //历史搜索点击事件
                SoftUtil.hideSoft(ExcellentWeikeSearchActivity.this);//隐藏键盘
                SearchHistoryDao historyDao = new SearchHistoryDao(ExcellentWeikeSearchActivity.this, TABLE_NAME, DB_NAME);
                historyDao.add(historyRecyclerList.get(position));
                Intent intent = new Intent(ExcellentWeikeSearchActivity.this, ExcellentWeikeSearchResultActivity.class);
                intent.putExtra("searchContent", historyRecyclerList.get(position));
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void afterInitView() {
    }

    @OnClick({R.id.iv_back, R.id.tv_cancel, R.id.ll_clean_history})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_cancel:
                //取消键
                if (et_search.getText().toString().trim() != null && et_search.getText().toString().trim().length() > 0) {
                    et_search.setText("");
                } else {
                    SoftUtil.hideSoft(this);
                    finish();
                }
                break;
            case R.id.ll_clean_history:
                //清空搜索历史
                if (historyRecyclerList == null || historyRecyclerList.size() == 0) {
                    ToastUtil.show(this, "搜索记录为空", 0);
                    return;
                }

                BaseDialog.Builder builder = new BaseDialog.Builder(this);
                builder.setTitle("确定清空历史搜索记录吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SearchHistoryDao historyDao = new SearchHistoryDao(ExcellentWeikeSearchActivity.this, TABLE_NAME, DB_NAME);
                        historyDao.deleteAll();
                        selectData();
                        excellentCourseSearchRecyclerAdapter.notifyDataSetChanged();
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectData();
        excellentCourseSearchRecyclerAdapter.notifyDataSetChanged();
    }
}
