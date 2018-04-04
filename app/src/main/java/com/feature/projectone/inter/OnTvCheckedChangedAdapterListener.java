package com.feature.projectone.inter;

/**
 * Created by Administrator on 2018/4/3.
 * 写这个接口是为了将gridview的check时间从adapter里面传到外面，将业务逻辑层分开，条理更清晰
 */

public interface OnTvCheckedChangedAdapterListener {
    void onCheckedChanged(boolean isChecked, int position);
}
