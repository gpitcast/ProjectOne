package com.youyi.YWL.wxapi;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.youyi.YWL.activity.BaseActivity;
import com.youyi.YWL.other.Constanst;
import com.youyi.YWL.other.MyApplication;
import com.youyi.YWL.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/7/12.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "WXEntryActivity";
    private static final int RETURN_MSG_TYPE_LOGIN = 1;//登录
    private static final int RETURN_MSG_TYPE_SHARE = 2;//分享
    private Context mContext;
    private static String tokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        //这句没有写,是不能执行回调的方法的
        MyApplication.wxapi.handleIntent(getIntent(), this);
    }


    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq baseReq) {
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    //app发送消息给微信，处理返回消息的回调
    @Override
    public void onResp(BaseResp baseResp) {
        Log.i(TAG, "onResp:------>");
        Log.i(TAG, "error_code:---->" + baseResp.errCode);
        int type = baseResp.getType();
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //用户拒绝授权
                ToastUtil.show(mContext, "拒绝授权微信登录", 0);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //用户取消
                String message = "";
                if (type == RETURN_MSG_TYPE_LOGIN) {
                    message = "取消了微信登录";
                } else if (type == RETURN_MSG_TYPE_SHARE) {
                    message = "取消了微信分享";
                }
                ToastUtil.show(mContext, message, 0);
                break;
            case BaseResp.ErrCode.ERR_OK:
                //用户同意
                if (type == RETURN_MSG_TYPE_LOGIN) {
                    //用户换取access_token的code，仅在ErrCode为0时有效
                    String code = ((SendAuth.Resp) baseResp).code;
                    Log.i(TAG, "code:------>" + code);

                    //这里拿到了这个code，去做2次网络请求获取access_token和用户个人信息
                    ToastUtil.show(mContext, "2此请求网络获取access_token和用户个人信息", 0);
                    //请求token接口
                    PostTokenList(code);
                } else if (type == RETURN_MSG_TYPE_SHARE) {
                    ToastUtil.show(mContext, "微信分享成功", 0);
                }
                break;
        }
    }

    private void PostTokenList(String code) {

        OkHttpUtils.post(tokenUrl)
                .connTimeOut(3000)
                .readTimeOut(3000)
                .writeTimeOut(3000)
                .params("appid", Constanst.WEIXIN_APP_ID)
                .params("secret", Constanst.WEIXIN_APP_SECRET)
                .params("code", code)
                .params("grant_type", "authorization_code")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.i(TAG, "    response.code()   " + response.code());
                        Log.i(TAG, "    s:   " + s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Log.i(TAG, "   e:    " + e.toString());
                    }
                });
    }
}
