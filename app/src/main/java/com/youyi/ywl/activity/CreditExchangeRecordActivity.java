package com.youyi.ywl.activity;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.youyi.ywl.R;
import com.youyi.ywl.adapter.CreditExchangeRecordAdapter;
import com.youyi.ywl.other.Constanst;
import com.youyi.ywl.util.HttpUtils;
import com.youyi.ywl.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/6.
 * 积分兑换记录界面
 */

public class CreditExchangeRecordActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;
    @BindView(R.id.ll_preload_layout)
    LinearLayout ll_preload_layout;//预载布局
    @BindView(R.id.iv_preload_layout)
    ImageView iv_preload_layout;//预载布局的图片
    @BindView(R.id.tv_preload_layout)
    TextView tv_preload_layout;//预载布局的文字

    private static final String DATA_URL = HttpUtils.Host + "/scoreMall/exchangeLog";
    private CreditExchangeRecordAdapter creditExchangeRecordAdapter;
    private TextView tv_status;
    private int pageno = 1;//分页页数
    List<HashMap<String, Object>> dataList = new ArrayList<>();//数据集合
    private boolean isLoadMore = false;

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case DATA_URL:

                dismissLoadingLayout();

                if (Constanst.success_net_code.equals(code)) {
                    HashMap<String, Object> resultMap = (HashMap<String, Object>) result;

                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {

                        //兑换记录列表数据
                        ArrayList<HashMap<String, Object>> arrayList = (ArrayList<HashMap<String, Object>>) resultMap.get("data");
                        if (arrayList != null && arrayList.size() > 0) {
                            if (isLoadMore) {
                                xRecyclerView.loadMoreComplete();
                                isLoadMore = false;
                                this.dataList.addAll(arrayList);
                            } else {
                                this.dataList.addAll(arrayList);
                            }
                            creditExchangeRecordAdapter.notifyDataSetChanged();

                        } else {
                            if (isLoadMore) {
                                xRecyclerView.loadMoreComplete();
                                isLoadMore = false;
                                pageno--;
                                tv_status.setText(resultMap.get("msg") + "");
                                xRecyclerView.setNoMore(true);
                            } else {
                                if (pageno == 1) {
                                    showErrorOrNothingLayout();
                                } else {
                                    ToastUtil.show(this, resultMap.get("msg") + "", 0);
                                }
                            }
                        }
                    } else {
                        //1或其他代表无数据或者有问题
                        if (isLoadMore) {
                            isLoadMore = false;
                            pageno--;
                            xRecyclerView.loadMoreComplete();
                            tv_status.setText(resultMap.get("msg") + "");
                            xRecyclerView.setNoMore(true);
                        } else {
                            if (pageno == 1) {
                                showErrorOrNothingLayout();
                            } else {
                                ToastUtil.show(this, resultMap.get("msg") + "", 0);
                            }
                        }
                    }
                } else {
                    if (isLoadMore) {
                        isLoadMore = false;
                        pageno--;
                        xRecyclerView.loadMoreComplete();
                        tv_status.setText(msg);
                    } else {
                        if (pageno == 1) {
                            showErrorOrNothingLayout();
                        } else {
                            ToastUtil.show(this, msg, 0);
                        }
                    }

                }
                break;
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_credit_exchange_record);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        showLoadingLayout();
        tv_title.setText("兑换记录");
        PostList();
        initRecyclerView();
    }

    private void PostList() {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("controller", "scoreMall");
        map.put("action", "exchangeLog");
        map.put("page", pageno + "");
        getJsonUtil().PostJson(this, map);
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        xRecyclerView.setLayoutManager(manager);
        creditExchangeRecordAdapter = new CreditExchangeRecordAdapter(this, dataList);
        xRecyclerView.setAdapter(creditExchangeRecordAdapter);
        View headerView1 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        View headerView2 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        xRecyclerView.addHeaderView(headerView1);
        xRecyclerView.addHeaderView(headerView2);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                isLoadMore = true;
                pageno++;
                PostList();
                tv_status.setText("正在加载...");
            }
        });

        //自定义加载footer
        View footerView = LayoutInflater.from(this).inflate(R.layout.layout_recyclerview_footer, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        footerView.setLayoutParams(layoutParams);
        xRecyclerView.setFootView(footerView);
        tv_status = (TextView) footerView.findViewById(R.id.tv_status);
        tv_status.setTextColor(getResources().getColor(R.color.grayone));
    }

    @Override
    public void afterInitView() {
    }

    private AnimationDrawable drawable;

    //显示加载中的布局
    private void showLoadingLayout() {
        ll_preload_layout.setVisibility(View.VISIBLE);
        iv_preload_layout.setImageResource(R.drawable.animation_boy_running);
        drawable = (AnimationDrawable) iv_preload_layout.getDrawable();
        drawable.start();
        tv_preload_layout.setText("努力加载中...");
        tv_preload_layout.setTextColor(getResources().getColor(R.color.normal_black));
    }

    //隐藏预加载布局
    private void dismissLoadingLayout() {
        drawable.stop();
        ll_preload_layout.setVisibility(View.GONE);
    }

    //显示网络请求错误的布局或者第一页没有数据的布局
    private void showErrorOrNothingLayout() {
        ll_preload_layout.setVisibility(View.VISIBLE);
        iv_preload_layout.setImageResource(R.mipmap.img_boy_find);
        tv_preload_layout.setText("这里什么都没有");
        tv_preload_layout.setTextColor(getResources().getColor(R.color.grayone));
    }

    @OnClick({R.id.ll_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }
}
