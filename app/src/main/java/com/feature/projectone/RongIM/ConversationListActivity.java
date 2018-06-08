package com.feature.projectone.RongIM;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.feature.projectone.R;

import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2018/5/4.
 * 融云回话列表界面
 */

public class ConversationListActivity extends FragmentActivity {

    private String title;
    private String targetId;
    private String status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversationlist);

        title = getIntent().getData().getQueryParameter("title");
        targetId = getIntent().getData().getQueryParameter("targetId");
        status = getIntent().getData().getLastPathSegment();//获取当前会话类型

        Log.i("ConversationListAc", "          title:         " + title);
        Log.i("ConversationListAc", "          targetId:         " + targetId);
        Log.i("ConversationListAc", "          status:         " + status);

        ((LinearLayout) findViewById(R.id.ll_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.orangeone));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("ConversationListAc", "          onResume()        ");
    }
}
