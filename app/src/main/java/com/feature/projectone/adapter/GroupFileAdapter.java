package com.feature.projectone.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feature.projectone.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/5/28.
 * 群组信息里面的群文件的适配器
 */

public class GroupFileAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<HashMap<String, Object>> mFileLists = new ArrayList<>();

    public GroupFileAdapter(Context context) {
        this.context = context;
    }

    public void refresh(List<HashMap<String, Object>> mFileLists) {
        this.mFileLists.clear();
        this.mFileLists.addAll(mFileLists);
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_group_file, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        HashMap<String, Object> map = mFileLists.get(position);

        //类型图片
        String extension = map.get("extension") + "";
        if (!TextUtils.isEmpty(extension)) {
            if (extension.equals("png") || extension.equals("jpg")) {
                vh.iv_file_type.setImageResource(R.mipmap.img_type_png);
            } else if (extension.equals("xls")) {
                vh.iv_file_type.setImageResource(R.mipmap.img_type_excel);
            } else if (extension.equals("mp3")) {
                vh.iv_file_type.setImageResource(R.mipmap.img_type_music);
            } else if (extension.equals("pdf")) {
                vh.iv_file_type.setImageResource(R.mipmap.img_type_pdf);
            } else if (extension.equals("ppt")) {
                vh.iv_file_type.setImageResource(R.mipmap.img_type_ppt);
            } else if (extension.equals("pptx")) {
                vh.iv_file_type.setImageResource(R.mipmap.img_type_ppt);
            } else if (extension.equals("mp4")) {
                vh.iv_file_type.setImageResource(R.mipmap.img_type_video);
            } else if (extension.equals("doc")) {
                vh.iv_file_type.setImageResource(R.mipmap.img_type_word);
            } else if (extension.equals("zip")) {
                vh.iv_file_type.setImageResource(R.mipmap.img_type_zip);
            } else if (extension.equals("txt")) {
                vh.iv_file_type.setImageResource(R.mipmap.img_type_word);
            } else {
                vh.iv_file_type.setImageResource(R.mipmap.img_type_word);
            }
        }

        vh.tv_title.setText(map.get("title") + "");
        vh.tv_size.setText(map.get("size") + "");
        vh.tv_time.setText(map.get("addtime") + "");
    }

    @Override
    public int getItemCount() {
        return mFileLists == null ? 0 : mFileLists.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView iv_file_type;
        private final TextView tv_title;
        private final TextView tv_size;
        private final TextView tv_time;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_file_type = ((ImageView) itemView.findViewById(R.id.iv_file_type));
            tv_title = ((TextView) itemView.findViewById(R.id.tv_title));
            tv_size = ((TextView) itemView.findViewById(R.id.tv_size));
            tv_time = ((TextView) itemView.findViewById(R.id.tv_time));
        }
    }
}
