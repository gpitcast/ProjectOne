package com.feature.projectone.RongIM;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.activity.GroupInformationActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;

/**
 * Created by Administrator on 2018/5/4.
 * 融云会话界面
 */

public class ConversationActivity extends FragmentActivity implements View.OnClickListener {

    private String title;
    private String targetId;
    private String status;
    private LinearLayout ll_group_users;
    private LinearLayout ll_back;
    private String id;
    private TextView tv_title;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);

        title = getIntent().getData().getQueryParameter("title");
        targetId = getIntent().getData().getQueryParameter("targetId");
        status = getIntent().getData().getLastPathSegment();//获取当前会话类型
        Log.i("ConversationActivity", "          title:         " + title);
        Log.i("ConversationActivity", "          targetId:         " + targetId);
        Log.i("ConversationActivity", "          status:         " + status);


        initViews();
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.orangeone));

        RongIM.getInstance().setMessageAttachedUserInfo(true);
    }

    private void initViews() {
        EventBus.getDefault().register(this);

        ll_back = ((LinearLayout) findViewById(R.id.ll_back));
        ll_back.setOnClickListener(this);

        tv_title = ((TextView) findViewById(R.id.tv_title));
        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }

        ll_group_users = ((LinearLayout) findViewById(R.id.ll_group_users));
        //群组会话界面
        if (!TextUtils.isEmpty(status) && "group".equals(status)) {
            //群会话界面,显示标题栏右边的群信息图标
            ll_group_users.setVisibility(View.VISIBLE);
            ll_group_users.setOnClickListener(this);

            //请求群组详细信息的id参数是从融云的群组的ID-->targetId中截取"-"后面的
            if (targetId != null) {
                id = targetId.substring(targetId.lastIndexOf("-") + 1);
            }
        } else {
            //非群会话界面,隐藏标题栏右边的群信息图标
            ll_group_users.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_group_users:
                if (!TextUtils.isEmpty(id)) {
                    Intent intent = new Intent(this, GroupInformationActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("targetId", targetId);
                    startActivity(intent);
                }
                break;
        }
    }

    @Subscribe
    public void onEventMainThread(String str) {
        if (str == null){
            return;
        }
        switch (str) {
            case "关闭ConversationActivity":
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
