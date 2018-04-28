package com.feature.projectone.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.feature.projectone.R;

import java.util.List;

/**
 * Created by Administrator on 2018/4/19.
 * 搜索历史fragment的适配器
 */

public class SearchHistoryAdapter extends BaseAdapter {
    private Context context;
    private List<String> mDataList;

    public SearchHistoryAdapter(Context context, List<String> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_search_history, null);
            vh.tv_history = (TextView) convertView.findViewById(R.id.tv_history);
            convertView.setTag(vh);
        } else {
            vh = ((MyViewHolder) convertView.getTag());
        }
        vh.tv_history.setText(mDataList.get(position));
        return convertView;
    }

    private class MyViewHolder {
        private TextView tv_history;
    }
}
