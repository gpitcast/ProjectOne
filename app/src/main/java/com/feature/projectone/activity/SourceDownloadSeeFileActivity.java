package com.feature.projectone.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.util.LoadFileModel;
import com.feature.projectone.util.Md5Tool;
import com.feature.projectone.view.SuperFileView2;
import com.feature.projectone.view.ZZoomImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2018/4/27.
 * 资源下载查看文件界面
 */

public class SourceDownloadSeeFileActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.superFileView2)
    SuperFileView2 mSuperFileView;
    @BindView(R.id.iv)
    ZZoomImageView iv;

    private String fileName;
    private String filePath;
    private static final String TAG = "DownloadSeeFile";

    @Override
    protected void handleIntent(Intent intent) {
        if (intent != null) {
            fileName = intent.getStringExtra("fileName");
            filePath = intent.getStringExtra("filePath");
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
        Log.i(TAG, "     filePath    " + filePath);
        tv_title.setText(fileName);

        if (filePath == null) {
            return;
        }

        if (filePath.endsWith(".png") || filePath.endsWith(".jpg")) {
            mSuperFileView.setVisibility(View.GONE);
            iv.setVisibility(View.VISIBLE);
            try {
                FileInputStream fis = new FileInputStream(filePath);
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                iv.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
//            Picasso.with(this).load(filePath).placeholder(R.mipmap.img_loading_default).error(R.mipmap.img_load_error).into(iv);
        } else {
            mSuperFileView.setVisibility(View.VISIBLE);
            iv.setVisibility(View.GONE);

            File file = new File(filePath);
            mSuperFileView.setOnGetFilePathListener(new SuperFileView2.OnGetFilePathListener() {
                @Override
                public void onGetFilePath(SuperFileView2 mSuperFileView2) {
                    getFilePathAndShowFile(mSuperFileView2);
                }
            });

            if (!TextUtils.isEmpty(filePath) && file.exists()) {
                setFilePath(filePath);
            }

            mSuperFileView.show();
        }
    }

    public void setFilePath(String fileUrl) {
        this.filePath = fileUrl;
    }

    private String getFilePath() {
        return filePath;
    }

    private void getFilePathAndShowFile(SuperFileView2 mSuperFileView2) {
//        mSuperFileView2.displayFile(new File(filePath));
        if (getFilePath().contains("http")) {//网络地址要先下载

            downLoadFromNet(getFilePath(), mSuperFileView2);

        } else {
            mSuperFileView2.displayFile(new File(getFilePath()));
        }
    }

    private void downLoadFromNet(final String url, final SuperFileView2 mSuperFileView2) {

        //1.网络下载、存储路径、
        File cacheFile = getCacheFile(url);
        if (cacheFile.exists()) {
            if (cacheFile.length() <= 0) {
                Log.i(TAG, "删除空文件！！");
                cacheFile.delete();
                return;
            }
        }


        LoadFileModel.loadPdfFile(url, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, "下载文件-->onResponse");
                boolean flag;
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    ResponseBody responseBody = response.body();
                    is = responseBody.byteStream();
                    long total = responseBody.contentLength();

                    File file1 = getCacheDir(url);
                    if (!file1.exists()) {
                        file1.mkdirs();
                        Log.i(TAG, "创建缓存目录： " + file1.toString());
                    }


                    //fileN : /storage/emulated/0/pdf/kauibao20170821040512.pdf
                    File fileN = getCacheFile(url);//new File(getCacheDir(url), getFileName(url))

                    Log.i(TAG, "创建缓存文件： " + fileN.toString());
                    if (!fileN.exists()) {
                        boolean mkdir = fileN.createNewFile();
                    }
                    fos = new FileOutputStream(fileN);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        Log.i(TAG, "写入缓存文件" + fileN.getName() + "进度: " + progress);
                    }
                    fos.flush();
                    Log.i(TAG, "文件下载成功,准备展示文件。");
                    //2.ACache记录文件的有效期
                    mSuperFileView2.displayFile(fileN);
                } catch (Exception e) {
                    Log.i(TAG, "文件下载异常 = " + e.toString());
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, "文件下载失败");
                File file = getCacheFile(url);
                if (!file.exists()) {
                    Log.i(TAG, "删除下载失败文件");
                    file.delete();
                }
            }
        });
    }

    /***
     * 获取缓存目录
     *
     * @param url
     * @return
     */
    private File getCacheDir(String url) {

        return new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/007/");

    }

    /***
     * 绝对路径获取缓存文件
     *
     * @param url
     * @return
     */
    private File getCacheFile(String url) {
        File cacheFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/007/"
                + getFileName(url));
        Log.i(TAG, "缓存文件 = " + cacheFile.toString());
        return cacheFile;
    }

    /***
     * 根据链接获取文件名（带类型的），具有唯一性
     *
     * @param url
     * @return
     */
    private String getFileName(String url) {
        String fileName = Md5Tool.hashKey(url) + "." + getFileType(url);
        return fileName;
    }

    /***
     * 获取文件类型
     *
     * @param paramString
     * @return
     */
    private String getFileType(String paramString) {
        String str = "";

        if (TextUtils.isEmpty(paramString)) {
            Log.i(TAG, "paramString---->null");
            return str;
        }
        Log.i(TAG, "paramString:" + paramString);
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            Log.i(TAG, "i <= -1");
            return str;
        }


        str = paramString.substring(i + 1);
        Log.i(TAG, "paramString.substring(i + 1)------>" + str);
        return str;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "FileDisplayActivity-->onDestroy");
        if (mSuperFileView != null) {
            mSuperFileView.onStopDisplay();
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
}
