package com.youyi.ywl.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.youyi.ywl.R;
import com.youyi.ywl.adapter.CreditMallAdapter;
import com.youyi.ywl.inter.RecyclerViewOnItemClickListener;
import com.youyi.ywl.other.Constanst;
import com.youyi.ywl.util.HttpUtils;
import com.youyi.ywl.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/9.
 * 积分商城界面
 */

public class CreditMallActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;
    @BindView(R.id.tv_credit_count)
    TextView tv_credit_count;
    @BindView(R.id.ll_preload_layout)
    LinearLayout ll_preload_layout;//预载布局
    @BindView(R.id.iv_preload_layout)
    ImageView iv_preload_layout;//预载布局的图片
    @BindView(R.id.tv_preload_layout)
    TextView tv_preload_layout;//预载布局的文字


    private static final String DATA_URL = HttpUtils.Host + "/scoreMall/goods";
    private CreditMallAdapter creditMallAdapter;
    private TextView tv_status;
    private int pageno = 1;//分页的页数
    private List<HashMap<String, Object>> dataList = new ArrayList<>();//数据
    private boolean isLoadMore = false;

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case DATA_URL:

                dismissLoadingLayout();

                if (Constanst.success_net_code.equals(code)) {
                    HashMap<String, Object> resultMap = (HashMap<String, Object>) result;
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) resultMap.get("data");

                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        tv_credit_count.setText(dataMap.get("score") + "");

                        //商品列表数据
                        ArrayList<HashMap<String, Object>> arrayList = (ArrayList<HashMap<String, Object>>) dataMap.get("goodsLists");
                        if (arrayList != null && arrayList.size() > 0) {
                            if (isLoadMore) {
                                xRecyclerView.loadMoreComplete();
                                isLoadMore = false;
                                dataList.addAll(arrayList);
                            } else {
                                dataList.addAll(arrayList);
                            }
                            creditMallAdapter.notifyDataSetChanged();

                        } else {
                            if (isLoadMore) {
                                xRecyclerView.loadMoreComplete();
                                isLoadMore = false;
                                pageno--;
                                tv_status.setText(resultMap.get("msg") + "");
                                xRecyclerView.setNoMore(true);
                            } else {
                                ToastUtil.show(this, resultMap.get("msg") + "", 0);
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
                            ToastUtil.show(this, resultMap.get("msg") + "", 0);
                        }
                    }

                } else {
                    if (isLoadMore) {
                        isLoadMore = false;
                        pageno--;
                        xRecyclerView.loadMoreComplete();
                        tv_status.setText(msg);
                    } else {
                        ToastUtil.show(this, msg, 0);
                    }
                }
                break;
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_credit_mall);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);

        showLoadingLayout();
        tv_title.setText("积分商城");
        tv_right.setText("说明");
        PostList();
        initRecyclerView();
    }

    //商城商品列表接口
    private void PostList() {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("controller", "scoreMall");
        map.put("action", "goods");
        map.put("page", pageno + "");
        getJsonUtil().PostJson(this, map);
    }


    private void initRecyclerView() {
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        xRecyclerView.setLayoutManager(manager);
        creditMallAdapter = new CreditMallAdapter(this, dataList);
        xRecyclerView.setAdapter(creditMallAdapter);
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

        creditMallAdapter.setOnItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                HashMap<String, Object> map = dataList.get(position);
                Intent intent = new Intent(CreditMallActivity.this, CreditGoodsDetailsActivity.class);
                intent.putExtra("name", map.get("title") + "");
                intent.putExtra("id", map.get("id") + "");
                startActivity(intent);
            }
        });

        //自定义加载footer
        View footerView = LayoutInflater.from(this).inflate(R.layout.layout_recyclerview_footer, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        footerView.setLayoutParams(layoutParams);
        xRecyclerView.setFootView(footerView);
        footerView.setBackgroundColor(getResources().getColor(R.color.light_gray5));
        tv_status = (TextView) footerView.findViewById(R.id.tv_status);
        tv_status.setTextColor(getResources().getColor(R.color.grayone));
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

    @Override
    public void afterInitView() {
    }

    @Subscribe
    public void onEventMainThread(String str) {
        if (str == null) {
            return;
        }

        if (str.startsWith("商品兑换成功")) {
            String creditCountStr = str.substring(6);
            int creditCount = Integer.parseInt(creditCountStr);//扣除的积分
            String creditTotalCountStr = tv_credit_count.getText().toString().trim();
            int creditTotalCount = Integer.parseInt(creditTotalCountStr);
            tv_credit_count.setText((creditTotalCount - creditCount) + "");
        }

    }

    @OnClick({R.id.ll_back, R.id.ll_exchange_history})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_exchange_history:
                //兑换记录
                startActivity(new Intent(this, CreditExchangeRecordActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
