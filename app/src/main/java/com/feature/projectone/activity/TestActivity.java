package com.feature.projectone.activity;

import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RelativeLayout;

import com.feature.projectone.R;
import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/22.
 * 测试界面
 */

public class TestActivity extends BaseActivity implements TbsReaderView.ReaderCallback {
    @BindView(R.id.tbsView)
    RelativeLayout tbsView;
    private TbsReaderView tbsReaderView;

    @Override
    protected void Response(String code, String msg, String url, Object result) {

    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_test);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        String filePath = "/data/user/0/com.feature.projectone/files/1526983713716/yasuo/test.doc";
        String fileName = "test.doc";
        tbsReaderView = new TbsReaderView(this, this);
        tbsView.addView(tbsReaderView, new RelativeLayout.LayoutParams(-1, -1));
        displayFile(filePath, fileName);
    }

    private String tbsReaderTemp = Environment.getExternalStorageDirectory() + "/TbsReaderTemp";

    private void displayFile(String filePath, String fileName) {
        //增加下面一句解决没有TbsReaderTemp文件夹存在导致加载文件失败
        String bsReaderTemp = tbsReaderTemp;
        File bsReaderTempFile = new File(bsReaderTemp);
        if (!bsReaderTempFile.exists()) {
            Log.i("TestActivity", "准备创建/TbsReaderTemp！！");
            boolean mkdir = bsReaderTempFile.mkdir();
            if (!mkdir) {
                Log.i("TestActivity", "创建/TbsReaderTemp失败！！");
            }
        }

        //打开文件
        Bundle bundle = new Bundle();
        bundle.putString("filePath", filePath);
        bundle.putString("tempPath", tbsReaderTemp);
        boolean result = tbsReaderView.preOpen(getFileType(fileName), false);
        if (result) {
            tbsReaderView.openFile(bundle);
        } else {

        }
    }

    private String getFileType(String paramString) {
        String str = "";

        if (TextUtils.isEmpty(paramString)) {
            Log.d("print", "paramString---->null");
            return str;
        }
        Log.d("print", "paramString:" + paramString);
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            Log.d("print", "i <= -1");
            return str;
        }

        str = paramString.substring(i + 1);
        Log.d("print", "paramString.substring(i + 1)------>" + str);
        return str;
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tbsReaderView.onStop();
    }
}
