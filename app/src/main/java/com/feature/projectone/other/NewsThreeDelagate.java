package com.feature.projectone.other;

import android.content.Context;
import android.widget.LinearLayout;

import com.feature.projectone.R;
import com.feature.projectone.util.CommonUtil;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by Administrator on 2018/3/21.
 */

public class NewsThreeDelagate implements ItemViewDelegate<String> {
    private Context context;

    public NewsThreeDelagate(Context context) {
        this.context = context;
    }


    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_newsthree;
    }

    @Override
    public boolean isForViewType(String item, int position)

    {
        if(position%4==2){
            return true;
        }
        return false;
    }

    @Override
    public void convert(ViewHolder holder, String s, int position) {
        holder.setText(R.id.tvTitle, "对中国式教育顽疾不能……");
        holder.setText(R.id.tvType, "教育资讯");
        holder.setText(R.id.tvTime, "10:18");
        holder.setText(R.id.tvCommentNum, "100");
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int width = (CommonUtil.getScreenWidth(context) - CommonUtil.dip2px(context, 45)) / 3;
        layoutParams.width = width;
        layoutParams.height = width * 139 / 220;
        holder.getView(R.id.imgIcon).setLayoutParams(layoutParams);
    }
}
