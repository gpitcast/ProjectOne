package com.feature.projectone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.feature.projectone.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/9.
 * 热点推荐列表的item里的gridview的适配器
 */

public class NewsHeadlinesGridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> img_lists;

    public NewsHeadlinesGridViewAdapter(Context context, ArrayList<String> img_lists) {
        this.context = context;
        this.img_lists = img_lists;
    }

    @Override
    public int getCount() {
        return img_lists == null ? 0 : img_lists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        MyViewHolder vh = null;
        if (convertView == null) {
            vh = new MyViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_news_headlines_gridview, null);
            vh.iv = ((ImageView) convertView.findViewById(R.id.iv));
            convertView.setTag(vh);
        } else {
            vh = ((MyViewHolder) convertView.getTag());
        }
        String imgUrl = img_lists.get(position);
        Picasso.with(context).load(imgUrl).placeholder(R.mipmap.img_loading_default).error(R.mipmap.img_load_error).into(vh.iv);
        return convertView;
    }

    private class MyViewHolder {
        private ImageView iv;
    }
}
