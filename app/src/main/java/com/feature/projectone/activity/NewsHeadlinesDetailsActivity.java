package com.feature.projectone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.adapter.NewsHeadLinesDetailsAdapter;
import com.feature.projectone.inter.ReplyIconClickListener;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.EtDrawableLeftUtil;
import com.feature.projectone.util.HttpUtils;
import com.feature.projectone.util.ShareUtil;
import com.feature.projectone.util.SoftUtil;
import com.feature.projectone.util.ToastUtil;
import com.feature.projectone.util.WebViewUtil;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.io.Serializable;
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
    @BindView(R.id.iv_dianzan)
    ImageView iv_dianzan;
    @BindView(R.id.tv_dianzan_count)
    TextView tv_dianzan_count;

    private static final String newsDetailUrl = HttpUtils.Host + "/news/detail";//新闻内容接口
    private static final String newsCommentsUrl = HttpUtils.Host + "/news/comments";//新闻评论列表接口
    private static final String newsWriteCommentsUrl = HttpUtils.Host + "/news/w_comment";//新闻发表评论接口
    private static final String newsDianZanUrl = HttpUtils.Host + "/news/dianzan";//新闻发表评论接口
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
    private boolean isDianZan;//记录是否点赞

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
                    tv_xiaobian.setText(((HashMap) dataMap.get("author")).get("name") + "");//小编
                    tv_time.setText(dataMap.get("atime") + "");//发表时间
                    tv_comments_count.setText(dataMap.get("comments") + "");//评论数量
                    String is_dianzan = dataMap.get("is_dianzan") + "";
                    if ("1".equals(is_dianzan)) {
                        //1代表已经点过赞
                        isDianZan = true;
                        iv_dianzan.setImageResource(R.mipmap.img_dianzan_blue);
                    } else {
                        //0或其他代表没有点过赞
                        isDianZan = false;
                        iv_dianzan.setImageResource(R.mipmap.img_dianzan_gray);
                    }
                    tv_dianzan_count.setText(dataMap.get("dianzan_nums") + "");//点赞数量
                    webView.loadDataWithBaseURL(null, dataMap.get("content") + "", "text/html", "utf-8", null);
                    String tags = dataMap.get("tags") + "";//存储每一条新闻的tags，新闻搜索的时候需要用到
                    if (tags != null && tags.length() > 0) {
                        ShareUtil.putString(this, Constanst.TAGS, tags);
                    }
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
            case newsDianZanUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap<String, Object> resultMap = (HashMap<String, Object>) result;
                    String message = resultMap.get("msg") + "";
                    ToastUtil.show(this, message, 0);
                    isDianZan = !isDianZan;//改变记录点赞状态
                    //根据点赞的状态来改变点赞图标和点赞数量
                    if (isDianZan) {
                        iv_dianzan.setImageResource(R.mipmap.img_dianzan_blue);
                        String dianzanCount = tv_dianzan_count.getText().toString().trim();
                        tv_dianzan_count.setText((Integer.parseInt(dianzanCount) + 1) + "");
                    } else {
                        iv_dianzan.setImageResource(R.mipmap.img_dianzan_gray);
                        String dianzanCount = tv_dianzan_count.getText().toString().trim();
                        tv_dianzan_count.setText((Integer.parseInt(dianzanCount) - 1) + "");
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
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        //刷新和加载监听
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
        xRecyclerView.loadMoreComplete();

        //评论列表的回复图标(也就是二级评论) 点击事件
        newsHeadLinesDetailsAdapter.setOnReplyIconClickListener(new ReplyIconClickListener() {
            @Override
            public void OnReplyIconClick(View view, int position) {
                ToastUtil.show(NewsHeadlinesDetailsActivity.this, "点击了第" + position + "个条目的评论图标", 0);
                Intent intent = new Intent(NewsHeadlinesDetailsActivity.this, ReplyDetailsActivity.class);
                intent.putExtra("id", id);//文章id
                intent.putExtra("c_id", ((HashMap<String, Object>) mCommentsDataList.get(position)).get("id") + "");//评论id
                HashMap<String, Object> map = mCommentsDataList.get(position);
                HashMap<String, Object> userinfo = (HashMap<String, Object>) map.get("userinfo");
                Bundle bundle = new Bundle();
                bundle.putString("img_url", userinfo.get("img_url") + "");
                bundle.putString("username", userinfo.get("username") + "");
                bundle.putString("content", map.get("content") + "");
                bundle.putString("atime", map.get("atime") + "");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
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

    @OnClick({R.id.tvBack, R.id.tv_write_comments, R.id.iv_close_comments, R.id.tv_comments_release, R.id.iv_comments, R.id.iv_dianzan})
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
                if (ShareUtil.isExist(this, Constanst.UER_TOKEN)) {
                    PostWriteComments();//发表评论
                } else {
                    ToastUtil.show(this, getResources().getString(R.string.need_login_first), 0);
                }
                break;
            case R.id.iv_comments:
                //评论图标，点击跳转到第一条评论处
                xRecyclerView.smoothScrollToPosition(3);
                break;
            case R.id.iv_dianzan:
                //点赞图标
                if (ShareUtil.isExist(this, Constanst.UER_TOKEN)) {
                    PostDianZanList();
                } else {
                    ToastUtil.show(this, getResources().getString(R.string.need_login_first), 0);
                }
                break;
        }
    }

    //点赞请求
    private void PostDianZanList() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("controller", "news");
        map.put("action", "dianzan");
        map.put("id", id);
        if (isDianZan) {
            //代表该文章已经点赞，再次请求接口是取消点赞
            map.put("type", "1");//这里的type 0 代表点赞 1 代表取消点赞
        } else {
            //代表该文章没有点赞，再次请求接口是给文章点赞
            map.put("type", "0");//这里的type 0 代表点赞 1 代表取消点赞
        }
        getJsonUtil().PostJson(this, map, iv_dianzan);
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

    //请求新闻评论列表
    public void PostCommentsList() {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("controller", "news");
        map.put("action", "comments");
        map.put("id", id);
        map.put("page", pageno);
        getJsonUtil().PostJson(this, map);
    }
}
