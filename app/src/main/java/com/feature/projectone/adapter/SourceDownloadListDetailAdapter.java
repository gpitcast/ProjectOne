package com.feature.projectone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.activity.SourceDownloadListDetailActivity;
import com.feature.projectone.bean.CheckItem;
import com.feature.projectone.inter.RecyclerViewOnItemClickListener;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/4/24.
 * 我的资源列表条目详情(下载和已下载的文件)界面适配器
 */

public class SourceDownloadListDetailAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<CheckItem> checkedList;
    private OnSelectListener onSelectListener;
    private boolean visible;
    private List<HashMap<String, Object>> mDataList;
    private RecyclerViewOnItemClickListener listener;

    public SourceDownloadListDetailAdapter(Context context, List<CheckItem> checkedList, List<HashMap<String, Object>> mDataList) {
        this.context = context;
        this.checkedList = checkedList;
        this.mDataList = mDataList;
    }

    public void setOnItemClickListener(RecyclerViewOnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewholder(LayoutInflater.from(context).inflate(R.layout.item_source_download_list_detail, null), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewholder vh = (MyViewholder) holder;
        HashMap<String, Object> map = mDataList.get(position);
        vh.tv_name.setText(map.get("fileName") + "");
        vh.tv_size.setText(map.get("size") + "");

        String fileName = map.get("fileName") + "";
        if (fileName.endsWith(".png") || fileName.endsWith("jpg")) {
            vh.iv_icon.setImageResource(R.mipmap.img_type_png);
        } else if (fileName.endsWith(".excel")) {
            vh.iv_icon.setImageResource(R.mipmap.img_type_excel);
        } else if (fileName.endsWith(".mp3")) {
            vh.iv_icon.setImageResource(R.mipmap.img_type_music);
        } else if (fileName.endsWith(".pdf")) {
            vh.iv_icon.setImageResource(R.mipmap.img_type_pdf);
        } else if (fileName.endsWith(".ppt")) {
            vh.iv_icon.setImageResource(R.mipmap.img_type_ppt);
        } else if (fileName.endsWith(".mp4")) {
            vh.iv_icon.setImageResource(R.mipmap.img_type_video);
        } else if (fileName.endsWith(".word")) {
            vh.iv_icon.setImageResource(R.mipmap.img_type_word);
        } else if (fileName.endsWith(".zip")) {
            vh.iv_icon.setImageResource(R.mipmap.img_type_zip);
        } else {
            vh.iv_icon.setImageResource(R.mipmap.img_type_word);
        }

        if (visible) {
            vh.checkBox.setVisibility(View.VISIBLE);
        } else {
            vh.checkBox.setVisibility(View.GONE);
        }

        if (checkedList.get(position).isSelect()) {
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

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    private class MyViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tv_name;
        private final CheckBox checkBox;
        private final TextView tv_size;
        private final ImageView iv_icon;
        private final LinearLayout ll_base;
        private RecyclerViewOnItemClickListener listener;

        public MyViewholder(View itemView, RecyclerViewOnItemClickListener listener) {
            super(itemView);
            ll_base = ((LinearLayout) itemView.findViewById(R.id.ll_base));//根布局
            tv_name = ((TextView) itemView.findViewById(R.id.tv_name));//名称
            checkBox = ((CheckBox) itemView.findViewById(R.id.checkBox));//复选框
            tv_size = ((TextView) itemView.findViewById(R.id.tv_size));//大小
            iv_icon = ((ImageView) itemView.findViewById(R.id.iv_icon));//图标

            this.listener = listener;
            ll_base.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null) {
                listener.OnItemClick(view, getPosition() - 3);
            }
        }
    }
}
