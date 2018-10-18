package com.youyi.ywl.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import java.util.List;

/**
 * Created by Administrator on 2018/7/13.
 * 精品课程-fragment的适配器
 */

public class SubjectAdapter_Course extends RecyclerView.Adapter {
    private Context context;
    private List<HashMap<String, Object>> dataList;
    private RecyclerViewOnItemClickListener itemClickListener;

    public SubjectAdapter_Course(Context context, List<HashMap<String, Object>> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_subject_course, null), itemClickListener);
    }

    public void setOnItemClickListener(RecyclerViewOnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder vh = (MyViewHolder) holder;
        HashMap<String, Object> map = dataList.get(position);

        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        vh.recyclerView.setLayoutManager(manager);
        SubjectAdapterTagAdapter subjectAdapterTagAdapter = new SubjectAdapterTagAdapter(context, ((ArrayList<String>) map.get("tags")));
        vh.recyclerView.setAdapter(subjectAdapterTagAdapter);
        vh.recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return vh.ll_base.onTouchEvent(event);
            }
        });

        GlideUtil.loadNetImageView(context,map.get("img") + "",vh.imageView);
        vh.tv_title.setText(map.get("title") + "");
        vh.tv_tag.setText(map.get("courseNums") + "节课");
        vh.tv_learned_count.setText(map.get("views") + " 人已学习");
        vh.tv_price.setText("¥ " + map.get("price"));
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final RecyclerView recyclerView;
        private final LinearLayout ll_base;
        private final ImageView imageView;
        private final TextView tv_title;
        private final TextView tv_tag;
        private final TextView tv_learned_count;
        private final TextView tv_price;
        private RecyclerViewOnItemClickListener itemClickListener;

        public MyViewHolder(View itemView, RecyclerViewOnItemClickListener itemClickListener) {
            super(itemView);
            recyclerView = ((RecyclerView) itemView.findViewById(R.id.recyclerView));
            ll_base = ((LinearLayout) itemView.findViewById(R.id.ll_base));
            imageView = ((ImageView) itemView.findViewById(R.id.imageView));
            tv_title = ((TextView) itemView.findViewById(R.id.tv_title));
            tv_tag = ((TextView) itemView.findViewById(R.id.tv_tag));
            tv_learned_count = ((TextView) itemView.findViewById(R.id.tv_learned_count));
            tv_price = ((TextView) itemView.findViewById(R.id.tv_price));

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
