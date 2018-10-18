package com.youyi.ywl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youyi.ywl.R;
import com.youyi.ywl.inter.RecyclerViewOnItemClickListener;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/7/13.
 */

public class ExcellentCourseAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<HashMap<String, Object>> recyclerList;
    private RecyclerViewOnItemClickListener itemClickListener;

    public ExcellentCourseAdapter(Context context, List<HashMap<String, Object>> recyclerList) {
        this.context = context;
        this.recyclerList = recyclerList;
    }

    public void setOnItemClickListener(RecyclerViewOnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_excellent_course, null), itemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        HashMap<String, Object> map = recyclerList.get(position);
        vh.tv_name.setText(map.get("cate_name") + "");
        Boolean ischecked = (Boolean) map.get("isChecked");
        if (ischecked) {
            vh.tv_name.setTextColor(context.getResources().getColor(R.color.orangeone));
        } else {
            vh.tv_name.setTextColor(context.getResources().getColor(R.color.normal_black));
        }

        //设置间距
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


    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tv_name;
        private RecyclerViewOnItemClickListener itemClickListener;

        public MyViewHolder(View itemView, RecyclerViewOnItemClickListener itemClickListener) {
            super(itemView);
            tv_name = ((TextView) itemView.findViewById(R.id.tv_name));//名称

            this.itemClickListener = itemClickListener;
            tv_name.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_name:
                    if (itemClickListener != null) {
                        itemClickListener.OnItemClick(tv_name, getPosition());
                    }
                    break;
            }
        }
    }
}
