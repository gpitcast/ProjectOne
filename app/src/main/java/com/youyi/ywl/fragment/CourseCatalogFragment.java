package com.youyi.ywl.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.youyi.ywl.R;
import com.youyi.ywl.adapter.CourseCatalogFragmentAdapter;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/7/16.
 * 精品课程-详情  课程目录fragment
 */

public class CourseCatalogFragment extends BaseFragment {
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;

    private TextView tv_status;
    private CourseCatalogFragmentAdapter courseCatalogFragmentAdapter;

    @Override
    public void onLazyLoad() {
    }

    @Override
    protected void Response(String code, String msg, String url, Object result) {
    }


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_course_catalog;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        xRecyclerView.setLayoutManager(manager);
        courseCatalogFragmentAdapter = new CourseCatalogFragmentAdapter(getActivity());
        xRecyclerView.setAdapter(courseCatalogFragmentAdapter);
        View headView1 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_course_catalog_head, null);
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
            }
        });

        //自定义加载footer
        View footerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_recyclerview_footer, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        footerView.setLayoutParams(layoutParams);
        xRecyclerView.setFootView(footerView);
        tv_status = (TextView) footerView.findViewById(R.id.tv_status);
        tv_status.setText("没有更多数据");
        tv_status.setTextColor(getResources().getColor(R.color.grayone));
    }
}
