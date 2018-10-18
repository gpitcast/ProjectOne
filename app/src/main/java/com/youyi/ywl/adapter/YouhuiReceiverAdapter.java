package com.youyi.ywl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youyi.ywl.R;

/**
 * Created by Administrator on 2018/7/24.
 */

public class YouhuiReceiverAdapter extends RecyclerView.Adapter {
    private Context context;

    public YouhuiReceiverAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_youhui_service, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        if (position == 0) {
            vh.tv_tag.setBackgroundResource(R.drawable.bg_dark_orange_half_circle);
            vh.tv_name.setTextColor(context.getResources().getColor(R.color.normal_orange1));
            vh.tv_tag.setText("优惠 ");
        } else {
            vh.tv_tag.setBackgroundResource(R.drawable.bg_normal_orange_half_circle);
            vh.tv_name.setTextColor(context.getResources().getColor(R.color.normal_black));
            vh.tv_name.setText("初中新领航备课教案 1 ");
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_tag;
        private final TextView tv_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_tag = ((TextView) itemView.findViewById(R.id.tv_tag));
            tv_name = ((TextView) itemView.findViewById(R.id.tv_name));
        }
    }
}
