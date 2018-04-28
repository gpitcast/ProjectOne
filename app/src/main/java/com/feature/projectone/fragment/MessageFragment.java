package com.feature.projectone.fragment;

import android.view.View;
import android.widget.TextView;

import com.feature.projectone.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/19.
 * 主界面 消息fragment
 */

public class MessageFragment extends BaseFragment {
    @BindView(R.id.tv_title)
    TextView tv_title;

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
        return R.layout.fragment_message;
    }

    @Override
    protected void initViewsAndEvents(View view) {

    }

    @OnClick({R.id.tv_title})
    public void OnClick(View view) {
        tv_title.setText("改变了的消息fragment");
    }
}
