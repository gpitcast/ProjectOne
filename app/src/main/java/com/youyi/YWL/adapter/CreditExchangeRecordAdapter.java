package com.youyi.YWL.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youyi.YWL.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/7/6.
 * 积分兑换记录界面的适配器
 */

public class CreditExchangeRecordAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<HashMap<String, Object>> dataList;

    public CreditExchangeRecordAdapter(Context context, List<HashMap<String, Object>> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_credit_exchange_record, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        HashMap<String, Object> map = dataList.get(position);
        vh.tv_name.setText(map.get("title") + "");
        vh.tv_credit_count.setText(map.get("score") + "分");
        vh.tv_time.setText(map.get("add_time") + "");
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_name;
        private final TextView tv_credit_count;
        private final TextView tv_time;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_name = ((TextView) itemView.findViewById(R.id.tv_name));//兑换的商品名称
            tv_credit_count = ((TextView) itemView.findViewById(R.id.tv_credit_count));//话费的积分
            tv_time = ((TextView) itemView.findViewById(R.id.tv_time));//兑换的时间
        }
    }
}
