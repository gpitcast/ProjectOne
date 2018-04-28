package com.feature.projectone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.activity.SourceDownloadListActivity;
import com.feature.projectone.bean.CheckItem;
import com.feature.projectone.inter.RecyclerViewOnItemClickListener;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/4/24.
 * 我的资源列表(下载和已下载的文件)界面适配器
 */

public class SourceDownloadListAdapter extends RecyclerView.Adapter {
    private Context context;
    private OnSelectListener onSelectListener;//checkbox被选中的回调
    private RecyclerViewOnItemClickListener itemClickListener;
    private List<CheckItem> checkList;
    private boolean visible;
    private List<HashMap<String, Object>> mDataList;

    public SourceDownloadListAdapter(Context context, List<CheckItem> checkList, List<HashMap<String, Object>> mDataList) {
        this.context = context;
        this.checkList = checkList;
        this.mDataList = mDataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_source_download_list, null), itemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        HashMap<String, Object> map = mDataList.get(position);
        vh.tv_name.setText(map.get("name") + "");
        vh.tv_time.setText(map.get("time") + "");
        vh.tv_size.setText(map.get("size") + "");
        String folderName = map.get("folderName") + "";
        if (folderName == null || folderName.endsWith("null")) {
            vh.tv_type.setText("文件出错");
        } else {
            vh.tv_type.setText("已完成");
        }

        if (visible) {
            vh.checkBox.setVisibility(View.VISIBLE);
        } else {
            vh.checkBox.setVisibility(View.GONE);
        }
        if (checkList.get(position).isSelect()) {
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
        return mDataList == null ? 0 : mDataList.size();
    }

    public interface OnSelectListener {
        void SelectListener(int position);
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    public void setOnItemClickListener(RecyclerViewOnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    //显示隐藏所有的checkbox
    public void setChecBoxVisible(boolean visible) {
        this.visible = visible;
        notifyDataSetChanged();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tv_name;
        private final CheckBox checkBox;
        private final LinearLayout ll_base;
        private final TextView tv_time;
        private final TextView tv_size;
        private final TextView tv_type;
        RecyclerViewOnItemClickListener itemClickListener;

        public MyViewHolder(View itemView, RecyclerViewOnItemClickListener itemClickListener) {
            super(itemView);
            ll_base = ((LinearLayout) itemView.findViewById(R.id.ll_base));//根布局
            tv_name = ((TextView) itemView.findViewById(R.id.tv_name));//资源名称
            tv_time = ((TextView) itemView.findViewById(R.id.tv_time));//下载时间
            tv_size = ((TextView) itemView.findViewById(R.id.tv_size));//文件大小
            tv_type = ((TextView) itemView.findViewById(R.id.tv_type));//文件状态
            checkBox = ((CheckBox) itemView.findViewById(R.id.checkBox));//选择checkbox

            this.itemClickListener = itemClickListener;
            ll_base.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ll_base:
                    if (itemClickListener != null && !visible) {
                        itemClickListener.OnItemClick(ll_base, getPosition() - 3);
                    }
                    break;
            }
        }
    }
}
