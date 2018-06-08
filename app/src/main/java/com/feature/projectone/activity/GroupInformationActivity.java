package com.feature.projectone.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.adapter.GroupInformationGridViewAdapter;
import com.feature.projectone.fragment.GroupFileFragment;
import com.feature.projectone.fragment.GroupNoticeFragment;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.HttpUtils;
import com.feature.projectone.util.ToastUtil;
import com.feature.projectone.view.CircleImageView;
import com.feature.projectone.view.IosSwitch;
import com.feature.projectone.view.MyGridView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.CSCustomServiceInfo;
import io.rong.imlib.model.Conversation;

/**
 * Created by Administrator on 2018/5/25.
 * 群组信息界面
 */

public class GroupInformationActivity extends BaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.gridView)
    MyGridView gridView;
    @BindView(R.id.iv_group_notice)
    ImageView iv_group_notice;
    @BindView(R.id.tv_group_notice)
    TextView tv_group_notice;
    @BindView(R.id.iv_group_notice_line)
    ImageView iv_group_notice_line;
    @BindView(R.id.iv_group_file)
    ImageView iv_group_file;
    @BindView(R.id.tv_group_file)
    TextView tv_group_file;
    @BindView(R.id.iv_group_file_line)
    ImageView iv_group_file_line;
    @BindView(R.id.iosSwitch_no_msg)
    IosSwitch iosSwitch_no_msg;
    @BindView(R.id.iosSwitch_top_tlak)
    IosSwitch iosSwitch_top_tlak;
    @BindView(R.id.tv_group_name)
    TextView tv_group_name;
    @BindView(R.id.tv_group_member_count)
    TextView tv_group_member_count;
    @BindView(R.id.circleImageView)
    CircleImageView circleImageView;


    private static final String dataUrl = HttpUtils.Host + "/qunzhu/get_group_info";
    private static final String logoutGroupUrl = HttpUtils.Host + "/rongyun/quit_group";
    private GroupInformationGridViewAdapter groupInformationGridViewAdapter;
    private GroupNoticeFragment groupNoticeFragment;
    private GroupFileFragment groupFileFragment;
    private String id;//群组ID
    private List<HashMap<String, Object>> mUserLists = new ArrayList<>();//群组成员信息的list
    private List<HashMap<String, Object>> mNoticeLists = new ArrayList<>();//群组成员信息的list
    private List<HashMap<String, Object>> mFileLists = new ArrayList<>();//群组成员信息的list
    private String targetId;


    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case dataUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;

                    //群组头像,名称,群成员数量
                    HashMap<String, Object> group_info = (HashMap<String, Object>) resultMap.get("group_info");
                    Picasso.with(this).load(group_info.get("img") + "").placeholder(R.mipmap.img_loading)
                            .error(R.mipmap.img_load_error).into(circleImageView);//头像
                    tv_group_name.setText(group_info.get("name") + "");//群组名称
                    tv_group_member_count.setText("(" + resultMap.get("count") + ")");//群成员数量

                    //gridview的群组成员信息
                    ArrayList<HashMap<String, Object>> user_lists = (ArrayList<HashMap<String, Object>>) resultMap.get("user_lists");
                    if (user_lists != null && user_lists.size() > 0) {
                        mUserLists.clear();
                        mUserLists.addAll(user_lists);
                        groupInformationGridViewAdapter.notifyDataSetChanged();
                    }

                    //群公告fragment的信息
                    ArrayList<HashMap<String, Object>> notice_lists = (ArrayList<HashMap<String, Object>>) resultMap.get("notice_lists");
                    if (notice_lists != null && notice_lists.size() > 0) {
                        mNoticeLists.clear();
                        mNoticeLists.addAll(notice_lists);
                        showGroupNoticeFragment();
                    }

                    //群文件fragment的信息
                    ArrayList<HashMap<String, Object>> file_lists = (ArrayList<HashMap<String, Object>>) resultMap.get("file_lists");
                    if (file_lists != null && file_lists.size() > 0) {
                        mFileLists.clear();
                        mFileLists.addAll(file_lists);
                    }

                } else {
                    ToastUtil.show(this, msg, 0);
                }
                break;
            case logoutGroupUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        //请求成功
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                        RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, targetId, new RongIMClient.ResultCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                            }
                        });
                        EventBus.getDefault().post("关闭ConversationActivity");
                        EventBus.getDefault().post("刷新CommunityGroupsFragment");
                        finish();
                    } else {
                        //请求失败
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                    }
                } else {
                    ToastUtil.show(this, msg, 0);
                }
                break;
        }
    }

    @Override
    protected void handleIntent(Intent intent) {
        if (intent != null) {
            id = intent.getStringExtra("id");
            targetId = intent.getStringExtra("targetId");
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_group_information);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tvTitle.setText("群组信息");
        PostList();
        initGridView();//初始化成员的gridview

        iosSwitch_no_msg.setOnToggleListener(new IosSwitch.OnToggleListener() {
            @Override
            public void onSwitchChangeListener(boolean switchState) {
                ToastUtil.show(GroupInformationActivity.this, "消息免打扰点击了  :" + switchState, 0);
            }
        });

        iosSwitch_top_tlak.setOnToggleListener(new IosSwitch.OnToggleListener() {
            @Override
            public void onSwitchChangeListener(boolean switchState) {
                ToastUtil.show(GroupInformationActivity.this, "置顶聊天点击了  :" + switchState, 0);
            }
        });
    }

    //群组信息详情接口
    private void PostList() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "qunzhu");
        map.put("action", "get_group_info");
        map.put("id", id);
        getJsonUtil().PostJson(this, map);
    }

    //退出群组接口
    private void PostLogoutList() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "rongyun");
        map.put("action", "quit_group");
        map.put("group_id", id);
        getJsonUtil().PostJson(this, map);
    }

    public void hideFragment(FragmentTransaction transaction) {
        if (groupNoticeFragment != null) {
            transaction.hide(groupNoticeFragment);
        }
        if (groupFileFragment != null) {
            transaction.hide(groupFileFragment);
        }
    }

    //显示群公告fragment
    public void showGroupNoticeFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (groupNoticeFragment == null) {
            groupNoticeFragment = new GroupNoticeFragment(id);
            transaction.add(R.id.framelayout, groupNoticeFragment);
        }
        hideFragment(transaction);
        transaction.show(groupNoticeFragment);//显示选中的fragment
        transaction.commitNow();//提交
        groupNoticeFragment.notifyDataSetChanged(mNoticeLists);
    }

    //显示群文件fragment
    public void showGroupFileFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (groupFileFragment == null) {
            groupFileFragment = new GroupFileFragment(id);
            transaction.add(R.id.framelayout, groupFileFragment);
        }
        hideFragment(transaction);
        transaction.show(groupFileFragment);
        transaction.commitNow();
        groupFileFragment.notifyDataSetChanged(mFileLists);
    }


    private void initGridView() {
        groupInformationGridViewAdapter = new GroupInformationGridViewAdapter(this, mUserLists);
        gridView.setAdapter(groupInformationGridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtil.show(GroupInformationActivity.this, "点击了第" + position + "个条目", 0);
            }
        });

        gridView.setClickable(false);
        gridView.setEnabled(false);
        gridView.setPressed(false);
    }

    @Override
    public void afterInitView() {
    }

    private boolean isLeft = true;//记录是否左边为选中状态

    @OnClick({R.id.tvBack, R.id.rl_group_notice, R.id.rl_group_file, R.id.tv_delete_logout, R.id.ll_group_member})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                finish();
                break;
            case R.id.rl_group_notice:
                //群公告
                if (!isLeft) {
                    //表示右边为选中,切换为左边的群公告
                    showGroupNoticeFragment();
                    isLeft = true;
                    changeSelectColor(isLeft);
                }
                break;
            case R.id.rl_group_file:
                //群文件
                if (isLeft) {
                    //表示左边为选中,切换为右边的群文件
                    showGroupFileFragment();
                    isLeft = false;
                    changeSelectColor(isLeft);
                }
                break;
            case R.id.tv_delete_logout:
                //删除并且退出
                PostLogoutList();
                break;
            case R.id.ll_group_member:
                //群成员
                Intent intent = new Intent(this, GroupMemberListActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                break;
        }
    }


    public void changeSelectColor(boolean isLeft) {
        if (isLeft) {
            //选中左边
            Picasso.with(this).load(R.mipmap.icon_group_blue_notice).into(iv_group_notice);
            tv_group_notice.setTextColor(getResources().getColor(R.color.orangeone));
            Picasso.with(this).load(R.mipmap.icon_group_blue_line).into(iv_group_notice_line);

            Picasso.with(this).load(R.mipmap.icon_group_gray_files).into(iv_group_file);
            tv_group_file.setTextColor(getResources().getColor(R.color.light_gray16));
            Picasso.with(this).load(R.mipmap.icon_group_gray_line).into(iv_group_file_line);
        } else {
            //选中右边
            Picasso.with(this).load(R.mipmap.icon_group_gray_notice).into(iv_group_notice);
            tv_group_notice.setTextColor(getResources().getColor(R.color.light_gray16));
            Picasso.with(this).load(R.mipmap.icon_group_gray_line).into(iv_group_notice_line);

            Picasso.with(this).load(R.mipmap.icon_group_blue_files).into(iv_group_file);
            tv_group_file.setTextColor(getResources().getColor(R.color.orangeone));
            Picasso.with(this).load(R.mipmap.icon_group_blue_line).into(iv_group_file_line);
        }
    }
}
