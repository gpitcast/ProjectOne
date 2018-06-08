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
import com.feature.projectone.view.CircleImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/5/29.
 */

public class GroupListAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<HashMap> dataList;
    private RecyclerViewOnItemClickListener listener;

    public GroupListAdapter(Context context, List<HashMap> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public void setOnItemClickListener(RecyclerViewOnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_group_list, null), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        HashMap map = dataList.get(position);
        vh.tv_name.setText(map.get("name") + "");
        vh.tv_count.setText("一共有" + map.get("user_nums") + "名成员");
        Picasso.with(context).load(map.get("img") + "").error(R.mipmap.img_load_error).into(vh.iv_head);
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final CircleImageView iv_head;
        private final TextView tv_name;
        private final TextView tv_count;
        private final LinearLayout ll_base;
        private RecyclerViewOnItemClickListener listener;

        public MyViewHolder(View itemView, RecyclerViewOnItemClickListener listener) {
            super(itemView);
            ll_base = ((LinearLayout) itemView.findViewById(R.id.ll_base));
            iv_head = ((CircleImageView) itemView.findViewById(R.id.iv_head));
            tv_name = ((TextView) itemView.findViewById(R.id.tv_name));
            tv_count = ((TextView) itemView.findViewById(R.id.tv_count));

            this.listener = listener;
            ll_base.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_base:
                    if (listener != null) {
                        listener.OnItemClick(ll_base, getPosition() - 3);
                    }
                    break;
            }
        }
    }
}
