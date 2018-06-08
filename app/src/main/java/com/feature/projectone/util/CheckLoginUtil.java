package com.feature.projectone.util;

import android.content.Context;

import com.feature.projectone.other.Constanst;

/**
 * Created by Administrator on 2018/4/21.
 * 该工具类用来检验用户是否登录
 */

public class CheckLoginUtil {

    public static boolean isLogin(Context context) {
        return ShareUtil.isExist(context, Constanst.UER_TOKEN);
    }
}
