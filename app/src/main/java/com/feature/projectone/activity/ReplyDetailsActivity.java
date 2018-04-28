package com.feature.projectone.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.azimolabs.keyboardwatcher.KeyboardWatcher;
import com.feature.projectone.R;
import com.feature.projectone.adapter.ReplyDetailsAdapter;
import com.feature.projectone.inter.ReplyIconClickListener;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.HttpUtils;
import com.feature.projectone.util.ShareUtil;
import com.feature.projectone.util.SoftUtil;
import com.feature.projectone.util.ToastUtil;
import com.feature.projectone.view.CircleImageView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/21.
 * 新闻详情界面的评论详情界面
 */

public class ReplyDetailsActivity extends BaseActivity implements KeyboardWatcher.OnKeyboardToggleListener {
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;
    private ReplyDetailsAdapter replyDetailsAdapter;
    @BindView(R.id.et_write_comments)
    EditText et_write_comments;
    @BindView(R.id.tv_comments_release)
    TextView tv_comments_release;
    @BindView(R.id.tv_reply_count)
    TextView tv_reply_count;

    private static final String newsDetailUrl = HttpUtils.Host + "/news/my_comment";//回复列表
    private static final String newsWriteCommentsUrl = HttpUtils.Host + "/news/w_comment";//新闻发表评论和评论发表回复接口
    private int pageno = 1;//分页
    private String id;//文章id
    private String c_id;//该条根评论的id
    private String pid = null;//评论的回复id
    private List<HashMap<String, Object>> mDataList = new ArrayList<>();//列表的数据
    private CircleImageView circleImageView;
    private TextView tv_name;
    private TextView tv_content;
    private TextView tv_time;
    private Bundle bundle;
    private boolean isLoadMore;
    private boolean isRefresh;
    private boolean isWithPidReply;

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case newsDetailUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap<String, Object> resultMap = (HashMap<String, Object>) result;

