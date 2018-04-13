package com.feature.projectone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.feature.projectone.R;
import com.feature.projectone.view.ShaixuanTextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/4/3.
 * 资源下载筛选popwindow里的gridview 适配器
 */

public class PopGridViewAdapter extends BaseAdapter {
    private Context context;
    private List<HashMap<String, Object>> dataList;
    private int selectPosition;

    public PopGridViewAdapter(Context context, List<HashMap<String, Object>> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();// 返回Adapter中数据集的条目数
    }

    @Override
    public Object getItem(int position) {
        return null; // 获取数据集中与指定索引对应的数据项
    }

    @Override
    public long getItemId(int position) {
        return position;// 取在列表中与指定索引对应的行id
    }


    public void changeState(int position) {
        selectPosition = position;
        notifyDataSetChanged();
    }

    public void changeDefault() {
        selectPosition = -1;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pop_gridview, null);
            vh = new ViewHolder();
            vh.tv_title = ((ShaixuanTextView) convertView.findViewById(R.id.tv_title));
            convertView.setTag(vh);
        } else {
            vh = ((ViewHolder) convertView.getTag());
        }
        vh.tv_title.setText(((HashMap<String, Object>) dataList.get(position)).get("cate_name") + "");
        //如果当前的position等于传过来点击的position,就去改变他的状态
        if (selectPosition != -1) {
            if (position == selectPosition) {
                vh.tv_title.setChecked();
            } else {
                //其他的恢复原来的状态
                vh.tv_title.setUnCheckd();
            }
        } else {
            //其他的恢复原来的状态
            vh.tv_title.setUnCheckd();
        }
        return convertView;
    }

    class ViewHolder {
        ShaixuanTextView tv_title;
    }
}
