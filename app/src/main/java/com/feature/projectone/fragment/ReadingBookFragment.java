package com.feature.projectone.fragment;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.feature.projectone.R;
import com.feature.projectone.adapter.SourceDownloadFragmentAdapter;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.HttpUtils;
import com.feature.projectone.util.ToastUtil;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/13.
 * 教材朗读fragment
 */

public class ReadingBookFragment extends BaseFragment {

    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;

    private String cate_id;
    private int pageno = 1;//分页
    private boolean isLoadMore;
    private boolean isRefresh;
    private static final String sourceInexUrl = HttpUtils.Host + "/source/index";//下载列表数据接口
    private List<HashMap<String, Object>> mDataList = new ArrayList<>();//存储列表数据的集合
    private SourceDownloadFragmentAdapter sourceDownloadFragmentAdapter;

    public ReadingBookFragment() {
    }

    @SuppressLint("ValidFragment")
    public ReadingBookFragment(String cate_id) {
        this.cate_id = cate_id;
    }

    //提供方法给附着的activity筛选功能刷新数据
    public void shaiXuanRefresh(String cate_id) {
        this.cate_id = cate_id;
        xRecyclerView.refresh();
    }

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case sourceInexUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap<String, Object> resultMap = (HashMap<String, Object>) result;
                    String status = resultMap.get("status") + "";
                    ArrayList<HashMap<String, Object>> dataList = (ArrayList<HashMap<String, Object>>) resultMap.get("data");

                    if ("0".equals(status)) {
                        //status=0代表有数据
                        if (dataList != null && dataList.size() > 0) {
                            //代表dataList数据集合有数据
                            if (isLoadMore) {
                                xRecyclerView.loadMoreComplete();
                                isLoadMore = false;
                                mDataList.addAll(dataList);
                            } else if (isRefresh) {
                                xRecyclerView.refreshComplete();
                                isRefresh = false;
                                mDataList.clear();
                                mDataList.addAll(dataList);
                            } else {
                                mDataList.addAll(dataList);
                            }
                            sourceDownloadFragmentAdapter.notifyDataSetChanged();
                        } else {
                            //代表dataList数据集合为空
                            if (isLoadMore) {
                                isLoadMore = false;
                                pageno--;
                                xRecyclerView.loadMoreComplete();
                            } else if (isRefresh) {
                                isRefresh = false;
                                mDataList.clear();
                                xRecyclerView.refreshComplete();
                            }
                            sourceDownloadFragmentAdapter.notifyDataSetChanged();
                            ToastUtil.show(getActivity(), msg, 0);
                        }
                    } else {
//                        //status不等于0代表暂无数据，取消下拉或者上拉状态，弹吐司提醒用户
                        if (isLoadMore) {
                            isLoadMore = false;
                            pageno--;
                            xRecyclerView.loadMoreComplete();
                        } else if (isRefresh) {
                            mDataList.clear();
                            isRefresh = false;
                            xRecyclerView.refreshComplete();
                        }
                        sourceDownloadFragmentAdapter.notifyDataSetChanged();
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
                        sourceDownloadFragmentAdapter.notifyDataSetChanged();
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
        return R.layout.fragment_source_download;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        initRecyclerView();
    }

    private void initRecyclerView() {
        final LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        xRecyclerView.setLayoutManager(manager);
        sourceDownloadFragmentAdapter = new SourceDownloadFragmentAdapter(getActivity(), mDataList);
        xRecyclerView.setAdapter(sourceDownloadFragmentAdapter);
        View headView1 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty_head, null);
        View headView2 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty_head, null);
        xRecyclerView.addHeaderView(headView1);
        xRecyclerView.addHeaderView(headView2);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        //刷新和加载监听
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
    }

    //请求列表数据接口
    public void PostList() {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("controller", "source");
        map.put("action", "index");
        map.put("page", pageno + "");
        map.put("cate_id", cate_id);
        getJsonUtil().PostJson(getActivity(), map);
    }
}
