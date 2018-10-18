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
 * Created by Administrator on 2018/9/12.
 * 学习轨迹  -- 视频课fragment 的适配器
 */

public class VedioClassFragmentAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<HashMap<String, Object>> dataList;
    private RecyclerViewOnItemClickListener itemClickListener;

    public VedioClassFragmentAdapter(Context context, List<HashMap<String, Object>> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public void setOnItemClickListener(RecyclerViewOnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_online_hearing_fragment, null), itemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder vh = (MyViewHolder) holder;
        HashMap<String, Object> map = dataList.get(position);

        ArrayList<String> tagList = (ArrayList<String>) map.get("tags");
        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        vh.recyclerView.setLayoutManager(manager);
        SubjectAdapterTagAdapter subjectAdapterTagAdapter = new SubjectAdapterTagAdapter(context, tagList);
        vh.recyclerView.setAdapter(subjectAdapterTagAdapter);
        vh.recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return vh.ll_base.onTouchEvent(event);
            }
        });

        vh.tv_time.setText(map.get("date") + "");

        GlideUtil.loadNetImageView(context,map.get("img") + "",vh.imageView);

        vh.tv_title.setText(map.get("title") + "");
        vh.tv_classes.setText(map.get("courseNums") + "节课");
        vh.tv_looked_count.setText(map.get("views") + "人正在学");
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final RecyclerView recyclerView;
        private final LinearLayout ll_base;
        private final TextView tv_time;
        private final TextView tv_classes;
        private final ImageView imageView;
        private final TextView tv_looked_count;
        private final TextView tv_title;
        private RecyclerViewOnItemClickListener itemClickListener;

        public MyViewHolder(View itemView, RecyclerViewOnItemClickListener itemClickListener) {
            super(itemView);
            recyclerView = ((RecyclerView) itemView.findViewById(R.id.recyclerView));
            ll_base = ((LinearLayout) itemView.findViewById(R.id.ll_base));
            tv_time = ((TextView) itemView.findViewById(R.id.tv_time));
            imageView = ((ImageView) itemView.findViewById(R.id.imageView));
            tv_classes = ((TextView) itemView.findViewById(R.id.tv_classes));
            tv_looked_count = ((TextView) itemView.findViewById(R.id.tv_looked_count));
            tv_title = ((TextView) itemView.findViewById(R.id.tv_title));

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
