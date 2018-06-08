package com.feature.projectone.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.activity.JoinGroupActivity;
import com.feature.projectone.adapter.CommunityGroupsFragmentAdapter;
import com.feature.projectone.inter.ReplyIconClickListener;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.HttpUtils;
import com.feature.projectone.util.SoftUtil;
import com.feature.projectone.util.ToastUtil;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * Created by Administrator on 2018/5/30.
 * 联系人的社区群组fragment
 */

public class CommunityGroupsFragment extends BaseFragment {
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;
    @BindView(R.id.et_search)
    EditText et_search;

    private static final String dataUrl = HttpUtils.Host + "/qunzhu/get_list";
    private CommunityGroupsFragmentAdapter communityGroupsFragmentAdapter;
    private int pageno = 1;
    private boolean isLoadMore;
    private boolean isSearch;
    private List<HashMap> dataList = new ArrayList<>();

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case dataUrl:
                if (Constanst.success_net_code.equals(code)) {
                    if (result != null) {
                        HashMap resultMap = (HashMap) result;
                        String status = resultMap.get("status") + "";
                        if (status != null) {
                            ArrayList<HashMap<String, Object>> arrayList = (ArrayList<HashMap<String, Object>>) resultMap.get("data");
                            if (arrayList != null && arrayList.size() > 0) {
                                if (isLoadMore) {
                                    isLoadMore = false;
                                    xRecyclerView.loadMoreComplete();
                                }
                                if (isSearch) {
                                    isSearch = false;
                                    dataList.clear();
                                }
                                dataList.addAll(arrayList);
                            } else {
                                if (isLoadMore) {
                                    pageno--;
                                    isLoadMore = false;
                                    xRecyclerView.loadMoreComplete();
                                }
                                if (isSearch) {
                                    isSearch = false;
                                    ToastUtil.show(getActivity(), "搜索不到更多的数据", 0);
                                }
                            }
                            communityGroupsFragmentAdapter.notifyDataSetChanged();
                        } else {
                            if (isLoadMore) {
                                pageno--;
                                isLoadMore = false;
                                xRecyclerView.loadMoreComplete();
                            }
                            ToastUtil.show(getActivity(), resultMap.get("msg") + "", 0);
                        }
                    }
                } else {
                    if (isLoadMore) {
                        pageno--;
                        isLoadMore = false;
                        xRecyclerView.loadMoreComplete();
                    }
                    ToastUtil.show(getActivity(), msg, 0);
                }
                break;
        }
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
        return R.layout.fragment_community_groups;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        EventBus.getDefault().register(this);
        PostList();
        initRecyclerView();
        initView();
    }

    private void initView() {
        //添加软键盘的监听
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE
                        || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (et_search.getText().toString().trim() != null && et_search.getText().toString().trim().length() > 0) {
                        SoftUtil.hideSoft(getActivity());//隐藏键盘
                        PostList(textView);
                    } else {
                        ToastUtil.show(getActivity(), "搜索关键字不能为空", 0);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void PostList() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "qunzhu");
        map.put("action", "get_list");
        map.put("p", pageno + "");
        map.put("keywords", et_search.getText().toString().trim());
        getJsonUtil().PostJson(getActivity(), map);
    }

    //专门搜索的接口请求方法
    public void PostList(View view) {
        isSearch = true;
        pageno = 1;
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "qunzhu");
        map.put("action", "get_list");
        map.put("p", pageno + "");
        map.put("keywords", et_search.getText().toString().trim());
        getJsonUtil().PostJson(getActivity(), map, view);
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        xRecyclerView.setLayoutManager(manager);
        communityGroupsFragmentAdapter = new CommunityGroupsFragmentAdapter(getActivity(), dataList);
        xRecyclerView.setAdapter(communityGroupsFragmentAdapter);
        View headerView1 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty_head, null);
        View headerView2 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty_head, null);
        xRecyclerView.addHeaderView(headerView1);
        xRecyclerView.addHeaderView(headerView2);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        //刷新和加载监听
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                pageno++;
                isLoadMore = true;
                PostList();
            }
        });

        View footerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_recyclerview_footer, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
        layoutParams.gravity = Gravity.CENTER;
        footerView.setLayoutParams(layoutParams);
        footerView.setBackgroundColor(getResources().getColor(R.color.white));
        xRecyclerView.setFootView(footerView);
        xRecyclerView.loadMoreComplete();

        //条目上的开始聊天按钮的点击事件
        communityGroupsFragmentAdapter.setOnIntoButtonClickListener(new ReplyIconClickListener() {
            @Override
            public void OnReplyIconClick(View view, int position) {
                HashMap map = dataList.get(position);
                RongIM.getInstance().startConversation(getActivity(), Conversation.ConversationType.GROUP, map.get("groupId") + "", map.get("name") + "");
            }
        });
    }

    @OnClick({R.id.ll_join_group})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_join_group:
                //跳转到加入社区群界面
                startActivity(new Intent(getActivity(), JoinGroupActivity.class));
                break;
        }
    }

    @Subscribe
    public void onEventMainThread(String str) {
        if (str == null) {
            return;
        }
        switch (str) {
            case "刷新CommunityGroupsFragment":
                Log.i("CommunityGroups", "      执行刷新CommunityGroupsFragment      ");
                dataList.clear();
                pageno = 1;
                PostList();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
