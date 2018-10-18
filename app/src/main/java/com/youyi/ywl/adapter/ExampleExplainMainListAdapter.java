package com.youyi.ywl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youyi.ywl.R;
import com.youyi.ywl.inter.RecyclerViewOnItemClickListener;
import com.youyi.ywl.util.GlideUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2018/8/25.
 * 习题讲解首页 - 列表的适配器
 */

public class ExampleExplainMainListAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<HashMap<String, Object>> dataList;
    private RecyclerViewOnItemClickListener itemClickListener;

    public ExampleExplainMainListAdapter(Context context, ArrayList<HashMap<String, Object>> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public void setonItemClickListener(RecyclerViewOnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_example_explain_main_list, null), itemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        HashMap<String, Object> map = dataList.get(position);
        GlideUtil.loadNetImageView(context,map.get("img") + "",vh.imageView);
        vh.tv_name.setText(map.get("subject") + "·" + map.get("grade"));
        vh.tv_instroction.setText(map.get("title") + "");
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView imageView;
        private final TextView tv_name;
        private final TextView tv_instroction;
        private final LinearLayout ll_base;
        private RecyclerViewOnItemClickListener itemClickListener;

        public MyViewHolder(View itemView, RecyclerViewOnItemClickListener itemClickListener) {
            super(itemView);
            imageView = ((ImageView) itemView.findViewById(R.id.imageView));
            tv_name = ((TextView) itemView.findViewById(R.id.tv_name));
            tv_instroction = ((TextView) itemView.findViewById(R.id.tv_instroction));
            ll_base = ((LinearLayout) itemView.findViewById(R.id.ll_base));

            this.itemClickListener = itemClickListener;
            ll_base.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_base:
                    if (itemClickListener != null) {
                        itemClickListener.OnItemClick(ll_base, getPosition() - 3);
                    }
                    break;
            }
        }
    }
}
