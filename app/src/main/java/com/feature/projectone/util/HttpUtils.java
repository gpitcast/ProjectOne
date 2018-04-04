package com.feature.projectone.util;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
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
    public static void postRequest(Object msg, StringCallback stringCallback) {
        hashMap = (HashMap) msg;
        controller = ((String) hashMap.get("controller"));
        action = ((String) hashMap.get("action"));
        URL = HttpUtils.Host + "/" + controller + "/" + action;

        Gson gson = new Gson();
        String vars = gson.toJson(msg);
        Logger.e("请求接送：" + URL + "   " + vars);
        if (vars != null) {
            OkHttpUtils.post(Host)
                    .connTimeOut(DEFAULT_MILLISECONDS)
                    .readTimeOut(DEFAULT_MILLISECONDS)
                    .writeTimeOut(DEFAULT_MILLISECONDS)
                    .headers("Accept-Encoding", "identity")
                    .headers("http.keepAlive", "false")
                    .headers("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .params("vars", vars)
                    .execute(stringCallback);
        }
    }
}
