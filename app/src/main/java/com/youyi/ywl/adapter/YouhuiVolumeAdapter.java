package com.youyi.ywl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.youyi.ywl.R;
import com.youyi.ywl.inter.ReplyIconClickListener;

import java.util.List;

/**
 * Created by Administrator on 2018/7/20.
 */

public class YouhuiVolumeAdapter extends RecyclerView.Adapter {
    private Context context;
    private ReplyIconClickListener iconClickListener;
    private List<Boolean> checkedList;

    public YouhuiVolumeAdapter(Context context, List<Boolean> checkedList) {
        this.context = context;
        this.checkedList = checkedList;
    }

    public void setOnCheckedClickListener(ReplyIconClickListener iconClickListener) {
        this.iconClickListener = iconClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_youhui_volume, null), iconClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        Boolean isChecked = checkedList.get(position);
        if (isChecked){
            vh.iv_checked.setImageResource(R.mipmap.icon_brown_choose_circle_solid);
        }else {
            vh.iv_checked.setImageResource(R.mipmap.icon_brown_choose_circle_line);
        }
    }

    @Override
    public int getItemCount() {
        return checkedList == null ? 0 : checkedList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView iv_checked;
        private ReplyIconClickListener iconClickListener;

        public MyViewHolder(View itemView, ReplyIconClickListener iconClickListener) {
            super(itemView);
            iv_checked = ((ImageView) itemView.findViewById(R.id.iv_checked));

            this.iconClickListener = iconClickListener;
            iv_checked.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_checked:
                    if (iconClickListener != null) {
                        iconClickListener.OnReplyIconClick(iv_checked, getPosition() - 3);
                    }
                    break;
            }
        }
    }

}
