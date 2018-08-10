package com.youyi.YWL.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.youyi.YWL.R;
import com.youyi.YWL.activity.OnlineHearingShaixuanActivity;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/8/2.
 * 在线听力筛选界面的gridview适配器
 */

public class OnlineHearingShaixuanGridAdapter extends BaseAdapter {
    private Context context;
    private List<HashMap<String, Object>> firstList;
    private int selectPosition;//记录被选中的条目的position

    public OnlineHearingShaixuanGridAdapter(Context context, List<HashMap<String, Object>> firstList) {
        this.context = context;
        this.firstList = firstList;
    }

    @Override
    public int getCount() {
        return firstList == null ? 0 : firstList.size();
    }

    @Override
    public Object getItem(int position) {
        return firstList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //选中条目
    public void changeState(int position) {
        selectPosition = position;
        notifyDataSetChanged();
    }

    //默认不选中任何一个条目
    public void changeDefault() {
        selectPosition = -1;
        notifyDataSetChanged();
    }

    //设置textview为选中状态
    private void setTxtChecked(TextView textView) {
        textView.setTextColor(context.getResources().getColor(R.color.white));
        textView.setBackgroundResource(R.drawable.bg_blue_fillet_rectangle);
    }

    //设置textview为未选中状态
    private void setTxtUnCheched(TextView textView) {
        textView.setTextColor(context.getResources().getColor(R.color.normal_black3));
        textView.setBackgroundResource(R.drawable.bg_blue_line);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder = null;
        if (convertView == null) {
            holder = new MyViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_online_hearing_shaixuan_grid, null);
            holder.tv_name = ((TextView) convertView.findViewById(R.id.tv_name));
            convertView.setTag(holder);
        } else {
            holder = ((MyViewHolder) convertView.getTag());
        }

        HashMap<String, Object> map = firstList.get(position);
        holder.tv_name.setText(map.get("cate_name") + "");


        if (selectPosition != -1) {
            if (position == selectPosition) {
                //如果当前的position等于传过来点击的position,就去改变他的状态
                setTxtChecked(holder.tv_name);
            } else {
                //其他的回复未选中状态
                setTxtUnCheched(holder.tv_name);
            }
        } else {
            //等于-1的时候是回复默认状态
            setTxtUnCheched(holder.tv_name);
        }

        return convertView;
    }

    private class MyViewHolder {
        private TextView tv_name;
    }
}
