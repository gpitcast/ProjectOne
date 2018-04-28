package com.feature.projectone.inter;

/**
 * Created by Administrator on 2018/4/26.
 * 解压文件的dialog显示的接口
 */

public interface SolveDialogListener {
    void dialogListener(boolean showOrClose);

    void dialogResultListener(boolean isSuccess);
}
