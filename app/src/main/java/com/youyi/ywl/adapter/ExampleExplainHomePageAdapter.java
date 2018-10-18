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

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/8/31.
 * 首页  习题讲解 的列表 适配器
 */

public class ExampleExplainHomePageAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<HashMap<String, Object>> recyclerVierExampleList;
    private RecyclerViewOnItemClickListener itemClickListener;

    public ExampleExplainHomePageAdapter(Context context, List<HashMap<String, Object>> recyclerVierExampleList) {
        this.context = context;
        this.recyclerVierExampleList = recyclerVierExampleList;
    }

    public void setOnItemClickListener(RecyclerViewOnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_example_explain_home_page, null), itemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        HashMap<String, Object> map = recyclerVierExampleList.get(position);
        GlideUtil.loadNetImageView(context,map.get("himg") + "",vh.imageView);
        vh.tv_title.setText(map.get("title") + "");
        vh.tv_type.setText(map.get("subject") + " · " + map.get("grade"));
    }

    @Override
    public int getItemCount() {
        return recyclerVierExampleList == null ? 0 : recyclerVierExampleList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView imageView;
        private final TextView tv_title;
        private final TextView tv_type;
        private final LinearLayout ll_base;
        private RecyclerViewOnItemClickListener itemClickListener;

        public MyViewHolder(View itemView, RecyclerViewOnItemClickListener itemClickListener) {
            super(itemView);
            imageView = ((ImageView) itemView.findViewById(R.id.imageView));
            tv_title = ((TextView) itemView.findViewById(R.id.tv_title));
            tv_type = ((TextView) itemView.findViewById(R.id.tv_type));
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
