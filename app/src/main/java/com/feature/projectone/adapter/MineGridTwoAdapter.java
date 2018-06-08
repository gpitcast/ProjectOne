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
 * 首页我的fragment里的第二个gridView的适配器
 */

public class MineGridTwoAdapter extends BaseAdapter {
    private String[] strListTwo;
    private int[] imgListTwo;

    public MineGridTwoAdapter(String[] strListTwo, int[] imgListTwo) {
        this.strListTwo = strListTwo;
        this.imgListTwo = imgListTwo;
    }

    @Override
    public int getCount() {
        return strListTwo == null ? 0 : strListTwo.length;
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
            convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mine_grid_two, null);
            vh.tv_name = ((TextView) convertView.findViewById(R.id.tv_name));
            vh.iv_icon = ((ImageView) convertView.findViewById(R.id.iv_icon));
            convertView.setTag(vh);
        } else {
            vh = ((MyViewHolder) convertView.getTag());
        }
        vh.tv_name.setText(strListTwo[position]);
        vh.iv_icon.setImageResource(imgListTwo[position]);
        return convertView;
    }

    private class MyViewHolder {
        private ImageView iv_icon;
        private TextView tv_name;
    }
}
