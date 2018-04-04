package com.feature.projectone.util;

import android.graphics.Paint;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/3/30.
 */

public class TvUtil {

    /**
     * 给TextView添加下划线并抗锯齿
     *
     * @param tv
     */
    public static void addUnderLine(TextView tv) {

        tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);//添加下划线和抗锯齿
//        tv.getPaint().setAntiAlias(true);//抗锯齿  此方法无效，以示后人
    }
}
