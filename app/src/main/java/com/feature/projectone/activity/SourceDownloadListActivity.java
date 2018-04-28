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
import com.feature.projectone.adapter.SourceDownloadListAdapter;
import com.feature.projectone.adapter.SourceLoadingListAdapter;
import com.feature.projectone.bean.CheckItem;
import com.feature.projectone.inter.RecyclerViewOnItemClickListener;
import com.feature.projectone.util.FileUtil;
import com.feature.projectone.util.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/24.
 * 我的资源列表(下载和已下载的文件)界面
 */

public class SourceDownloadListActivity extends BaseActivity {
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
    @BindView(R.id.xRecyclerView_loading)
    XRecyclerView xRecyclerView_loading;

    private SourceDownloadListAdapter sourceDownloadListAdapter;
    private List<CheckItem> checkList = new ArrayList<>();//用来记录下载完成列表checkbox是否被选中集合
    private List<CheckItem> checkListLoading = new ArrayList<>();//用来记录下载暂停列表checkbox是否被选中集合
    private boolean isCheckBoxVisible;//记录checkbox的状态
    private int count = 0;//记录被选中的条目数量
    private List<HashMap<String, Object>> mDataList = new ArrayList<>();//存储列表下载完成的数据集合
    private List<HashMap<String, Object>> loadingDataList = new ArrayList<>();//存储列表中下载未完成的数据集合
    private SourceLoadingListAdapter sourceLoadingListAdapter;

