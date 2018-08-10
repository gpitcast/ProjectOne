package com.youyi.YWL.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youyi.YWL.R;

/**
 * Created by Administrator on 2018/7/23.
 * 精品课程-详情  课程介绍fragment 里的'适用人群'或者'教学目标' 适配器
 */

public class FitPeopleOrTeachingTargetAdapter extends RecyclerView.Adapter {
    private Context context;

    public FitPeopleOrTeachingTargetAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_fitpeople_or_teachingtarget, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        vh.textView.setText(position + ".");
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = ((TextView) itemView.findViewById(R.id.textView));
        }
    }
}
