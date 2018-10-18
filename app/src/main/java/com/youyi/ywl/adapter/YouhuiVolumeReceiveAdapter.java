package com.youyi.ywl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youyi.ywl.R;
import com.youyi.ywl.inter.ReplyIconClickListener;

/**
 * Created by Administrator on 2018/7/24.
 */

public class YouhuiVolumeReceiveAdapter extends RecyclerView.Adapter {
    private Context context;
    private ReplyIconClickListener iconClickListener;

    public YouhuiVolumeReceiveAdapter(Context context) {
        this.context = context;
    }

    public void setOnIconClickListener(ReplyIconClickListener iconClickListener) {
        this.iconClickListener = iconClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_youhui_volume_receive, null), iconClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }


    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tv_receive_stage;
        private ReplyIconClickListener iconClickListener;

        public MyViewHolder(View itemView, ReplyIconClickListener iconClickListener) {
            super(itemView);

            tv_receive_stage = ((TextView) itemView.findViewById(R.id.tv_receive_stage));//领取状态

            this.iconClickListener = iconClickListener;
            tv_receive_stage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_receive_stage:
                    if (iconClickListener != null) {
                        iconClickListener.OnReplyIconClick(tv_receive_stage, getPosition() - 3);
                    }
                    break;
            }
        }
    }
}
