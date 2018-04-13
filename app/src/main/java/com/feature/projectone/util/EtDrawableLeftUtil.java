package com.feature.projectone.util;

import android.graphics.drawable.Drawable;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

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
            leftDrawable.setBounds(0, 0, 45, 42);
            et.setCompoundDrawables(leftDrawable, et.getCompoundDrawables()[1], et.getCompoundDrawables()[2], et.getCompoundDrawables()[3]);
        }
    }

    /**
     * 设置TextView左边图片的宽高
     *
     * @param tv 要设置的editText
     */
    public static void setTvImgSize(TextView tv) {
        Drawable leftDrawable = tv.getCompoundDrawables()[0];
        if (leftDrawable != null) {
            leftDrawable.setBounds(0, 0, 30, 32);
            tv.setCompoundDrawables(leftDrawable, tv.getCompoundDrawables()[1], tv.getCompoundDrawables()[2], tv.getCompoundDrawables()[3]);
        }
    }
}
