package com.feature.projectone.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/20.
 * 资讯头条搜索的 学习咨询fragment
 */

public class StudyConsultSearchFragment extends BaseFragment {

    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;

    private int pageno = 1;//分页
    private boolean isLoadMore;
    private boolean isRefresh;
    private NewsHeadlinesAdapter newsHeadlinesAdapter;
    private HashMap<String, Object> typeMap;
    private static final String SearchUrl = HttpUtils.Host + "/news/search";//新闻搜索列表接口
    private List<HashMap<String, Object>> mDataList = new ArrayList<>();//存储列表数据集合
    private EditText etSearch;
    private String searchKey;

    public StudyConsultSearchFragment() {
    }

    @SuppressLint("ValidFragment")
    public StudyConsultSearchFragment(HashMap<String, Object> map) {
        this.typeMap = map;
    }

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case SearchUrl:
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
        mDataList.clear();
        PostSearchList();

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        xRecyclerView.setLayoutManager(manager);
        newsHeadlinesAdapter = new NewsHeadlinesAdapter(getActivity(), mDataList, searchKey, true);

        xRecyclerView.setAdapter(newsHeadlinesAdapter);
        View headView1 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty_head, null);
        View headView2 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty_head, null);
        xRecyclerView.addHeaderView(headView1);
        xRecyclerView.addHeaderView(headView2);
        xRecyclerView.setPullRefreshEnabled(false);
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
                PostSearchList();
            }

            @Override
            public void onLoadMore() {
                isLoadMore = true;
                pageno++;
                PostSearchList();
            }
        });
        View footerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_recyclerview_footer, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
        layoutParams.gravity = Gravity.CENTER;
        footerView.setLayoutParams(layoutParams);
        footerView.setBackgroundColor(getResources().getColor(R.color.white));
        xRecyclerView.setFootView(footerView);
        xRecyclerView.loadMoreComplete();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        etSearch = ((MyPassViewListener) context).getEtSearch();
    }

    //搜索列表请求数据
    public void PostSearchList() {
        searchKey = etSearch.getText().toString().trim();
        if (searchKey == null || searchKey.length() == 0) {
            ToastUtil.show(getActivity(), "搜索关键字不能为空", 0);
            return;
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("controller", "news");
        map.put("action", "search");
        map.put("page", pageno);
        map.put("keywords", searchKey);
        map.put("tags", ShareUtil.getString(getActivity(), Constanst.TAGS));
        map.put("type", typeMap.get("type") + "");
        getJsonUtil().PostJson(getActivity(), map);
    }
}
