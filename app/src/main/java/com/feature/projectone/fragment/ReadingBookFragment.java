package com.feature.projectone.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.feature.projectone.R;
import com.feature.projectone.adapter.SourceDownloadFragmentAdapter;
import com.feature.projectone.inter.DownloadIconClickListener;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.HttpUtils;
import com.feature.projectone.util.PermissionUtil;
import com.feature.projectone.util.ShareUtil;
import com.feature.projectone.util.ToastUtil;
import com.feature.projectone.view.BaseDialog;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/13.
 * 教材朗读fragment
 */

public class ReadingBookFragment extends BaseFragment {
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;

    private String cate_id;
    private int pageno = 1;//分页
    private boolean isLoadMore;
    private boolean isRefresh;
    private static final String sourceInexUrl = HttpUtils.Host + "/source/index";//下载列表数据接口
    private static final String sourceDownloadUrl = HttpUtils.Host + "/source/download";//下载对应资源链接数据接口
    private List<HashMap<String, Object>> mDataList = new ArrayList<>();//存储列表数据的集合
    private SourceDownloadFragmentAdapter sourceDownloadFragmentAdapter;
    private final int writePermissionCode = 1001;//读取SD卡权限请求码
    private String id;//每条下载条目的id
    private String title;//每一个条目对应的文件名称

    public ReadingBookFragment() {
    }

    @SuppressLint("ValidFragment")
    public ReadingBookFragment(String cate_id) {
        this.cate_id = cate_id;
    }

    //提供方法给附着的activity筛选功能刷新数据
    public void shaiXuanRefresh(String cate_id) {
        this.cate_id = cate_id;
        xRecyclerView.refresh();
    }

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case sourceInexUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap<String, Object> resultMap = (HashMap<String, Object>) result;
                    String status = resultMap.get("status") + "";
                    ArrayList<HashMap<String, Object>> dataList = (ArrayList<HashMap<String, Object>>) resultMap.get("data");

