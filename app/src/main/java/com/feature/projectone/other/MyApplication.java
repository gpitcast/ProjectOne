package com.feature.projectone.other;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.feature.projectone.view.ExceptionHandler;
import com.lzy.okgo.OkGo;
import com.lzy.okserver.OkDownload;
import com.tencent.smtt.sdk.QbSdk;
import com.xiasuhuei321.loadingdialog.manager.StyleManager;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

/**
 * Created by Administrator on 2018/4/17.
 * 应用程序启动，自动初始化的application
 * 用来初始化一些数据，实体等
 */

public class MyApplication extends Application {

    private static Context mContext;
    private static OkDownload mOkDownload;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();//全局上下文

        Log.i("ListDownloadListener", "            MyApplication       设置了下载路径     " + Constanst.DOWN_LOAD_PATH);
        OkGo.getInstance().init(this);//初始化okgo
        mOkDownload = OkDownload.getInstance();
        mOkDownload.setFolder(Constanst.DOWN_LOAD_PATH);//设置下载的路径
        mOkDownload.getThreadPool().setCorePoolSize(3);//设置同时下载数量

        //初始化好看的diaolog
        StyleManager styleManager = new StyleManager();
        styleManager.Anim(true).repeatTime(0).contentSize(-1).intercept(true);//设置一些影响全局的属性
        LoadingDialog.initStyle(styleManager);

        //增加这句话(这里是初始化X5)
        QbSdk.initX5Environment(this, null);
        ExceptionHandler.getInstance().initConfig(this);
    }

    public static OkDownload getOkDownload() {
        return mOkDownload;
    }

    //获取全局的context
    public static Context getAppContext() {
        return mContext;
    }
}
