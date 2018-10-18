package com.youyi.ywl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.youyi.ywl.R;
import com.youyi.ywl.inter.RecyclerViewOnItemClickListener;

import java.util.HashMap;
import java.util.List;


/**
 * Created by Administrator on 2018/8/25.
 * 习题讲解 - 选择页码适配器
 */

public class ExampleExplainSelectPageAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<HashMap<String, Object>> mSingleClassList;
    private RecyclerViewOnItemClickListener itemClickListener;


    public ExampleExplainSelectPageAdapter(Context context, List<HashMap<String, Object>> mSingleClassList) {
        this.context = context;
        this.mSingleClassList = mSingleClassList;
    }

    public void setOnItemClickListener(RecyclerViewOnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_example_explain_select_page, null), itemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        HashMap<String, Object> map = mSingleClassList.get(position);
        vh.tv_name.setText(map.get("page") + "");
        String zd = map.get("zd") + "";
        if ("0".equals(zd)) {
            //非重点
            vh.iv_star.setVisibility(View.GONE);
        } else {
            //是重点
            vh.iv_star.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mSingleClassList == null ? 0 : mSingleClassList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tv_name;
        private final ImageView iv_star;
        private RecyclerViewOnItemClickListener itemClickListener;

        public MyViewHolder(View itemView, RecyclerViewOnItemClickListener itemClickListener) {
            super(itemView);
            tv_name = ((TextView) itemView.findViewById(R.id.tv_name));
            iv_star = ((ImageView) itemView.findViewById(R.id.iv_star));

            this.itemClickListener = itemClickListener;
            tv_name.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_name:
                    if (itemClickListener != null) {
                        itemClickListener.OnItemClick(tv_name, getPosition() - 3);
                    }
                    break;
            }
        }
    }
}
