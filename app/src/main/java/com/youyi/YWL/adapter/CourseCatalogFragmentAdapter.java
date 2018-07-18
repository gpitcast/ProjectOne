package com.youyi.YWL.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youyi.YWL.R;

/**
 * Created by Administrator on 2018/7/17.
 * 精品课程-详情  课程目录fragment的recyclerview适配器
 */

public class CourseCatalogFragmentAdapter extends RecyclerView.Adapter {
    private Context context;

    public CourseCatalogFragmentAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_course_catalog_fragment, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        vh.tv_position.setText((position + 1) + "");
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_position;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_position = ((TextView) itemView.findViewById(R.id.tv_position));
        }
    }
}
