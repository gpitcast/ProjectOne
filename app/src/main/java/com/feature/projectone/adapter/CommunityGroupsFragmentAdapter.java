package com.feature.projectone.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.inter.ReplyIconClickListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/5/30.
 * 联系人的社群群组fragment的适配器
 */

public class CommunityGroupsFragmentAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<HashMap> dataList;
    private ReplyIconClickListener listener;

    public CommunityGroupsFragmentAdapter(Context context, List<HashMap> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public void setOnIntoButtonClickListener(ReplyIconClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_community_groups_fragment, null), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        HashMap map = dataList.get(position);
        vh.tv_name.setText(map.get("name") + "");
        Picasso.with(context).load(map.get("img") + "").placeholder(R.mipmap.img_loading)
                .error(R.mipmap.img_load_error).into(vh.iv_head);
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final FrameLayout fl_into_group;
        private final ImageView iv_head;
        private final TextView tv_name;
        private ReplyIconClickListener listener;

        public MyViewHolder(View itemView, ReplyIconClickListener listener) {
            super(itemView);
            fl_into_group = ((FrameLayout) itemView.findViewById(R.id.fl_into_group));
            iv_head = ((ImageView) itemView.findViewById(R.id.iv_head));
            tv_name = ((TextView) itemView.findViewById(R.id.tv_name));

            this.listener = listener;
            fl_into_group.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fl_into_group:
                    if (listener != null) {
                        listener.OnReplyIconClick(fl_into_group, getPosition() - 3);
                    }
                    break;
            }
        }
    }
}
