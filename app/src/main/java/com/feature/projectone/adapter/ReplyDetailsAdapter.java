package com.feature.projectone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.activity.ReplyDetailsActivity;
import com.feature.projectone.inter.ReplyIconClickListener;
import com.feature.projectone.view.CircleImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/4/23.
 * 评论详情界面适配器
 */

public class ReplyDetailsAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<HashMap<String, Object>> mDataList;
    private ReplyIconClickListener listener;

    public ReplyDetailsAdapter(Context context, List<HashMap<String, Object>> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    public void setOnReplyClickListener(ReplyIconClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_news_headlines_details, null), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        vh.ll_reply.setVisibility(View.GONE);
        vh.tv_reply.setVisibility(View.VISIBLE);
        HashMap<String, Object> map = mDataList.get(position);
        vh.tv_time.setText(map.get("atime") + "");

        //本条评论的用户信息
        HashMap<String, Object> c_user_info = (HashMap<String, Object>) map.get("c_user_info");
        String img_url = c_user_info.get("img_url") + "";
        Picasso.with(context).load(img_url).placeholder(R.mipmap.img_loading).error(R.mipmap.img_load_error).into(vh.circleImageView);
        vh.tv_name.setText(c_user_info.get("username") + "");

        //本条评论的回复用户信息
        String content = map.get("content") + "";
        Object c_content = map.get("c_content");
        if (c_content == null) {
            //没有c_content就不做处理直接设置内容
            vh.tv_content.setText(content + "");
        } else {
            //有c_content代表有@的评论，继续取出数据
            HashMap<String, Object> r_user_info = (HashMap<String, Object>) map.get("r_user_info");
            String r_username = r_user_info.get("username") + "";
            SpannableStringBuilder builder = new SpannableStringBuilder(content + "//@" + r_username + ":" + c_content);
            builder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.orangeone)), (content.length() + 2), (content.length() + 3 + r_username.length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            vh.tv_content.setText(builder);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final LinearLayout ll_reply;
        private final CircleImageView circleImageView;
        private final TextView tv_name;
        private final TextView tv_content;
        private final TextView tv_time;
        private final TextView tv_reply;
        private ReplyIconClickListener listener;

        public MyViewHolder(View itemView, ReplyIconClickListener listener) {
            super(itemView);
            ll_reply = ((LinearLayout) itemView.findViewById(R.id.ll_reply));//带图标的回复布局
            circleImageView = ((CircleImageView) itemView.findViewById(R.id.circleImageView));//头像
            tv_name = ((TextView) itemView.findViewById(R.id.tv_name));//名称
            tv_content = ((TextView) itemView.findViewById(R.id.tv_content));//内容
            tv_time = ((TextView) itemView.findViewById(R.id.tv_time));//时间
            tv_reply = ((TextView) itemView.findViewById(R.id.tv_reply));//回复字体按钮

            this.listener = listener;
            tv_reply.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_reply:
                    if (listener != null) {
                        listener.OnReplyIconClick(tv_reply, getPosition() - 3);
                    }
                    break;
            }
        }
    }
}
