package com.feature.projectone.util;

import android.view.View;

/**
 * Created by Administrator on 2018/4/12.
 * 控件的测量工具类
 */

public class ViewMeasureUtil {

    public static int getViewWidth(View view) {
        int resultWidth;
        if (view == null) {
            return 0;
        }

        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(h, 0);
        resultWidth = view.getMeasuredHeight();
        return resultWidth;
    }


    public static int getViewHeight(View view) {
        int resultHeight;
        if (view == null) {
            return 0;
        }

        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(h, 0);
        resultHeight = view.getMeasuredHeight();
        return resultHeight;
    }
}
