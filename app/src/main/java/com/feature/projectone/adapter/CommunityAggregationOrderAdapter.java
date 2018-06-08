package com.feature.projectone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.inter.RecyclerViewOnItemClickListener;
import com.feature.projectone.inter.ReplyIconClickListener;

/**
 * Created by Administrator on 2018/5/17.
 * 我的社区-社区集结令适配器
 */

public class CommunityAggregationOrderAdapter extends RecyclerView.Adapter {
    private Context context;
    private RecyclerViewOnItemClickListener listener;
    private ReplyIconClickListener iconListener;

    public CommunityAggregationOrderAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemClickListener(RecyclerViewOnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnJoinClickListener(ReplyIconClickListener iconListener) {
        this.iconListener = iconListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_community_aggregation_order, null), listener, iconListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final LinearLayout ll_base;
        private final TextView tv_join;
        private RecyclerViewOnItemClickListener listener;
        private ReplyIconClickListener iconListener;

        public MyViewHolder(View itemView, RecyclerViewOnItemClickListener listener, ReplyIconClickListener iconListener) {
            super(itemView);
            ll_base = ((LinearLayout) itemView.findViewById(R.id.ll_base));
            tv_join = ((TextView) itemView.findViewById(R.id.tv_join));

            this.listener = listener;
            this.iconListener = iconListener;
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
                case R.id.tv_join:
                    if (iconListener != null) {
                        iconListener.OnReplyIconClick(tv_join, getPosition() - 3);
                    }
                    break;
            }
        }
    }
}
