package com.feature.projectone.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feature.projectone.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/4/2.
 * 资源下载所有fragment 适配器
 */

public class SourceDownloadFragmentAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<HashMap<String, Object>> mDataList;

    public SourceDownloadFragmentAdapter(Context context, List<HashMap<String, Object>> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_source_download, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        HashMap<String, Object> map = mDataList.get(position);
        vh.tv_title.setText(map.get("title") + "");
        vh.tv_time.setText("更新时间： " + map.get("atime"));
        vh.tv_size.setText("大小： " + map.get("size"));
        switch (position) {
            case 0:
                Picasso.with(context).load(R.mipmap.img_downing_btn).into(vh.iv_download);
                vh.tv_loading_percent.setVisibility(View.VISIBLE);
                break;
            case 1:
                Picasso.with(context).load(R.mipmap.img_download_btn).into(vh.iv_download);
                break;
            case 2:
                Picasso.with(context).load(R.mipmap.img_downing_btn).into(vh.iv_download);
                vh.tv_loading_percent.setVisibility(View.VISIBLE);
                break;
            case 3:
                Picasso.with(context).load(R.mipmap.img_down_ok_btn).into(vh.iv_download);
                break;
            case 4:
                Picasso.with(context).load(R.mipmap.img_down_error_btn).into(vh.iv_download);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    private class MyViewHolder extends BaseViewHolder {

        private final ImageView iv_download;
        private final TextView tv_loading_percent;
        private final TextView tv_title;
        private final TextView tv_time;
        private final TextView tv_size;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_download = ((ImageView) itemView.findViewById(R.id.iv_download));//下载状态的图标
            tv_loading_percent = ((TextView) itemView.findViewById(R.id.tv_loading_percent));//下载的进度
            tv_title = ((TextView) itemView.findViewById(R.id.tv_title));//标题
            tv_time = ((TextView) itemView.findViewById(R.id.tv_time));//时间
            tv_size = ((TextView) itemView.findViewById(R.id.tv_size));//大小
        }
    }
}



