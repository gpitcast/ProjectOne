package com.feature.projectone.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.CheckLoginUtil;
import com.feature.projectone.util.ShareUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/6/6.
 * 进入APP引导页
 */

public class ChannelActivity extends BaseActivity {
    @BindView(R.id.tv_jump)
    TextView tv_jump;

    private int totalSeconds = 5;
    private int currentSeconds;

    @Override
    protected void Response(String code, String msg, String url, Object result) {
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            currentSeconds += 1;
            tv_jump.setText("倒计时 : " + currentSeconds + "秒");
            if (currentSeconds < totalSeconds) {
                mHandler.postDelayed(this, 1000);
            } else {
                mHandler.removeCallbacks(this);
                String string = ShareUtil.getString(ChannelActivity.this, Constanst.UER_TOKEN);
                if (CheckLoginUtil.isLogin(ChannelActivity.this)) {
                    startActivity(new Intent(ChannelActivity.this, MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(ChannelActivity.this, LoginAndRegistActivity.class));
                    finish();
                }
            }
        }
    };

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_channel);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        mHandler.postDelayed(mRunnable, 1000);
    }

    @Override
    public void afterInitView() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
    }

    @OnClick({R.id.tv_jump})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_jump:
                mHandler.removeCallbacks(mRunnable);
                String string = ShareUtil.getString(this, Constanst.UER_TOKEN);
                Log.i("CheckLoginUtil", "      tv_jump的token:    " + string);
                if (CheckLoginUtil.isLogin(ChannelActivity.this)) {
                    startActivity(new Intent(ChannelActivity.this, MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(ChannelActivity.this, LoginAndRegistActivity.class));
                    finish();
                }
                break;
        }
    }
}
