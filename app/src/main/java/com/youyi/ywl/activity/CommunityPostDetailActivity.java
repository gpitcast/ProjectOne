package com.youyi.ywl.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.youyi.ywl.R;
import com.youyi.ywl.adapter.CommunityPostDetailAdapter;
import com.youyi.ywl.adapter.CommunityPostDetailGridAdapter;
import com.youyi.ywl.other.Constanst;
import com.youyi.ywl.util.GlideUtil;
import com.youyi.ywl.util.HttpUtils;
import com.youyi.ywl.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/6/27.
 * 社区帖子详情界面
 */

public class CommunityPostDetailActivity extends BaseActivity {
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;
    @BindView(R.id.tv_title)
    TextView tv_title;

    private static final String postDetailUrl = HttpUtils.Host + "/qunzhu/forums_post_info";//帖子详情数据
    private static final String postReplyUrl = HttpUtils.Host + "/qunzhu/forums_post_info_reply";//帖子回复列表数据
    private static final String postReplyPostUrl = HttpUtils.Host + "/qunzhu/w_comment";//回复帖子
    private List<String> postImgList = new ArrayList<>();//帖子显示的图片结合
    private List<HashMap<String, Object>> replyDataList = new ArrayList<>();//评论列表的数据
    private TextView tv_status;
    private CommunityPostDetailAdapter communityPostDetailAdapter;
    private GridView gridView;
    private CommunityPostDetailGridAdapter communityPostDetailGridAdapter;
    private String id;
    private TextView tv_post_title;
    private CircleImageView circleImageView;
    private TextView tv_writer_name;
    private TextView tv_time;
    private TextView tv_content;
    private TextView tv_send;
    private EditText et_reply_content;
    private int pageno = 1;
    private boolean isLoadMore;

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case postDetailUrl:
                //在详情接口数据返回时请求评论列表数据
                PostReplyList();//获取帖子列表

                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        HashMap<String, Object> dataMap = (HashMap<String, Object>) resultMap.get("data");
                        tv_post_title.setText(dataMap.get("title") + "");//帖子标题
                        GlideUtil.loadNetImageView(this,dataMap.get("u_img") + "",circleImageView);//作者头像
                        tv_writer_name.setText(dataMap.get("nickname") + "");//作者的昵称
                        tv_time.setText(dataMap.get("atime") + "");//发帖时间
                        tv_content.setText(dataMap.get("content") + "");//帖子内容

