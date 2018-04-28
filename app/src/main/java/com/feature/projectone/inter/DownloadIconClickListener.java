package com.feature.projectone.inter;

import android.view.View;

import com.feature.projectone.adapter.SourceDownloadFragmentAdapter;

/**
 * Created by Administrator on 2018/4/16.
 * 资源列表下载图标点击事件接口
 */

public interface DownloadIconClickListener {
    void OnDownLoadClick(SourceDownloadFragmentAdapter.MyViewHolder holder, View view, int position);
}
