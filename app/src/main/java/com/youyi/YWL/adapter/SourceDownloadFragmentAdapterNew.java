package com.youyi.YWL.adapter;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;
import com.squareup.picasso.Picasso;
import com.youyi.YWL.R;
import com.youyi.YWL.fragment.PeitaoSourceFragmentNew;
import com.youyi.YWL.inter.DownloadIconClickListener;
import com.youyi.YWL.other.Constanst;
import com.youyi.YWL.util.CheckLoginUtil;
import com.youyi.YWL.util.LoginInterceptor;
import com.youyi.YWL.util.NetWorkUtils;
import com.youyi.YWL.util.PermissionUtil;
import com.youyi.YWL.util.ToastUtil;
import com.youyi.YWL.view.BaseDialog;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

import java.io.File;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/2.
 * 资源下载所有fragment 适配器
 */

public class SourceDownloadFragmentAdapterNew extends RecyclerView.Adapter {

    public static final int TYPE_ALL = 0;//全部任务
    public static final int TYPE_FINISH = 1;//已经完成任务
    public static final int TYPE_ING = 2;//下载中的任务

    private final NumberFormat numberFormat;
    private Context context;
    private List<HashMap<String, Object>> mDataList;
//    private DownloadIconClickListener listener;
    private List<DownloadTask> values = new ArrayList<>();
    private int type;

    public SourceDownloadFragmentAdapterNew(Context context, List<HashMap<String, Object>> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
        numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMinimumFractionDigits(2);
    }

    //这里是将数据库的下载数据恢复,刷新适配器
    public void updateData(int type) {
        this.type = type;
        if (type == TYPE_ALL) {
            values = OkDownload.restore(DownloadManager.getInstance().getAll());
        }
        if (type == TYPE_FINISH) {
            values = OkDownload.restore(DownloadManager.getInstance().getFinished());
        }
        if (type == TYPE_ING) {
            values = OkDownload.restore(DownloadManager.getInstance().getDownloading());
        }
        notifyDataSetChanged();
    }

    //将数据库的下载数据恢复,不刷新适配器
    public void justUpdateData(int type) {
        this.type = type;
        if (type == TYPE_ALL) {
            values = OkDownload.restore(DownloadManager.getInstance().getAll());
        }
        if (type == TYPE_FINISH) {
            values = OkDownload.restore(DownloadManager.getInstance().getFinished());
        }
        if (type == TYPE_ING) {
            values = OkDownload.restore(DownloadManager.getInstance().getDownloading());
        }
    }

//    //下载图标点击事件
//    public void setOnDownloadIconListener(DownloadIconClickListener listener) {
//        this.listener = listener;
//    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_source_download, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.i("SourceDownload", "      执行了onBindViewHolder      ");
        MyViewHolder vh = (MyViewHolder) holder;
        HashMap<String, Object> map = mDataList.get(position);
        vh.tv_title.setText(map.get("title") + "     position:      " + position);
        vh.tv_time.setText("更新时间： " + map.get("atime"));
        vh.tv_size.setText("大小： " + map.get("size"));

