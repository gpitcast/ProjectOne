package com.youyi.YWL.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youyi.YWL.R;
import com.youyi.YWL.activity.ExcellentCourseActivity;

import java.util.List;

/**
 * Created by Administrator on 2018/7/13.
 */

public class ExcellentCourseAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<String> recyclerList;

    public ExcellentCourseAdapter(Context context, List<String> recyclerList) {
        this.context = context;
        this.recyclerList = recyclerList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_excellent_course, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        vh.tv_name.setText(recyclerList.get(position));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        switch (position) {
            case 0:
                break;
            case 1:
                params.leftMargin = 30;
                params.rightMargin = 30;
                vh.tv_name.setLayoutParams(params);
                break;
            case 2:

                break;
        }
    }

    @Override
    public int getItemCount() {
        return recyclerList == null ? 0 : recyclerList.size();
    }


    private class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = ((TextView) itemView.findViewById(R.id.tv_name));//名称
        }
    }
}
