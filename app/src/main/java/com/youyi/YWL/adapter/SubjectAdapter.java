package com.youyi.YWL.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.youyi.YWL.R;
import com.youyi.YWL.inter.RecyclerViewOnItemClickListener;

/**
 * Created by Administrator on 2018/7/13.
 * 精品课程-fragment的适配器
 */

public class SubjectAdapter extends RecyclerView.Adapter {
    private Context context;
    private String type;
    private RecyclerViewOnItemClickListener itemClickListener;

    public SubjectAdapter(Context context, String type) {
        this.context = context;
        this.type = type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_subject, null), itemClickListener);
    }

    public void setOnItemClickListener(RecyclerViewOnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder vh = (MyViewHolder) holder;
        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        vh.recyclerView.setLayoutManager(manager);
        SubjectAdapterTagAdapter subjectAdapterTagAdapter = new SubjectAdapterTagAdapter(context, type);
        vh.recyclerView.setAdapter(subjectAdapterTagAdapter);

        vh.recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return vh.ll_base.onTouchEvent(event);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final RecyclerView recyclerView;
        private final LinearLayout ll_base;
        private RecyclerViewOnItemClickListener itemClickListener;

        public MyViewHolder(View itemView, RecyclerViewOnItemClickListener itemClickListener) {
            super(itemView);
            recyclerView = ((RecyclerView) itemView.findViewById(R.id.recyclerView));
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
