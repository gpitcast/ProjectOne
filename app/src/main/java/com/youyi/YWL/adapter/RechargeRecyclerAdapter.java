package com.youyi.YWL.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youyi.YWL.R;
import com.youyi.YWL.inter.RecyclerViewOnItemClickListener;

/**
 * Created by Administrator on 2018/7/25.
 * 充值界面的 金额的适配器
 */

public class RechargeRecyclerAdapter extends RecyclerView.Adapter {
    private Context context;
    private RecyclerViewOnItemClickListener itemClickListener;

    public RechargeRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemClickListener(RecyclerViewOnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recharge_recycler, null), itemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        switch (position) {
            case 0:
                vh.tv_recommend_tag.setVisibility(View.VISIBLE);
                vh.fl_bg_outside.setBackgroundColor(context.getResources().getColor(R.color.light_blue10));
                vh.ll_inside.setBackgroundColor(context.getResources().getColor(R.color.light_blue11));
                vh.ll_jifen_give.setVisibility(View.VISIBLE);
                vh.tv_jifen_give.setText("赠送100积分");
                vh.tv_money_amount.setText("¥ 1000.00");
                break;
            case 1:
                vh.tv_recommend_tag.setVisibility(View.GONE);
                vh.fl_bg_outside.setBackgroundColor(context.getResources().getColor(R.color.light_blue12));
                vh.ll_inside.setBackgroundColor(context.getResources().getColor(R.color.light_blue13));
                vh.ll_jifen_give.setVisibility(View.VISIBLE);
                vh.tv_jifen_give.setText("赠送50积分");
                vh.tv_money_amount.setText("¥ 500.00");
                break;
            case 2:
                vh.tv_recommend_tag.setVisibility(View.GONE);
                vh.fl_bg_outside.setBackgroundColor(context.getResources().getColor(R.color.light_blue14));
                vh.ll_inside.setBackgroundColor(context.getResources().getColor(R.color.light_blue15));
                vh.ll_jifen_give.setVisibility(View.INVISIBLE);
                vh.tv_money_amount.setText("¥ 200.00");
                break;
            case 3:
                vh.tv_recommend_tag.setVisibility(View.GONE);
                vh.fl_bg_outside.setBackgroundColor(context.getResources().getColor(R.color.light_blue16));
                vh.ll_inside.setBackgroundColor(context.getResources().getColor(R.color.light_blue17));
                vh.ll_jifen_give.setVisibility(View.INVISIBLE);
                vh.tv_money_amount.setText("¥ 100.00");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tv_recommend_tag;
        private final FrameLayout fl_bg_outside;
        private final LinearLayout ll_inside;
        private final LinearLayout ll_jifen_give;
        private final TextView tv_jifen_give;
        private final TextView tv_money_amount;
        private final RelativeLayout rl_base;
        private RecyclerViewOnItemClickListener itemClickListener;

        public MyViewHolder(View itemView, RecyclerViewOnItemClickListener itemClickListener) {
            super(itemView);
            rl_base = ((RelativeLayout) itemView.findViewById(R.id.rl_base));//基础
            tv_recommend_tag = ((TextView) itemView.findViewById(R.id.tv_recommend_tag));//推荐标签
            fl_bg_outside = ((FrameLayout) itemView.findViewById(R.id.fl_bg_outside));//外面的矩形背景
            ll_inside = ((LinearLayout) itemView.findViewById(R.id.ll_inside));//里面的矩形背景
            ll_jifen_give = ((LinearLayout) itemView.findViewById(R.id.ll_jifen_give));//整个赠送积分布局
            tv_jifen_give = ((TextView) itemView.findViewById(R.id.tv_jifen_give));//赠送积分说明的textview
            tv_money_amount = ((TextView) itemView.findViewById(R.id.tv_money_amount));//充值金额

            this.itemClickListener = itemClickListener;
            rl_base.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_base:
                    if (itemClickListener != null) {
                        itemClickListener.OnItemClick(rl_base, getPosition());
                    }
                    break;
            }
        }
    }
}
