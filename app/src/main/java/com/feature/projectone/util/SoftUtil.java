package com.feature.projectone.util;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

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
        manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    //判断点击屏幕的地方是否是软键盘（有BUG）
    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && v instanceof EditText) {
            int[] leftTop = {0, 0};
            v.getLocationInWindow(leftTop);
            int left = leftTop[0], top = leftTop[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
