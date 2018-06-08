package com.feature.projectone.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

/**
 * Created by Administrator on 2018/4/4.
 * 网络连接工具类
 */

public class NetWorkUtils {

    //判断本地网络连接状态
    public static Boolean isNetConnected(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                return networkInfo.isAvailable();
            }
        }
        return false;
    }

    //判断wifi是否连接

    public static boolean checkWifiState(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //API版本23以下时调用此方法进行检测
        //因为API23后getNetworkInfo(int networkType)方法被弃用
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return networkInfo.isConnected();
        } else {
            // API 23及以上时调用此方法进行网络的检测
            // getAllNetworks() 在API 21后开始使用
            Network[] networks = manager.getAllNetworks();
            for (int i = 0; i < networks.length; i++) {
                NetworkInfo networkInfo = manager.getNetworkInfo(networks[i]);
                if (networkInfo.getTypeName().equals("WIFI")) {
                    return networkInfo.isConnected();
                }
            }
        }
        return false;
    }
}