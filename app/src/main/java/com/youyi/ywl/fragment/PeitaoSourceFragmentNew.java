package com.youyi.ywl.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lzy.okserver.OkDownload;
import com.youyi.ywl.R;
import com.youyi.ywl.adapter.SourceDownloadFragmentAdapter;
import com.youyi.ywl.adapter.SourceDownloadFragmentAdapterNew;
import com.youyi.ywl.inter.SolveDialogListener;
import com.youyi.ywl.other.Constanst;
import com.youyi.ywl.util.HttpUtils;
import com.youyi.ywl.util.PermissionUtil;
import com.youyi.ywl.util.ToastUtil;
import com.youyi.ywl.view.BaseDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/7/9.
 */

public class PeitaoSourceFragmentNew extends BaseFragment {


    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;
    @BindView(R.id.ll_preload_layout)
    LinearLayout ll_preload_layout;//预载布局
    @BindView(R.id.iv_preload_layout)
    ImageView iv_preload_layout;//预载布局的图片
    @BindView(R.id.tv_preload_layout)
    TextView tv_preload_layout;//预载布局的文字

    public static  PeitaoSourceFragmentNew instance;
    private String cate_id;
    private int pageno = 1;//分页
    private boolean isLoadMore;
    private boolean isRefresh;
    private static final String sourceInexUrl = HttpUtils.Host + "/source/index";//下载列表数据接口
    //    private static final String sourceDownloadUrl = HttpUtils.Host + "/source/download";//下载对应资源链接数据接口
    private List<HashMap<String, Object>> mDataList = new ArrayList<>();//存储列表数据的集合
    private SourceDownloadFragmentAdapterNew sourceDownloadFragmentAdapter;
    private final int writePermissionCode = 1001;//读取SD卡权限请求码
    private String id;//每条下载条目的id
    private String title;//每一个条目对应的文件名称
    private SolveDialogListener listener;
    private int clickPosition;//记录点击的position
    private TextView tv_status;
    private boolean isFristshow = true;//是否是第一次变得可见

    public PeitaoSourceFragmentNew() {
    }

    @SuppressLint("ValidFragment")
    public PeitaoSourceFragmentNew(String cate_id) {
        this.cate_id = cate_id;
    }

    //提供方法给附着的activity筛选功能刷新数据
    public void shaiXuanRefresh(String cate_id) {
        this.cate_id = cate_id;
        xRecyclerView.refresh();
    }

    @Override
    public void onLazyLoad() {
        xRecyclerView.refresh();
    }

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case sourceInexUrl:

