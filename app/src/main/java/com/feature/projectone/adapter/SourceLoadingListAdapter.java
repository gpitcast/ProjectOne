package com.feature.projectone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.bean.CheckItem;
import com.feature.projectone.inter.RecyclerViewOnItemClickListener;
import com.feature.projectone.util.FileUtil;
import com.lzy.okgo.model.Progress;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/4/27.
 * 我的下载列表中下载中的适配器
 */

public class SourceLoadingListAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<HashMap<String, Object>> loadingDataList;
    private RecyclerViewOnItemClickListener listener;
    private List<CheckItem> checkListLoading;
    private boolean visible;
    private OnSelectListener onSelectListener;//checkbox被选中的回调

    public SourceLoadingListAdapter(Context context, List<CheckItem> checkListLoading, List<HashMap<String, Object>> loadingDataList) {
        this.context = context;
        this.loadingDataList = loadingDataList;
        this.checkListLoading = checkListLoading;
    }

    public void setOnPlayClickListener(RecyclerViewOnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_source_loading_list, null), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        HashMap<String, Object> map = loadingDataList.get(position);
        vh.tv_name.setText(map.get("name") + "");
        int status = Integer.parseInt(map.get("status") + "");

        switch (status) {
            case Progress.NONE:
                //无状态
                vh.tv_type.setText("暂停下载");
                vh.iv_icon.setImageResource(R.mipmap.img_stop_download);
                break;
            case Progress.WAITING:
                //等待
                vh.tv_type.setText("等待");
                vh.iv_icon.setImageResource(R.mipmap.img_loading);
                break;
            case Progress.LOADING:
                //下载中
                vh.tv_type.setText("下载中");
                vh.iv_icon.setImageResource(R.mipmap.img_loading);
                break;
            case Progress.PAUSE:
                //暂停
                vh.tv_type.setText("暂停下载");
                vh.iv_icon.setImageResource(R.mipmap.img_stop_download);
                break;
            case Progress.ERROR:
                //错误
                vh.tv_type.setText("下载出错");
                vh.iv_icon.setImageResource(R.mipmap.img_loading);
                break;
            case Progress.FINISH:
                //完成
                vh.tv_type.setText("下载完成");
                vh.iv_icon.setImageResource(R.mipmap.img_folder);
                break;
        }

        int currentSize = Integer.parseInt(map.get("currentSize") + "");
        int totalSize = Integer.parseInt(map.get("totalSize") + "");
        String size = FileUtil.FormetFileSize(totalSize);
        vh.tv_size.setText(size);

        vh.progressBar.setMax(totalSize / 1024);
        vh.progressBar.setProgress(currentSize / 1024);

        if (visible) {
            if (status == 0 || status == 3 || status == 1) {
                vh.checkBox.setVisibility(View.VISIBLE);
            } else {
                vh.checkBox.setVisibility(View.GONE);
            }
        } else {
            vh.checkBox.setVisibility(View.GONE);
        }
        if (checkListLoading.get(position).isSelect()) {
            vh.checkBox.setChecked(true);
        } else {
            vh.checkBox.setChecked(false);
        }
        vh.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSelectListener != null) {
                    onSelectListener.SelectListener(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return loadingDataList == null ? 0 : loadingDataList.size();
    }

    public interface OnSelectListener {
        void SelectListener(int position);
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    //显示隐藏所有的checkbox
    public void setChecBoxVisible(boolean visible) {
        this.visible = visible;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tv_name;
        private final TextView tv_type;
        private final TextView tv_size;
        private final ProgressBar progressBar;
        private final ImageView iv_icon;
        private final CheckBox checkBox;
        private RecyclerViewOnItemClickListener listener;

        public MyViewHolder(View itemView, RecyclerViewOnItemClickListener listener) {
            super(itemView);
            tv_name = ((TextView) itemView.findViewById(R.id.tv_name));//文件夹名称
            tv_type = ((TextView) itemView.findViewById(R.id.tv_type));//下载状态
            tv_size = ((TextView) itemView.findViewById(R.id.tv_size));//大小
            progressBar = ((ProgressBar) itemView.findViewById(R.id.progressBar));//进度
            iv_icon = ((ImageView) itemView.findViewById(R.id.iv_icon));//下载图标
            checkBox = ((CheckBox) itemView.findViewById(R.id.checkBox));//复选框

            this.listener = listener;
            iv_icon.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_icon:
                    if (listener != null && !visible) {
                        listener.OnItemClick(iv_icon, getPosition() - 3);
                    }
                    break;
            }
        }


//        public void refresh(Progress progress) {
//            NumberFormat numberFormat = NumberFormat.getPercentInstance();
//            String currentSize = Formatter.formatFileSize(context, progress.currentSize);
//            String totalSize = Formatter.formatFileSize(context, progress.totalSize);
//            float fraction = progress.fraction;
//            progressBar.setMax(Integer.parseInt((progress.totalSize / 1024) + ""));
//            progressBar.setProgress(Integer.parseInt((progress.currentSize / 1024) + ""));
//            switch (progress.status) {
//                case Progress.NONE:
//                    //无状态
//                    tv_type.setText("暂停下载");
//                    break;
//                case Progress.WAITING:
//                    //等待
//                    tv_type.setText("等待");
//                    break;
//                case Progress.LOADING:
//                    //下载中
//                    tv_type.setText("正在下载");
//                    break;
//                case Progress.PAUSE:
//                    //暂停
//                    tv_type.setText("暂停下载");
//                    break;
//                case Progress.ERROR:
//                    //错误
//                    tv_type.setText("下载错误");
//                    break;
//                case Progress.FINISH:
//                    //完成
//                    tv_type.setText("下载完成");
//                    break;
//            }
//        }
    }
}
