package com.feature.projectone.util;

import android.graphics.drawable.Drawable;
import android.widget.EditText;

/**
 * Created by Administrator on 2018/3/30.
 */

public class EtDrawableLeftUtil {

    /**
     * 设置editText左边图片的宽高
     *
     * @param et 要设置的editText
     */
    public static void setEtImgSize(EditText et) {
        Drawable leftDrawable = et.getCompoundDrawables()[0];
        if (leftDrawable != null) {
            leftDrawable.setBounds(0, 0, 40, 42);
            et.setCompoundDrawables(leftDrawable, et.getCompoundDrawables()[1], et.getCompoundDrawables()[2], et.getCompoundDrawables()[3]);
        }
    }
}
