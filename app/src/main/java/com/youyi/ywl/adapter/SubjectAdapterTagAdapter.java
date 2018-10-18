package com.youyi.ywl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youyi.ywl.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/7/13.
 * 精品课程-fragment里的适配器里的-标签的适配器
 */

public class SubjectAdapterTagAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<String> tagsList;

    public SubjectAdapterTagAdapter(Context context, ArrayList<String> tagsList) {
        this.context = context;
        this.tagsList = tagsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_subject_tag, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        if (position == 1) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 24;
            params.rightMargin = 24;
            vh.ll_base.setLayoutParams(params);
        }

        vh.tv_tag_name.setText(tagsList.get(position));
    }

    @Override
    public int getItemCount() {
        return tagsList == null ? 0 : tagsList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_tag_name;
        private final LinearLayout ll_base;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_tag_name = ((TextView) itemView.findViewById(R.id.tv_tag_name));
            ll_base = ((LinearLayout) itemView.findViewById(R.id.ll_base));
        }
    }
}
