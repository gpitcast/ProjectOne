package com.youyi.ywl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youyi.ywl.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/8/10.
 * 智能排序popwindow的适配器
 */

public class CleverSortAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<HashMap<String, Object>> sortList;

    public CleverSortAdapter(Context context, List<HashMap<String, Object>> sortList) {
        this.context = context;
        this.sortList = sortList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_clever_sort, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        HashMap<String, Object> map = sortList.get(position);
        vh.tv_name.setText(map.get("name") + "");
    }

    @Override
    public int getItemCount() {
        return sortList == null ? 0 : sortList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = ((TextView) itemView.findViewById(R.id.tv_name));
        }
    }
}
