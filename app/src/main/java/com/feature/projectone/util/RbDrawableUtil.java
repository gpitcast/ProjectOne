package com.feature.projectone.util;

import android.graphics.drawable.Drawable;
import android.widget.RadioButton;

/**
 * Created by Administrator on 2018/4/13.
 * 处理radiobutton图片大小的工具类
 */

public class RbDrawableUtil {
    public static void setEbImgSize(RadioButton rb) {
        Drawable topDrawable = rb.getCompoundDrawables()[1];
        if (topDrawable != null) {
            topDrawable.setBounds(0, 0, 40, 42);
            rb.setCompoundDrawables(null, topDrawable, null, null);
        }
    }
}
