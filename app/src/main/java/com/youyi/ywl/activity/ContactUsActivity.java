package com.youyi.ywl.activity;

import android.view.View;
import android.widget.TextView;

import com.youyi.ywl.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/6/28.
 * 设置-关于我们-联系我们界面
 */

public class ContactUsActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @Override
    protected void Response(String code, String msg, String url, Object result) {

    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_contact_us);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        tv_title.setText("联系我们");
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
