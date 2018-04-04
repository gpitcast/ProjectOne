package com.feature.projectone.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.inter.OnTvCheckedChangedAdapterListener;
import com.feature.projectone.inter.OnTvCheckedChangedListener;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.view.ShaixuanTextView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/3.
 * 资源下载筛选popwindow里的gridview 适配器
 */

public class PopGridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> stringList;
    private OnTvCheckedChangedAdapterListener listener;

    public PopGridViewAdapter(Context context, ArrayList<String> stringList) {
        this.context = context;
        this.stringList = stringList;
    }

    @Override
    public int getCount() {
        return stringList == null ? 0 : stringList.size();// 返回Adapter中数据集的条目数
    }

    @Override
    public Object getItem(int position) {
        return null; // 获取数据集中与指定索引对应的数据项
    }

    @Override
    public long getItemId(int position) {
        return position;// 取在列表中与指定索引对应的行id
    }

    public void setOnCheckedListener(OnTvCheckedChangedAdapterListener listener) {
        this.listener = listener;
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
        vh.tv_title.setText(stringList.get(position));
        vh.tv_title.setOnCheckedChangedListener(new OnTvCheckedChangedListener() {
            @Override
            public void onCheckedChange(boolean isChecked) {
                listener.onCheckedChanged(isChecked, position);
            }
        });
        return convertView;
    }

    class ViewHolder {
        ShaixuanTextView tv_title;
    }
}
