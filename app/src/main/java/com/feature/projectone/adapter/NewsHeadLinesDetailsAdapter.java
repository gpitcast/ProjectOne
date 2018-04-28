package com.feature.projectone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.inter.ReplyIconClickListener;
import com.feature.projectone.view.CircleImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/3/30.
 * 资讯头条详情适配器
 */

public class NewsHeadLinesDetailsAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<HashMap<String, Object>> mCommentsDataList;
    private ReplyIconClickListener listener;

    public NewsHeadLinesDetailsAdapter(Context context, List<HashMap<String, Object>> mCommentsDataList) {
        this.context = context;
        this.mCommentsDataList = mCommentsDataList;
    }

    public void setOnReplyIconClickListener(ReplyIconClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_news_headlines_details, null), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        HashMap<String, Object> map = mCommentsDataList.get(position);
        HashMap<String, Object> userinfo = (HashMap<String, Object>) map.get("userinfo");
        if (userinfo != null && userinfo.size() != 0) {
            Picasso.with(context).load(userinfo.get("img_url") + "")
                    .placeholder(R.mipmap.img_loading_default).error(R.mipmap.img_load_error).into(vh.circleImageView);//头像
            vh.tv_name.setText(userinfo.get("username") + "");//用户名称
        }
        vh.tv_content.setText(map.get("content") + "");//评论内容
        vh.tv_time.setText(map.get("atime") + "");//评论时间
        vh.tv_reply_count.setText(map.get("reply_nums") + "");//回复本条回复的数量
    }

    @Override
    public int getItemCount() {
        return mCommentsDataList == null ? 0 : mCommentsDataList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final CircleImageView circleImageView;
        private final TextView tv_name;
        private final TextView tv_content;
        private final TextView tv_time;
        private final TextView tv_reply_count;
        private final LinearLayout ll_reply;
        private ReplyIconClickListener listener;

        public MyViewHolder(View itemView, ReplyIconClickListener listener) {
            super(itemView);
            circleImageView = ((CircleImageView) itemView.findViewById(R.id.circleImageView));//头像
            tv_name = ((TextView) itemView.findViewById(R.id.tv_name));//用户名称
            tv_content = ((TextView) itemView.findViewById(R.id.tv_content));//评论内容
            tv_time = ((TextView) itemView.findViewById(R.id.tv_time));//评论时间
            tv_reply_count = ((TextView) itemView.findViewById(R.id.tv_reply_count));//评论的数量
            ll_reply = ((LinearLayout) itemView.findViewById(R.id.ll_reply));

            this.listener = listener;
            ll_reply.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ll_reply:
                    if (listener != null) {
                        listener.OnReplyIconClick(ll_reply, getPosition() - 3);
                    }
                    break;
            }
        }
    }
}
