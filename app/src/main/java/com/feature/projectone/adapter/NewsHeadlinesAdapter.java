package com.feature.projectone.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.inter.RecyclerViewOnItemClickListener;
import com.feature.projectone.view.MyGridView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/3/21.
 * 教育头条，热点推荐，教育咨询，学习咨询 等公用的适配器
 */

public class NewsHeadlinesAdapter extends RecyclerView.Adapter {
    private Context context;
    private RecyclerViewOnItemClickListener listener;
    private List<HashMap<String, Object>> mDataList;
    private NewsHeadlinesGridViewAdapter newsHeadlinesGridViewAdapter;
    private boolean isSearch;
    private String searchKey;

    public NewsHeadlinesAdapter(Context context, List<HashMap<String, Object>> mDataList, String searchKey, boolean isSearch) {
        this.context = context;
        this.mDataList = mDataList;
        this.isSearch = isSearch;
        this.searchKey = searchKey;
    }

    public void setOnItemClickListener(RecyclerViewOnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_news_headlines, null), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        HashMap<String, Object> map = mDataList.get(position);

        String att = map.get("att") + "";//新闻类型(1:置顶,2:热点,3:普通)
        switch (att) {
            case "1":
                vh.ll_setTop.setVisibility(View.VISIBLE);//显示置顶图标
                vh.ll_hot.setVisibility(View.GONE);
                break;
            case "2":
                vh.ll_hot.setVisibility(View.VISIBLE);//显示热点图标
                vh.ll_setTop.setVisibility(View.GONE);
                break;
            case "3":
                vh.ll_hot.setVisibility(View.GONE);
                vh.ll_setTop.setVisibility(View.GONE);
                //什么都不用干
                break;
        }

        vh.tv_style.setText(map.get("cate_name") + "");//类型
        vh.tv_time.setText(map.get("atime") + "");//时间
        vh.tv_reply_count.setText(map.get("comments") + "");//评论数
        String title = map.get("title") + "";

