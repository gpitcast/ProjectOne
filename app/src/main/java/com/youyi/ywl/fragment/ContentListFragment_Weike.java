package com.youyi.ywl.fragment;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.youyi.ywl.R;
import com.youyi.ywl.adapter.ContentListFragmentAdapter;
import com.youyi.ywl.other.Constanst;
import com.youyi.ywl.util.HttpUtils;
import com.youyi.ywl.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/8/13.
 * 精品微课详情 - 内容列表fragment
 */

@SuppressLint("ValidFragment")
public class ContentListFragment_Weike extends BaseFragment {
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;

    private static final String DATA_URL = HttpUtils.Host + "/weike/getVideoList";//内容列表接口
    private TextView tv_status;
    private ContentListFragmentAdapter contentListFragmentAdapter;
    private String id;
    private int pageno = 1;
    private List<HashMap<String,Object>> dataList = new ArrayList<>();//列表数据集合
    private boolean isLoadMore;

    public ContentListFragment_Weike(String id) {
        this.id = id;
    }

    @Override
    public void onLazyLoad() {
        PostList();
    }

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
                        if (isLoadMore){
                            xRecyclerView.loadMoreComplete();
                            isLoadMore = false;
                            dataList.addAll(arrayList);
                            tv_status.setText("加载完毕");
                        }else {
                            dataList.clear();
                            dataList.addAll(arrayList);
                        }
                        contentListFragmentAdapter.notifyDataSetChanged();

                    } else {
                        //其他代表不正常
                        if (isLoadMore) {
                            isLoadMore = false;
                            pageno--;
                            xRecyclerView.loadMoreComplete();
                            tv_status.setText(resultMap.get("msg") + "");
                            xRecyclerView.setNoMore(true);
                        } else {
                            dataList.clear();
                            ToastUtil.show(getActivity(), resultMap.get("msg") + "", 0);
                            contentListFragmentAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    if (isLoadMore) {
                        isLoadMore = false;
                        pageno--;
                        xRecyclerView.loadMoreComplete();
                        tv_status.setText(msg);
                    } else {
                        ToastUtil.show(getActivity(), msg, 0);
                        contentListFragmentAdapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_content_list;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        initRecyclerView();
    }

    //请求内容列表数据
    private void PostList() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("controller", "weike");
        map.put("action", "getVideoList");
        map.put("id", id);
        map.put("page", pageno);
        getJsonUtil().PostJson(getActivity(), map);
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        xRecyclerView.setLayoutManager(manager);
        contentListFragmentAdapter = new ContentListFragmentAdapter(getActivity(),dataList);
        xRecyclerView.setAdapter(contentListFragmentAdapter);
        View headView1 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty_head, null);
        View headView2 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty_head, null);
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
                isLoadMore = true;
                pageno++;
                PostList();
            }
        });

        //自定义加载footer
        View footerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_recyclerview_footer, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        footerView.setLayoutParams(layoutParams);
        xRecyclerView.setFootView(footerView);
        tv_status = (TextView) footerView.findViewById(R.id.tv_status);
        tv_status.setTextColor(getResources().getColor(R.color.grayone));
    }
}
