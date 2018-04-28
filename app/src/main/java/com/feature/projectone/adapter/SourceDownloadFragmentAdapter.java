package com.feature.projectone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.inter.DownloadIconClickListener;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadTask;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/4/2.
 * 资源下载所有fragment 适配器
 */

public class SourceDownloadFragmentAdapter extends RecyclerView.Adapter {
    public static final int TYPE_ALL = 0;//全部任务
    public static final int TYPE_FINISH = 1;//已经完成任务
    public static final int TYPE_ING = 2;//下载中的任务
    private final NumberFormat numberFormat;
    private Context context;
    private List<HashMap<String, Object>> mDataList;
    private DownloadIconClickListener listener;
    private List<DownloadTask> values;

    public SourceDownloadFragmentAdapter(Context context, List<HashMap<String, Object>> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
        numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMinimumFractionDigits(2);

    }

    ////这里是将数据库的下载数据恢复
    public void updateData(int type) {
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

    //下载图标点击事件
    public void setOnDownloadIconListener(DownloadIconClickListener listener) {
        this.listener = listener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_source_download, null), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        HashMap<String, Object> map = mDataList.get(position);
        vh.tv_title.setText(map.get("title") + "");
        vh.tv_time.setText("更新时间： " + map.get("atime"));
        vh.tv_size.setText("大小： " + map.get("size"));

        //初始化下载的数据  （在每一次初始化条目的时候，拿到数据库的下载task集合，
        // 用task的唯一标识tag和下载的url进行对比，一样的就是已经有此下载任务，就去设置他的下载状态和进度）
        if (values != null && values.size() > 0) {
            for (int i = 0; i < values.size(); i++) {
                DownloadTask downloadTask = values.get(i);
                if (downloadTask.progress.tag != null && downloadTask.progress.tag.contains(map.get("source_url") + "")) {
                    vh.refresh(downloadTask.progress);
                    return;
                } else {
                    //代表下载任务集合没有此条目对应的任务，没有产生过下载操作
                    vh.tv_loading_percent.setVisibility(View.GONE);
                    Picasso.with(context).load(R.mipmap.img_download_btn).into(vh.iv_download);
                }
            }
        } else {
            //代表任务集合里一个下载任务都没有
            vh.tv_loading_percent.setVisibility(View.GONE);
            Picasso.with(context).load(R.mipmap.img_download_btn).into(vh.iv_download);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView iv_download;
        private final TextView tv_loading_percent;
        private final TextView tv_title;
        private final TextView tv_time;
        private final TextView tv_size;
        private final DownloadIconClickListener listener;

        public MyViewHolder(View itemView, DownloadIconClickListener listener) {
            super(itemView);
            iv_download = ((ImageView) itemView.findViewById(R.id.iv_download));//下载状态的图标
            tv_loading_percent = ((TextView) itemView.findViewById(R.id.tv_loading_percent));//下载的进度
            tv_title = ((TextView) itemView.findViewById(R.id.tv_title));//标题
            tv_time = ((TextView) itemView.findViewById(R.id.tv_time));//时间
            tv_size = ((TextView) itemView.findViewById(R.id.tv_size));//大小

            this.listener = listener;
            iv_download.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_download:
                    if (listener != null) {
                        listener.OnDownLoadClick(this, iv_download, getPosition() - 3);
                    }
                    break;
            }
        }

        public void refresh(Progress progress) {
            NumberFormat numberFormat = NumberFormat.getPercentInstance();
//            String currentSize = Formatter.formatFileSize(context, progress.currentSize);
//            String totalSize = Formatter.formatFileSize(context, progress.totalSize);
            float fraction = progress.fraction;

//            tv_loading_percent.setVisibility(View.VISIBLE);
//            Picasso.with(context).load(R.mipmap.img_downing_btn).into(iv_download);
//            tv_loading_percent.setText(numberFormat.format(fraction));

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

}



