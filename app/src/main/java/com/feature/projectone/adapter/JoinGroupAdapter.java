package com.feature.projectone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.activity.JoinGroupActivity;
import com.feature.projectone.inter.ReplyIconClickListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/6/1.
 * 加入社区群组界面适配器
 */

public class JoinGroupAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<HashMap<String, Object>> dataList;
    private ReplyIconClickListener listener;

    public JoinGroupAdapter(Context context, List<HashMap<String, Object>> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public void setOnJoinClickListener(ReplyIconClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_join_group, null), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        HashMap<String, Object> map = dataList.get(position);
        Picasso.with(context).load(map.get("img") + "").placeholder(R.mipmap.img_loading).error(R.mipmap.img_load_error).into(vh.iv_head);
        vh.tv_name.setText(map.get("name") + "");
        vh.tv_address.setText(map.get("address") + "");
        vh.tv_count.setText(map.get("user_nums") + "");
        vh.tv_instraction.setText(map.get("desc") + "");

        //用户加群状态(0:立即加群,1:待审核,2:已经加群)
        String status = map.get("status") + "";
        switch (status) {
            case "0":
                vh.tv_join_group.setText("立即加群");
                vh.tv_join_group.setBackgroundResource(R.drawable.bg_dark_blue_btn);
                break;
            case "1":
                vh.tv_join_group.setText("待审核");
                vh.tv_join_group.setBackgroundResource(R.drawable.bg_dark_blue_btn);
                break;
            case "2":
                vh.tv_join_group.setText("已加群");
                vh.tv_join_group.setBackgroundResource(R.drawable.bg_dark_gray_btn);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tv_name;
        private final TextView tv_address;
        private final TextView tv_count;
        private final TextView tv_instraction;
        private final ImageView iv_head;
        private final TextView tv_join_group;
        private ReplyIconClickListener listener;

        public MyViewHolder(View itemView, ReplyIconClickListener listener) {
            super(itemView);
            iv_head = ((ImageView) itemView.findViewById(R.id.iv_head));
            tv_name = ((TextView) itemView.findViewById(R.id.tv_name));
            tv_address = ((TextView) itemView.findViewById(R.id.tv_address));
            tv_count = ((TextView) itemView.findViewById(R.id.tv_count));
            tv_instraction = ((TextView) itemView.findViewById(R.id.tv_instraction));
            tv_join_group = ((TextView) itemView.findViewById(R.id.tv_join_group));

            this.listener = listener;
            tv_join_group.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_join_group:
                    if (listener != null) {
                        listener.OnReplyIconClick(tv_join_group, getPosition() - 3);
                    }
                    break;
            }
        }
    }
}
