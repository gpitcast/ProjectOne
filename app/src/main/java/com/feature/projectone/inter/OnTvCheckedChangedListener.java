package com.feature.projectone.inter;

/**
 * Created by Administrator on 2018/4/3.
 * 给自定义的筛选TextView定义的接口，便于监听它的选中状态
 */

public interface OnTvCheckedChangedListener {
    void onCheckedChange(boolean isChecked);
}
