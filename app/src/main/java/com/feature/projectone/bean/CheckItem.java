package com.feature.projectone.bean;

/**
 * Created by Administrator on 2018/4/24.
 *  记录checkbox状态的bean
 */

public class CheckItem {
    private boolean isSelect;//是否被选择，这个是解决item错乱的因素

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isSelect() {
        return isSelect;
    }
}