                        ArrayList<String> p_imgs = (ArrayList<String>) dataMap.get("p_imgs");
                        if (p_imgs != null && p_imgs.size() > 0) {
                            postImgList.addAll(p_imgs);
                            communityPostDetailGridAdapter.notifyDataSetChanged();
                        }
                    } else {
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                    }
                } else {
                    ToastUtil.show(this, msg, 0);
                }
                break;
            case postReplyUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        ArrayList<HashMap<String, Object>> arrayList = (ArrayList<HashMap<String, Object>>) resultMap.get("data");
                        if (arrayList != null && arrayList.size() > 0) {
                            if (isLoadMore) {
                                isLoadMore = false;
                                xRecyclerView.loadMoreComplete();
                                tv_status.setText("加载完毕");
                            }
                            replyDataList.addAll(arrayList);
                            communityPostDetailAdapter.notifyDataSetChanged();
                        } else {
                            if (isLoadMore) {
                                isLoadMore = false;
                                xRecyclerView.loadMoreComplete();
                                tv_status.setText(resultMap.get("msg") + "");
                                xRecyclerView.setNoMore(true);
                            } else {
                                ToastUtil.show(this, resultMap.get("msg") + "", 0);
                            }
                        }
                    } else {
                        if (isLoadMore) {
                            isLoadMore = false;
                            xRecyclerView.loadMoreComplete();
                            tv_status.setText(resultMap.get("msg") + "");
                            xRecyclerView.setNoMore(true);
                        } else {
                            ToastUtil.show(this, resultMap.get("msg") + "", 0);
                        }
                    }
                } else {
                    if (isLoadMore) {
                        isLoadMore = false;
                        xRecyclerView.loadMoreComplete();
                        tv_status.setText(msg);
                    } else {
                        ToastUtil.show(this, msg, 0);
                    }
                }
                break;
            case postReplyPostUrl:
                dismissLoadingDialog();
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        //评论成功
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                        et_reply_content.setText("");
                        replyPopupWindow.dismiss();
                        //评论成功后刷新评论列表
                        replyDataList.clear();
                        pageno = 1;
                        PostReplyList();
                    } else {
                        //评论失败
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
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_community_post_detail);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tv_title.setText("帖子详情");
        PostPostDetailList();//获取帖子详情
        initRecyclerView();
    }

    //获取评论列表

    private void PostReplyList() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "qunzhu");
        map.put("action", "forums_post_info_reply");
        map.put("id", id);
        map.put("page", pageno);
        getJsonUtil().PostJson(this, map);
    }

    //获取帖子详情
    private void PostPostDetailList() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "qunzhu");
        map.put("action", "forums_post_info");
        map.put("id", id);
        getJsonUtil().PostJson(this, map);
    }

    //回复帖子
    private void PostReplyPostList() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "qunzhu");
        map.put("action", "w_comment");
        map.put("id", id);
        map.put("content", et_reply_content.getText().toString().trim());
        getJsonUtil().PostJson(this, map);
    }


    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        xRecyclerView.setLayoutManager(manager);
        communityPostDetailAdapter = new CommunityPostDetailAdapter(this, replyDataList);
        xRecyclerView.setAdapter(communityPostDetailAdapter);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(true);
        View headerView1 = LayoutInflater.from(this).inflate(R.layout.community_post_headview, null);
        View headerView2 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        gridView = ((GridView) headerView1.findViewById(R.id.gridView));
        initHeadGridView();//初始化帖子详情头部图片的grifdview
        initHeadView(headerView1);
        xRecyclerView.addHeaderView(headerView1);
        xRecyclerView.addHeaderView(headerView2);

        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                pageno++;
                isLoadMore = true;
                PostReplyList();
                tv_status.setText("正在加载...");
            }
        });

        View footerView = LayoutInflater.from(this).inflate(R.layout.layout_recyclerview_footer, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        footerView.setLayoutParams(layoutParams);
        tv_status = ((TextView) footerView.findViewById(R.id.tv_status));
        xRecyclerView.setFootView(footerView);
        xRecyclerView.loadMoreComplete();
    }

    private void initHeadView(View headerView1) {
        tv_post_title = ((TextView) headerView1.findViewById(R.id.tv_title));
        circleImageView = ((CircleImageView) headerView1.findViewById(R.id.circleImageView));
        tv_writer_name = ((TextView) headerView1.findViewById(R.id.tv_writer_name));
        tv_time = ((TextView) headerView1.findViewById(R.id.tv_time));
        tv_content = ((TextView) headerView1.findViewById(R.id.tv_content));
    }

    //初始化帖子详情头部图片的grifdview
    private void initHeadGridView() {
        communityPostDetailGridAdapter = new CommunityPostDetailGridAdapter(this, postImgList);
        gridView.setAdapter(communityPostDetailGridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @Override
    public void afterInitView() {
    }

    @OnClick({R.id.ll_back, R.id.ll_reply_post})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_reply_post:
                //我要回帖
                showReplyPopWindow();
                break;
        }
    }

    private PopupWindow replyPopupWindow;
    private View popView;

    private void showReplyPopWindow() {

        if (popView == null) {
            popView = LayoutInflater.from(this).inflate(R.layout.layout_reply_post_pop, null);
        }
        tv_send = ((TextView) popView.findViewById(R.id.tv_send));//发送按钮
        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_reply_content.getText().toString().trim() == null || et_reply_content.getText().toString().trim().length() == 0) {
                    ToastUtil.show(CommunityPostDetailActivity.this, "请输入评论内容", 0);
                } else {
                    showLoadingDialog();
                    PostReplyPostList();
                }
            }
        });
        et_reply_content = ((EditText) popView.findViewById(R.id.et_reply_content));//评论内容

        if (replyPopupWindow == null) {
            replyPopupWindow = new PopupWindow(this);
            replyPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            replyPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            replyPopupWindow.setContentView(popView);
            replyPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
            replyPopupWindow.setOutsideTouchable(true);
            replyPopupWindow.setFocusable(true);
            replyPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    setWindowBackgroundAlpha(1f);
                }
            });
        }
        setWindowBackgroundAlpha(0.5f);
        replyPopupWindow.showAtLocation(findViewById(R.id.ll_base), Gravity.BOTTOM, 0, 0);
    }


    /**
     * 控制窗口背景的不透明度
     */
    private void setWindowBackgroundAlpha(float alpha) {
        Window window = this.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.alpha = alpha;
        window.setAttributes(layoutParams);
    }
}
