package com.youyi.ywl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.youyi.ywl.R;
import com.youyi.ywl.util.GlideUtil;

import java.util.List;

/**
 * Created by Administrator on 2018/6/27.
 * 社区帖子详情界面的评论gridview的适配器
 */

public class CommunityPostDetailGridAdapter extends BaseAdapter {
    private Context context;
    private List<String> postImgList;

    public CommunityPostDetailGridAdapter(Context context, List<String> postImgList) {
        this.context = context;
        this.postImgList = postImgList;
    }

    @Override
    public int getCount() {
        return postImgList == null ? 0 : postImgList.size();
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
        MyViewHolder holder;
        if (convertView == null) {
            holder = new MyViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_community_post_gridview, null);
            holder.imageView = ((ImageView) convertView.findViewById(R.id.imageView));
            convertView.setTag(holder);
        } else {
            holder = ((MyViewHolder) convertView.getTag());
        }

        GlideUtil.loadNetImageView(context,postImgList.get(position),holder.imageView);
        return convertView;
    }

    private class MyViewHolder {
        private ImageView imageView;
    }
}
