package com.youyi.YWL.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;

import com.youyi.YWL.R;
import com.youyi.YWL.adapter.ExcellentCourseSearchGridAdapter;
import com.youyi.YWL.adapter.ExcellentCourseSearchRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/7/16.
 * 精品课程-搜索界面  的 关于搜索(热门搜索,历史搜索)界面
 */

public class ExcellentCourseSearchAboutFragment extends BaseFragment {
    @BindView(R.id.gridView)
    GridView gridView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private ExcellentCourseSearchGridAdapter excellentCourseSearchGridAdapter;
    private List<String> gridList;
    private List<String> recyclerList;
    private ExcellentCourseSearchRecyclerAdapter excellentCourseSearchRecyclerAdapter;

    @Override
    protected void Response(String code, String msg, String url, Object result) {

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
        return R.layout.fragment_excellent_course_search_about;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        initData();
        initGridView();
        initRecyclerView();
    }

    private void initData() {
        gridList = new ArrayList<>();
        gridList.add("数学");
        gridList.add("语文");
        gridList.add("期末考试");
        gridList.add("小学作文");
        gridList.add("期中考试");
        gridList.add("初中物理");
        gridList.add("升学考试");
        gridList.add("名牌中学");

        recyclerList = new ArrayList<>();
        recyclerList.add("初中语文");
        recyclerList.add("初中语文");
        recyclerList.add("初中语文");
    }

    private void initGridView() {
        excellentCourseSearchGridAdapter = new ExcellentCourseSearchGridAdapter(getActivity(), gridList);
        gridView.setAdapter(excellentCourseSearchGridAdapter);
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        excellentCourseSearchRecyclerAdapter = new ExcellentCourseSearchRecyclerAdapter(getActivity(), recyclerList);
        recyclerView.setAdapter(excellentCourseSearchRecyclerAdapter);
    }

}
