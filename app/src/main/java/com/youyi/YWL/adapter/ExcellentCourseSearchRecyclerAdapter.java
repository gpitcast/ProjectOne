package com.youyi.YWL.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youyi.YWL.R;

import java.util.List;

/**
 * Created by Administrator on 2018/7/16.
 * 精品课程-搜索界面 的 热门搜索fragment 的recyclerview 适配器
 */

public class ExcellentCourseSearchRecyclerAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<String> recyclerList;

    public ExcellentCourseSearchRecyclerAdapter(Context context, List<String> recyclerList) {
        this.context = context;
        this.recyclerList = recyclerList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_excellent_course_search_recycler, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        vh.tv_name.setText(recyclerList.get(position));
    }

    @Override
    public int getItemCount() {
        return recyclerList == null ? 0 : recyclerList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = ((TextView) itemView.findViewById(R.id.tv_name));
        }
    }
}
