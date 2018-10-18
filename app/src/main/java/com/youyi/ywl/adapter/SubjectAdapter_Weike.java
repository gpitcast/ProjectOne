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

public class SubjectAdapter_Weike extends RecyclerView.Adapter {
    private Context context;
    private List<HashMap<String, Object>> dataList;
    private RecyclerViewOnItemClickListener itemClickListener;

    public SubjectAdapter_Weike(Context context, List<HashMap<String, Object>> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_subject_weike, null), itemClickListener);
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
        vh.tv_looked_count.setText(map.get("views") + " 人已观看");

        //类型
        vh.tv_type.setVisibility(View.VISIBLE);
        if (position % 2 == 0) {
            vh.tv_type.setTextColor(context.getResources().getColor(R.color.white));
            vh.tv_type.setBackgroundResource(R.drawable.bg_half_circle_alpha_black);
            vh.tv_type.setText("视频");
        } else {
            vh.tv_type.setTextColor(context.getResources().getColor(R.color.normal_black));
            vh.tv_type.setBackgroundResource(R.drawable.bg_half_circle_alpha_white);
            vh.tv_type.setText("图片");
        }
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
        private final TextView tv_looked_count;
        private final TextView tv_type;
        private RecyclerViewOnItemClickListener itemClickListener;

        public MyViewHolder(View itemView, RecyclerViewOnItemClickListener itemClickListener) {
            super(itemView);
            recyclerView = ((RecyclerView) itemView.findViewById(R.id.recyclerView));
            ll_base = ((LinearLayout) itemView.findViewById(R.id.ll_base));
            imageView = ((ImageView) itemView.findViewById(R.id.imageView));//图片
            tv_title = ((TextView) itemView.findViewById(R.id.tv_title));//标题
            tv_tag = ((TextView) itemView.findViewById(R.id.tv_tag));//标签
            tv_looked_count = ((TextView) itemView.findViewById(R.id.tv_looked_count));//观看人数
            tv_type = ((TextView) itemView.findViewById(R.id.tv_type));//类型

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
