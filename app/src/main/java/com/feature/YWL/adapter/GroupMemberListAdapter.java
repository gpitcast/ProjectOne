package com.feature.projectone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.inter.RecyclerViewOnItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/5/25.
 * 群成员列表适配器
 */

public class GroupMemberListAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<HashMap<String, Object>> dataList;
    private RecyclerViewOnItemClickListener listener;

    public GroupMemberListAdapter(Context context, List<HashMap<String, Object>> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public void setOnItemClickListener(RecyclerViewOnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_group_member_list, null), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        HashMap<String, Object> map = dataList.get(position);
        vh.tv_name.setText(map.get("identity") + "-" + map.get("nickname"));
        String role_id = map.get("role_id") + "";
        if (!TextUtils.isEmpty(role_id) && role_id.equals("2")) {
            vh.iv_group_admin.setVisibility(View.VISIBLE);
        } else {
            vh.iv_group_admin.setVisibility(View.GONE);
        }
        Picasso.with(context).load(map.get("img") + "").placeholder(R.mipmap.img_loading)
                .error(R.mipmap.img_load_error).into(vh.iv_head);
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tv_name;
        private final ImageView iv_group_admin;
        private final LinearLayout ll_base;
        private final ImageView iv_head;
        private RecyclerViewOnItemClickListener listener;

        public MyViewHolder(View itemView, RecyclerViewOnItemClickListener listener) {
            super(itemView);
            ll_base = ((LinearLayout) itemView.findViewById(R.id.ll_base));
            tv_name = ((TextView) itemView.findViewById(R.id.tv_name));
            iv_group_admin = ((ImageView) itemView.findViewById(R.id.iv_group_admin));
            iv_head = ((ImageView) itemView.findViewById(R.id.iv_head));

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
