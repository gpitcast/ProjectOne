package com.youyi.YWL.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.youyi.YWL.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/6/28.
 * 关于我们界面
 */

public class AboutUsActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_version)
    TextView tv_version;

    @Override
    protected void Response(String code, String msg, String url, Object result) {
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_about_us);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        tv_title.setText("关于我们");
        setVersionCode();
    }

    //获取并设置版本号
    private void setVersionCode() {
        try {
            PackageManager pm = getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            String versionName = packageInfo.versionName;
            tv_version.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterInitView() {

    }

    @OnClick({R.id.ll_back, R.id.rl_contact_us})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.rl_contact_us:
                //联系我们
                startActivity(new Intent(this, ContactUsActivity.class));
                break;
        }
    }
}
