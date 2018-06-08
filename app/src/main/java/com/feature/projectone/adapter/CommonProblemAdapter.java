package com.feature.projectone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.activity.CommonProblemActivity;
import com.feature.projectone.inter.RecyclerViewOnItemClickListener;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/5/18.
 * 常见问题适配器
 */

public class CommonProblemAdapter extends RecyclerView.Adapter {
    private Context context;
    private RecyclerViewOnItemClickListener listener;
    private List<HashMap<String, Object>> dataList;

    public CommonProblemAdapter(Context context, List<HashMap<String, Object>> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public void setOnItemClickLinstener(RecyclerViewOnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_common_problem, null), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        HashMap<String, Object> map = dataList.get(position);
        vh.tv_title.setText(map.get("title") + "");
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final LinearLayout ll_base;
        private final TextView tv_title;
        private RecyclerViewOnItemClickListener listener;

        public MyViewHolder(View itemView, RecyclerViewOnItemClickListener listener) {
            super(itemView);
            ll_base = ((LinearLayout) itemView.findViewById(R.id.ll_base));
            tv_title = ((TextView) itemView.findViewById(R.id.tv_title));

            this.listener = listener;
            ll_base.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ll_base:
                    if (listener != null) {
                        listener.OnItemClick(ll_base, getPosition() - 3);
                    }
                    break;
            }
        }
    }
}
