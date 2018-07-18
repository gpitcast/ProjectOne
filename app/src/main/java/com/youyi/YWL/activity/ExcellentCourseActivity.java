package com.youyi.YWL.activity;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.youyi.YWL.R;
import com.youyi.YWL.adapter.ExcellentCourseAdapter;
import com.youyi.YWL.adapter.ExcellentCourseSubjectAdapter;
import com.youyi.YWL.fragment.SubjectBiologyFragment;
import com.youyi.YWL.fragment.SubjectChineseFragment;
import com.youyi.YWL.fragment.SubjectEnglishFragment;
import com.youyi.YWL.fragment.SubjectGeographyFragment;
import com.youyi.YWL.fragment.SubjectHistoryFragment;
import com.youyi.YWL.fragment.SubjectMathFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/13.
 * 精品课程界面
 */

public class ExcellentCourseActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.gridView_subject)
    GridView gridView_subject;
    @BindView(R.id.framelayout)
    FrameLayout framelayout;
    @BindView(R.id.img_right_search)
    ImageView img_right_search;

    private List<String> recyclerList;
    private List<HashMap<String, Object>> subjectList;
    private ExcellentCourseAdapter excellentCourseAdapter;
    private ExcellentCourseSubjectAdapter excellentCourseSubjectAdapter;
    private SubjectChineseFragment subjectChineseFragment;
    private SubjectMathFragment subjectMathFragment;
    private SubjectEnglishFragment subjectEnglishFragment;
    private SubjectHistoryFragment subjectHistoryFragment;
    private SubjectGeographyFragment subjectGeographyFragment;
    private SubjectBiologyFragment subjectBiologyFragment;

    @Override
    protected void Response(String code, String msg, String url, Object result) {

    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_excellent_course);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tv_title.setText("精品课程");
        img_right_search.setVisibility(View.VISIBLE);
        initData();
        initRecyclerView();
        initSubjectRecyclerView();
        initFrameLayout();
    }

    private void initFrameLayout() {
        showChineseFragment();
    }

    //显示语文fragment
    private void showChineseFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (subjectChineseFragment == null) {
            subjectChineseFragment = new SubjectChineseFragment();
            transaction.add(R.id.framelayout, subjectChineseFragment);
        }
        hideFragment(transaction);
        transaction.show(subjectChineseFragment);
        transaction.commit();
    }

    //显示数学fragment
    public void showMathFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (subjectMathFragment == null) {
            subjectMathFragment = new SubjectMathFragment();
            transaction.add(R.id.framelayout, subjectMathFragment);
        }
        hideFragment(transaction);
        transaction.show(subjectMathFragment);
        transaction.commit();
    }

    //显示英语fragment
    public void showEnglishFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (subjectEnglishFragment == null) {
            subjectEnglishFragment = new SubjectEnglishFragment();
            transaction.add(R.id.framelayout, subjectEnglishFragment);
        }
        hideFragment(transaction);
        transaction.show(subjectEnglishFragment);
        transaction.commit();
    }

    //显示历史fragment
    public void showHistoryFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (subjectHistoryFragment == null) {
            subjectHistoryFragment = new SubjectHistoryFragment();
            transaction.add(R.id.framelayout, subjectHistoryFragment);
        }
        hideFragment(transaction);
        transaction.show(subjectHistoryFragment);
        transaction.commit();
    }

    //显示地理fragment
    public void showGeographyFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (subjectGeographyFragment == null) {
            subjectGeographyFragment = new SubjectGeographyFragment();
            transaction.add(R.id.framelayout, subjectGeographyFragment);
        }
        hideFragment(transaction);
        transaction.show(subjectGeographyFragment);
        transaction.commit();
    }

    //显示生物fragment
    public void showBiologyFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (subjectBiologyFragment == null) {
            subjectBiologyFragment = new SubjectBiologyFragment();
            transaction.add(R.id.framelayout, subjectBiologyFragment);
        }
        hideFragment(transaction);
        transaction.show(subjectBiologyFragment);
        transaction.commit();
    }

    //隐藏所有的fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (subjectChineseFragment != null) {
            transaction.hide(subjectChineseFragment);
        }

        if (subjectMathFragment != null) {
            transaction.hide(subjectMathFragment);
        }

        if (subjectEnglishFragment != null) {
            transaction.hide(subjectEnglishFragment);
        }

        if (subjectHistoryFragment != null) {
            transaction.hide(subjectHistoryFragment);
        }

        if (subjectGeographyFragment != null) {
            transaction.hide(subjectGeographyFragment);
        }

        if (subjectBiologyFragment != null) {
            transaction.hide(subjectBiologyFragment);
        }
    }

    private void initSubjectRecyclerView() {
        excellentCourseSubjectAdapter = new ExcellentCourseSubjectAdapter(this, subjectList);
        gridView_subject.setAdapter(excellentCourseSubjectAdapter);
        gridView_subject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> map = subjectList.get(position);
                Boolean isChecked = (Boolean) map.get("isChecked");
                if (!isChecked) {
                    //修改当前选中的未ture
                    map.put("isChecked", true);
                    subjectList.set(position, map);
                    //修改其他的未false
                    for (int i = 0; i < subjectList.size(); i++) {
                        if (position != i) {
                            HashMap<String, Object> map1 = subjectList.get(i);
                            map1.put("isChecked", false);
                            subjectList.set(i, map1);
                        }
                    }

                    Log.i("gridView_subject", "        " + subjectList.toString());
                    excellentCourseSubjectAdapter.notifyDataSetChanged();

                    switch (position) {
                        case 0:
                            showChineseFragment();
                            break;
                        case 1:
                            showMathFragment();
                            break;
                        case 2:
                            showEnglishFragment();
                            break;
                        case 3:
                            showHistoryFragment();
                            break;
                        case 4:
                            showGeographyFragment();
                            break;
                        case 5:
                            showBiologyFragment();
                            break;
                    }
                }
            }
        });
    }

    private void initData() {
        recyclerList = new ArrayList<>();
        recyclerList.add("全部");
        recyclerList.add("小学");
        recyclerList.add("初中");

        subjectList = new ArrayList<>();
        HashMap<String, Object> map0 = new HashMap<>();
        map0.put("name", "语文");
        map0.put("isChecked", true);
        subjectList.add(map0);

        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("name", "数学");
        map1.put("isChecked", false);
        subjectList.add(map1);

        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("name", "英语");
        map2.put("isChecked", false);
        subjectList.add(map2);

        HashMap<String, Object> map3 = new HashMap<>();
        map3.put("name", "历史");
        map3.put("isChecked", false);
        subjectList.add(map3);

        HashMap<String, Object> map4 = new HashMap<>();
        map4.put("name", "地理");
        map4.put("isChecked", false);
        subjectList.add(map4);

        HashMap<String, Object> map5 = new HashMap<>();
        map5.put("name", "生物");
        map5.put("isChecked", false);
        subjectList.add(map5);
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(manager);
        excellentCourseAdapter = new ExcellentCourseAdapter(this, recyclerList);
        recyclerView.setAdapter(excellentCourseAdapter);

    }

    @Override
    public void afterInitView() {
    }

    @OnClick({R.id.ll_back, R.id.img_right_search})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.img_right_search:
                startActivity(new Intent(this, ExcellentCourseActivity2.class));
                break;
        }
    }
}
