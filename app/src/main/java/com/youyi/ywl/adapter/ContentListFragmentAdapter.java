package com.youyi.ywl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youyi.ywl.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/8/13.
 * 精品微课-详情  内容列表fragment的recyclerview适配器
 */

public class ContentListFragmentAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<HashMap<String, Object>> dataList;

    public ContentListFragmentAdapter(Context context, List<HashMap<String, Object>> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_content_list_fragment, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        HashMap<String, Object> map = dataList.get(position);
        vh.tv_position.setText(map.get("sort") + "");
        vh.tv_title.setText(map.get("title")+"");
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_position;
        private final TextView tv_title;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_position = ((TextView) itemView.findViewById(R.id.tv_position));
            tv_title = ((TextView) itemView.findViewById(R.id.tv_title));
        }
    }
}
