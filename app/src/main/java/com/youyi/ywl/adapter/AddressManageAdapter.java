package com.youyi.ywl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youyi.ywl.R;
import com.youyi.ywl.inter.ReplyIconClickListener;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/9/5.
 * 地址管理 列表适配器
 */

public class AddressManageAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<HashMap<String, Object>> dataList;
    private ReplyIconClickListener iconClickListener1;
    private ReplyIconClickListener iconClickListener2;

    public AddressManageAdapter(Context context, List<HashMap<String, Object>> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public void setOnEditClickListener(ReplyIconClickListener iconClickListener1) {
        this.iconClickListener1 = iconClickListener1;
    }

    public void setOnDeleteClickListener(ReplyIconClickListener iconClickListener2) {
        this.iconClickListener2 = iconClickListener2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_address_manage, null), iconClickListener1, iconClickListener2);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        HashMap<String, Object> map = dataList.get(position);
        vh.tv_name.setText(map.get("username") + "");
        vh.tv_phone_number.setText(map.get("tel") + "");
        vh.tv_address.setText(map.get("address") + "");
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tv_name;
        private final TextView tv_phone_number;
        private final TextView tv_address;
        private final LinearLayout ll_edit;
        private final LinearLayout ll_delete;
        private ReplyIconClickListener iconClickListener1;
        private ReplyIconClickListener iconClickListener2;

        public MyViewHolder(View itemView, ReplyIconClickListener iconClickListener1, ReplyIconClickListener iconClickListener2) {
            super(itemView);
            tv_name = ((TextView) itemView.findViewById(R.id.tv_name));
            tv_phone_number = ((TextView) itemView.findViewById(R.id.tv_phone_number));
            tv_address = ((TextView) itemView.findViewById(R.id.tv_address));
            ll_edit = ((LinearLayout) itemView.findViewById(R.id.ll_edit));
            ll_delete = ((LinearLayout) itemView.findViewById(R.id.ll_delete));

            this.iconClickListener1 = iconClickListener1;
            ll_edit.setOnClickListener(this);

            this.iconClickListener2 = iconClickListener2;
            ll_delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_edit:
                    if (iconClickListener1 != null) {
                        iconClickListener1.OnReplyIconClick(ll_edit, getPosition() - 1);
                    }
                    break;
                case R.id.ll_delete:
                    if (iconClickListener2 != null) {
                        iconClickListener2.OnReplyIconClick(ll_delete, getPosition() - 1);
                    }
                    break;
            }
        }
    }
}
