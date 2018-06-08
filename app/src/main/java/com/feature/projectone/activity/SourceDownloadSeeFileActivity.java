package com.feature.projectone.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.util.ToastUtil;
import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/27.
 * 资源下载查看文件界面
 */

public class SourceDownloadSeeFileActivity extends BaseActivity implements TbsReaderView.ReaderCallback {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.tbsView)
    RelativeLayout tbsView;

    private String fileName;
    private String filePath;
    private static final String TAG = "DownloadSeeFileActivity";
    private TbsReaderView tbsReaderView;

    @Override
    protected void handleIntent(Intent intent) {
        if (intent != null) {
            fileName = intent.getStringExtra("fileName");
            filePath = intent.getStringExtra("filePath");
            Log.i(TAG, "     filePath    " + filePath);
            Log.i(TAG, "     fileName    " + fileName);
        }
    }

    @Override
    protected void Response(String code, String msg, String url, Object result) {
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_resource_download_see_file);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tv_title.setText(fileName);

        if (filePath == null) {
            ToastUtil.show(this, "文件路径为空", 0);
            return;
        }

        if (fileName == null) {
            ToastUtil.show(this, "文件名称为空", 0);
            return;
        }

        if (filePath.endsWith(".png") || filePath.endsWith(".jpg")) {
            //图片预览
            tbsView.setVisibility(View.GONE);
            iv.setVisibility(View.VISIBLE);
            try {
                FileInputStream fis = new FileInputStream(filePath);
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                iv.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            //非图片的文件预览
            tbsView.setVisibility(View.VISIBLE);
            iv.setVisibility(View.GONE);

            tbsReaderView = new TbsReaderView(this, this);
            tbsView.addView(tbsReaderView, new RelativeLayout.LayoutParams(-1, -1));
            displayFile(filePath, fileName);
        }

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
            ToastUtil.show(this, "打开文件失败", 0);
        }
    }

    /**
     * 判断文件类型
     *
     * @param paramString
     * @return
     */
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
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "FileDisplayActivity-->onDestroy");
        if (tbsReaderView != null) {
            tbsReaderView.onStop();
        }
    }

    @Override
    public void afterInitView() {
    }

    @OnClick({R.id.ll_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }

    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {

    }
}
