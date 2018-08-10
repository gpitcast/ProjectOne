package com.youyi.YWL.activity;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.youyi.YWL.R;
import com.youyi.YWL.adapter.ExcellentCourseSearchGridAdapter;
import com.youyi.YWL.fragment.ExcellentCourseSearchAboutFragment;
import com.youyi.YWL.util.SearchHistoryDao;
import com.youyi.YWL.util.SoftUtil;
import com.youyi.YWL.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/13.
 * 精品课程-搜索界面
 */

public class ExcellentCourseSearchActivity extends BaseActivity {
    @BindView(R.id.et_search)
    EditText et_search;

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

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE
                        || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (et_search.getText().toString().trim() != null && et_search.getText().toString().trim().length() > 0) {
                        SoftUtil.hideSoft(ExcellentCourseSearchActivity.this);//隐藏键盘
                        startActivity(new Intent(ExcellentCourseSearchActivity.this, ExcellentCourseActivity2.class));
                    } else {
                        ToastUtil.show(ExcellentCourseSearchActivity.this, "搜索关键字不能为空", 0);
                    }
                    return true;
                }
                return false;
            }
        });

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