        //初始化下载的数据  （在每一次初始化条目的时候，拿到数据库的下载task集合，
        // 用task的唯一标识tag和下载的url进行对比，一样的就是已经有此下载任务，就去设置他的下载状态和进度）
        if (values != null && values.size() > 0) {
            boolean isDownload = false;
            int hasProgressIndex = -1;

            loop:
            for (int i = 0; i < values.size(); i++) {
                DownloadTask downloadTask = values.get(i);
                if (downloadTask.progress.tag != null && downloadTask.progress.tag.contains(map.get("source_url") + "")) {
                    Log.i("SourceDownload", "      tag      " + downloadTask.progress.tag);
                    isDownload = true;
                    hasProgressIndex = i;
                    break loop;
                } else {
                    isDownload = false;
                }
            }
            Log.i("SourceDownload", "        position:         " + position + "      isDownload:      " + isDownload + "      hasProgressIndex:      " + hasProgressIndex);
            if (isDownload) {
                if (hasProgressIndex != -1) {
                    DownloadTask downloadTask = values.get(hasProgressIndex);
                    vh.setTask(downloadTask);
                    downloadTask.register(new ListDownloadListener(downloadTask.progress.tag, vh));
                    vh.refresh(downloadTask.progress, vh);
                }
            } else {
                Log.i("SourceDownload", "        position:         " + position + "         执行了else,显示下载的黄标        ");
                //代表下载任务集合没有此条目对应的任务，没有产生过下载操作
                vh.tv_loading_percent.setVisibility(View.GONE);
                Picasso.with(context).load(R.mipmap.img_download_btn).into(vh.iv_download);
            }

        } else {
            //代表下载任务集合没有此条目对应的任务，没有产生过下载操作
            vh.tv_loading_percent.setVisibility(View.GONE);
            Picasso.with(context).load(R.mipmap.img_download_btn).into(vh.iv_download);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView iv_download;
        private final TextView tv_loading_percent;
        private final TextView tv_title;
        private final TextView tv_time;
        private final TextView tv_size;
//        private final DownloadIconClickListener listener;
        private NumberFormat numberFormat;
        private String tag;
        private DownloadTask task;
        private int position;
        private final int writePermissionCode = 1001;//读取SD卡权限请求码
        private String title;
        private String id;
        private MyViewHolder holder;


        public MyViewHolder(View itemView) {
            super(itemView);
            holder = this;

            iv_download = ((ImageView) itemView.findViewById(R.id.iv_download));//下载状态的图标
            tv_loading_percent = ((TextView) itemView.findViewById(R.id.tv_loading_percent));//下载的进度
            tv_title = ((TextView) itemView.findViewById(R.id.tv_title));//标题
            tv_time = ((TextView) itemView.findViewById(R.id.tv_time));//时间
            tv_size = ((TextView) itemView.findViewById(R.id.tv_size));//大小

//            this.listener = listener;
            iv_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show(context, "点击了第" + (getPosition() - 3) + "个条目", 0);
                    position = getPosition() - 3;
                    DownloadClick();
                }
            });
        }

        private void DownloadClick() {

            id = ((HashMap<String, Object>) mDataList.get(position)).get("id") + "";
            title = ((HashMap<String, Object>) mDataList.get(position)).get("title") + "";
            if (CheckLoginUtil.isLogin(context)) {
                if (PermissionUtil.checkPermission(context, PeitaoSourceFragmentNew.instance, Manifest.permission.WRITE_EXTERNAL_STORAGE, writePermissionCode)) {
                    //有权限就检查wifi的连接状态
                    if (NetWorkUtils.checkWifiState(context)) {
                        DownloadFile(((HashMap<String, Object>) mDataList.get(position)));
                    } else {
                        final BaseDialog.Builder builder = new BaseDialog.Builder(context);
                        builder.setTitle("下载需要消耗数据流量，是否在wifi连接下再进行下载操作？");
                        builder.setNegativeButton("好", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                        builder.setPositiveButton("继续下载", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DownloadFile(((HashMap<String, Object>) mDataList.get(position)));
                                dialogInterface.dismiss();
                            }
                        });
                        builder.create().show();
                    }
                } else {
                    //没有权限,不需要做任何操作，在工具类已经做过请求权限处理，会执行请求权限回调那一步
//                    PeitaoSourceFragmentNew.this.holder = holder;
                }

            } else {
                Bundle bundle = new Bundle();
                bundle.putString("type", "SourceDownloadActivity1");
                LoginInterceptor.interceptor(context, "com.youyi.YWL.activity.SourceDownloadActivity", bundle);
            }
        }


        private String DOWN_LOAD_URL;
        private String chName;//文件中文名称

        //下载文件
        private void DownloadFile(final Map map) {
            OkDownload instance = OkDownload.getInstance();
            instance.setFolder(Constanst.ZYXZ_DOWN_LOAD_PATH);
            String downLoadUrl = map.get("source_url") + "";
            this.DOWN_LOAD_URL = downLoadUrl;

            //2.从下载的url中截取文件名称
            final String fileName = DOWN_LOAD_URL.substring(DOWN_LOAD_URL.lastIndexOf("/") + 1);

            Log.i("GroupFolderListActivity", "DOWN_LOAD_URL:" + DOWN_LOAD_URL);
            //检查下载任务是否有重复tag
            DownloadTask task = OkDownload.getInstance().getTask(DOWN_LOAD_URL);
            if (task != null && task.progress.status == Progress.FINISH) {
                //完成状态
                ToastUtil.show(context, title + "已存在，不必重复下载", 0);
            } else if (task != null && task.progress.status == Progress.LOADING) {
                //下载中状态
                ToastUtil.show(context, title + "正在下载，请稍候", 0);
            } else {
                //文件不存在，直接开始是下载
                chName = map.get("title") + "";
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
                        //记录下载文件的中文名称和下载时间
                        task.progress.extra1 = chName;
                        Date date = new Date(System.currentTimeMillis());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String format = sdf.format(date);
                        task.progress.extra2 = format;//记录下载时间
                        //开始下载任务
                        task.start();
                        break;
                }
            }
        };


        public void setTask(DownloadTask task) {
            this.task = task;
        }

        public void refresh(Progress progress, MyViewHolder holder) {
            if (numberFormat == null) {
                numberFormat = NumberFormat.getPercentInstance();
            }
            float fraction = progress.fraction;

            switch (progress.status) {
                case Progress.NONE:
                    //无状态
                    tv_loading_percent.setVisibility(View.VISIBLE);
                    iv_download.setImageResource(R.mipmap.img_downing_btn);
                    tv_loading_percent.setText(numberFormat.format(fraction));
                    break;
                case Progress.WAITING:
                    //等待
                    iv_download.setImageResource(R.mipmap.img_downing_btn);
                    tv_loading_percent.setVisibility(View.VISIBLE);
                    tv_loading_percent.setText("等待");
                    break;
                case Progress.LOADING:
                    //下载中
                    tv_loading_percent.setVisibility(View.VISIBLE);
                    iv_download.setImageResource(R.mipmap.img_downing_btn);
                    tv_loading_percent.setText(numberFormat.format(progress.fraction));
                    break;
                case Progress.PAUSE:
                    //暂停
                    tv_loading_percent.setVisibility(View.VISIBLE);
                    iv_download.setImageResource(R.mipmap.img_downing_btn);
                    tv_loading_percent.setText("暂停");
                    break;
                case Progress.ERROR:
                    //错误
                    tv_loading_percent.setVisibility(View.VISIBLE);
                    iv_download.setImageResource(R.mipmap.img_downing_btn);
                    tv_loading_percent.setText("错误");
                    break;
                case Progress.FINISH:
                    //完成
                    tv_loading_percent.setVisibility(View.GONE);
                    iv_download.setImageResource(R.mipmap.img_down_ok_btn);
                    break;
            }
        }
    }

    private class ListDownloadListener extends DownloadListener {
        private MyViewHolder holder;

        public ListDownloadListener(Object tag, MyViewHolder holder) {
            super(tag);
            this.holder = holder;
        }

        @Override
        public void onStart(Progress progress) {
            Log.i("ListDownloadListener", "          onStart              " + progress.toString());
        }

        @Override
        public void onProgress(Progress progress) {
            if (holder != null) {
                holder.refresh(progress, holder);//刷新进度条
            }
        }

        @Override
        public void onError(Progress progress) {
            Log.i("ListDownloadListener", "          onError              " + progress.toString());
        }

        @Override
        public void onFinish(File file, Progress progress) {
            //文件下载完成，直接进行zip解压(目前只支持zip解压)
            Log.i("ListDownloadListener", "          onFinish    配套资源fragment里  开始解压         " + progress.toString());
            solveZipFile(progress);
        }

        @Override
        public void onRemove(Progress progress) {
        }
    }


    //解压下载下来的zip文件
    private void solveZipFile(Progress progress) {
        String filePath = progress.filePath;
        //多加一层目录，文件夹名称取当前的时间毫秒值，避免加压出的文件名称重复覆盖,在文件路径中加入'zyxz',代表是'资源下载',用于区分群组文件还是资源下载的文件
        String outPath = progress.folder + "/" + System.currentTimeMillis();
        try {
            ZipFile zFile = new ZipFile(filePath);
            zFile.setFileNameCharset("GBK");//设置编码，防止乱码
            if (!zFile.isValidZipFile()) {
                //解压失败删除掉zip文件
                File file = new File(filePath);
                if (file.exists()) {
                    file.delete();
                }
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
    }

}



