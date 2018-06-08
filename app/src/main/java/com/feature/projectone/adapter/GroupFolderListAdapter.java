package com.feature.projectone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.inter.RecyclerViewOnItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/5/25.
 * 群文件夹列表适配器
 */

public class GroupFolderListAdapter extends RecyclerView.Adapter {
    private Context context;
    private RecyclerViewOnItemClickListener listener;
    private List<HashMap<String, Object>> dataList;

    public GroupFolderListAdapter(Context context, List<HashMap<String, Object>> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public void setOnItemClickListener(RecyclerViewOnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_group_folder_list, null), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        HashMap<String, Object> map = dataList.get(position);
        vh.tv_name.setText(map.get("title") + "");
        vh.tv_time.setText(map.get("addtime") + "");
        vh.tv_size.setText(map.get("size") + "");

        //类型图片
        String extension = map.get("extension") + "";
        switch (extension) {
            case "zip":
                Picasso.with(context).load(R.mipmap.img_type_zip).into(vh.iv_file_type);
                Picasso.with(context).load(R.mipmap.icon_download_pause).into(vh.iv_download_status);
                break;
            case "ppt":
                Picasso.with(context).load(R.mipmap.img_type_ppt).into(vh.iv_file_type);
                Picasso.with(context).load(R.mipmap.icon_download_continue).into(vh.iv_download_status);
                break;
            case "pptx":
                Picasso.with(context).load(R.mipmap.img_type_ppt).into(vh.iv_file_type);
                Picasso.with(context).load(R.mipmap.icon_download_continue).into(vh.iv_download_status);
                break;
            case "xls":
                Picasso.with(context).load(R.mipmap.img_type_excel).into(vh.iv_file_type);
                Picasso.with(context).load(R.mipmap.icon_download_finish).into(vh.iv_download_status);
                break;
            case "map3":
                Picasso.with(context).load(R.mipmap.img_type_music).into(vh.iv_file_type);
                break;
            case "pdf":
                Picasso.with(context).load(R.mipmap.img_type_pdf).into(vh.iv_file_type);
                break;
            case "png":
                Picasso.with(context).load(R.mipmap.img_type_png).into(vh.iv_file_type);
                break;
            case "jpg":
                Picasso.with(context).load(R.mipmap.img_type_png).into(vh.iv_file_type);
                break;
            case "mp4":
                Picasso.with(context).load(R.mipmap.img_type_video).into(vh.iv_file_type);
                break;
            case "doc":
                Picasso.with(context).load(R.mipmap.img_type_word).into(vh.iv_file_type);
                break;
            case "txt":
                Picasso.with(context).load(R.mipmap.img_type_word).into(vh.iv_file_type);
                break;
            default:
                Picasso.with(context).load(R.mipmap.img_type_zip).into(vh.iv_file_type);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final LinearLayout ll_base;
        private final TextView tv_name;
        private final TextView tv_time;
        private final TextView tv_size;
        private RecyclerViewOnItemClickListener listener;
        private final ImageView iv_file_type;
        private final ImageView iv_download_status;

        public MyViewHolder(View itemView, RecyclerViewOnItemClickListener listener) {
            super(itemView);
            ll_base = ((LinearLayout) itemView.findViewById(R.id.ll_base));//根布局
            iv_file_type = (ImageView) itemView.findViewById(R.id.iv_file_type);//文件类型
            iv_download_status = (ImageView) itemView.findViewById(R.id.iv_download_status);//文件下载状态
            tv_name = ((TextView) itemView.findViewById(R.id.tv_name));//文件名称
            tv_time = ((TextView) itemView.findViewById(R.id.tv_time));//名称
            tv_size = ((TextView) itemView.findViewById(R.id.tv_size));//大小

            this.listener = listener;
            ll_base.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ll_base:
                    if (listener != null) {
                        listener.OnItemClick(ll_base, getPosition() - 3);
                    }
                    break;
            }
        }
    }
}
