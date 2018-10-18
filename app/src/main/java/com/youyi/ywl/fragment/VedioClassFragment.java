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
import com.youyi.ywl.activity.ExcellentWeikeActivity;
import com.youyi.ywl.activity.ExcellentWeikeDetailActivity;
import com.youyi.ywl.adapter.VedioClassFragmentAdapter;
import com.youyi.ywl.inter.RecyclerViewOnItemClickListener;
import com.youyi.ywl.other.Constanst;
import com.youyi.ywl.util.LearningPathDao3;
import com.youyi.ywl.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/9/11.
 * 学习轨迹  -- 视频课fragment
 */

public class VedioClassFragment extends BaseFragment {
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;
    @BindView(R.id.tv_excellent_weike)
    TextView tv_excellent_weike;
    @BindView(R.id.tv_excellent_course)
    TextView tv_excellent_course;
    @BindView(R.id.ll_nothing_layout)
    LinearLayout ll_nothing_layout;

    private VedioClassFragmentAdapter vedioClassFragmentAdapter;
    private TextView tv_status;
    private List<HashMap<String, Object>> dataList = new ArrayList<>();
    private String DB_NAME = Constanst.userPhoneNum + "_excellent_weike_learning_info.db";
    private String TABLE_NAME = "excellentWeikeLearning";
    private LearningPathDao3 learningPathDao3;

    @Override
    public void onLazyLoad() {
        loadData();
    }

    public void loadData() {
        dataList.clear();
        if (learningPathDao3 == null) {
            learningPathDao3 = new LearningPathDao3(getActivity(), TABLE_NAME, DB_NAME);
        }
        List<HashMap<String, Object>> list = learningPathDao3.select();
        dataList.addAll(list);
        vedioClassFragmentAdapter.notifyDataSetChanged();
        if (dataList == null || dataList.size() == 0) {
            xRecyclerView.setVisibility(View.GONE);
            ll_nothing_layout.setVisibility(View.VISIBLE);
        } else {
            xRecyclerView.setVisibility(View.VISIBLE);
            ll_nothing_layout.setVisibility(View.GONE);
        }
    }

    public void deleteData() {
        if (learningPathDao3 == null) {
            learningPathDao3 = new LearningPathDao3(getActivity(), TABLE_NAME, DB_NAME);
        }
        int lines = learningPathDao3.deleteAll();
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
        return R.layout.fragment_vedio_class;
    }

    @Override
    protected void initViewsAndEvents(View view) {

        initRecyclerView();

    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        xRecyclerView.setLayoutManager(manager);
        vedioClassFragmentAdapter = new VedioClassFragmentAdapter(getActivity(), dataList);
        xRecyclerView.setAdapter(vedioClassFragmentAdapter);
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

        vedioClassFragmentAdapter.setOnItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ExcellentWeikeDetailActivity.class);
                HashMap<String, Object> map = dataList.get(position);
                intent.putExtra("id", map.get("id") + "");
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

    private int selectedIndex = 0;//选中类型的角标,默认选中第一个精品微课
    private boolean isGotoLearn = false;

    @OnClick({R.id.tv_excellent_weike, R.id.tv_excellent_course, R.id.tv_goto_learn_now})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_excellent_weike:
                //精品微课按钮
                if (selectedIndex != 0) {
                    tv_excellent_weike.setTextColor(getContext().getResources().getColor(R.color.orangeone));
                    tv_excellent_course.setTextColor(getContext().getResources().getColor(R.color.normal_black));
                    selectedIndex = 0;
                }
                break;
            case R.id.tv_excellent_course:
                //精品课程按钮
                if (selectedIndex != 1) {
                    tv_excellent_weike.setTextColor(getContext().getResources().getColor(R.color.normal_black));
                    tv_excellent_course.setTextColor(getContext().getResources().getColor(R.color.orangeone));
                    selectedIndex = 1;
                }
                break;
            case R.id.tv_goto_learn_now:
                //立即学习
                isGotoLearn = true;
                startActivity(new Intent(getActivity(), ExcellentWeikeActivity.class));
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
