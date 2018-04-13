package com.feature.projectone.util;

import android.content.Context;

import com.feature.projectone.other.Constanst;
import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.model.HttpHeaders;
import com.orhanobut.logger.Logger;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/3/28.
 * 网络请求基于OkhttpUtils的二次封装
 * github地址   compile 'com.lzy.net:okhttputils:1.8.1'
 */

public class HttpUtils {
    public static final int DEFAULT_MILLISECONDS = 1000;//超时时间
        public static final String Host = "http://47.104.128.245/api/data";//主机IP
//    public static final String Host = "http://47.104.128.245/apidemo/data";//测试主机IP
    private static HashMap hashMap;
    private static String controller;
    private static String action;
    private static String URL;

    /**
     * 请求服务器接口
     *
     * @param msg            携带的参数
     * @param stringCallback
     */
    public static void postRequest(Context context, Object msg, StringCallback stringCallback) {
        hashMap = (HashMap) msg;
        controller = ((String) hashMap.get("controller"));
        action = ((String) hashMap.get("action"));
        URL = HttpUtils.Host + "/" + controller + "/" + action;

        Gson gson = new Gson();
        String json = gson.toJson(msg);
        Logger.e("请求接送：  " + "User-Token:" + ShareUtil.getString(context, "User-Token") + "         " + URL + "   " + json);
        HttpHeaders httpHeaders = new HttpHeaders("User-Token", ShareUtil.getString(context, "User-Token"));
        if (json != null) {
            OkHttpUtils.post(Host)
                    .connTimeOut(DEFAULT_MILLISECONDS)
                    .readTimeOut(DEFAULT_MILLISECONDS)
                    .writeTimeOut(DEFAULT_MILLISECONDS)
                    .headers("Accept-Encoding", "identity123456789")
                    .headers("http.keepAlive", "false")
                    .headers("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .headers("User-Token", ShareUtil.getString(context, "User-Token"))
                    .params("vars", json)
                    .execute(stringCallback);
        }
    }
}
