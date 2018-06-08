package com.feature.projectone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feature.projectone.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/5/28.
 * 群公告的适配器
 */

public class GroupNoticeAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<HashMap<String, Object>> mNoticeLists = new ArrayList<>();

    public GroupNoticeAdapter(Context context) {
        this.context = context;
    }

    public void refresh(List<HashMap<String, Object>> mNoticeLists) {
        this.mNoticeLists.clear();
        this.mNoticeLists.addAll(mNoticeLists);
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_group_notice, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        HashMap<String, Object> map = mNoticeLists.get(position);
        vh.tv_content.setText(map.get("desc") + "");
        vh.tv_time.setText("by群助手:" + map.get("addtime"));
    }

    @Override
    public int getItemCount() {
        return mNoticeLists == null ? 0 : mNoticeLists.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_content;
        private final TextView tv_time;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_content = ((TextView) itemView.findViewById(R.id.tv_content));
            tv_time = ((TextView) itemView.findViewById(R.id.tv_time));
        }
    }
}
