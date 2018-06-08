package com.feature.projectone.util;

import android.content.Context;
import android.util.Log;

import com.feature.projectone.other.Constanst;
import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.model.HttpHeaders;
import com.lzy.okhttputils.request.PostRequest;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by Administrator on 2018/3/28.
 * 网络请求基于OkhttpUtils的二次封装
 * github地址   compile 'com.lzy.net:okhttputils:1.8.1'
 */

public class HttpUtils {
    public static final int DEFAULT_MILLISECONDS = 3000;//超时时间
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
     * @param stringCallback 回调
     */
    public static void postRequest(Context context, Object msg, StringCallback stringCallback) {
        hashMap = (HashMap) msg;
        controller = ((String) hashMap.get("controller"));
        action = ((String) hashMap.get("action"));
        URL = HttpUtils.Host + "/" + controller + "/" + action;

        Gson gson = new Gson();
        String json = gson.toJson(msg);
        Logger.e("请求接送：  " + "User-Token:" + ShareUtil.getString(context, Constanst.UER_TOKEN) + "         " + URL + "   " + json);
        HttpHeaders httpHeaders = new HttpHeaders(Constanst.UER_TOKEN, ShareUtil.getString(context, Constanst.UER_TOKEN));
        if (json != null) {
            OkHttpUtils.post(Host)
                    .connTimeOut(DEFAULT_MILLISECONDS)
                    .readTimeOut(DEFAULT_MILLISECONDS)
                    .writeTimeOut(DEFAULT_MILLISECONDS)
                    .headers("Accept-Encoding", "identity123456789")
                    .headers("http.keepAlive", "false")
                    .headers("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .headers(Constanst.UER_TOKEN, ShareUtil.getString(context, Constanst.UER_TOKEN))
                    .params("vars", json)
                    .execute(stringCallback);
        }
    }

    /**
     * 请求服务器(带图片)
     *
     * @param context        上下文
     * @param msg            文本参数
     * @param mPicFileList   图片文件集合
     * @param stringCallback 回调
     */
    public static void postRequest(Context context, Object msg, List<File> mPicFileList, StringCallback stringCallback) {
        hashMap = (HashMap) msg;
        controller = ((String) hashMap.get("controller"));
        action = ((String) hashMap.get("action"));
        URL = HttpUtils.Host + "/" + controller + "/" + action;

        Gson gson = new Gson();
        String json = gson.toJson(msg);
        Logger.e("请求接送：  " + "User-Token:" + ShareUtil.getString(context, Constanst.UER_TOKEN) + "         " + URL + "   " + json);
        HttpHeaders httpHeaders = new HttpHeaders(Constanst.UER_TOKEN, ShareUtil.getString(context, Constanst.UER_TOKEN));
        if (json != null) {
            final PostRequest request = OkHttpUtils.post(Host)
                    .connTimeOut(DEFAULT_MILLISECONDS)
                    .readTimeOut(DEFAULT_MILLISECONDS)
                    .writeTimeOut(DEFAULT_MILLISECONDS)
                    .headers("Accept-Encoding", "identity123456789")
                    .headers("http.keepAlive", "false")
                    .headers("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .headers(Constanst.UER_TOKEN, ShareUtil.getString(context, Constanst.UER_TOKEN))
                    .params("vars", json);

            //添加图片文件参数
            if (mPicFileList != null && mPicFileList.size() > 0) {
                if (mPicFileList.size() == 1) {
                    //一张照片的操作(应后台要求)
                    File file = mPicFileList.get(0);
                    request.params("img", file);
                } else {
                    //多张照片的操作(应后台要求)
                    for (int i = 0; i < mPicFileList.size(); i++) {
                        final File file = mPicFileList.get(i);
                        if (file.exists()) {
                            request.params("img[]", file);
                        }
                    }
                }

            }
            request.execute(stringCallback);
        }
    }
}
