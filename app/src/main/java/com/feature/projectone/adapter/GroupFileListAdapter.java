package com.feature.projectone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.feature.projectone.R;
import com.feature.projectone.inter.RecyclerViewOnItemClickListener;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2018/5/25.
 * 群文件列表适配器
 */

public class GroupFileListAdapter extends RecyclerView.Adapter {
    private Context context;
    private RecyclerViewOnItemClickListener listener;

    public GroupFileListAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemClickListener(RecyclerViewOnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_group_file_list, null), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        switch (position) {
            case 0:
                Picasso.with(context).load(R.mipmap.img_type_video).into(vh.iv_file_type);
                break;
            case 1:
                Picasso.with(context).load(R.mipmap.img_type_music).into(vh.iv_file_type);
                break;
            case 2:
                Picasso.with(context).load(R.mipmap.img_type_png).into(vh.iv_file_type);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final LinearLayout ll_base;
        private RecyclerViewOnItemClickListener listener;
        private final ImageView iv_file_type;

        public MyViewHolder(View itemView, RecyclerViewOnItemClickListener listener) {
            super(itemView);
            ll_base = ((LinearLayout) itemView.findViewById(R.id.ll_base));//根布局
            iv_file_type = ((ImageView) itemView.findViewById(R.id.iv_file_type));//文件类型图片

            this.listener = listener;
            ll_base.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ll_base:
                    if (listener != null) {
                        listener.OnItemClick(ll_base, getPosition() - 3);
                    }
                    break;
            }
        }
    }
}
