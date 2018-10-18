package com.youyi.ywl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.youyi.ywl.R;
import com.youyi.ywl.adapter.AddressManageAdapter;
import com.youyi.ywl.inter.ReplyIconClickListener;
import com.youyi.ywl.other.Constanst;
import com.youyi.ywl.util.HttpUtils;
import com.youyi.ywl.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/9/3.
 * 个人地址管理界面
 */

public class AddressManageActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;
    @BindView(R.id.ll_add_new_address)
    LinearLayout ll_add_new_address;

    private static final String ADDRESS_LIST_URL = HttpUtils.Host + "/user/myAddress";//收货地址列表接口
    private List<HashMap<String, Object>> dataList = new ArrayList<>();//列表数据
    private static final String DELETE_URL = HttpUtils.Host + "/user/delAddress";//删除收货地址接口
    private AddressManageAdapter addressManageAdapter;
    private int deletePosition = -1;//记录删除的position

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case ADDRESS_LIST_URL:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        //0代表正常
                        ArrayList<HashMap<String, Object>> arrayList = (ArrayList<HashMap<String, Object>>) resultMap.get("data");
                        if (arrayList != null && arrayList.size() > 0) {
                            if (ll_add_new_address.getVisibility() == View.VISIBLE) {
                                ll_add_new_address.setVisibility(View.GONE);
                            }
                            dataList.clear();
                            xRecyclerView.setVisibility(View.VISIBLE);
                            dataList.addAll(arrayList);
                            addressManageAdapter.notifyDataSetChanged();
                        } else {
                            ll_add_new_address.setVisibility(View.VISIBLE);
                        }
                    } else {
                        //其他代表不正常
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                    }
                } else {
                    ToastUtil.show(this, msg, 0);
                }
                break;
            case DELETE_URL:
                dismissLoadingDialog();

                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status) && deletePosition != -1) {
                        dataList.remove(deletePosition);
                        addressManageAdapter.notifyDataSetChanged();

                        if (dataList.size() == 0) {
                            xRecyclerView.setVisibility(View.GONE);
                            ll_add_new_address.setVisibility(View.VISIBLE);
                        }
                    }
                    ToastUtil.show(this, resultMap.get("msg") + "", 0);
                } else {
                    ToastUtil.show(this, msg, 0);
                }
                break;
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_address_manage);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);

        tv_title.setText("地址管理");
        PostAddressList();//请求收货列表数据接口
        initRecyclerView();
    }

    //请求收货列表数据接口
    private void PostAddressList() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user");
        map.put("action", "myAddress");
        getJsonUtil().PostJson(this, map);
    }

    //初始化地址列表
    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        xRecyclerView.setLayoutManager(manager);
        addressManageAdapter = new AddressManageAdapter(this, dataList);
        xRecyclerView.setAdapter(addressManageAdapter);

        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(false);

        addressManageAdapter.setOnEditClickListener(new ReplyIconClickListener() {
            @Override
            public void OnReplyIconClick(View view, int position) {
                HashMap<String, Object> map = dataList.get(position);
                Intent intent = new Intent(AddressManageActivity.this, AddressEditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("dataMap", map);
                intent.putExtras(bundle);
                intent.putExtra("type", "0");
                startActivity(intent);
            }
        });

        addressManageAdapter.setOnDeleteClickListener(new ReplyIconClickListener() {
            @Override
            public void OnReplyIconClick(View view, int position) {
                showLoadingDialog();

                HashMap<String, Object> map = dataList.get(position);
                PostDeleteList(map.get("id") + "");//请求删除地址接口
                deletePosition = position;
            }
        });
    }

    //请求删除地址接口
    private void PostDeleteList(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user");
        map.put("action", "delAddress");
        map.put("id", id);
        getJsonUtil().PostJson(this, map);
    }

    @Override
    public void afterInitView() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(String str) {
        if (str == null) {
            return;
        }
        switch (str) {
            case "刷新收货地址列表":
                PostAddressList();
                break;
        }
    }

    @OnClick({R.id.ll_back, R.id.tv_new_address})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_new_address:
                //新建地址
                Intent intent = new Intent(this, AddressEditActivity.class);
                intent.putExtra("type", "1");
                startActivity(intent);
                break;
        }
    }
}
