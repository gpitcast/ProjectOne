package com.youyi.YWL.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.youyi.YWL.R;
import com.youyi.YWL.adapter.RechargeAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/12.
 * 未来币充值界面
 */

public class RechargeActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.gridView)
    GridView gridView;

    private List<HashMap<String, Object>> gridViewList;
    private RechargeAdapter rechargeAdapter;

    @Override
    protected void Response(String code, String msg, String url, Object result) {

    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_recharge);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        tv_title.setText("未来币充值");
        initGridViewData();
        initGridView();
    }

    private void initGridViewData() {
        gridViewList = new ArrayList<>();

        HashMap<String, Object> map0 = new HashMap<>();
        map0.put("amount", "8.00");
        map0.put("isChecked", false);
        gridViewList.add(map0);

        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("amount", "58.00");
        map1.put("isChecked", false);
        gridViewList.add(map1);

        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("amount", "88.00");
        map2.put("isChecked", false);
        gridViewList.add(map2);

        HashMap<String, Object> map3 = new HashMap<>();
        map3.put("amount", "8.00");
        map3.put("isChecked", false);
        gridViewList.add(map3);

        HashMap<String, Object> map4 = new HashMap<>();
        map4.put("amount", "108.00");
        map4.put("isChecked", false);
        gridViewList.add(map4);

        HashMap<String, Object> map5 = new HashMap<>();
        map5.put("amount", "188.00");
        map5.put("isChecked", false);
        gridViewList.add(map5);

        HashMap<String, Object> map6 = new HashMap<>();
        map6.put("amount", "588.00");
        map6.put("isChecked", false);
        gridViewList.add(map6);

        HashMap<String, Object> map7 = new HashMap<>();
        map7.put("amount", "888.00");
        map7.put("isChecked", false);
        gridViewList.add(map7);

        HashMap<String, Object> map8 = new HashMap<>();
        map8.put("amount", "1088.00");
        map8.put("isChecked", false);
        gridViewList.add(map8);
    }

    private void initGridView() {
        rechargeAdapter = new RechargeAdapter(this, gridViewList);
        gridView.setAdapter(rechargeAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> map = gridViewList.get(position);
                Boolean isChecked = (Boolean) map.get("isChecked");
                
            }
        });
    }

    @Override
    public void afterInitView() {

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
