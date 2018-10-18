package com.youyi.ywl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.youyi.ywl.R;
import com.youyi.ywl.util.GlideUtil;

/**
 * Created by Administrator on 2018/9/12.
 * 学习轨迹  -- 直播课fragment 的适配器
 */

public class OnliveClassFragmentAdapter extends RecyclerView.Adapter {
    private Context context;

    public OnliveClassFragmentAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_onlive_class_fragment, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;

        GlideUtil.loadNetImageView(context, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1538127311&di=ebf4ed866f663b2dbbad485d971cd0c0&imgtype=jpg&er=1&src=http%3A%2F%2Fpic9.photophoto.cn%2F20081031%2F0020032716310114_b.jpg", vh.imageView);
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = ((ImageView) itemView.findViewById(R.id.imageView));
        }
    }
}
