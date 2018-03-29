package com.feature.projectone.other;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.feature.projectone.R;
import com.feature.projectone.util.CommonUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/3/21.
 */

public class NewsOneDelagate implements ItemViewDelegate<String> {

    private CommonAdapter<String> photoAdapter;

    private Context context;
    private ArrayList<String> photoList;

    public NewsOneDelagate( Context context, ArrayList<String> photoList) {
        this.context = context;
        this.photoList = photoList;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_newsone;
    }

    @Override
    public boolean isForViewType(String item, int position) {

        if(position%4==0){
            return  true;
        }
        return false;
    }

    @Override
    public void convert(ViewHolder holder, String s, int position) {
        holder.setText(R.id.tvTitle, "对中国式教育顽疾不能……");
        holder.setText(R.id.tvNewsType, "教育资讯");
        holder.setText(R.id.tvTitle, "10:18");
        holder.setText(R.id.tvCommentNum, "100");

        photoAdapter=new CommonAdapter<String>(context,R.layout.adapter_photo,photoList) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                int width=(CommonUtil.getScreenWidth(context)-CommonUtil.dip2px(context,45))/3;
                layoutParams.width= width;
                layoutParams.height=width*139/220;
                holder.getView(R.id.imgIcon).setLayoutParams(layoutParams);
            }
        };


        ((RecyclerView)holder.getView(R.id.recyclerPhoto)).setLayoutManager(new GridLayoutManager(context,3));
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL);
        dividerItemDecoration.setDrawable(context.getResources().getDrawable(R.drawable.divider_white_horizontal_10));
//        ((RecyclerView)holder.getView(R.id.recyclerPhoto)).addItemDecoration(dividerItemDecoration);
        ((RecyclerView)holder.getView(R.id.recyclerPhoto)).setAdapter(photoAdapter);
    }
}
