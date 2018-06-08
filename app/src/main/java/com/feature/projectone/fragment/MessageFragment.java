package com.feature.projectone.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.activity.ContactsActivity;
import com.feature.projectone.activity.GroupFolderListActivity;
import com.feature.projectone.activity.GroupInformationActivity;
import com.feature.projectone.activity.GroupListActivity;
import com.feature.projectone.activity.GroupMemberListActivity;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by Administrator on 2018/4/19.
 * 主界面 消息fragment
 */

public class MessageFragment extends BaseFragment {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.ll_jump_to_contact)
    LinearLayout ll_jump_to_contact;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;

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
        tv_title.setText("消息");
        ll_back.setVisibility(View.GONE);
        ll_jump_to_contact.setVisibility(View.VISIBLE);

    }


    private List<String> idList = new ArrayList<>();
    private String discussionID;

    @OnClick({R.id.bt1, R.id.bt2, R.id.bt3, R.id.tv_title, R.id.bt4, R.id.bt5, R.id.bt6, R.id.bt7, R.id.bt8, R.id.ll_jump_to_contact})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.bt1:
                idList.add("13554136943");
                idList.add("13080613218");

                /**
                 * <p>启动会话界面。</p>
                 * <p>使用时，可以传入多种会话类型 {@link io.rong.imlib.model.Conversation.ConversationType} 对应不同的会话类型，开启不同的会话界面。
                 * 如果传入的是 {@link io.rong.imlib.model.Conversation.ConversationType#CHATROOM}，sdk 会默认调用
                 * {@link RongIMClient#joinChatRoom(String, int, RongIMClient.OperationCallback)} 加入聊天室。
                 * 如果你的逻辑是，只允许加入已存在的聊天室，请使用接口 {@link #startChatRoomChat(Context, String, boolean)} 并且第三个参数为 true</p>
                 *
                 * @param context          应用上下文。
                 * @param conversationType 会话类型。
                 * @param targetId         根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 或聊天室 Id。
                 * @param title            聊天的标题，开发者可以在聊天界面通过 intent.getData().getQueryParameter("title") 获取该值, 再手动设置为标题。
                 */
                RongIM.getInstance().createDiscussionChat(getActivity(), idList, "我和110的讨论组", new RongIMClient.CreateDiscussionCallback() {
                    @Override
                    public void onSuccess(String s) {
                        discussionID = s;
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                    }
                });

                break;
            case R.id.bt2:
                /**
                 * 启动会话列表界面。
                 *
                 * @param context               应用上下文。
                 * @param supportedConversation 定义会话列表支持显示的会话类型，及对应的会话类型是否聚合显示。
                 *                              例如：supportedConversation.put(Conversation.ConversationType.PRIVATE.getName(), false) 非聚合式显示 private 类型的会话。
                 */
                HashMap<String, Boolean> map1 = new HashMap<>();
                map1.put(Conversation.ConversationType.PRIVATE.getName(), false);// 非聚合式显示 private 类型的会话。
                RongIM.getInstance().startConversationList(getActivity(), map1);
                break;
            case R.id.bt3:
                /**
                 * 启动聚合后的某类型的会话列表。<br> 例如：如果设置了单聊会话为聚合，则通过该方法可以打开包含所有的单聊会话的列表。
                 *
                 * @param context          应用上下文。
                 * @param conversationType 会话类型。
                 */
                HashMap<String, Boolean> map2 = new HashMap<>();
                map2.put(Conversation.ConversationType.PRIVATE.getName(), false);// 非聚合式显示 private 类型的会话。
                RongIM.getInstance().startSubConversationList(getActivity(), Conversation.ConversationType.PRIVATE);
                break;
            case R.id.tv_title:
                tv_title.setText("改变了的消息fragment");
                break;
            case R.id.bt4:
                //群成员列表
                startActivity(new Intent(getActivity(), GroupMemberListActivity.class));
                break;
            case R.id.bt5:
                //群文件夹
                startActivity(new Intent(getActivity(), GroupFolderListActivity.class));
                break;
            case R.id.bt6:
                //群组信息
                startActivity(new Intent(getActivity(), GroupInformationActivity.class));
                break;
            case R.id.bt7:
                //群组信息
                startActivity(new Intent(getActivity(), GroupListActivity.class));
                break;
            case R.id.bt8:
                //联系人
                startActivity(new Intent(getActivity(), ContactsActivity.class));
                break;

            case R.id.ll_jump_to_contact:
                //跳转到联系人界面
                startActivity(new Intent(getActivity(), ContactsActivity.class));
                break;
        }
    }
}
