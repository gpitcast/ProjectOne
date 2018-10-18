package com.youyi.ywl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youyi.ywl.R;
import com.youyi.ywl.inter.RecyclerViewOnItemClickListener;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/8/25.
 * 习题讲解 - 首页的popwindow里的recyclerView适配器
 */

public class ExampleExplainMainPopAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<HashMap<String, Object>> shaixuanList;
    private RecyclerViewOnItemClickListener itemClickListener;

    public ExampleExplainMainPopAdapter(Context context, List<HashMap<String, Object>> shaixuanList) {
        this.context = context;
        this.shaixuanList = shaixuanList;
    }

    public void setOnItemClickListener(RecyclerViewOnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_example_explain_main_pop, null), itemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        HashMap<String, Object> map = shaixuanList.get(position);
        vh.tv_name.setText(map.get("name") + "");
        boolean isSelected = (boolean) map.get("isSelected");
        if (isSelected) {
            vh.tv_name.setTextColor(context.getResources().getColor(R.color.white));
            vh.tv_name.setBackgroundResource(R.drawable.bg_blue_fillet_rectangle);
        } else {
            vh.tv_name.setTextColor(context.getResources().getColor(R.color.normal_black3));
            vh.tv_name.setBackgroundResource(R.drawable.bg_blue_line_white_solid);
        }
    }

    @Override
    public int getItemCount() {
        return shaixuanList == null ? 0 : shaixuanList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tv_name;
        private RecyclerViewOnItemClickListener itemClickListener;

        public MyViewHolder(View itemView, RecyclerViewOnItemClickListener itemClickListener) {
            super(itemView);
            tv_name = ((TextView) itemView.findViewById(R.id.tv_name));

            this.itemClickListener = itemClickListener;
            tv_name.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_name:
                    if (itemClickListener != null) {
                        itemClickListener.OnItemClick(tv_name, getPosition());
                    }
                    break;
            }
        }
    }
}
