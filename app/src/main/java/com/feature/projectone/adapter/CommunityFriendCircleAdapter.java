package com.feature.projectone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.feature.projectone.R;
import com.feature.projectone.fragment.CommunityFriendCircleFragment;
import com.feature.projectone.inter.RecyclerViewOnItemClickListener;

/**
 * Created by Administrator on 2018/5/17.
 * 我的社区-社区朋友圈适配器
 */

public class CommunityFriendCircleAdapter extends RecyclerView.Adapter {
    private Context context;
    private RecyclerViewOnItemClickListener listener;

    public CommunityFriendCircleAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemClickListener(RecyclerViewOnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_community_friend_circle, null), listener);
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
        private RecyclerViewOnItemClickListener listener;

        public MyViewHolder(View itemView, RecyclerViewOnItemClickListener listener) {
            super(itemView);
            ll_base = ((LinearLayout) itemView.findViewById(R.id.ll_base));

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
