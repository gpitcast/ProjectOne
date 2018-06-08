package com.feature.projectone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.activity.GroupNoticeListActivity;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/6/1.
 * 群公告列表的适配器
 */

public class GroupNoticeListAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<HashMap<String, Object>> dataList;

    public GroupNoticeListAdapter(Context context, List<HashMap<String, Object>> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_group_notice_list, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        HashMap<String, Object> map = dataList.get(position);
        vh.tv_content.setText(map.get("desc") + "");
        vh.tv_time.setText("by群助手 : " + map.get("addtime"));
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_content;
        private final TextView tv_time;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_content = ((TextView) itemView.findViewById(R.id.tv_content));
            tv_time = ((TextView) itemView.findViewById(R.id.tv_time));

        }
    }
}
