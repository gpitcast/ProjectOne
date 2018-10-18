package com.youyi.ywl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youyi.ywl.R;
import com.youyi.ywl.inter.RecyclerViewOnItemClickListener;
import com.youyi.ywl.util.GlideUtil;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/7/23.
 * <p>
 * 精品课程-详情  课程介绍fragment 里的
 */

public class LecturerIntroduceAdapter extends RecyclerView.Adapter {
    private Context context;
    private RecyclerViewOnItemClickListener itemClickListener;
    private List<HashMap<String, Object>> mTeacherList;

    public LecturerIntroduceAdapter(Context context, List<HashMap<String, Object>> mTeacherList) {
        this.context = context;
        this.mTeacherList = mTeacherList;
    }

    public void setOnItemClickListener(RecyclerViewOnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_lecturer_introduce, null), itemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        HashMap<String, Object> map = mTeacherList.get(position);
        GlideUtil.loadNetImageView(context,map.get("img") + "",vh.circleImageView);
        vh.tv_name.setText(map.get("name") + "");
    }

    @Override
    public int getItemCount() {
        return mTeacherList == null ? 0 : mTeacherList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final LinearLayout ll_base;
        private final CircleImageView circleImageView;
        private final TextView tv_name;
        private RecyclerViewOnItemClickListener itemClickListener;

        public MyViewHolder(View itemView, RecyclerViewOnItemClickListener itemClickListener) {
            super(itemView);
            circleImageView = ((CircleImageView) itemView.findViewById(R.id.circleImageView));
            tv_name = ((TextView) itemView.findViewById(R.id.tv_name));
            ll_base = ((LinearLayout) itemView.findViewById(R.id.ll_base));

            this.itemClickListener = itemClickListener;
            ll_base.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_base:
                    if (itemClickListener != null) {
                        itemClickListener.OnItemClick(ll_base, getPosition());
                    }
                    break;
            }
        }
    }
}
