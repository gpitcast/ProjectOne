package com.feature.projectone.RongIM;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;

import java.util.Locale;

import butterknife.BindView;
import io.rong.imlib.model.Conversation;

/**
 * Created by Administrator on 2018/5/4.
 * 聚合会话列表
 */

public class SubConversationListActivtiy extends FragmentActivity {

    private String title;
    private String targetId;
    private String status;
    private TextView tv_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subconversationlist);

        title = getIntent().getData().getQueryParameter("title");
        targetId = getIntent().getData().getQueryParameter("targetId");
        status = getIntent().getData().getLastPathSegment();//获取当前会话类型

        tv_title = ((TextView) findViewById(R.id.tv_title));
        String type = getIntent().getData().getQueryParameter("type");
        if (type != null) {
            if (Conversation.ConversationType.GROUP.getName().equals(type)) {
                tv_title.setText("我的群组");
            }
            if (Conversation.ConversationType.DISCUSSION.getName().equals(type)) {
                tv_title.setText("我的讨论组");
            }
            if (Conversation.ConversationType.PRIVATE.getName().equals(type)) {
                tv_title.setText("我的私人会话");
            }
            if (Conversation.ConversationType.SYSTEM.getName().equals(type)) {
                tv_title.setText("系统会话");
            }
            if (Conversation.ConversationType.CHATROOM.getName().equals(type)) {
                tv_title.setText("我的聊天室");
            }
        }

        Log.i("ConversationListAc", "          type:         " + type);
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
}