        String type = map.get("type") + "";//新闻格式(1:普通,2:视频)
        switch (type) {
            case "1":
                //普通(继续判断图片的数量)
                ArrayList<String> img_lists = (ArrayList<String>) map.get("img_lists");
                if (img_lists == null || img_lists.size() == 0) {
                    //没有图片，只显示横向标题
                    vh.ll_two.setVisibility(View.VISIBLE);
                    vh.ll_one.setVisibility(View.GONE);
                    vh.gridView.setVisibility(View.GONE);
                    Log.i("SpannableStringBuilder", "     isSearch1     " + isSearch);
                    if (isSearch) {
                        //搜索适配器。改变字体颜色
                        SpannableStringBuilder builder = setBuilder(title, searchKey);
                        vh.tv_title_two.setText(builder);
                    } else {
                        vh.tv_title_two.setText(title);
                    }

                } else if (img_lists.size() == 1) {
                    //只有一张图片，显示标题在左，图片在右的布局
                    vh.ll_one.setVisibility(View.VISIBLE);
                    vh.gridView.setVisibility(View.GONE);
                    vh.ll_two.setVisibility(View.GONE);
                    Log.i("SpannableStringBuilder", "     isSearch2     " + isSearch);
                    if (isSearch) {
                        //搜索适配器。改变字体颜色
                        SpannableStringBuilder builder = setBuilder(title, searchKey);
                        vh.tv_title_one.setText(builder);
                    } else {
                        vh.tv_title_one.setText(title);
                    }
                    Picasso.with(context).load(img_lists.get(0)).placeholder(R.mipmap.img_loading).error(R.mipmap.img_load_error).into(vh.iv_one);
                } else {
                    //不止一张图片,显示横向标题和gridview
                    vh.ll_two.setVisibility(View.VISIBLE);
                    vh.gridView.setVisibility(View.VISIBLE);
                    vh.ll_one.setVisibility(View.GONE);
                    Log.i("SpannableStringBuilder", "     isSearch3     " + isSearch);
                    if (isSearch) {
                        //搜索适配器。改变字体颜色
                        SpannableStringBuilder builder = setBuilder(title, searchKey);
                        vh.tv_title_two.setText(builder);
                    } else {
                        vh.tv_title_two.setText(title);
                    }
                    vh.gridView.setFocusable(false);
                    newsHeadlinesGridViewAdapter = new NewsHeadlinesGridViewAdapter(context, img_lists);
                    vh.gridView.setAdapter(newsHeadlinesGridViewAdapter);
                }
                break;
            case "2":
                //视频(显示title和视频布局)
                vh.ll_two.setVisibility(View.VISIBLE);
                vh.ll_video.setVisibility(View.VISIBLE);
                Log.i("SpannableStringBuilder", "     isSearch4     " + isSearch);
                if (isSearch) {
                    //搜索适配器。改变字体颜色
                    SpannableStringBuilder builder = setBuilder(title, searchKey);
                    vh.tv_title_two.setText(builder);
                } else {
                    vh.tv_title_two.setText(title);
                }
                Picasso.with(context).load(map.get("img") + "").placeholder(R.mipmap.img_loading).error(R.mipmap.img_load_error).into(vh.iv_vedio);
                break;
        }
    }

    public SpannableStringBuilder setBuilder(String str, String key) {
        if (str != null && key != null) {
            SpannableStringBuilder builder = new SpannableStringBuilder(str);
            int startIndex = str.indexOf(key);//*第一个出现的索引位置
            Log.i("SpannableStringBuilder", "     startIndex     " + startIndex);
            while (startIndex != -1) {
                builder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.orangeone1)),
                        startIndex, startIndex + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                startIndex = str.indexOf(key, startIndex + key.length());//*从这个索引往后开始第一个出现的位置
            }
            return builder;
        } else {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, AdapterView.OnItemClickListener {

        private final MyGridView gridView;
        private final LinearLayout ll_one;
        private final LinearLayout ll_two;
        private final LinearLayout ll_video;
        private final LinearLayout ll_setTop;
        private final LinearLayout ll_hot;
        private final LinearLayout ll_base;
        private final TextView tv_title_one;
        private final ImageView iv_one;
        private final TextView tv_title_two;
        private final ImageView iv_vedio;
        private final TextView tv_style;
        private final TextView tv_time;
        private final TextView tv_reply_count;
        private RecyclerViewOnItemClickListener listener;

        public MyViewHolder(View itemView, RecyclerViewOnItemClickListener listener) {
            super(itemView);
            ll_base = ((LinearLayout) itemView.findViewById(R.id.ll_base));//根布局
            gridView = ((MyGridView) itemView.findViewById(R.id.gridView));//显示多张图片的gridview
            ll_base.setOnClickListener(this);
            gridView.setOnItemClickListener(this);
            this.listener = listener;
            ll_one = ((LinearLayout) itemView.findViewById(R.id.ll_one));//第一个上部分布局，左边文字 右边图片
            tv_title_one = ((TextView) itemView.findViewById(R.id.tv_title_one));//只有一张图片布局的title
            iv_one = ((ImageView) itemView.findViewById(R.id.iv_one));//只有一张图片布局的图片

            ll_two = ((LinearLayout) itemView.findViewById(R.id.ll_two));//第二个上部分布局，上面文字，下面vedio图片或者gridview
            tv_title_two = ((TextView) itemView.findViewById(R.id.tv_title_two));//第二个布局的title

            ll_video = ((LinearLayout) itemView.findViewById(R.id.ll_video));//显示vedio图片的布局
            iv_vedio = ((ImageView) itemView.findViewById(R.id.iv_vedio));//显示video图片的imageview

            ll_setTop = ((LinearLayout) itemView.findViewById(R.id.ll_setTop));//置顶的布局
            ll_hot = ((LinearLayout) itemView.findViewById(R.id.ll_hot));//热点的布局
            tv_style = ((TextView) itemView.findViewById(R.id.tv_style));//类型
            tv_time = ((TextView) itemView.findViewById(R.id.tv_time));//时间
            tv_reply_count = ((TextView) itemView.findViewById(R.id.tv_reply_count));//评论数
        }

        @Override
        public void onClick(View view) {
            if (listener != null) {
                listener.OnItemClick(view, getPosition() - 3);
            }
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (listener != null) {
                listener.OnItemClick(view, getPosition() - 3);
            }
        }
    }
}
