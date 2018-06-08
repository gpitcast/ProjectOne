package com.feature.projectone.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.inter.RecyclerViewOnItemClickListener;

/**
 * Created by Administrator on 2018/5/2.
 * 首页我的fragment里的RecyclerView的适配器
 */

public class MineRecyclerAdapter extends RecyclerView.Adapter {
    private String[] strListThree;
    private int[] imgListThree;
    private MyViewHolder vh;
    private RecyclerViewOnItemClickListener listener;

    public MineRecyclerAdapter(String[] strListThree, int[] imgListThree) {
        this.strListThree = strListThree;
        this.imgListThree = imgListThree;
    }

    public void setOnItemClickListener(RecyclerViewOnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mine_recyclerview, null), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        vh = ((MyViewHolder) holder);
        vh.tv_name.setText(strListThree[position]);
        vh.iv_icon.setImageResource(imgListThree[position]);
    }

    @Override
    public int getItemCount() {
        return strListThree == null ? 0 : strListThree.length;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView iv_icon;
        private final TextView tv_name;
        private final LinearLayout ll_base;
        private RecyclerViewOnItemClickListener listener;

        public MyViewHolder(View itemView, RecyclerViewOnItemClickListener listener) {
            super(itemView);
            iv_icon = ((ImageView) itemView.findViewById(R.id.iv_icon));//图标
            tv_name = ((TextView) itemView.findViewById(R.id.tv_name));//名称
            ll_base = ((LinearLayout) itemView.findViewById(R.id.ll_base));//根布局

            this.listener = listener;
            ll_base.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.ll_base:
                    if (listener!=null){
                        listener.OnItemClick(ll_base,getPosition());
                    }
                    break;
            }
        }
    }
}
