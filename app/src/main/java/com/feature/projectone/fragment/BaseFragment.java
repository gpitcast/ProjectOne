package com.feature.projectone.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feature.projectone.dialog.LoadingDialog;
import com.feature.projectone.inter.JsonInterface;
import com.feature.projectone.util.JsonUtils;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/13.
 */

public abstract class BaseFragment extends Fragment implements JsonInterface {


    private boolean isFirstInvisible;
    private boolean isPrepared;
    private boolean isFirstVisible;

    private LoadingDialog loadingDialog;
    private JsonUtils jsonUtils;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initViewsAndEvents(view);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getContentViewLayoutID() != 0) {
            return inflater.inflate(getContentViewLayoutID(), null);
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }

    private synchronized void initPrepare() {
        if (isPrepared) {
            onFirstUserVisible();
        } else {
            isPrepared = true;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false;
                initPrepare();
            } else {
                onUserVisible();
            }
        } else {
            if (isFirstInvisible) {
                isFirstInvisible = false;
                onFirstUserInvisible();
            } else {
                onUserInvisible();
            }
        }
    }

    public void showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(getContext());
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
    protected JsonUtils getJsonUtil() {
        if (jsonUtils == null) {
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

    protected abstract void onFirstUserVisible();

    protected abstract void onUserVisible();

    private void onFirstUserInvisible() {
    }

    protected abstract void onUserInvisible();

    protected abstract int getContentViewLayoutID();

    protected abstract void initViewsAndEvents(View view);

}
