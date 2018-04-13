package com.feature.projectone.inter;

import android.view.View;

/**
 * Created by Administrator on 2018/4/9.
 * 给recyclerView添加item点击监听的接口
 */

public interface RecyclerViewOnItemClickListener {
    void OnItemClick(View view, int position);
}
