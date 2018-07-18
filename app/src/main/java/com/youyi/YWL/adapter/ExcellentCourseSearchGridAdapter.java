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
    private List<String> gridList;

    public ExcellentCourseSearchGridAdapter(Context context, List<String> gridList) {
        this.context = context;
        this.gridList = gridList;
    }

    @Override
    public int getCount() {
        return gridList == null ? 0 : gridList.size();
    }

    @Override
    public Object getItem(int position) {
        return gridList.get(position);
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

        holder.tv_name.setText(gridList.get(position));

        return convertView;
    }

    private class MyViewHolder {
        private TextView tv_name;
    }
}
