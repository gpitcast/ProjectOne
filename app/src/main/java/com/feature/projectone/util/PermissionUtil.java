package com.feature.projectone.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/17.
 * 6.0敏感权限请求工具类
 */

public class PermissionUtil {

    //在Activity中检查权限，并在没有权限时候请求权限，并执行
    public static boolean checkPermission(Context context, String permission, int code) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            //没有权限，调用系统请求权限
            ActivityCompat.requestPermissions(((Activity) context), new String[]{permission}, code);
            return false;
        } else {
            //已经拥有权限
            return true;
        }
    }

    public static List<String> permissionList = new ArrayList<>();

    //一次请求多个权限
    public static boolean checkPermission(Context context, String[] permissions, int code) {
        permissionList.clear();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(context, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permissions[i]);
            }
        }

        if (permissionList.isEmpty()) {
            return true;//遍历完所有的权限，list为空代表所有的权限都是拥有的
        } else {
            String[] deniedPermissions = new String[permissionList.size()];
            //没有权限，调用系统请求权限
            for (int i = 0; i < permissionList.size(); i++) {
                deniedPermissions[i] = permissionList.get(i);
            }
            ActivityCompat.requestPermissions(((Activity) context), deniedPermissions, code);
            return false;
        }
    }

    //在Fragment中检查权限，并在没有权限时候请求权限，并执行
    public static boolean checkPermission(Context context, Fragment fragment, String permission, int code) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            //没有权限，调用系统请求权限
            fragment.requestPermissions(new String[]{permission}, code);
            return false;
        } else {
            //已经拥有权限
            return true;
        }
    }

    //跳转到权限设置界面
    public static void jumpToPermissionActivity(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", ((Activity) context).getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }
}
