package com.feature.projectone.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2018/4/8.
 * SharePrePreferences存储操作工具类
 */

public class ShareUtil {

    //获取sp
    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    //存储字符串类型
    public static void putString(Context context, String key, String value) {
        getSharedPreferences(context).edit().putString(key, value).commit();
    }

    //读取字符串类型
    public static String getString(Context context, String key) {
        return getSharedPreferences(context).getString(key, "");
    }
}
