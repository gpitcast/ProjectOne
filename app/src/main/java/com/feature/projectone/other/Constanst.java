package com.feature.projectone.other;

import java.util.logging.Logger;

/**
 * Created by Administrator on 2018/3/30.
 * 储存常量类
 */

public class Constanst {
    public static String token = "";
    public static String phoneCode;//注册验证码
    public static String registTemplate = "SMS_130785205";//注册的短信模板
    public static String registSignature = "顾攀4";//注册的短信签名
    public static final String success_net_code = "200";//网络请求成功code
    public static final String error_net_code1 = "-200";//本地网络异常code(指wifi或者数据没有打开)
    public static final String Login = "/user/login";//登录接口
    public static final String Regist = "/user/register";//注册接口
    public static final String Home_Page = "/index/index";//APP首页接口
    public static int UpHeight;
}
