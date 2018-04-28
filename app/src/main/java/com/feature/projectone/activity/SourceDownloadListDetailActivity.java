package com.feature.projectone.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.adapter.SourceDownloadListDetailAdapter;
import com.feature.projectone.bean.CheckItem;
import com.feature.projectone.inter.RecyclerViewOnItemClickListener;
import com.feature.projectone.util.FileUtil;
import com.feature.projectone.util.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/24.
 * 我的资源列表条目详情(下载和已下载的文件)界面
 */

public class SourceDownloadListDetailActivity extends BaseActivity implements SourceDownloadListDetailAdapter.OnSelectListener, CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;
    @BindView(R.id.checkBox_all)
    CheckBox checkBox_all;
    @BindView(R.id.tv_count)
    TextView tv_count;
    @BindView(R.id.ll_caozuo)
    LinearLayout ll_caozuo;
    @BindView(R.id.ll_delete)
    LinearLayout ll_delete;
    @BindView(R.id.tv_title)
    TextView tv_title;

    private SourceDownloadListDetailAdapter sourceDownloadListDetailAdapter;
    private List<CheckItem> checkedList = new ArrayList<>();//用来记录checkbox是否被选中集合
    private int count = 0;//记录被选中的条目数量
    private boolean isCheckBoxVisible;//记录checkbox的状态
    private String folderName;
    private List<HashMap<String, Object>> mDataList = new ArrayList<>();//存储文件信息的集合
    private String name;

    @Override
    protected void handleIntent(Intent intent) {
        folderName = intent.getStringExtra("folderName");
        name = intent.getStringExtra("name");
        Log.i("DownloadListDetail", "        folderName        " + folderName);
        Log.i("DownloadListDetail", "        name        " + name);
    }

    @Override
    protected void Response(String code, String msg, String url, Object result) {
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_resource_download_list_detail);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tv_title.setText(name);
        initData();//初始化数据
        initRecyclerView();
    }

    private void initData() {
        mDataList.clear();
        checkedList.clear();

        File folder = new File(folderName);
        if (folder == null || !folder.exists() || !folder.isDirectory()) {
            return;
        }
        for (File file : folder.listFiles()) {
            HashMap<String, Object> map = new HashMap<>();
            if (file.isFile()) {
                map.put("type", "0");//文件
            } else {
                map.put("type", "1");//文件夹
            }
            map.put("fileName", file.getName());
            map.put("size", FileUtil.getAutoFileOrFilesSize(file.getPath()));
            map.put("folderName", folderName);
            map.put("filePath", folderName + "/" + file.getName());
            mDataList.add(map);
        }

        for (int i = 0; i < mDataList.size(); i++) {
            CheckItem checkItem = new CheckItem();
            checkedList.add(checkItem);
        }
    }


    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        xRecyclerView.setLayoutManager(manager);
        sourceDownloadListDetailAdapter = new SourceDownloadListDetailAdapter(this, checkedList, mDataList);
        xRecyclerView.setAdapter(sourceDownloadListDetailAdapter);
        View headView1 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        View headView2 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        xRecyclerView.addHeaderView(headView1);
        xRecyclerView.addHeaderView(headView2);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(false);


        sourceDownloadListDetailAdapter.setOnSelectListener(this);

        checkBox_all.setOnCheckedChangeListener(this);//设置全选按钮的checked状态改变监听

        sourceDownloadListDetailAdapter.setOnItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                ToastUtil.show(SourceDownloadListDetailActivity.this, "点击了第" + position + "个条目", 0);
                HashMap<String, Object> map = mDataList.get(position);
                Intent intent = new Intent(SourceDownloadListDetailActivity.this, SourceDownloadSeeFileActivity.class);
                intent.putExtra("fileName", map.get("fileName") + "");
                intent.putExtra("filePath", map.get("filePath") + "");
                startActivity(intent);
            }
        });
    }

    @Override
    public void afterInitView() {
    }

    @Override
    public void SelectListener(int position) {
        if (checkedList.get(position).isSelect()) {
            //如果当前是选中的，点击后置为false;
            checkedList.get(position).setSelect(false);
            count--;
        } else {
            //如果当前是没选中的，点击后置为true;
            checkedList.get(position).setSelect(true);
            count++;
        }
        tv_count.setText(count + "");//设置被选中的条目数
        sourceDownloadListDetailAdapter.notifyDataSetChanged();
    }

    //全选和取消全选
    public void selectOrCancelAll(boolean selectOrCancel) {
        if (selectOrCancel) {
            //全选
            for (int i = 0; i < checkedList.size(); i++) {
                checkedList.get(i).setSelect(true);
            }
            count = checkedList.size();
        } else {
            //取消全选
            for (int i = 0; i < checkedList.size(); i++) {
                checkedList.get(i).setSelect(false);
            }
            count = 0;
        }
        tv_count.setText(count + "");//设置被选中的条目数
        sourceDownloadListDetailAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.tv_editOrCancel, R.id.ll_delete, R.id.ll_back})
    public void OnClick(View view) {
        switch (view.getId()) {

            case R.id.tv_editOrCancel:
                disPlayOrHide();
                break;
            case R.id.ll_delete:
                //点击删除按钮
                for (int i = 0; i < checkedList.size(); i++) {
                    boolean select = checkedList.get(i).isSelect();
                    if (select) {
                        HashMap<String, Object> map = mDataList.get(i);
                        String filePath = map.get("filePath") + "";//获取文件的路径
                        //判断一下文件是文件还是文件，不同的删除方式
                        File file = new File(filePath);
                        if (file != null) {
                            if (file.isFile()) {
                                file.delete();
                            } else {
                                FileUtil.deleteDir(filePath);
                            }
                        }
                    }
                }
                disPlayOrHide();
                initData();
                sourceDownloadListDetailAdapter.notifyDataSetChanged();
                break;
            case R.id.ll_back:
                finish();
                break;
        }
    }

    //显示或隐藏删除选择布局
    private void disPlayOrHide() {
        if (isCheckBoxVisible) {
            //checkbox显示状态，做出隐藏动作，并清空所有的被选中状态为未选中状态
            isCheckBoxVisible = false;
            sourceDownloadListDetailAdapter.setChecBoxVisible(false);
            selectOrCancelAll(false);
            count = 0;
            tv_count.setText(count + "");//设置被选中的条目数
            ll_caozuo.setVisibility(View.GONE);//隐藏删除布局
            if (checkBox_all.isChecked()) {
                checkBox_all.setChecked(false);//如果全选按钮被选中的状态下 初始化未选中状态
            }
        } else {
            //checkbox未显示状态，做出显示checkbox动作
            isCheckBoxVisible = true;
            sourceDownloadListDetailAdapter.setChecBoxVisible(true);
            ll_caozuo.setVisibility(View.VISIBLE);//显示删除布局
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked) {
            //点击做出全选操作
            selectOrCancelAll(true);
        } else {
            //点击做出取消全选操作
            selectOrCancelAll(false);
        }
    }
}
