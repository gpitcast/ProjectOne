package com.feature.projectone.util;

import android.app.Activity;
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
    public static void show(final Activity context, final String text, final int type) {
        if (toast == null) {
            if ("main".equals(Thread.currentThread().getName())) {
                toast = Toast.makeText(context, text, type);
                toast.show();
            } else {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toast = Toast.makeText(context, text, type);
                        toast.show();
                    }
                });
            }
        } else {
            if ("main".equals(Thread.currentThread().getName())) {
                toast.setText(text);
                toast.setDuration(type);
                toast.show();
            } else {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toast.setText(text);
                        toast.setDuration(type);
                        toast.show();
                    }
                });
            }
        }
    }

    public static void show(final Context context, final String text, final int type) {

        if (toast == null) {
            toast = Toast.makeText(context, text, type);
            toast.show();
        } else {
            toast.setText(text);
            toast.setDuration(type);
            toast.show();
        }
    }
}
