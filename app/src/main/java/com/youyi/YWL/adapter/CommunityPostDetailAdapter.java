package com.youyi.YWL.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.youyi.YWL.R;
import com.youyi.YWL.activity.CommunityPostDetailActivity;
import com.youyi.YWL.view.CircleImageView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/6/27.
 * 社区帖子详情界面的评论recyclerview的适配器
 */

public class CommunityPostDetailAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<HashMap<String, Object>> replyDataList;

    public CommunityPostDetailAdapter(Context context, List<HashMap<String, Object>> replyDataList) {
        this.context = context;
        this.replyDataList = replyDataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_community_post_detail, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        HashMap<String, Object> map = replyDataList.get(position);
        Picasso.with(context).load(map.get("u_img") + "").placeholder(R.mipmap.img_loading).error(R.mipmap.img_load_error).into(vh.circleImageView);
        vh.tv_name.setText(map.get("nickname") + "");
        vh.tv_content.setText(map.get("content") + "");
        vh.tv_time.setText(map.get("atime") + "");
    }

    @Override
    public int getItemCount() {
        return replyDataList == null ? 0 : replyDataList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView circleImageView;
        private final TextView tv_name;
        private final TextView tv_content;
        private final TextView tv_time;

        public MyViewHolder(View itemView) {
            super(itemView);
            circleImageView = ((CircleImageView) itemView.findViewById(R.id.circleImageView));
            tv_name = ((TextView) itemView.findViewById(R.id.tv_name));
            tv_content = ((TextView) itemView.findViewById(R.id.tv_content));
            tv_time = ((TextView) itemView.findViewById(R.id.tv_time));
        }
    }
}
