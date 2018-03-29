package com.feature.projectone.other;

import com.feature.projectone.R;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by Administrator on 2018/3/21.
 */

public class NewsFourDelagate implements ItemViewDelegate<String> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_newsfour;
    }

    @Override
    public boolean isForViewType(String item, int position) {
        if(position%4==3){
            return true;
        }
        return false;
    }
    @Override
    public void convert(ViewHolder holder, String s, int position) {
        holder.setText(R.id.tvTitle, "对中国式教育顽疾不能……");
        holder.setText(R.id.tvType, "学习资讯");
        holder.setText(R.id.tvTime, "昨天");
        holder.setText(R.id.tvCommentNum, "昨天");
    }
}
