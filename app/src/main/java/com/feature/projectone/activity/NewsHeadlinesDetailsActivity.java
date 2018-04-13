package com.feature.projectone.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.adapter.NewsHeadLinesDetailsAdapter;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.EtDrawableLeftUtil;
import com.feature.projectone.util.HttpUtils;
import com.feature.projectone.util.SoftUtil;
import com.feature.projectone.util.ToastUtil;
import com.feature.projectone.util.WebViewUtil;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/30.\
 * 新闻头条详情界面
 */

public class NewsHeadlinesDetailsActivity extends BaseActivity {
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.imgTitleRight)
    ImageView imgTitleRight;
    @BindView(R.id.tv_comments_count)
    TextView tv_comments_count;
    @BindView(R.id.tv_write_comments)
    TextView tv_write_comments;
    @BindView(R.id.ll_comments_one)
    LinearLayout ll_comments_one;
    @BindView(R.id.ll_comments_two)
    LinearLayout ll_comments_two;
    @BindView(R.id.et_write_comments)
    EditText et_write_comments;
    @BindView(R.id.tv_comments_release)
    TextView tv_comments_release;

    private static final String newsDetailUrl = HttpUtils.Host + "/news/detail";//新闻内容接口
    private static final String newsCommentsUrl = HttpUtils.Host + "/news/comments";//新闻评论列表接口
    private static final String newsWriteCommentsUrl = HttpUtils.Host + "/news/w_comment";//新闻发表评论接口
    private NewsHeadLinesDetailsAdapter newsHeadLinesDetailsAdapter;
    private int pageno = 1;//分页
    private String id;//新闻的ID
    private WebView webView;//显示新闻内容的webview
    private TextView tv_title;//新闻标题
    private TextView tv_xiaobian;//新闻小编
    private TextView tv_time;//新闻时间
    private List<HashMap<String, Object>> mCommentsDataList = new ArrayList<>();//存储评论列表数据集合
    private boolean isLoadMore;
    private boolean isRefresh;

    @Override
    protected void handleIntent(Intent intent) {
        id = intent.getStringExtra("id");
    }

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case newsDetailUrl:
                //新闻内容
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) resultMap.get("data");
                    tv_title.setText(dataMap.get("title") + "");//标题
                    tv_xiaobian.setText(((HashMap) dataMap.get("author")).get("name") + "");
                    tv_time.setText(dataMap.get("atime") + "");
                    tv_comments_count.setText(dataMap.get("comments") + "");
                    webView.loadDataWithBaseURL(null, dataMap.get("content") + "", "text/html", "utf-8", null);
                } else {
                    ToastUtil.show(this, msg, 0);
                }

                PostCommentsList();//在欣荣内容接口数据返回的时候再请求新闻评论数据，防止自己封装的网络工具多线程请求错乱
                break;
            case newsCommentsUrl:
                //新闻评论
                if (Constanst.success_net_code.equals(code)) {
                    HashMap<String, Object> resultMap = (HashMap<String, Object>) result;
                    ArrayList<HashMap<String, Object>> dataList = (ArrayList<HashMap<String, Object>>) resultMap.get("data");
                    if (dataList != null && dataList.size() > 0) {
                        if (isLoadMore) {
                            isLoadMore = false;
                            xRecyclerView.loadMoreComplete();
                            mCommentsDataList.addAll(dataList);
                        } else if (isRefresh) {
                            isRefresh = false;
                            xRecyclerView.refreshComplete();
                            mCommentsDataList.clear();
                            mCommentsDataList.addAll(dataList);
                            xRecyclerView.smoothScrollToPosition(3);
                            updateTvCommentsCount();
                        } else {
                            mCommentsDataList.addAll(dataList);
                        }
                        newsHeadLinesDetailsAdapter.notifyDataSetChanged();
                    } else {
                        if (isLoadMore) {
                            isLoadMore = false;
                            pageno--;
                            xRecyclerView.loadMoreComplete();
                        } else if (isRefresh) {
                            isRefresh = false;
                            xRecyclerView.refreshComplete();
                            mCommentsDataList.clear();
                            mCommentsDataList.addAll(dataList);
                        }
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                    }
                } else {
                    if (isLoadMore) {
                        isLoadMore = false;
                        pageno--;
                        xRecyclerView.loadMoreComplete();
                        newsHeadLinesDetailsAdapter.notifyDataSetChanged();
                    } else if (isRefresh) {
                        isRefresh = false;
                        xRecyclerView.refreshComplete();
                    }
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
                        ll_comments_one.setVisibility(View.VISIBLE);
                        ll_comments_two.setVisibility(View.GONE);
                        //请求数据，刷新适配器
                        pageno = 1;
                        isRefresh = true;
                        PostCommentsList();
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

    //前端手动添加评论数量
    public void updateTvCommentsCount() {
        String str = tv_comments_count.getText().toString().trim();
        if (str != null && str.length() > 0) {
            String newStr = (Integer.parseInt(str) + 1) + "";
            tv_comments_count.setText(newStr);
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_news_headlines_details);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        initXRecyclerView();
        PostDetailList();//请求新闻内容数据

        //设置评论的textview左边图片的大小
        EtDrawableLeftUtil.setTvImgSize(tv_write_comments);
        //设置评论editText文本变化的监听
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

    /**
     * 初始化XRecyclerView
     */
    private void initXRecyclerView() {
        tvTitle.setText(getString(R.string.news_headlines));
        imgTitleRight.setVisibility(View.VISIBLE);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        xRecyclerView.setLayoutManager(manager);
        newsHeadLinesDetailsAdapter = new NewsHeadLinesDetailsAdapter(this, mCommentsDataList);
        xRecyclerView.setAdapter(newsHeadLinesDetailsAdapter);
        View headView = LayoutInflater.from(this).inflate(R.layout.head_news_headlines_details, null);
        View headView2 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        initHeadView(headView);
        xRecyclerView.addHeaderView(headView);
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
                PostCommentsList();
            }
        });
        //添加xrecyclerview的footer
        View footerView = LayoutInflater.from(this).inflate(R.layout.layout_recyclerview_footer, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
        layoutParams.gravity = Gravity.CENTER;
        footerView.setLayoutParams(layoutParams);
        footerView.setBackgroundColor(getResources().getColor(R.color.white));
        xRecyclerView.setFootView(footerView);
    }

    private void initHeadView(View headView) {
        webView = ((WebView) headView.findViewById(R.id.webView));
        WebViewUtil.setWebViewSettings(webView);
        tv_title = ((TextView) headView.findViewById(R.id.tv_title));
        tv_xiaobian = ((TextView) headView.findViewById(R.id.tv_xiaobian));
        tv_time = ((TextView) headView.findViewById(R.id.tv_time));
    }

    @Override
    public void afterInitView() {
    }

    @OnClick({R.id.tvBack, R.id.tv_write_comments, R.id.iv_close_comments, R.id.tv_comments_release})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                //返回键
                finish();
                break;
            case R.id.tv_write_comments:
                //评论的textview
                ll_comments_one.setVisibility(View.GONE);
                ll_comments_two.setVisibility(View.VISIBLE);
                SoftUtil.showSoft(this);
                et_write_comments.requestFocus();//获取焦点 光标出现
                break;
            case R.id.iv_close_comments:
                //关闭评论的按钮
                SoftUtil.hideSoft(this);
                et_write_comments.setText("");
                ll_comments_one.setVisibility(View.VISIBLE);
                ll_comments_two.setVisibility(View.GONE);
                break;
            case R.id.tv_comments_release:
                String str = et_write_comments.getText().toString().trim();
                if (str == null || str.length() == 0) {
                    ToastUtil.show(this, getString(R.string.comments_content), 0);
                    return;
                }
                PostWriteComments();//发表评论
                break;

        }
    }

    //发表评论
    private void PostWriteComments() {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("controller", "news");
        map.put("action", "w_comment");
        map.put("id", id);
        map.put("type", "0");//0 代表评论 1代表回复
        map.put("content", et_write_comments.getText().toString().trim());
        getJsonUtil().PostJson(this, map);
    }

    //请求新闻内容
    public void PostDetailList() {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("controller", "news");
        map.put("action", "detail");
        map.put("id", id);
        getJsonUtil().PostJson(this, map);
    }

    //请求新闻评论
    public void PostCommentsList() {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("controller", "news");
        map.put("action", "comments");
        map.put("id", id);
        map.put("page", pageno);
        getJsonUtil().PostJson(this, map);
    }
}
