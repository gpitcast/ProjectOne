package com.feature.projectone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.inter.OnTvCheckedChangedAdapterListener;
import com.feature.projectone.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/3.
 * 资源下载界面筛选popwindow 适配器
 */

public class SourceDownloadPopAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<ArrayList<String>> dataList;

    public SourceDownloadPopAdapter(Context context, List<ArrayList<String>> dataList) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_shaixuan_pop, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        switch (position) {
            case 0:
                vh.tv_title.setText(context.getResources().getString(R.string.book));
                PopGridViewAdapter popGridViewAdapter0 = new PopGridViewAdapter(context, dataList.get(position));
                vh.gridView.setAdapter(popGridViewAdapter0);
                popGridViewAdapter0.setOnCheckedListener(new OnTvCheckedChangedAdapterListener() {
                    @Override
                    public void onCheckedChanged(boolean isChecked, int position) {

                    }
                });
                vh.gridView.setNumColumns(2);
                break;
            case 1:
                vh.tv_title.setText(context.getResources().getString(R.string.xueke));
                PopGridViewAdapter popGridViewAdapter1 = new PopGridViewAdapter(context, dataList.get(position));
                vh.gridView.setAdapter(popGridViewAdapter1);
                popGridViewAdapter1.setOnCheckedListener(new OnTvCheckedChangedAdapterListener() {
                    @Override
                    public void onCheckedChanged(boolean isChecked, int position) {

                    }
                });
                vh.gridView.setNumColumns(5);
                break;
            case 2:
                vh.tv_title.setText(context.getResources().getString(R.string.version));
                PopGridViewAdapter popGridViewAdapter2 = new PopGridViewAdapter(context, dataList.get(position));
                vh.gridView.setAdapter(popGridViewAdapter2);
                popGridViewAdapter2.setOnCheckedListener(new OnTvCheckedChangedAdapterListener() {
                    @Override
                    public void onCheckedChanged(boolean isChecked, int position) {

                    }
                });
                vh.gridView.setNumColumns(4);
                break;
            case 3:
                vh.tv_title.setText(context.getResources().getString(R.string.grade));
                PopGridViewAdapter popGridViewAdapter3 = new PopGridViewAdapter(context, dataList.get(position));
                vh.gridView.setAdapter(popGridViewAdapter3);
                popGridViewAdapter3.setOnCheckedListener(new OnTvCheckedChangedAdapterListener() {
                    @Override
                    public void onCheckedChanged(boolean isChecked, int position) {

                    }
                });
                vh.gridView.setNumColumns(4);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_title;
        private final MyGridView gridView;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_title = ((TextView) itemView.findViewById(R.id.tv_title));
            gridView = ((MyGridView) itemView.findViewById(R.id.gridView));
        }
    }
}
