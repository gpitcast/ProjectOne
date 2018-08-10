package com.youyi.YWL.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.youyi.YWL.R;
import com.youyi.YWL.activity.RechargePaymentActivity;
import com.youyi.YWL.inter.ReplyIconClickListener;

import java.util.List;

/**
 * Created by Administrator on 2018/7/25.
 * 充值支付界面 的 支付类型适配器
 */

public class RechargePaymentRecyclerAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Boolean> checkedList;
    private ReplyIconClickListener iconClickListener;

    public RechargePaymentRecyclerAdapter(Context context, List<Boolean> checkedList) {
        this.context = context;
        this.checkedList = checkedList;
    }

    public void setOnCheckedClickListener(ReplyIconClickListener iconClickListener) {
        this.iconClickListener = iconClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recharge_payment_recycler, null), iconClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        switch (position) {
            case 0:
                vh.iv_pay_type.setImageResource(R.mipmap.icon_alipay);
                vh.tv_pay_type.setText("支付宝");
                break;
            case 1:
                vh.iv_pay_type.setImageResource(R.mipmap.icon_wechat_pay);
                vh.tv_pay_type.setText("微信支付");
                break;
        }

        Boolean isChecked = checkedList.get(position);
        if (isChecked) {
            vh.iv_checked.setImageResource(R.mipmap.icon_checked_circle);
        } else {
            vh.iv_checked.setImageResource(R.mipmap.icon_nochecked_circle);
        }
    }

    @Override
    public int getItemCount() {
        return checkedList == null ? 0 : checkedList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView iv_pay_type;
        private final TextView tv_pay_type;
        private final ImageView iv_checked;
        private ReplyIconClickListener iconClickListener;

        public MyViewHolder(View itemView, ReplyIconClickListener iconClickListener) {
            super(itemView);
            iv_pay_type = ((ImageView) itemView.findViewById(R.id.iv_pay_type));
            tv_pay_type = ((TextView) itemView.findViewById(R.id.tv_pay_type));
            iv_checked = ((ImageView) itemView.findViewById(R.id.iv_checked));

            this.iconClickListener = iconClickListener;
            iv_checked.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_checked:
                    if (iconClickListener != null) {
                        iconClickListener.OnReplyIconClick(iv_checked, getPosition());
                    }
                    break;
            }
        }
    }
}