                    if ("0".equals(status)) {
                        //status=0代表有数据
                        if (dataList != null && dataList.size() > 0) {
                            //代表dataList数据集合有数据
                            if (isLoadMore) {
                                xRecyclerView.loadMoreComplete();
                                isLoadMore = false;
                                mDataList.addAll(dataList);
                            } else if (isRefresh) {
                                xRecyclerView.refreshComplete();
                                isRefresh = false;
                                mDataList.clear();
                                mDataList.addAll(dataList);
                            } else {
                                mDataList.addAll(dataList);
                            }
                            sourceDownloadFragmentAdapter.notifyDataSetChanged();
                        } else {
                            //代表dataList数据集合为空
                            if (isLoadMore) {
                                isLoadMore = false;
                                pageno--;
                                xRecyclerView.loadMoreComplete();
                            } else if (isRefresh) {
                                isRefresh = false;
                                mDataList.clear();
                                xRecyclerView.refreshComplete();
                            }
                            sourceDownloadFragmentAdapter.notifyDataSetChanged();
                            ToastUtil.show(getActivity(), msg, 0);
                        }
                    } else {
//                        //status不等于0代表暂无数据，取消下拉或者上拉状态，弹吐司提醒用户
                        if (isLoadMore) {
                            isLoadMore = false;
                            pageno--;
                            xRecyclerView.loadMoreComplete();
                        } else if (isRefresh) {
                            mDataList.clear();
                            isRefresh = false;
                            xRecyclerView.refreshComplete();
                        }
                        sourceDownloadFragmentAdapter.notifyDataSetChanged();
                        ToastUtil.show(getActivity(), resultMap.get("msg") + "", 0);
                    }
                } else {
                    if (isLoadMore) {
                        isLoadMore = false;
                        pageno--;
                        xRecyclerView.loadMoreComplete();
                    } else if (isRefresh) {
                        isRefresh = false;
                        mDataList.clear();
                        xRecyclerView.refreshComplete();
                        sourceDownloadFragmentAdapter.notifyDataSetChanged();
                    }
                    ToastUtil.show(getActivity(), msg, 0);
                }
                break;
            case sourceDownloadUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap<String, Object> resultMap = (HashMap<String, Object>) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        //数据正常
                        String data = resultMap.get("data") + "";//下载链接
                        DownloadFile(data);
                    } else {
                        //数据不正常
                        ToastUtil.show(getActivity(), resultMap.get("msg") + "", 0);
                    }
                } else {
                    ToastUtil.show(getActivity(), msg, 0);
                }

                break;
        }
    }

    private String DOWN_LOAD_URL;

    //下载文件
    private void DownloadFile(String downLoadUrl) {
        OkDownload instance = OkDownload.getInstance();
        Log.i("ListDownloadListener", "          instance              " + instance);


        //1.去除下载链接的不规范符号
        String replaceStr = downLoadUrl.replace("//", "/");
        String replaceStr2 = replaceStr.replace("http:/", "http://");
        this.DOWN_LOAD_URL = replaceStr2;

        //2.从下载的url中截取文件名称
        final String fileName = DOWN_LOAD_URL.substring(DOWN_LOAD_URL.lastIndexOf("/") + 1);

        //检查下载文件夹下是否存在同名文件，如果有同名文件就进一步检查文件的大小，确认大小一样就不重复下载
        final File file = new File(getActivity().getFilesDir(), fileName);
        if (file.exists()) {
            URL url = null;
            try {
                url = new URL(DOWN_LOAD_URL);
                final URL finalUrl = url;
                Executors.newCachedThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        URLConnection conn = null;
                        try {
                            conn = finalUrl.openConnection();
                            int contentLength = conn.getContentLength();
                            Log.i("ListDownloadListener", "          contentLength              " + contentLength);
                            Log.i("ListDownloadListener", "          file.length()              " + file.length());
                            if (contentLength == file.length()) {
                                ToastUtil.show(getActivity(), title + "已存在，不必重复下载", 0);
                                return;
                            } else {
                                Message msg = new Message();
                                msg.what = 0;
                                msg.obj = fileName;
                                handler.sendMessage(msg);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            //文件不存在，直接开爱是下载
            Message msg = new Message();
            msg.what = 0;
            msg.obj = fileName;
            handler.sendMessage(msg);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //开始下载
                    //这里只是演示，表示请求可以传参，怎么传都行，和okgo使用方法一样
                    GetRequest<File> request = OkGo.<File>get(DOWN_LOAD_URL)
                            .headers("aaa", "111")
                            .params("bbb", "222");
                    //这里第一个参数是tag，代表下载任务的唯一标识，传任意字符串都行，需要保证唯一,我这里用url作为了tag
                    DownloadTask task = OkDownload.request(DOWN_LOAD_URL, request)
                            .save()
                            .fileName(msg.obj + "")
                            .register(new ListDownloadListener(DOWN_LOAD_URL, holder));
                    task.start();
                    break;
            }
        }
    };

    private class ListDownloadListener extends DownloadListener {
        private SourceDownloadFragmentAdapter.MyViewHolder holder;

        public ListDownloadListener(Object tag, SourceDownloadFragmentAdapter.MyViewHolder holder) {
            super(tag);
            this.holder = holder;
        }

        @Override
        public void onStart(Progress progress) {
            Log.i("ListDownloadListener", "          onStart              " + progress.toString());
        }

        @Override
        public void onProgress(Progress progress) {
            Log.i("ListDownloadListener", "          onProgress              " + progress.toString());
            if (holder != null) {
                holder.refresh(progress);//刷新进度条
            }
        }

        @Override
        public void onError(Progress progress) {
            Log.i("ListDownloadListener", "          onError              " + progress.toString());
            OkDownload.getInstance().getTask(DOWN_LOAD_URL).restart();
        }

        @Override
        public void onFinish(File file, Progress progress) {
            Log.i("ListDownloadListener", "          onFinish              " + progress.toString());
        }

        @Override
        public void onRemove(Progress progress) {

        }
    }


    @Override
    protected void onFirstUserVisible() {
    }

    @Override
    protected void onUserVisible() {
    }

    @Override
    protected void onUserInvisible() {
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_source_download;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        initRecyclerView();
    }

    private void initRecyclerView() {
        final LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        xRecyclerView.setLayoutManager(manager);
        sourceDownloadFragmentAdapter = new SourceDownloadFragmentAdapter(getActivity(), mDataList);
        xRecyclerView.setAdapter(sourceDownloadFragmentAdapter);
        View headView1 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty_head, null);
        View headView2 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty_head, null);
        xRecyclerView.addHeaderView(headView1);
        xRecyclerView.addHeaderView(headView2);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        //刷新和加载监听
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                pageno = 1;
                PostList();
            }

            @Override
            public void onLoadMore() {
                isLoadMore = true;
                pageno++;
                PostList();
            }
        });
        xRecyclerView.refresh();
        View footerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_recyclerview_footer, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
        layoutParams.gravity = Gravity.CENTER;
        footerView.setLayoutParams(layoutParams);
        footerView.setBackgroundColor(getResources().getColor(R.color.white));
        xRecyclerView.setFootView(footerView);
        xRecyclerView.loadMoreComplete();

        sourceDownloadFragmentAdapter.setOnDownloadIconListener(new DownloadIconClickListener() {

            @Override
            public void OnDownLoadClick(SourceDownloadFragmentAdapter.MyViewHolder holder, View view, int position) {
                id = ((HashMap<String, Object>) mDataList.get(position)).get("id") + "";
                title = ((HashMap<String, Object>) mDataList.get(position)).get("title") + "";
                ReadingBookFragment.this.holder = holder;
                if (ShareUtil.isExist(getActivity(), Constanst.UER_TOKEN)) {
                    if (PermissionUtil.checkPermission(getContext(), ReadingBookFragment.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, writePermissionCode)) {
                        //有权限直接操作
                        PostDownloadList(id);
                    } else {
                        //没有权限,不需要做任何操作，在工具类已经做过请求权限处理，会执行请求权限回调那一步
                    }
                } else {
                    ToastUtil.show(getActivity(), getResources().getString(R.string.need_login_first), 0);
                }
            }
        });

        sourceDownloadFragmentAdapter.updateData(SourceDownloadFragmentAdapter.TYPE_ALL);//每次更新下载任务的数据库数据
    }

    private SourceDownloadFragmentAdapter.MyViewHolder holder;

    //请求权限的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case writePermissionCode:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //权限请求成功，继续操作
                    PostDownloadList(id);
                } else {
                    //权限请求失败
                    //当权限申请被拒绝并且shouldShowRequestPermissionRationale() 返回 false 就表示勾选了不再询问
                    if (!ReadingBookFragment.this.shouldShowRequestPermissionRationale(permissions[0])) {
                        //勾选了不在询问，需要弹出dialog提醒用户去权限设置里打开权限
                        BaseDialog.Builder builder = new BaseDialog.Builder(getActivity());
                        builder.setTitle("下载资料需要写SD卡的权限，是否跳转到设置权限界面？");
                        //积极的按钮,点击跳转到权限设置界面
                        builder.setPositiveButton("跳转", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                PermissionUtil.jumpToPermissionActivity(getActivity());
                                dialogInterface.dismiss();
                            }
                        });
                        //消极的按钮,点击隐藏dialog
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.create().show();
                    } else {
                        //没有勾选不在询问,不需要做任何操作
                    }
                }
                break;
        }
    }

    //请求列表数据接口
    public void PostList() {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("controller", "source");
        map.put("action", "index");
        map.put("page", pageno + "");
        map.put("cate_id", cate_id);
        getJsonUtil().PostJson(getActivity(), map);
    }

    //请求资源下载链接接口
    public void PostDownloadList(String id) {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("controller", "source");
        map.put("action", "download");
        map.put("id", id);
        getJsonUtil().PostJson(getActivity(), map);
    }
}