                    ArrayList<HashMap<String, Object>> dataList = (ArrayList<HashMap<String, Object>>) resultMap.get("data");
                    if (dataList != null && dataList.size() > 0) {
                        if (isLoadMore) {
                            isLoadMore = false;
                            xRecyclerView.loadMoreComplete();
                            mDataList.addAll(dataList);
                        } else if (isRefresh) {
                            isRefresh = false;
                            xRecyclerView.refreshComplete();
                            mDataList.clear();
                            mDataList.addAll(dataList);
                            xRecyclerView.smoothScrollToPosition(3);
                        } else {
                            mDataList.addAll(dataList);
                        }
                        String reply_nums = resultMap.get("reply_nums") + "";
                        tv_reply_count.setText(reply_nums + "条回复");
                        replyDetailsAdapter.notifyDataSetChanged();
                    } else {
                        if (isLoadMore) {
                            isLoadMore = false;
                            pageno--;
                            xRecyclerView.loadMoreComplete();
                        } else if (isRefresh) {
                            isRefresh = false;
                            xRecyclerView.refreshComplete();
                            tv_reply_count.setText("0条回复");
                        }
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                    }
                } else {
                    ToastUtil.show(this, msg, 0);
                }
                break;
            case newsWriteCommentsUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap<String, Object> resultMap = (HashMap<String, Object>) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        //评论成功，清空评论框
                        et_write_comments.setText("");
                        SoftUtil.hideSoft(this);
                        pageno = 1;
                        isRefresh = true;
                        PostMyCommentList();
                        //弹出吐司提醒用户
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                    } else {
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
        id = intent.getStringExtra("id");//文章id
        c_id = intent.getStringExtra("c_id");//评论id
        bundle = intent.getExtras();//装根评论的bundle
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_reply_details);
    }

    @Override
    public void beforeInitView() {
        KeyboardWatcher keyboardWatcher = new KeyboardWatcher(this);
        keyboardWatcher.setListener(this);
    }

    @Override
    public void initView() {
        PostMyCommentList();
        initRecyclerView();
        listenEtChanged();
    }

    private String etCommentsText;

    private void listenEtChanged() {
        et_write_comments.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkedText();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


    }

    private void checkedText() {
        etCommentsText = et_write_comments.getText().toString().trim();
        if (etCommentsText != null && etCommentsText.length() > 0) {
            tv_comments_release.setBackground(getResources().getDrawable(R.drawable.bg_comments_release_blue));
        } else {
            tv_comments_release.setBackground(getResources().getDrawable(R.drawable.bg_comments_release_gray));
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        xRecyclerView.setLayoutManager(manager);
        replyDetailsAdapter = new ReplyDetailsAdapter(this, mDataList);
        xRecyclerView.setAdapter(replyDetailsAdapter);
        View headView1 = LayoutInflater.from(this).inflate(R.layout.layout_reply_details_head, null);
        initHeadView(headView1);
        View headView2 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        xRecyclerView.addHeaderView(headView1);
        xRecyclerView.addHeaderView(headView2);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                isLoadMore = true;
                pageno++;
                PostMyCommentList();
            }
        });

        //添加xrecyclerview的footer
        View footerView = LayoutInflater.from(this).inflate(R.layout.layout_recyclerview_footer, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
        layoutParams.gravity = Gravity.CENTER;
        footerView.setLayoutParams(layoutParams);
        footerView.setBackgroundColor(getResources().getColor(R.color.white));
        xRecyclerView.setFootView(footerView);
        xRecyclerView.loadMoreComplete();

        replyDetailsAdapter.setOnReplyClickListener(new ReplyIconClickListener() {
            @Override
            public void OnReplyIconClick(View view, int position) {
                //点击了回复字体按钮，记录该条的pid(回复id)，并弹出键盘
                pid = ((HashMap<String, Object>) mDataList.get(position)).get("id") + "";
                et_write_comments.setCursorVisible(true);//显示EditText的光标
                et_write_comments.setFocusable(true);//获取焦点
                et_write_comments.setFocusableInTouchMode(true);
                et_write_comments.requestFocus();
                SoftUtil.showSoft(ReplyDetailsActivity.this);//隐藏键盘
                isWithPidReply = true;//记录当前点击了列表的回复字体按钮而触发的
            }
        });
    }

    //初始化头部
    private void initHeadView(View headView1) {
        circleImageView = ((CircleImageView) headView1.findViewById(R.id.circleImageView));//头像
        tv_name = ((TextView) headView1.findViewById(R.id.tv_name));//名称
        tv_content = ((TextView) headView1.findViewById(R.id.tv_content));//内容
        tv_time = ((TextView) headView1.findViewById(R.id.tv_time));//时间
        Picasso.with(this).load(bundle.getString("img_url")).placeholder(R.mipmap.img_loading_default).error(R.mipmap.img_load_error).into(circleImageView);
        tv_name.setText(bundle.getString("username"));
        tv_content.setText(bundle.getString("content"));
        tv_time.setText(bundle.getString("atime"));
    }

    @Override
    public void afterInitView() {
    }

    @OnClick({R.id.iv_close_comments, R.id.ll_back, R.id.tv_comments_release, R.id.et_write_comments})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close_comments:
                //关闭评论的按钮
                SoftUtil.hideSoft(this);
                et_write_comments.setText("");
                et_write_comments.setCursorVisible(false);//隐藏EditText的光标
                //如果pid有值，清除掉pid
                if (isWithPidReply) {
                    isWithPidReply = false;
                    pid = null;
                }
                break;
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_comments_release:
                //发布评论按钮
                String str = et_write_comments.getText().toString().trim();
                if (str == null || str.length() == 0) {
                    ToastUtil.show(this, getString(R.string.comments_content), 0);
                    return;
                }
                if (ShareUtil.isExist(this, Constanst.UER_TOKEN)) {
                    PostWriteComments();//发表评论
                } else {
                    ToastUtil.show(this, getResources().getString(R.string.need_login_first), 0);
                }
                break;
            case R.id.et_write_comments:
                //评论框点击事件
                et_write_comments.setCursorVisible(true);//隐藏EditText的光标
                break;
        }
    }

    //发表评论接口
    private void PostWriteComments() {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("controller", "news");
        map.put("action", "w_comment");
        map.put("id", id);//文章id
        map.put("type", "1");//0 代表评论 1代表回复
        map.put("content", et_write_comments.getText().toString().trim());
        map.put("c_id", c_id);//评论id
        map.put("pid", pid);//回复id
        getJsonUtil().PostJson(this, map);
    }

    //请求评论列表接口
    private void PostMyCommentList() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("controller", "news");
        map.put("action", "my_comment");
        map.put("page", pageno);
        map.put("id", id);//文章id
        map.put("c_id", c_id);//评论id
        getJsonUtil().PostJson(this, map);
    }

    //键盘显示
    @Override
    public void onKeyboardShown(int keyboardSize) {
        Log.i("Keyboard", "       onKeyboardShown        ");
    }

    //键盘隐藏
    @Override
    public void onKeyboardClosed() {
        Log.i("Keyboard", "       onKeyboardClosed        ");
    }

    //点击键盘其他地方隐藏键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (SoftUtil.isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }

        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return super.onTouchEvent(ev);
    }
}