    @Override
    protected void Response(String code, String msg, String url, Object result) {
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_resource_download_list);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        initData();//初始化文件列表数据
        initRcyclerView();//初始化下载完成的列表
        initLoadingRecyclerView();//初始化下载中的列表
    }


    //查询下载任务信息，放入集合存储
    private void initData() {
        //已完成的任务
        mDataList.clear();
        OkDownload.restore(DownloadManager.getInstance().getAll());
        List<DownloadTask> tasks = OkDownload.restore(DownloadManager.getInstance().getFinished());
        Log.i("sourceDownload", "     tasks.size()     " + tasks.size());
        for (int i = 0; i < tasks.size(); i++) {
            DownloadTask task = tasks.get(i);
            HashMap<String, Object> map = new HashMap<>();
            Log.i("sourceDownload", "     task.progress     " + task.progress.toString());
//            Log.i("sourceDownload", "     task.progress.extra1     " + task.progress.extra1);
//            Log.i("sourceDownload", "     task.progress.extra2     " + task.progress.extra2);
//            Log.i("sourceDownload", "     task.progress.extra3     " + task.progress.extra3);
            String folderName = task.progress.extra3 + "";//解压的文件夹名称
            map.put("name", task.progress.extra1 + "");//资料名称
            map.put("time", task.progress.extra2 + "");//下载时间
            map.put("folderName", folderName);//下载并解压后的目录路径
            //计算文件大小
            String folderSize = FileUtil.getAutoFileOrFilesSize(folderName);
            map.put("size", folderSize);
            map.put("tag", task.progress.tag);
            mDataList.add(map);
        }

        checkList.clear();
        for (int i = 0; i < mDataList.size(); i++) {
            CheckItem checkItem = new CheckItem();
            checkList.add(checkItem);
        }

        //未完成，下载中的任务
        loadingDataList.clear();
        List<DownloadTask> tasks1 = OkDownload.restore(DownloadManager.getInstance().getDownloading());
        Log.i("sourceDownload", "     tasks1.size()     " + tasks1.size());
        for (int i = 0; i < tasks1.size(); i++) {
            DownloadTask task1 = tasks1.get(i);
            HashMap<String, Object> map1 = new HashMap<>();
            Log.i("sourceDownload", "     task1.progress     " + task1.progress.toString());
            map1.put("name", task1.progress.extra1 + "");//资料名称
            map1.put("status", task1.progress.status + "");//下载状态
            map1.put("totalSize", task1.progress.totalSize + "");//文件总大小
            map1.put("currentSize", task1.progress.currentSize + "");//当前下载的大小
            map1.put("filePath", task1.progress.filePath);
            map1.put("tag", task1.progress.tag);
            loadingDataList.add(map1);

            //注册下载监听
            task1.register(new DownLoadingListener(task1.progress.tag, i));
        }

        checkListLoading.clear();
        for (int i = 0; i < loadingDataList.size(); i++) {
            CheckItem checkItem = new CheckItem();
            checkListLoading.add(checkItem);
        }
    }

    private void initRcyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        xRecyclerView.setLayoutManager(manager);
        sourceDownloadListAdapter = new SourceDownloadListAdapter(this, checkList, mDataList);
        xRecyclerView.setAdapter(sourceDownloadListAdapter);
        View headerView1 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        View headerView2 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        xRecyclerView.addHeaderView(headerView1);
        xRecyclerView.addHeaderView(headerView2);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(false);

        //设置checkbox点击接口监听
        sourceDownloadListAdapter.setOnSelectListener(new SourceDownloadListAdapter.OnSelectListener() {
            @Override
            public void SelectListener(int position) {
                if (checkList.get(position).isSelect()) {
                    //如果当前是选中的，点击后置为false;
                    checkList.get(position).setSelect(false);
                    count--;
                } else {
                    //如果当前是没选中的，点击后置为true;
                    checkList.get(position).setSelect(true);
                    count++;
                }
                tv_count.setText(count + "");//设置被选中的条目数
                sourceDownloadListAdapter.notifyDataSetChanged();
            }
        });

        //点击条目跳转到下级界面
        sourceDownloadListAdapter.setOnItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                HashMap<String, Object> map = mDataList.get(position);
                Intent intent = new Intent(SourceDownloadListActivity.this, SourceDownloadListDetailActivity.class);
                intent.putExtra("name", map.get("name") + "");
                intent.putExtra("folderName", map.get("folderName") + "");
                startActivity(intent);
            }
        });

        //设置全选按钮的checked状态改变监听
        checkBox_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        });
    }

    private void initLoadingRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        xRecyclerView_loading.setLayoutManager(manager);
        sourceLoadingListAdapter = new SourceLoadingListAdapter(this, checkListLoading, loadingDataList);
        xRecyclerView_loading.setAdapter(sourceLoadingListAdapter);
        View headerView1 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        View headerView2 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        xRecyclerView_loading.addHeaderView(headerView1);
        xRecyclerView_loading.addHeaderView(headerView2);
        xRecyclerView_loading.setPullRefreshEnabled(false);
        xRecyclerView_loading.setLoadingMoreEnabled(false);

        //设置checkbox点击接口监听
        sourceLoadingListAdapter.setOnSelectListener(new SourceLoadingListAdapter.OnSelectListener() {
            @Override
            public void SelectListener(int position) {
                if (checkListLoading.get(position).isSelect()) {
                    //如果当前是选中的，点击后置为false;
                    checkListLoading.get(position).setSelect(false);
                    count--;
                } else {
                    //如果当前是没选中的，点击后置为true;
                    checkListLoading.get(position).setSelect(true);
                    count++;
                }
                tv_count.setText(count + "");//设置被选中的条目数
                sourceLoadingListAdapter.notifyDataSetChanged();
            }
        });

        //设置继续下载 与 暂停下载按钮的监听
        sourceLoadingListAdapter.setOnPlayClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                HashMap<String, Object> map = loadingDataList.get(position);
                String tag = map.get("tag") + "";
                DownloadTask task = OkDownload.getInstance().getTask(tag);
                if (task == null) {
                    return;
                }
                int status = task.progress.status;
                switch (status) {
                    case Progress.NONE:
                        //无状态
                        task.start();
                        break;
                    case Progress.WAITING:
                        //等待
                        break;
                    case Progress.LOADING:
                        //下载中
                        task.pause();
                        break;
                    case Progress.PAUSE:
                        //暂停
                        task.start();
                        break;
                    case Progress.ERROR:
                        //错误
                        break;
                    case Progress.FINISH:
                        //完成
                        break;
                }
                initData();
                sourceLoadingListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void afterInitView() {
    }


    //全选和取消全选
    public void selectOrCancelAll(boolean selectOrCancel) {
        if (selectOrCancel) {
            //全选（下载完成）
            for (int i = 0; i < checkList.size(); i++) {
                checkList.get(i).setSelect(true);
            }
            //全选（下载未完成）
            for (int i = 0; i < checkListLoading.size(); i++) {
                checkListLoading.get(i).setSelect(true);
            }
            count = checkList.size() + checkListLoading.size();
        } else {
            //取消全选（下载完成）
            for (int i = 0; i < checkList.size(); i++) {
                checkList.get(i).setSelect(false);
            }
            //取消全选（下载未完成）
            for (int i = 0; i < checkListLoading.size(); i++) {
                checkListLoading.get(i).setSelect(false);
            }
            count = 0;
        }
        tv_count.setText(count + "");//设置被选中的条目数
        sourceDownloadListAdapter.notifyDataSetChanged();
        sourceLoadingListAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.tv_editOrCancel, R.id.ll_delete, R.id.ll_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_editOrCancel:
                disPlayOrHide();
                break;
            case R.id.ll_delete:
                //点击删除按钮（删除选中的下载完成的文件）
                for (int i = 0; i < checkList.size(); i++) {
                    boolean select = checkList.get(i).isSelect();
                    Log.i("ll_delete", "已完成  第" + i + "个条目的选中状态为" + select);
                    if (select) {
                        //该条木选中状态点击了删除按钮，清除对应的文件夹
                        HashMap<String, Object> map = mDataList.get(i);
                        String folderName = map.get("folderName") + "";//对应的文件夹路径
                        String substring = folderName.substring(folderName.lastIndexOf("/"));
                        String newFolderName = folderName.substring(0, folderName.length() - substring.length());
                        Log.i("sourceDownload", "     newFolderName      " + newFolderName);
                        FileUtil.deleteDir(newFolderName);
                        //删除该条对应的下载任务
                        DownloadTask task = OkDownload.getInstance().getTask(map.get("tag") + "");
                        Log.i("sourceDownload", "     delete task.progress     " + task.progress.toString());
                        task.remove(true);
                    }
                }

                //点击删除按钮（删除选中的下载未完成的文件）
                for (int i = 0; i < checkListLoading.size(); i++) {
                    boolean select = checkListLoading.get(i).isSelect();
                    Log.i("ll_delete", "未完成   第" + i + "个条目的选中状态为" + select);
                    if (select) {
                        HashMap<String, Object> map = loadingDataList.get(i);
                        String filePath = map.get("filePath") + "";
                        File file = new File(filePath);
                        if (file.exists() && file.isFile()) {
                            //文件存在，并且是一个文件时，直接删除
                            file.delete();
                        }
                        //删除该条对应的下载任务
                        DownloadTask task1 = OkDownload.getInstance().getTask(map.get("tag") + "");
                        Log.i("sourceDownload", "     delete task1.progress     " + task1.progress.toString());
                        task1.remove(true);
                    }
                }

                //刷新适配器
                disPlayOrHide();
                initData();
                sourceDownloadListAdapter.notifyDataSetChanged();
                break;
            case R.id.ll_back:
                finish();
                break;
        }
    }

    //显示或隐藏删除选择布局
    private void disPlayOrHide() {

        if (isCheckBoxVisible) {
            isCheckBoxVisible = false;
            //checkbox显示状态，做出隐藏动作，并清空所有的被选中状态为未选中状态(下载已完成状态的recyclerview)
            sourceDownloadListAdapter.setChecBoxVisible(false);

            //checkbox显示状态，做出隐藏动作，并清空所有的被选中状态为未选中状态(下载未完成状态的recyclerview)
            sourceLoadingListAdapter.setChecBoxVisible(false);

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
            sourceDownloadListAdapter.setChecBoxVisible(true);
            sourceLoadingListAdapter.setChecBoxVisible(true);

            ll_caozuo.setVisibility(View.VISIBLE);//显示删除布局
        }
    }

    private class DownLoadingListener extends DownloadListener {
        private int position;

        public DownLoadingListener(Object tag, int position) {
            super(tag);
            this.position = position;
        }

        @Override
        public void onStart(Progress progress) {
            Log.i("ListDownloadListener", "          onStart              " + progress.toString());
        }

        @Override
        public void onProgress(Progress progress) {
//            Log.i("ListDownloadListener", "          onProgress              " + progress.toString());
            HashMap<String, Object> map = loadingDataList.get(position);
            map.put("status", progress.status + "");//下载状态
            map.put("totalSize", progress.totalSize + "");//文件总大小
            map.put("currentSize", progress.currentSize + "");//当前下载的大小
            loadingDataList.set(position, map);
            sourceLoadingListAdapter.notifyDataSetChanged();
        }

        @Override
        public void onError(Progress progress) {
            Log.i("ListDownloadListener", "          onError              " + progress.toString());
        }

        @Override
        public void onFinish(File file, Progress progress) {
            Log.i("ListDownloadListener", "          onFinish  下载列表界面 开始解压            " + progress.toString());
            solveZipFile(progress);
        }

        @Override
        public void onRemove(Progress progress) {
        }
    }

    //解压下载下来的zip文件
    private void solveZipFile(Progress progress) {
        String filePath = progress.filePath;
        String outPath = progress.folder + "/" + System.currentTimeMillis();//多加一层目录，文件夹名称取当前的时间毫秒值，避免加压出的文件名称重复覆盖
        try {
            ZipFile zFile = new ZipFile(filePath);
            zFile.setFileNameCharset("GBK");//设置编码，防止乱码
            if (!zFile.isValidZipFile()) {
                throw new ZipException("文件不合法");
            }
            File desDir = new File(outPath);
            if (desDir.isDirectory() && !desDir.exists()) {
                desDir.mkdir();
            }
            if (zFile.isEncrypted()) {
                zFile.setPassword("123".toCharArray());
            }
            int total = zFile.getFileHeaders().size();//解压总进度
            for (int i = 0; i < zFile.getFileHeaders().size(); i++) {
                FileHeader fh = ((FileHeader) zFile.getFileHeaders().get(i));
                //在递归解压文件中，首次解压的就是最外层的文件夹名称，拿到文件夹名称并将该文件夹的路径存储在task.progress.extra3当中
                if (i == 0) {
                    DownloadTask task = OkDownload.getInstance().getTaskMap().get(progress.tag);//获取当前下载的任务
                    String fileName = outPath + "/" + fh.getFileName().substring(0, fh.getFileName().length() - 1);//文件夹名称
                    //检查下载目录是否有同名的文件夹，有的话就改名字
                    File file = new File(fileName);
                    if (!file.exists()) {
                        task.progress.extra3 = fileName;//拿到文件夹名称并将该文件夹的路径存储在task.progress.extra3当中
                        task.save();
                    }
                }
                Log.i("zip文件中文件夹的名称", "               " + fh.getFileName());
                zFile.extractFile(fh, outPath);
            }
        } catch (ZipException e) {
            e.printStackTrace();
            //抛出异常，表示解压失败
            return;
        }
        //解压成功，删除下载的.zip文件
        File downloadZipFile = new File(filePath);
        if (downloadZipFile.exists()) {
            downloadZipFile.delete();
        }

        //刷新适配器
        initData();
        sourceDownloadListAdapter.notifyDataSetChanged();
        sourceDownloadListAdapter.notifyDataSetChanged();
    }
}
