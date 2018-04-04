package com.feature.projectone.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/4/2.
 * 吐司工具类
 */

public class ToastUtil {

    private static Toast toast;

    /**
     * 上下文必须要传application，否则内存泄漏
     *
     * @param context
     * @param text
     * @param type
     */
    public static void show(Context context, String text, int type) {
        if (toast == null) {
            toast = Toast.makeText(context, text, type);
        } else {
            toast.setText(text);
            toast.setDuration(type);
        }
        toast.show();
    }
}