                dismissLoadingLayout();

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
                                tv_status.setText("加载完毕");
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
                                tv_status.setText(resultMap.get("msg") + "");
                                xRecyclerView.setNoMore(true);
                            } else if (isRefresh) {
                                showErrorOrNothingLayout();
                                isRefresh = false;
                                mDataList.clear();
                                xRecyclerView.refreshComplete();
                            } else {
                                ToastUtil.show(getActivity(), msg, 0);
                            }
                            sourceDownloadFragmentAdapter.notifyDataSetChanged();
                        }
                    } else {
                        //status不等于0代表暂无数据，取消下拉或者上拉状态，弹吐司提醒用户
                        if (isLoadMore) {
                            isLoadMore = false;
                            pageno--;
                            xRecyclerView.loadMoreComplete();
                            tv_status.setText(resultMap.get("msg") + "");
                            xRecyclerView.setNoMore(true);
                        } else if (isRefresh) {
                            showErrorOrNothingLayout();
                            mDataList.clear();
                            isRefresh = false;
                            xRecyclerView.refreshComplete();
                        } else {
                            ToastUtil.show(getActivity(), resultMap.get("msg") + "", 0);
                        }
                        sourceDownloadFragmentAdapter.notifyDataSetChanged();
                    }
                } else {
                    if (isLoadMore) {
                        isLoadMore = false;
                        pageno--;
                        xRecyclerView.loadMoreComplete();
                        tv_status.setText(msg);
                    } else if (isRefresh) {
                        isRefresh = false;
                        mDataList.clear();
                        xRecyclerView.refreshComplete();
                        ToastUtil.show(getActivity(), msg, 0);
                        sourceDownloadFragmentAdapter.notifyDataSetChanged();

                        //刷新网络请求失败,显示加载出错布局
                        showErrorOrNothingLayout();
                    } else {
                        ToastUtil.show(getActivity(), msg, 0);
                        sourceDownloadFragmentAdapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }

    //显示网络请求错误的布局或者第一页没有数据的布局
    private void showErrorOrNothingLayout() {
        ll_preload_layout.setVisibility(View.VISIBLE);
        iv_preload_layout.setImageResource(R.mipmap.img_boy_find);
        tv_preload_layout.setText("这里什么都没有");
        tv_preload_layout.setTextColor(getResources().getColor(R.color.grayone));
    }

    //显示加载中的布局
    private void showLoadingLayout() {
        ll_preload_layout.setVisibility(View.VISIBLE);
        iv_preload_layout.setImageResource(R.mipmap.img_boy_running1);
        tv_preload_layout.setText("努力加载中...");
        tv_preload_layout.setTextColor(getResources().getColor(R.color.normal_black));
    }

    //隐藏预加载布局
    private void dismissLoadingLayout() {
        ll_preload_layout.setVisibility(View.GONE);
    }

//    private String DOWN_LOAD_URL;
//    private String chName;//文件中文名称

//    //下载文件
//    private void DownloadFile(final Map map) {
//        OkDownload instance = OkDownload.getInstance();
//        instance.setFolder(Constanst.ZYXZ_DOWN_LOAD_PATH);
//        String downLoadUrl = map.get("source_url") + "";
//        this.DOWN_LOAD_URL = downLoadUrl;
//
//        //2.从下载的url中截取文件名称
//        final String fileName = DOWN_LOAD_URL.substring(DOWN_LOAD_URL.lastIndexOf("/") + 1);
//
//        Log.i("GroupFolderListActivity", "DOWN_LOAD_URL:" + DOWN_LOAD_URL);
//        //检查下载任务是否有重复tag
//        DownloadTask task = OkDownload.getInstance().getTask(DOWN_LOAD_URL);
//        if (task != null && task.progress.status == Progress.FINISH) {
//            //完成状态
//            ToastUtil.show(getActivity(), title + "已存在，不必重复下载", 0);
//        } else if (task != null && task.progress.status == Progress.LOADING) {
//            //下载中状态
//            ToastUtil.show(getActivity(), title + "正在下载，请稍候", 0);
//        } else {
//            //文件不存在，直接开始是下载
//            chName = map.get("title") + "";
//            Message msg = new Message();
//            msg.what = 0;
//            msg.obj = fileName;
//            handler.sendMessage(msg);
//        }
//    }

//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 0:
//                    //开始下载
//                    //这里只是演示，表示请求可以传参，怎么传都行，和okgo使用方法一样
//                    GetRequest<File> request = OkGo.<File>get(DOWN_LOAD_URL)
//                            .headers("aaa", "111")
//                            .params("bbb", "222");
//                    //这里第一个参数是tag，代表下载任务的唯一标识，传任意字符串都行，需要保证唯一,我这里用url作为了tag
//                    DownloadTask task = OkDownload.request(DOWN_LOAD_URL, request)
//                            .save()
//                            .fileName(msg.obj + "")
//                            .register(new PeitaoSourceFragmentNew.ListDownloadListener(DOWN_LOAD_URL, holder));
//                    //记录下载文件的中文名称和下载时间
//                    task.progress.extra1 = chName;
//                    Date date = new Date(System.currentTimeMillis());
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    String format = sdf.format(date);
//                    task.progress.extra2 = format;//记录下载时间
//                    //开始下载任务
//                    task.start();
//                    sourceDownloadFragmentAdapter.justUpdateData(SourceDownloadFragmentAdapter.TYPE_ALL);//每次更新下载任务的数据库数据
//                    break;
//            }
//        }
//    };

//    private class ListDownloadListener extends DownloadListener {
//        private SourceDownloadFragmentAdapter.MyViewHolder holder;
//
//        public ListDownloadListener(Object tag, SourceDownloadFragmentAdapter.MyViewHolder holder) {
//            super(tag);
//            PeitaoSourceFragmentNew.ListDownloadListener.this.holder = holder;
//        }
//
//        @Override
//        public void onStart(Progress progress) {
//            Log.i("ListDownloadListener", "          onStart              " + progress.toString());
//        }
//
//        @Override
//        public void onProgress(Progress progress) {
//            //Log.i("ListDownloadListener", "          onProgress              " + progress.toString());
//            if (holder != null) {
//                holder.refresh(progress, holder);//刷新进度条
//            }
//        }
//
//        @Override
//        public void onError(Progress progress) {
//            Log.i("ListDownloadListener", "          onError              " + progress.toString());
//        }
//
//        @Override
//        public void onFinish(File file, Progress progress) {
//            //文件下载完成，直接进行zip解压(目前只支持zip解压)
//            Log.i("ListDownloadListener", "          onFinish    配套资源fragment里  开始解压         " + progress.toString());
//            solveZipFile(progress);
//        }
//
//        @Override
//        public void onRemove(Progress progress) {
//        }
//    }

//    //解压下载下来的zip文件
//    private void solveZipFile(Progress progress) {
//        String filePath = progress.filePath;
//        //多加一层目录，文件夹名称取当前的时间毫秒值，避免加压出的文件名称重复覆盖,在文件路径中加入'zyxz',代表是'资源下载',用于区分群组文件还是资源下载的文件
//        String outPath = progress.folder + "/" + System.currentTimeMillis();
//        try {
//            ZipFile zFile = new ZipFile(filePath);
//            zFile.setFileNameCharset("GBK");//设置编码，防止乱码
//            if (!zFile.isValidZipFile()) {
//                //解压失败删除掉zip文件
//                File file = new File(filePath);
//                if (file.exists()) {
//                    file.delete();
//                }
//                throw new ZipException("文件不合法");
//            }
//            File desDir = new File(outPath);
//            if (desDir.isDirectory() && !desDir.exists()) {
//                desDir.mkdir();
//            }
//            if (zFile.isEncrypted()) {
//                zFile.setPassword("123".toCharArray());
//            }
//            int total = zFile.getFileHeaders().size();//解压总进度
//            for (int i = 0; i < zFile.getFileHeaders().size(); i++) {
//                FileHeader fh = ((FileHeader) zFile.getFileHeaders().get(i));
//                //在递归解压文件中，首次解压的就是最外层的文件夹名称，拿到文件夹名称并将该文件夹的路径存储在task.progress.extra3当中
//                if (i == 0) {
//                    DownloadTask task = OkDownload.getInstance().getTaskMap().get(progress.tag);//获取当前下载的任务
//                    String fileName = outPath + "/" + fh.getFileName().substring(0, fh.getFileName().length() - 1);//文件夹名称
//                    //检查下载目录是否有同名的文件夹，有的话就改名字
//                    File file = new File(fileName);
//                    if (!file.exists()) {
//                        task.progress.extra3 = fileName;//拿到文件夹名称并将该文件夹的路径存储在task.progress.extra3当中
//                        task.save();
//                    }
//                }
//                Log.i("zip文件中文件夹的名称", "               " + fh.getFileName());
//                zFile.extractFile(fh, outPath);
//            }
//        } catch (ZipException e) {
//            e.printStackTrace();
//            //抛出异常，表示解压失败
//            return;
//        }
//        //解压成功，删除下载的.zip文件
//        File downloadZipFile = new File(filePath);
//        if (downloadZipFile.exists()) {
//            downloadZipFile.delete();
//        }
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        OkDownload.getInstance().pauseAll();
    }


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_source_download;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        instance = this;
        showLoadingLayout();
        initRecyclerView();
    }

    private void initRecyclerView() {
        final LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        xRecyclerView.setLayoutManager(manager);
        sourceDownloadFragmentAdapter = new SourceDownloadFragmentAdapterNew(getActivity(), mDataList);
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
                tv_status.setText("正在加载...");
            }
        });

        View footerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_recyclerview_footer, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        footerView.setLayoutParams(layoutParams);
        tv_status = ((TextView) footerView.findViewById(R.id.tv_status));
        xRecyclerView.setFootView(footerView);
        xRecyclerView.loadMoreComplete();

