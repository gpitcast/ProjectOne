package com.feature.projectone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.inter.RecyclerViewOnItemClickListener;
import com.feature.projectone.util.FileTypeImgUtil;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/6/11.
 * 查看解压的zip文件,里面包含了文件夹和文件的处理界面的adapter
 */

public class FileListAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<HashMap<String, Object>> mDataList;
    private RecyclerViewOnItemClickListener listener;

    public FileListAdapter(Context context, List<HashMap<String, Object>> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    public void setOnItemClickListener(RecyclerViewOnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_file_list, null), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        HashMap<String, Object> map = mDataList.get(position);
        String fileName = map.get("fileName") + "";
        vh.tv_name.setText(fileName);
        vh.tv_size.setText(map.get("size") + "");

        if (fileName != null && fileName.contains(".")) {
            //文件
            String typeStr = fileName.substring(fileName.lastIndexOf(".") + 1);
            FileTypeImgUtil.showTypeImg(context, typeStr, vh.iv_file_type);
        } else {
            //文件夹
            Picasso.with(context).load(R.mipmap.icon_folder).into(vh.iv_file_type);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView iv_file_type;
        private final TextView tv_name;
        private final TextView tv_size;
        private final LinearLayout ll_base;
        private RecyclerViewOnItemClickListener listener;

        public MyViewHolder(View itemView, RecyclerViewOnItemClickListener listener) {
            super(itemView);
            ll_base = ((LinearLayout) itemView.findViewById(R.id.ll_base));
            iv_file_type = ((ImageView) itemView.findViewById(R.id.iv_file_type));//类型图片
            tv_name = ((TextView) itemView.findViewById(R.id.tv_name));//名称
            tv_size = ((TextView) itemView.findViewById(R.id.tv_size));//大小

            this.listener = listener;
            ll_base.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_base:
                    if (listener != null) {
                        listener.OnItemClick(v, getPosition());
                    }
                    break;
            }
        }
    }
}
