package com.feature.projectone.other;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.feature.projectone.inter.JsonInterface;
import com.feature.projectone.util.CheckLoginUtil;
import com.feature.projectone.util.HttpUtils;
import com.feature.projectone.util.JsonUtils;
import com.feature.projectone.util.ShareUtil;
import com.feature.projectone.util.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okserver.OkDownload;
import com.tencent.smtt.sdk.QbSdk;
import com.xiasuhuei321.loadingdialog.manager.StyleManager;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

/**
 * Created by Administrator on 2018/4/17.
 * 应用程序启动，自动初始化的application
 * 用来初始化一些数据，实体等
 */

public class MyApplication extends Application {

    private static Context mContext;
    private static OkDownload mOkDownload;
//    JsonUtils jsonUtils;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();//全局上下文

        Log.i("OkDownload", "            MyApplication       设置了下载路径     " + Constanst.DOWN_LOAD_PATH);
        OkGo.getInstance().init(this);//初始化okgo
        mOkDownload = OkDownload.getInstance();
        mOkDownload.setFolder(Constanst.DOWN_LOAD_PATH);//设置下载的路径
        mOkDownload.getThreadPool().setCorePoolSize(3);//设置同时下载数量

        //初始化好看的diaolog
        StyleManager styleManager = new StyleManager();
        styleManager.Anim(true).repeatTime(0).contentSize(-1).intercept(true);//设置一些影响全局的属性
        LoadingDialog.initStyle(styleManager);

        //增加这句话(这里是初始化X5,腾讯的tbs浏览服务)
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
            }

            @Override
            public void onViewInitFinished(boolean b) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.e("QbSdk", "加载内核是否成功:" + b);
            }
        });


        //初始化融云即时通讯
        RongIM.init(this);//初始化


    }


    public static OkDownload getOkDownload() {
        return mOkDownload;
    }

    //获取全局的context
    public static Context getAppContext() {
        return mContext;
    }

}
