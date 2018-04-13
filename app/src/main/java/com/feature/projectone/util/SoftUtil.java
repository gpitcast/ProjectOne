package com.feature.projectone.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Administrator on 2018/4/11.
 * 键盘操作工具类
 */

public class SoftUtil {

    private static InputMethodManager manager;

    //隐藏键盘
    public static void hideSoft(Activity activity) {
        if (activity != null) {
            if (manager == null) {
                manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            }
            manager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    //显示键盘
    public static void showSoft(Activity activity) {
        if (manager == null) {
            manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        manager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
