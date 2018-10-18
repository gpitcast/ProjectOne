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
import com.youyi.ywl.adapter.OnliveClassFragmentAdapter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/9/11.
 * 学习轨迹  -- 直播课fragment
 */

public class OnliveClassFragment extends BaseFragment {
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;
    @BindView(R.id.tv_onlive_reservation)
    TextView tv_onlive_reservation;
    @BindView(R.id.tv_onlive_course)
    TextView tv_onlive_course;

    private OnliveClassFragmentAdapter onliveClassFragmentAdapter;
    private TextView tv_status;

    @Override
    public void onLazyLoad() {
    }

    @Override
    protected void Response(String code, String msg, String url, Object result) {
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_onlive_class;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        xRecyclerView.setLayoutManager(manager);
        onliveClassFragmentAdapter = new OnliveClassFragmentAdapter(getActivity());
        xRecyclerView.setAdapter(onliveClassFragmentAdapter);
        View headView1 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty_head, null);
        View headView2 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty_head, null);
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
            }
        });

        View footerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_recyclerview_footer, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        footerView.setLayoutParams(layoutParams);
        tv_status = ((TextView) footerView.findViewById(R.id.tv_status));
        xRecyclerView.setFootView(footerView);
        xRecyclerView.loadMoreComplete();
        tv_status.setText("仅保存两个月的浏览记录");
    }

    private int selectedIndex = 0;//选中类型的角标,默认选中第一个精品微课

    @OnClick({R.id.tv_onlive_reservation, R.id.tv_onlive_course})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_onlive_reservation:
                //直播预约
                if (selectedIndex != 0) {
                    tv_onlive_reservation.setTextColor(getContext().getResources().getColor(R.color.orangeone));
                    tv_onlive_course.setTextColor(getContext().getResources().getColor(R.color.normal_black));
                    selectedIndex = 0;
                }
                break;
            case R.id.tv_onlive_course:
                //直播课程
                if (selectedIndex != 1) {
                    tv_onlive_reservation.setTextColor(getContext().getResources().getColor(R.color.normal_black));
                    tv_onlive_course.setTextColor(getContext().getResources().getColor(R.color.orangeone));
                    selectedIndex = 1;
                }
                break;
        }
    }
}
