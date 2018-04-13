package com.feature.projectone.util;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.feature.projectone.inter.JsonInterface;
import com.feature.projectone.other.Constanst;
import com.lzy.okhttputils.callback.StringCallback;
import com.orhanobut.logger.Logger;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/3/28.
 * 网络请求数据封装类
 */

public class JsonUtils {

    JsonInterface jsonInterface;
    private HashMap hashMap;
    private String controller;
    private String action;
    private String URL;

    public void setJsonInterfaceListener(JsonInterface jsonInterface) {
        this.jsonInterface = jsonInterface;
    }

    /**
     * 请求网络
     *
     * @param context 上下文
     * @param msg     请求参数
     * @param view    防连点击传入的点击控件
     */
    public void PostJson(final Context context, Object msg, final View view) {

        hashMap = (HashMap) msg;
        controller = ((String) hashMap.get("controller"));
        action = ((String) hashMap.get("action"));
        URL = HttpUtils.Host + "/" + controller + "/" + action;

        view.setEnabled(false);

        if (NetWorkUtils.isNetConnected(context)) {
            HttpUtils.postRequest(context, msg, new StringCallback() {
                @Override
                public void onSuccess(String json, Call call, Response response) {
                    if (response.headers().get("User-Token") != null && response.headers().get("User-Token").length() > 0) {
                        //存储登录返回的token
                        String user_token = response.headers().get("User-Token");
                        ShareUtil.putString(context, "User-Token", user_token);
                    }
                    Logger.e("地址：" + URL + json);
                    readJson(json);
                    view.setEnabled(true);
                }
                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    jsonInterface.JsonResponse("-10000" + "", "网络请求失败", URL, new HashMap<>());
                    view.setEnabled(true);
                }
            });
        } else {
            //本地网络异常处理
            jsonInterface.JsonResponse(Constanst.error_net_code1, "本地网络异常，请确保wifi或者数据流量是否开启", URL, new HashMap<>());
            view.setEnabled(true);
        }
    }


    /**
     * 请求网络
     *
     * @param context 上下文
     * @param msg     请求参数
     */
    public void PostJson(final Context context, Object msg) {

        hashMap = (HashMap) msg;
        controller = ((String) hashMap.get("controller"));
        action = ((String) hashMap.get("action"));
        URL = HttpUtils.Host + "/" + controller + "/" + action;

        if (NetWorkUtils.isNetConnected(context)) {
            HttpUtils.postRequest(context, msg, new StringCallback() {
                @Override
                public void onSuccess(String json, Call call, Response response) {
                    if (response.headers().get("User-Token") != null && response.headers().get("User-Token").length() > 0) {
                        //存储登录返回的token
                        String user_token = response.headers().get("User-Token");
                        ShareUtil.putString(context, "User-Token", user_token);
                    }
                    Logger.e("地址：" + URL + json);
                    readJson(json);
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    jsonInterface.JsonResponse("-10000" + "", "网络请求失败", URL, new HashMap<>());
                }
            });
        } else {
            //本地网络异常处理
            jsonInterface.JsonResponse(Constanst.error_net_code1, "本地网络异常，请确保wifi或者数据流量是否开启", URL, new HashMap<>());
        }
    }

    private void readJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap;
        try {
            jsonMap = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
            if (null != jsonMap) {
                if (null != jsonMap.get("code")) {
                    String code = (Integer) jsonMap.get("code") + "";
                    if (code != null && code.equals(Constanst.success_net_code)) {
                        String msg = (String) jsonMap.get("msg");
                        Object result = jsonMap.get("result");
                        if (Constanst.success_net_code.equals(code) && result != null) {
                            jsonInterface.JsonResponse(code, msg, URL, result);
                        } else {
                            jsonInterface.JsonResponse(code, msg, URL, result);
                        }
                    } else {
                        jsonInterface.JsonResponse(code, "网络异常", URL, new HashMap<>());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
