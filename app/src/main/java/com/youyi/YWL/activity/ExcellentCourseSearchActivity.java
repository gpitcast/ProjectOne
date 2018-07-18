package com.youyi.YWL.activity;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.youyi.YWL.R;
import com.youyi.YWL.adapter.ExcellentCourseSearchGridAdapter;
import com.youyi.YWL.fragment.ExcellentCourseSearchAboutFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/13.
 * 精品课程-搜索界面
 */

public class ExcellentCourseSearchActivity extends BaseActivity {

    private ExcellentCourseSearchAboutFragment excellentCourseSearchAboutFragment;

    @Override
    protected void Response(String code, String msg, String url, Object result) {
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_excellent_course_search);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        showSearchFragment();
    }

    public void showSearchFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (excellentCourseSearchAboutFragment == null) {
            excellentCourseSearchAboutFragment = new ExcellentCourseSearchAboutFragment();
            transaction.add(R.id.framelayout, excellentCourseSearchAboutFragment);
        }
        hideFragment(transaction);
        transaction.show(excellentCourseSearchAboutFragment);
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (excellentCourseSearchAboutFragment != null) {
            transaction.hide(excellentCourseSearchAboutFragment);
        }
    }


    @Override
    public void afterInitView() {
    }

    @OnClick({R.id.iv_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
