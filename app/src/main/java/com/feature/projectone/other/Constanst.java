package com.feature.projectone.other;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import java.util.logging.Logger;

/**
 * Created by Administrator on 2018/3/30.
 * 储存常量类
 */

public class Constanst {
    public static String token = "";//验证token

    public static String phoneCode;//记录注册验证码
    public static String registTemplate = "SMS_130785205";//注册的短信模板
    public static String registSignature = "顾攀3";//注册的短信签名

    public static final String success_net_code = "200";//网络请求成功code
    public static final String error_net_code1 = "-200";//本地网络异常code(指wifi或者数据没有打开)
    public static final String Login = "/user/login";//登录接口
    public static final String Regist = "/user/register";//注册接口
    public static final String Home_Page = "/index/index";//APP首页接口

    public static String DOWN_LOAD_PATH = MyApplication.getAppContext().getFilesDir().getAbsolutePath();//获取应用的内部存储(data/data/+包名+files)文件夹,用来当做存储下载的资源的文件夹

    /*                所有的SharedPreferences存储所对应key值（一些常用的存储，集中处理，方便管理）               */
    public static final String UER_TOKEN = "User-Token";//验证登录状态的token
    public static final String TAGS = "tags";//用户点击新闻列表进入新闻详情，新闻详情返回的新闻tags，存储起来，请求搜索接口的时候要用
    /**********************************************************************/
}
