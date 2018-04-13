package com.feature.projectone.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.feature.projectone.other.Constanst;

import org.greenrobot.eventbus.EventBus;

import java.util.Random;

/**
 * Created by Administrator on 2018/4/8.
 * 短信验证码工具类
 */

public class MsgCodeUtil {

    /**
     * 阿里云短信验证码发送方法
     *
     * @param context   上下文
     * @param number    手机号码
     * @param signature 签名
     * @param template  模板
     */
    public static void sendMsg(final Context context, String number, String signature, String template) {
        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
        final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
        //替换成你的AK
        final String accessKeyId = "LTAIBiiuWqGluuI3";//你的accessKeyId,参考本文档步骤2
        final String accessKeySecret = "72P1AgeCQtXlepViQFy9hbLrID8cjK";//你的accessKeySecret，参考本文档步骤2
        //初始化ascClient,暂时不支持多region（请勿修改）
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
                accessKeySecret);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            final IAcsClient acsClient = new DefaultAcsClient(profile);
            //组装请求对象
            final SendSmsRequest request = new SendSmsRequest();
            //使用post提交
            request.setMethod(MethodType.POST);
            //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
            request.setPhoneNumbers(number);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName(signature);
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode(template);
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
//            request.setTemplateParam("{\"name\":\"Tom\", \"code\":\"123\"}");
            Random random = new Random();
            String code = (random.nextInt(899999) + 100000) + "";
            Constanst.phoneCode = code;
            Log.i("response", "    code  " + code);
            request.setTemplateParam("{\"code\":\"" + code + "\"}");
            //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
            //request.setSmsUpExtendCode("90997");
            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            request.setOutId("yourOutId");
            //请求失败这里会抛ClientException异常
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
                        if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                            //请求成功
                            Log.i("response", "    response OK ");
                        } else {
                            Log.i("response", "    getMessage  " + sendSmsResponse.getMessage());
                            Log.i("response", "    getCode  " + sendSmsResponse.getCode());
                            Log.i("response", "    getBizId  " + sendSmsResponse.getBizId());
                            Log.i("response", "    getRequestId  " + sendSmsResponse.getRequestId());
                        }
                    } catch (ClientException e) {
                        e.printStackTrace();
                        Log.i("response", "    getMessage  " + e.getMessage());
                        Log.i("response", "    getErrCode  " + e.getErrCode());
                        Log.i("response", "    getErrMsg  " + e.getErrMsg());
                        Log.i("response", "    getErrorType  " + e.getErrorType());
                    }
                }
            }).start();
        } catch (ClientException e) {
            e.printStackTrace();
            Log.i("response", "    ClientException2  " + e.getMessage());
        }
    }
}