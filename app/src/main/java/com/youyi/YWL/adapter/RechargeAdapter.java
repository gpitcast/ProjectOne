package com.youyi.YWL.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youyi.YWL.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/7/12.
 * 未来币充值界面gridview的适配器
 */

public class RechargeAdapter extends BaseAdapter {
    private Context context;
    private List<HashMap<String, Object>> gridViewList;

    public RechargeAdapter(Context context, List<HashMap<String, Object>> gridViewList) {
        this.context = context;
        this.gridViewList = gridViewList;
    }

    @Override
    public int getCount() {
        return gridViewList == null ? 0 : gridViewList.size();
    }

    @Override
    public Object getItem(int position) {
        return gridViewList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder = null;
        if (convertView == null) {
            holder = new MyViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_recharge, null);
            holder.ll_base = ((LinearLayout) convertView.findViewById(R.id.ll_base));
            holder.ll_line = ((LinearLayout) convertView.findViewById(R.id.ll_line));
            holder.iv_money = ((ImageView) convertView.findViewById(R.id.iv_money));
            holder.tv_small_amount = ((TextView) convertView.findViewById(R.id.tv_small_amount));
            holder.tv_big_amount = ((TextView) convertView.findViewById(R.id.tv_big_amount));

            convertView.setTag(holder);
        } else {
            holder = ((MyViewHolder) convertView.getTag());
        }

        HashMap<String, Object> map = gridViewList.get(position);
        holder.tv_small_amount.setText(map.get("amount") + "未来币");
        holder.tv_big_amount.setText("¥ " + map.get("amount"));

        Boolean isChecked = (Boolean) map.get("isChecked");
        if (isChecked) {
            holder.iv_money.setImageResource(R.mipmap.icon_light_brown_future_money);
            holder.tv_small_amount.setTextColor(context.getResources().getColor(R.color.light_brown));
            holder.tv_big_amount.setTextColor(context.getResources().getColor(R.color.light_brown));
            holder.ll_base.setBackgroundColor(context.getResources().getColor(R.color.normal_brown));
            holder.ll_line.setBackgroundResource(R.drawable.bg_light_brown_rectangle_line);
        } else {
            holder.iv_money.setImageResource(R.mipmap.icon_dark_brown_future_money);
            holder.tv_small_amount.setTextColor(context.getResources().getColor(R.color.normal_brown));
            holder.tv_big_amount.setTextColor(context.getResources().getColor(R.color.normal_brown));
            holder.ll_base.setBackgroundColor(context.getResources().getColor(R.color.light_brown));
            holder.ll_line.setBackgroundResource(R.drawable.bg_normal_brown_rectangle_line);
        }

        return convertView;
    }

    private class MyViewHolder {
        private LinearLayout ll_base;
        private LinearLayout ll_line;
        private ImageView iv_money;
        private TextView tv_small_amount;
        private TextView tv_big_amount;
    }
}
