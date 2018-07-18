package com.youyi.YWL.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youyi.YWL.R;
import com.youyi.YWL.activity.CreditExchangeRecordActivity;

/**
 * Created by Administrator on 2018/7/6.
 * 积分兑换记录界面的适配器
 */

public class CreditExchangeRecordAdapter extends RecyclerView.Adapter {
    private Context context;

    public CreditExchangeRecordAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_credit_exchange_record,null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
