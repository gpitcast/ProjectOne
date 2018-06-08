package com.feature.projectone.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.feature.projectone.R;
import com.feature.projectone.inter.ReplyIconClickListener;
import com.feature.projectone.other.Constanst;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/18.
 * <p>
 * 违规举报界面照片gridView的适配器
 */

public class ForbidReportGridViewAdapter extends BaseAdapter {
    private Context context;
    private List<String> mPicList;

    private ReplyIconClickListener listener;

    public ForbidReportGridViewAdapter(Context context, List mPicList) {
        this.context = context;
        this.mPicList = mPicList;
    }

    public void setOnDeleteClickListener(ReplyIconClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        //return mList.size() + 1;//因为最后多了一个添加图片的ImageView
        int count = mPicList == null ? 1 : mPicList.size() + 1;
        if (count > Constanst.MAX_SELECT_PIC_NUM) {
            return mPicList.size();
        } else {
            return count;
        }
    }

    @Override
    public Object getItem(int position) {
        return mPicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder holder = null;
        if (convertView == null) {
            holder = new MyViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_forbid_report, null);
            holder.iv_picture = ((ImageView) convertView.findViewById(R.id.iv_picture));
            holder.iv_delete = ((ImageView) convertView.findViewById(R.id.iv_delete));
            convertView.setTag(holder);
        } else {
            holder = ((MyViewHolder) convertView.getTag());
        }

        if (position < mPicList.size()) {
            //代表+号之前的需要正常显示图片
            String picUrl = mPicList.get(position);
            Glide.with(context).load(picUrl).into(holder.iv_picture);
            holder.iv_delete.setVisibility(View.VISIBLE);
            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.OnReplyIconClick(v, position);
                    }
                }
            });
        } else {
            Glide.with(context).load(R.mipmap.icon_add_picture).into(holder.iv_picture);
            holder.iv_delete.setVisibility(View.GONE);
        }

        return convertView;
    }


    private class MyViewHolder {
        private ImageView iv_picture;
        private ImageView iv_delete;
    }
}
