package com.feature.projectone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.fragment.PeiTaoSourceFragment;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2018/4/2.
 * 配套资源fragment 适配器
 */

public class PeiTaoSourceAdapter extends RecyclerView.Adapter {
    private Context context;

    public PeiTaoSourceAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_peitao_source, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        switch (position) {
            case 1:
                Picasso.with(context).load(R.mipmap.img_download_btn).into(vh.iv_download);
                break;
            case 2:
                Picasso.with(context).load(R.mipmap.img_downing_btn).into(vh.iv_download);
                vh.tv_loading_percent.setVisibility(View.GONE);
                break;
            case 3:
                Picasso.with(context).load(R.mipmap.img_down_ok_btn).into(vh.iv_download);
                break;
            case 4:
                Picasso.with(context).load(R.mipmap.img_down_error_btn).into(vh.iv_download);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView iv_download;
        private final TextView tv_loading_percent;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_download = ((ImageView) itemView.findViewById(R.id.iv_download));
            tv_loading_percent = ((TextView) itemView.findViewById(R.id.tv_loading_percent));
        }
    }
}
