package com.youyi.ywl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youyi.ywl.R;
import com.youyi.ywl.inter.ReplyIconClickListener;
import com.youyi.ywl.util.ToastUtil;

/**
 * Created by Administrator on 2018/7/10.
 * 我的订单-全部 的适配器
 */

public class OrdersAdapter extends RecyclerView.Adapter {
    private Context context;
    private int index;
    private ReplyIconClickListener detailListener;

    public OrdersAdapter(Context context, int index) {
        this.context = context;
        this.index = index;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_orders, null), detailListener);
    }

    public void setOnOrderDetailClickListener(ReplyIconClickListener detailListener) {
        this.detailListener = detailListener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        switch (index) {
            case 0:
                //全部
                switch (position) {
                    case 1:
                        vh.tv_order_state.setTextColor(context.getResources().getColor(R.color.dark_red2));
                        vh.tv_order_state.setText("待支付");
                        vh.tv_operation.setBackground(context.getResources().getDrawable(R.drawable.bg_red_half_circle_line));
                        vh.tv_operation.setTextColor(context.getResources().getColor(R.color.dark_red2));
                        vh.tv_operation.setText("立即支付");
                        break;
                    case 2:
                        vh.tv_order_state.setTextColor(context.getResources().getColor(R.color.dark_red2));
                        vh.tv_order_state.setText("已取消");
                        vh.tv_operation.setBackground(context.getResources().getDrawable(R.drawable.bg_red_half_circle_line));
                        vh.tv_operation.setTextColor(context.getResources().getColor(R.color.dark_red2));
                        vh.tv_operation.setText("重新报名");
                        break;
                }
                break;
            case 1:
                //已支付
                vh.tv_order_state.setTextColor(context.getResources().getColor(R.color.light_gray27));
                vh.tv_order_state.setText("已支付");
                vh.tv_operation.setBackground(context.getResources().getDrawable(R.drawable.bg_blue_half_circle_line));
                vh.tv_operation.setTextColor(context.getResources().getColor(R.color.orangeone));
                vh.tv_operation.setText("开始学习");
                break;
            case 2:
                //待支付
                vh.tv_order_state.setTextColor(context.getResources().getColor(R.color.dark_red2));
                vh.tv_order_state.setText("待支付");
                vh.tv_operation.setBackground(context.getResources().getDrawable(R.drawable.bg_red_half_circle_line));
                vh.tv_operation.setTextColor(context.getResources().getColor(R.color.dark_red2));
                vh.tv_operation.setText("立即支付");
                break;
            case 3:
                //已取消
                vh.tv_order_state.setTextColor(context.getResources().getColor(R.color.dark_red2));
                vh.tv_order_state.setText("已取消");
                vh.tv_operation.setBackground(context.getResources().getDrawable(R.drawable.bg_red_half_circle_line));
                vh.tv_operation.setTextColor(context.getResources().getColor(R.color.dark_red2));
                vh.tv_operation.setText("重新报名");
                break;
        }

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tv_operation;
        private final TextView tv_order_state;
        private final TextView tv_order_detail;
        private ReplyIconClickListener detailListener;

        public MyViewHolder(View itemView, ReplyIconClickListener detailListener) {
            super(itemView);
            tv_order_state = ((TextView) itemView.findViewById(R.id.tv_order_state));//订单状态
            tv_operation = ((TextView) itemView.findViewById(R.id.tv_operation));//操作
            tv_order_detail = ((TextView) itemView.findViewById(R.id.tv_order_detail));//订单详情

            this.detailListener = detailListener;
            tv_order_detail.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_order_detail:
                    ToastUtil.show(context, "点击了详情按钮", 0);
                    if (detailListener != null) {
                        detailListener.OnReplyIconClick(tv_order_detail, getPosition() - 3);
                    }
                    break;
            }
        }
    }
}
