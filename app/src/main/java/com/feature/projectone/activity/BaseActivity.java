package com.feature.projectone.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.feature.projectone.dialog.LoadingDialog;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/13.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public boolean isFullScreen = false;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFullScreen(isFullScreen);
        setContentLayout();
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        beforeInitView();
        initView();
        afterInitView();
    }

    /**
     * 是否全屏
     */
    public void setFullScreen(boolean fullScreen) {
        if (fullScreen) {
            //注释掉的部分是之前继承Activity时候设置全屏的代码
//         requestWindowFeature(Window.FEATURE_NO_TITLE);
//         getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//         WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().hide();
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
