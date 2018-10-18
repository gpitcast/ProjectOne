package com.youyi.ywl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.youyi.ywl.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/7/13.
 */

public class ExcellentCourseSubjectAdapter extends BaseAdapter {
    private Context context;
    private List<HashMap<String, Object>> subjectList;

    public ExcellentCourseSubjectAdapter(Context context, List<HashMap<String, Object>> subjectList) {
        this.context = context;
        this.subjectList = subjectList;
    }

    @Override
    public int getCount() {
        return subjectList == null ? 0 : subjectList.size();
    }

    @Override
    public Object getItem(int position) {
        return subjectList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_excellent_course_subject, null);
            holder.tv_name = ((TextView) convertView.findViewById(R.id.tv_name));
            convertView.setTag(holder);
        } else {
            holder = ((MyViewHolder) convertView.getTag());
        }
        HashMap<String, Object> map = subjectList.get(position);
        holder.tv_name.setText(map.get("cate_name") + "");
        Boolean isChecked = (Boolean) map.get("isChecked");
        if (isChecked) {
            holder.tv_name.setTextColor(context.getResources().getColor(R.color.orangeone));
        } else {
            holder.tv_name.setTextColor(context.getResources().getColor(R.color.normal_black));
        }

        return convertView;
    }

    private class MyViewHolder {
        private TextView tv_name;
    }
}