//        sourceDownloadFragmentAdapter.setOnDownloadIconListener(new DownloadIconClickListener() {
//
//            @Override
//            public void OnDownLoadClick(SourceDownloadFragmentAdapter.MyViewHolder holder, View view, final int position) {
//                id = ((HashMap<String, Object>) mDataList.get(position)).get("id") + "";
//                title = ((HashMap<String, Object>) mDataList.get(position)).get("title") + "";
//                PeitaoSourceFragmentNew.this.holder = holder;
//                PeitaoSourceFragmentNew.this.holder.setTag(position + "");
//                if (CheckLoginUtil.isLogin(getActivity())) {
//                    clickPosition = position;
//                    if (PermissionUtil.checkPermission(getContext(), PeitaoSourceFragmentNew.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, writePermissionCode)) {
//                        //有权限就检查wifi的连接状态
//                        if (NetWorkUtils.checkWifiState(getActivity())) {
//                            DownloadFile(((HashMap<String, Object>) mDataList.get(position)));
//                        } else {
//                            final BaseDialog.Builder builder = new BaseDialog.Builder(getActivity());
//                            builder.setTitle("下载需要消耗数据流量，是否在wifi连接下再进行下载操作？");
//                            builder.setNegativeButton("好", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    dialogInterface.dismiss();
//                                }
//                            });
//
//                            builder.setPositiveButton("继续下载", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    DownloadFile(((HashMap<String, Object>) mDataList.get(position)));
//                                    dialogInterface.dismiss();
//                                }
//                            });
//                            builder.create().show();
//                        }
//                    } else {
//                        //没有权限,不需要做任何操作，在工具类已经做过请求权限处理，会执行请求权限回调那一步
//                        PeitaoSourceFragmentNew.this.holder = holder;
//                    }
//
//                } else {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("type", "SourceDownloadActivity1");
//                    LoginInterceptor.interceptor(getActivity(), "com.youyi.YWL.activity.SourceDownloadActivity", bundle);
//                }
//            }
//        });
        sourceDownloadFragmentAdapter.updateData(SourceDownloadFragmentAdapter.TYPE_ALL);//每次更新下载任务的数据库数据
    }

    private SourceDownloadFragmentAdapter.MyViewHolder holder;

    //请求权限的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case writePermissionCode:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //权限请求成功，继续操作(有问题，暂不做继续下载动作)
//                    DownloadFile(((HashMap<String, Object>) mDataList.get(clickPosition)));
                } else {

                    //权限请求失败
                    //当权限申请被拒绝并且shouldShowRequestPermissionRationale() 返回 false 就表示勾选了不再询问
                    if (!PeitaoSourceFragmentNew.this.shouldShowRequestPermissionRationale(permissions[0])) {
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

    @Override
    public void onResume() {
        super.onResume();
        if (!isFristshow) {
            //从夏季以界面返回时刷新会因为删除等操作改变的适配器的数据
            sourceDownloadFragmentAdapter.updateData(SourceDownloadFragmentAdapterNew.TYPE_ALL);//每次更新下载任务的数据库数据
            sourceDownloadFragmentAdapter.notifyDataSetChanged();
        }
        isFristshow = false;
    }
}
