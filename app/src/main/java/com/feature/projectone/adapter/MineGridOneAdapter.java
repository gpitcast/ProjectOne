package com.feature.projectone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.feature.projectone.R;

/**
 * Created by Administrator on 2018/5/2.
 * 首页我的fragment里的第一个gridView的适配器
 */

public class MineGridOneAdapter extends BaseAdapter {
    private String[] strListOne;
    private int[] imgListOne;

    public MineGridOneAdapter(String[] strListOne, int[] imgListOne) {
        this.strListOne = strListOne;
        this.imgListOne = imgListOne;
    }

    @Override
    public int getCount() {
        return strListOne == null ? 0 : strListOne.length;
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
            convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mine_grid_one, null);
            vh.iv_icon = ((ImageView) convertView.findViewById(R.id.iv_icon));
            vh.tv_name = ((TextView) convertView.findViewById(R.id.tv_name));
            convertView.setTag(vh);
        } else {
            vh = ((MyViewHolder) convertView.getTag());
        }
        vh.tv_name.setText(strListOne[position]);
        vh.iv_icon.setImageResource(imgListOne[position]);
        return convertView;
    }

    private class MyViewHolder {
        private ImageView iv_icon;
        private TextView tv_name;
    }
}
