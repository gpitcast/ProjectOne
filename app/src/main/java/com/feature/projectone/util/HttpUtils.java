package com.feature.projectone.util;

import android.content.Context;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.AbsCallback;
import com.lzy.okhttputils.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/3/28.
 * 网络请求基于OkhttpUtils的二次封装
 * github地址   compile 'com.lzy.net:okhttputils:1.8.1'
 */

public class HttpUtils {
    public static final String RootServerIP = "";
    public static final int DEFAULT_MILLISECONDS = 1000;

    /**
     * 请求服务器接口
     *
     * @param context        上下文
     * @param url            服务器接口地址
     * @param msg            携带的参数
     * @param stringCallback
     */
    public static void postRequest(Context context, String url, Object msg, StringCallback stringCallback) {
        Gson gson = new Gson();
        String json = gson.toJson(msg);
        if (url != null) {
            OkHttpUtils
                    .post(RootServerIP)
                    .tag(context)
                    .connTimeOut(DEFAULT_MILLISECONDS)
                    .readTimeOut(DEFAULT_MILLISECONDS)
                    .writeTimeOut(DEFAULT_MILLISECONDS)
                    .params("", json)
                    .execute(stringCallback);
        }
    }
}
