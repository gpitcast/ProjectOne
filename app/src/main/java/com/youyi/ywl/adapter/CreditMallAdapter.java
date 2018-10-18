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
 * Created by Administrator on 2018/7/9.
 * 学分商城界面的适配器
 */

public class CreditMallAdapter extends RecyclerView.Adapter {
    private Context context;
    private RecyclerViewOnItemClickListener listener;
    private List<HashMap<String, Object>> dataList;

    public CreditMallAdapter(Context context, List<HashMap<String, Object>> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public void setOnItemClickListener(RecyclerViewOnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.credit_mall, null), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        HashMap<String, Object> map = dataList.get(position);
        GlideUtil.loadNetImageView(context,map.get("img") + "",vh.iv_commodity);
        vh.tv_name.setText(map.get("title") + "");
        vh.tv_credit_count.setText(map.get("score") + "分");
        vh.tv_against_count.setText(map.get("exchangeCount") + "人已兑");
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final LinearLayout ll_base;
        private final ImageView iv_commodity;
        private final TextView tv_name;
        private final TextView tv_credit_count;
        private final TextView tv_against_count;
        private RecyclerViewOnItemClickListener listener;

        public MyViewHolder(View itemView, RecyclerViewOnItemClickListener listener) {
            super(itemView);
            ll_base = ((LinearLayout) itemView.findViewById(R.id.ll_base));//根布局
            iv_commodity = ((ImageView) itemView.findViewById(R.id.iv_commodity));//商品图片
            tv_name = ((TextView) itemView.findViewById(R.id.tv_name));//商品名称
            tv_credit_count = ((TextView) itemView.findViewById(R.id.tv_credit_count));//积分数量
            tv_against_count = ((TextView) itemView.findViewById(R.id.tv_against_count));//已兑人数


            this.listener = listener;
            ll_base.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_base:
                    if (listener != null) {
                        listener.OnItemClick(ll_base, getPosition() - 3);
                    }
                    break;
            }
        }
    }
}
