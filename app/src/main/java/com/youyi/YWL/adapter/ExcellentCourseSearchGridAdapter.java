package com.youyi.YWL.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.youyi.YWL.R;
import com.youyi.YWL.activity.ExcellentCourseSearchActivity;

import java.util.List;

/**
 * Created by Administrator on 2018/7/16.
 * 精品课程-搜索界面 的 热门搜索fragment 的gridview 适配器
 */

public class ExcellentCourseSearchGridAdapter extends BaseAdapter {
    private Context context;
    private List<String> hotGridList;

    public ExcellentCourseSearchGridAdapter(Context context, List<String> hotGridList) {
        this.context = context;
        this.hotGridList = hotGridList;
    }

    @Override
    public int getCount() {
        return hotGridList == null ? 0 : hotGridList.size();
    }

    @Override
    public Object getItem(int position) {
        return hotGridList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder = null;
        if (convertView == null) {
            holder = new MyViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_excellent_course_search_grid, null);
            holder.tv_name = ((TextView) convertView.findViewById(R.id.tv_name));

            convertView.setTag(holder);
        } else {
            holder = ((MyViewHolder) convertView.getTag());
        }

        holder.tv_name.setText(hotGridList.get(position));

        return convertView;
    }

    private class MyViewHolder {
        private TextView tv_name;
    }
}
