package com.feature.projectone.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import com.feature.projectone.R;
import com.feature.projectone.dialog.LoadingDialog;
import com.feature.projectone.inter.JsonInterface;
import com.feature.projectone.util.JsonUtils;
import com.feature.projectone.util.SoftUtil;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/13.
 */

public abstract class BaseActivity extends AppCompatActivity implements JsonInterface {

    public boolean isFullScreen = false;
    private LoadingDialog loadingDialog;
    JsonUtils jsonUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFullScreen(isFullScreen);
        setContentLayout();
        setFlagStatus();//设置状态栏颜色
        ButterKnife.bind(this);//初始化黃油刀插件

        if (getIntent() != null) {
            handleIntent(getIntent());
        }

        beforeInitView();
        initView();
        afterInitView();

    }

    protected void handleIntent(Intent intent) {
    }

    /**
     * 统一设置Activity状态栏颜色
     */
    public void setFlagStatus() {
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.orangeone));
    }

    ;

    /**
     * 是否全屏
     */
    public void setFullScreen(boolean fullScreen) {
        if (fullScreen) {
            //注释掉的部分是之前继承Activity时候设置全屏的代码
//         requestWindowFeature(Window.FEATURE_NO_TITLE);
//         getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//         WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            getSupportActionBar().hide();
        } else {
//            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
    }


    public void showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.show();
    }

    public void dismissLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 提供单例网络请求工具
     */
    public JsonUtils getJsonUtil() {
        if (jsonUtils == null) {
            Log.i("JsonUtils", "   jsonUtils == null，设置了接口  ");
            jsonUtils = new JsonUtils();
            jsonUtils.setJsonInterfaceListener(this);
            return jsonUtils;
        } else {
            return jsonUtils;
        }
    }

    /**
     * 实现JsonInterfaceListener接口方法，通过response给子activity传递网络请求响应数据
     *
     * @param code
     * @param msg
     * @param result
     */
    @Override
    public void JsonResponse(String code, String msg, String url, Object result) {
        Response(code, msg, url, result);
    }

    protected abstract void Response(String code, String msg, String url, Object result);

    /**
     * 设置布局文件
     */
    public abstract void setContentLayout();

    /**
     * 在实例化控件之前的逻辑操作
     */
    public abstract void beforeInitView();

    /**
     * 实例化控件
     */
    public abstract void initView();

    /**
     * 实例化控件之后的操作
     */
    public abstract void afterInitView();

}
