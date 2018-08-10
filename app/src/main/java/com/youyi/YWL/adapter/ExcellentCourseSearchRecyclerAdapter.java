package com.youyi.YWL.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youyi.YWL.R;
import com.youyi.YWL.inter.RecyclerViewOnItemClickListener;

import java.util.List;

/**
 * Created by Administrator on 2018/7/16.
 * 精品课程-搜索界面 的 热门搜索fragment 的recyclerview 适配器
 */

public class ExcellentCourseSearchRecyclerAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<String> historyRecyclerList;
    private RecyclerViewOnItemClickListener itemClickListener;

    public ExcellentCourseSearchRecyclerAdapter(Context context, List<String> historyRecyclerList) {
        this.context = context;
        this.historyRecyclerList = historyRecyclerList;
    }

    public void setOnItemClickListener(RecyclerViewOnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_excellent_course_search_recycler, null), itemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        vh.tv_name.setText(historyRecyclerList.get(position));
    }

    @Override
    public int getItemCount() {
        return historyRecyclerList == null ? 0 : historyRecyclerList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tv_name;
        private final LinearLayout ll_base;
        private RecyclerViewOnItemClickListener itemClickListener;

        public MyViewHolder(View itemView, RecyclerViewOnItemClickListener itemClickListener) {
            super(itemView);
            tv_name = ((TextView) itemView.findViewById(R.id.tv_name));
            ll_base = ((LinearLayout) itemView.findViewById(R.id.ll_base));

            this.itemClickListener = itemClickListener;
            ll_base.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_base:
                    if (itemClickListener != null) {
                        itemClickListener.OnItemClick(ll_base, getPosition());
                    }
                    break;
            }
        }
    }
}
