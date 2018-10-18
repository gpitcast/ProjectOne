package com.youyi.ywl.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.youyi.ywl.R;
import com.youyi.ywl.adapter.ExampleExplainSelectPageAdapter;
import com.youyi.ywl.inter.RecyclerViewOnItemClickListener;
import com.youyi.ywl.other.Constanst;
import com.youyi.ywl.util.GlideUtil;
import com.youyi.ywl.util.HttpUtils;
import com.youyi.ywl.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/8/25.
 * 习题讲解 - 选择页码界面
 */

public class ExampleExplainSelectPageActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;

    private static final String DETAIL_URL = HttpUtils.Host + "/xtjj/detail";//详情url
    private ExampleExplainSelectPageAdapter exampleExplainSelectPageAdapter;
    private String id;
    private ImageView iv_detail;
    private TextView tv_detail_title;
    private TextView tv_grade_and_version;
    private TextView tv_subject_count;
    private List<HashMap<String, Object>> mSingleClassList = new ArrayList();

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case DETAIL_URL:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        HashMap<String, Object> dataMap = (HashMap<String, Object>) resultMap.get("data");
                        GlideUtil.loadNetImageView(this,dataMap.get("img") + "",iv_detail);
                        tv_detail_title.setText(dataMap.get("title") + "");
                        tv_grade_and_version.setText(dataMap.get("grade") + "  " + dataMap.get("version"));
                        tv_subject_count.setText("此书总题量 " + dataMap.get("classNums") + " 道,   重点题 " + dataMap.get("zdClassNums") + " 道");
                        ArrayList<HashMap<String, Object>> singleClassList = (ArrayList<HashMap<String, Object>>) dataMap.get("singleClassList");
                        if (singleClassList != null && singleClassList.size() > 0) {
                            mSingleClassList.addAll(singleClassList);
                            exampleExplainSelectPageAdapter.notifyDataSetChanged();
                        }
                    } else {
                        ToastUtil.show(this, resultMap.get("status") + "", 0);
                    }
                } else {
                    ToastUtil.show(this, msg, 0);
                }
                break;
        }
    }

    @Override
    protected void handleIntent(Intent intent) {
        if (intent != null) {
            id = intent.getStringExtra("id");
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_example_explain_select_page);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tv_title.setText("请选择相应页码");
        PostList();
        initXRecyclerView();
    }

    //请求详情数据
    private void PostList() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("controller", "xtjj");
        map.put("action", "detail");
        map.put("id", id);
        getJsonUtil().PostJson(this, map);
    }

    private void initXRecyclerView() {
        GridLayoutManager manager = new GridLayoutManager(this, 5);
        xRecyclerView.setLayoutManager(manager);
        exampleExplainSelectPageAdapter = new ExampleExplainSelectPageAdapter(this, mSingleClassList);
        xRecyclerView.setAdapter(exampleExplainSelectPageAdapter);
        View headerView1 = LayoutInflater.from(this).inflate(R.layout.head_select_page, null);
        View headerView2 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        initHeaderView(headerView1);
        xRecyclerView.addHeaderView(headerView1);
        xRecyclerView.addHeaderView(headerView2);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(false);

        exampleExplainSelectPageAdapter.setOnItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                HashMap<String, Object> map = mSingleClassList.get(position);
                Intent intent = new Intent(ExampleExplainSelectPageActivity.this, ExampleExplainDetailActivity.class);
                intent.putExtra("name", tv_detail_title.getText().toString().trim()+tv_grade_and_version.getText().toString().trim());
                intent.putExtra("id", map.get("id") + "");
                startActivity(intent);
            }
        });
    }


    private void initHeaderView(View headerView1) {
        iv_detail = ((ImageView) headerView1.findViewById(R.id.iv_detail));
        tv_detail_title = ((TextView) headerView1.findViewById(R.id.tv_detail_title));
        tv_grade_and_version = ((TextView) headerView1.findViewById(R.id.tv_grade_and_version));
        tv_subject_count = ((TextView) headerView1.findViewById(R.id.tv_subject_count));
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
