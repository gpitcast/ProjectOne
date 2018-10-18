package com.youyi.ywl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youyi.ywl.R;
import com.youyi.ywl.inter.RecyclerViewOnItemClickListener;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/24.
 * 习题讲解首页 - 标题栏的适配器
 */

public class ExampleExplainMainTitleAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Map<String, Object>> titleList;
    private RecyclerViewOnItemClickListener itemClickListener;

    public ExampleExplainMainTitleAdapter(Context context, List<Map<String, Object>> titleList) {
        this.context = context;
        this.titleList = titleList;
    }

    public void setOnItemClickListener(RecyclerViewOnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_example_explain_main_title, null), itemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        Map<String, Object> map = titleList.get(position);
        vh.tv_name.setText(map.get("name") + "");

        boolean isSelected = (boolean) map.get("isSelected");
        if (isSelected) {
            vh.tv_name.setTextColor(context.getResources().getColor(R.color.orangeone));
            vh.line_down.setVisibility(View.VISIBLE);
        } else {
            vh.tv_name.setTextColor(context.getResources().getColor(R.color.normal_black));
            vh.line_down.setVisibility(View.INVISIBLE);
        }

        //设置布局的整体宽度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);//赋值给dm
        int width = dm.widthPixels;//屏幕宽度(像素)
        float density = dm.density;//屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;//屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);//屏幕宽度(dp)
        vh.rl_base.setLayoutParams(new RelativeLayout.LayoutParams(width / 3, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public int getItemCount() {
        return titleList == null ? 0 : titleList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tv_name;
        private final View line_down;
        private final RelativeLayout rl_base;
        private RecyclerViewOnItemClickListener itemClickListener;

        public MyViewHolder(View itemView, RecyclerViewOnItemClickListener itemClickListener) {
            super(itemView);
            tv_name = ((TextView) itemView.findViewById(R.id.tv_name));
            line_down = itemView.findViewById(R.id.line_down);
            rl_base = ((RelativeLayout) itemView.findViewById(R.id.ll_base));

            this.itemClickListener = itemClickListener;
            tv_name.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_name:
                    if (itemClickListener != null) {
                        itemClickListener.OnItemClick(tv_name, getPosition());
                    }
                    break;
            }
        }
    }
}
