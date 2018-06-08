package com.feature.projectone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.feature.projectone.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/5/25.
 * 群组信息显示群成员信息的gridview的adapter
 */

public class GroupInformationGridViewAdapter extends BaseAdapter {
    private Context context;
    private List<HashMap<String, Object>> mUserLists;

    public GroupInformationGridViewAdapter(Context context, List<HashMap<String, Object>> mUserLists) {
        this.context = context;
        this.mUserLists = mUserLists;
    }

    @Override
    public int getCount() {
        return mUserLists == null ? 0 : mUserLists.size() > 10 ? 10 : mUserLists.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder = null;
        if (convertView == null) {
            holder = new MyViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_group_information_gridview, null);
            holder.iv_head = ((ImageView) convertView.findViewById(R.id.iv_head));
            holder.tv_type = ((TextView) convertView.findViewById(R.id.tv_type));
            convertView.setTag(holder);
        } else {
            holder = ((MyViewHolder) convertView.getTag());
        }

        HashMap<String, Object> map = mUserLists.get(position);
        Picasso.with(context).load(map.get("img") + "").placeholder(R.mipmap.img_loading)
                .error(R.mipmap.img_load_error).into(holder.iv_head);
        holder.tv_type.setText(map.get("identity") + "");
        return convertView;
    }

    private class MyViewHolder {
        private ImageView iv_head;
        private TextView tv_type;
    }
}
