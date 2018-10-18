package com.youyi.ywl.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.youyi.ywl.R;
import com.youyi.ywl.activity.OnlineHearingActivity;
import com.youyi.ywl.activity.OnlineHearingDetailActivity;
import com.youyi.ywl.adapter.OnlineHearingAdapter_WithTime;
import com.youyi.ywl.inter.RecyclerViewOnItemClickListener;
import com.youyi.ywl.other.Constanst;
import com.youyi.ywl.util.LearningPathDao2;
import com.youyi.ywl.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/9/11.
 * 学习轨迹  -- 在线听力fragment
 */

public class OnlineHearingFragment extends BaseFragment {
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;
    @BindView(R.id.ll_nothing_layout)
    LinearLayout ll_nothing_layout;

    private OnlineHearingAdapter_WithTime onlineHearingAdapter_withTime;
    private TextView tv_status;
    private List<HashMap<String, Object>> dataList = new ArrayList<>();

    private String DB_NAME = Constanst.userPhoneNum + "_online_hearing_learning_info.db";
    private String TABLE_NAME = "onlineHearingLearning";
    private LearningPathDao2 learningPathDao2;

    @Override
    public void onLazyLoad() {
        loadData();
    }

    public void loadData() {
        if (learningPathDao2 == null) {
            learningPathDao2 = new LearningPathDao2(getActivity(), TABLE_NAME, DB_NAME);
        }
        dataList.clear();
        List<HashMap<String, Object>> list = learningPathDao2.select();
        dataList.addAll(list);
        onlineHearingAdapter_withTime.notifyDataSetChanged();
        if (dataList == null || dataList.size() == 0) {
            if (xRecyclerView.getVisibility() == View.VISIBLE) {
                xRecyclerView.setVisibility(View.GONE);
            }
            if (ll_nothing_layout.getVisibility() == View.GONE) {
                ll_nothing_layout.setVisibility(View.VISIBLE);
            }
        } else {
            if (xRecyclerView.getVisibility() == View.GONE) {
                xRecyclerView.setVisibility(View.VISIBLE);
            }
            if (ll_nothing_layout.getVisibility() == View.VISIBLE) {
                ll_nothing_layout.setVisibility(View.GONE);
            }
        }
    }

    public void deleteData() {
        if (learningPathDao2 == null) {
            learningPathDao2 = new LearningPathDao2(getActivity(), TABLE_NAME, DB_NAME);
        }
        int lines = learningPathDao2.deleteAll();
        if (lines > 0) {
            ToastUtil.show(getActivity(), "删除成功", 0);
            loadData();
        }
    }

    @Override
    protected void Response(String code, String msg, String url, Object result) {
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_online_hearing;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        xRecyclerView.setLayoutManager(manager);
        onlineHearingAdapter_withTime = new OnlineHearingAdapter_WithTime(getActivity(), dataList);
        xRecyclerView.setAdapter(onlineHearingAdapter_withTime);
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

        onlineHearingAdapter_withTime.setOnItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                //跳转界面
                HashMap<String, Object> map = dataList.get(position);
                Intent intent = new Intent(getActivity(), OnlineHearingDetailActivity.class);
                intent.putExtra("url", map.get("url") + "");
                startActivity(intent);
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

    private boolean isGotoLearn;

    @OnClick({R.id.tv_goto_learn_now})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_goto_learn_now:
                //立即学习
                isGotoLearn = true;
                startActivity(new Intent(getActivity(), OnlineHearingActivity.class));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isGotoLearn) {
            loadData();
        }
    }
}
