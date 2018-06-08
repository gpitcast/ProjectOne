package com.feature.projectone.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.feature.projectone.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/18.
 * 常见问题回答界面
 */

public class CommonProblemAnswerActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_question)
    TextView tv_question;
    @BindView(R.id.tv_answer)
    TextView tv_answer;

    private String content;
    private String title;

    @Override
    protected void Response(String code, String msg, String url, Object result) {

    }

    @Override
    protected void handleIntent(Intent intent) {
        if (intent != null) {
            title = intent.getStringExtra("title");
            content = intent.getStringExtra("content");
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_common_problem_answer);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tv_title.setText("常见问题");
        tv_question.setText(title);
        tv_answer.setText(content);
    }

    @Override
    public void afterInitView() {
    }

    @OnClick({R.id.ll_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }
}
