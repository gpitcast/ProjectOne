package com.youyi.ywl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youyi.ywl.R;
import com.youyi.ywl.inter.RecyclerViewOnItemClickListener;
import com.youyi.ywl.util.GlideUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/9/12.
 * 学习轨迹  -- 习题讲解fragment的适配器
 */

public class ExampleExplainFragmentAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<HashMap<String, Object>> dataList;
    private RecyclerViewOnItemClickListener itemClickListener;

    public ExampleExplainFragmentAdapter(Context context, List<HashMap<String, Object>> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public void setOnItemClickListener(RecyclerViewOnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_example_explain_fragment, null), itemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        HashMap<String, Object> map = dataList.get(position);
        vh.tv_time.setText(map.get("date") + "");

        GlideUtil.loadNetImageView(context, map.get("himg") + "", vh.imageView);

        vh.tv_title.setText(map.get("title") + "");
        vh.tv_tag.setText(map.get("version") + " · " + map.get("grade") + " · " + map.get("subject"));

    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }


    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final LinearLayout ll_time;
        private final FrameLayout fl_line;
        private final TextView tv_time;
        private final ImageView imageView;
        private final TextView tv_title;
        private final TextView tv_tag;
        private final LinearLayout ll_base;
        private RecyclerViewOnItemClickListener itemClickListener;

        public MyViewHolder(View itemView, RecyclerViewOnItemClickListener itemClickListener) {
            super(itemView);
            ll_base = ((LinearLayout) itemView.findViewById(R.id.ll_base));
            ll_time = ((LinearLayout) itemView.findViewById(R.id.ll_time));
            fl_line = ((FrameLayout) itemView.findViewById(R.id.fl_line));
            tv_time = ((TextView) itemView.findViewById(R.id.tv_time));
            imageView = ((ImageView) itemView.findViewById(R.id.imageView));
            tv_title = ((TextView) itemView.findViewById(R.id.tv_title));
            tv_tag = ((TextView) itemView.findViewById(R.id.tv_tag));

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
