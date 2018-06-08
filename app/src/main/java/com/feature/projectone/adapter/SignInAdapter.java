package com.feature.projectone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.activity.SignInActivity;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2018/5/24.
 * 我要签到界面适配器
 */

public class SignInAdapter extends RecyclerView.Adapter {
    private Context context;

    public SignInAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_sign_in, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        switch (position) {
            case 0:
                vh.iv_ranking.setVisibility(View.VISIBLE);
                vh.tv_ranking.setVisibility(View.GONE);
                Picasso.with(context).load(R.mipmap.icon_first_sign_in).into(vh.iv_ranking);
                break;
            case 1:
                vh.iv_ranking.setVisibility(View.VISIBLE);
                vh.tv_ranking.setVisibility(View.GONE);
                Picasso.with(context).load(R.mipmap.icon_second_sign_in).into(vh.iv_ranking);
                break;
            case 2:
                vh.iv_ranking.setVisibility(View.VISIBLE);
                vh.tv_ranking.setVisibility(View.GONE);
                Picasso.with(context).load(R.mipmap.icon_three_sign_in).into(vh.iv_ranking);
                break;
            default:
                vh.iv_ranking.setVisibility(View.GONE);
                vh.tv_ranking.setVisibility(View.VISIBLE);
                vh.tv_ranking.setText(position + 1 + "");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView iv_ranking;
        private final TextView tv_ranking;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_ranking = ((ImageView) itemView.findViewById(R.id.iv_ranking));
            tv_ranking = ((TextView) itemView.findViewById(R.id.tv_ranking));
        }
    }
}
