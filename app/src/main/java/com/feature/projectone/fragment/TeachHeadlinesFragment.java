package com.feature.projectone.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.feature.projectone.R;
import com.feature.projectone.activity.NewsHeadlinesDetailsActivity;
import com.feature.projectone.adapter.NewsHeadlinesAdapter;
import com.feature.projectone.inter.MyPassViewListener;
import com.feature.projectone.inter.RecyclerViewOnItemClickListener;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.HttpUtils;
import com.feature.projectone.util.ShareUtil;
import com.feature.projectone.util.ToastUtil;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2018/4/10.
 * <p>
 * 教育头条fragment
 */

public class TeachHeadlinesFragment extends BaseFragment {
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;
    private int pageno = 1;//分页
    private boolean isLoadMore;
    private boolean isRefresh;

    private NewsHeadlinesAdapter newsHeadlinesAdapter;
    private HashMap<String, Object> typeMap;//用来区分板块的map
    private static final String newsIndexUrl = HttpUtils.Host + "/news/index";//新闻列表接口

    private List<HashMap<String, Object>> mDataList = new ArrayList<>();//存储列表数据集合

    public TeachHeadlinesFragment() {
    }

    @SuppressLint("ValidFragment")
    public TeachHeadlinesFragment(HashMap<String, Object> map) {
        this.typeMap = map;

    }

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case newsIndexUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap<String, Object> resultMap = (HashMap<String, Object>) result;
                    ArrayList<HashMap<String, Object>> dataList = (ArrayList<HashMap<String, Object>>) resultMap.get("data");
                    if (dataList != null && dataList.size() > 0) {
                        if (isLoadMore) {
                            isLoadMore = false;
                            xRecyclerView.loadMoreComplete();
                            mDataList.addAll(dataList);
                        } else if (isRefresh) {
                            isRefresh = false;
                            xRecyclerView.refreshComplete();
                            mDataList.clear();
                            mDataList.addAll(dataList);
                        } else {
                            mDataList.addAll(dataList);
                        }
                        newsHeadlinesAdapter.notifyDataSetChanged();
                    } else {
                        if (isLoadMore) {
                            isLoadMore = false;
                            pageno--;
                            xRecyclerView.loadMoreComplete();
                        } else if (isRefresh) {
                            isRefresh = false;
                            mDataList.clear();
                            xRecyclerView.refreshComplete();
                        }
                        newsHeadlinesAdapter.notifyDataSetChanged();
                        ToastUtil.show(getActivity(), resultMap.get("msg") + "", 0);
                    }
                } else {
                    if (isLoadMore) {
                        isLoadMore = false;
                        pageno--;
                        xRecyclerView.loadMoreComplete();
                    } else if (isRefresh) {
                        isRefresh = false;
                        mDataList.clear();
                        xRecyclerView.refreshComplete();
                        newsHeadlinesAdapter.notifyDataSetChanged();
                    }
                    ToastUtil.show(getActivity(), msg, 0);
                }
                break;
        }
    }

    @Override
    protected void onFirstUserVisible() {
    }

    @Override
    protected void onUserVisible() {
    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_headlines;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        xRecyclerView.setLayoutManager(manager);
        newsHeadlinesAdapter = new NewsHeadlinesAdapter(getActivity(), mDataList,"",false);
        xRecyclerView.setAdapter(newsHeadlinesAdapter);
        View headView1 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty_head, null);
        View headView2 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty_head, null);
        xRecyclerView.addHeaderView(headView1);
        xRecyclerView.addHeaderView(headView2);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        //条目点击事件
        newsHeadlinesAdapter.setOnItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), NewsHeadlinesDetailsActivity.class);
                intent.putExtra("id", ((HashMap<String, Object>) mDataList.get(position)).get("id") + "");
                startActivity(intent);
            }
        });
        //下拉加载监听
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                pageno = 1;
                PostList();

            }

            @Override
            public void onLoadMore() {
                isLoadMore = true;
                pageno++;
                PostList();
            }
        });
        xRecyclerView.refresh();
        View footerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_recyclerview_footer, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
        layoutParams.gravity = Gravity.CENTER;
        footerView.setLayoutParams(layoutParams);
        footerView.setBackgroundColor(getResources().getColor(R.color.white));
        xRecyclerView.setFootView(footerView);
        xRecyclerView.loadMoreComplete();
    }

    //普通列表请求数据
    private void PostList() {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("controller", "news");
        map.put("action", "index");
        map.put("page", pageno + "");
        map.put("type", typeMap.get("type") + "");
        map.put("keywords", null);
        getJsonUtil().PostJson(getActivity(), map);
    }
}
