package com.feature.projectone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.feature.projectone.R;

/**
 * Created by Administrator on 2018/3/30.
 * 资讯头条详情适配器
 */

public class NewsHeadLinesDetailsAdapter extends RecyclerView.Adapter {

    private Context context;

    public NewsHeadLinesDetailsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_news_headlines_details, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 30;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {


        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
