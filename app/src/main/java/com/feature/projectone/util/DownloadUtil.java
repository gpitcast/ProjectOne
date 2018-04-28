package com.feature.projectone.util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2018/4/16.
 * 下载工具（暂时不用）
 */

public class DownloadUtil {

    private static ExecutorService sExecutorService = Executors.newCachedThreadPool();//使用线程池节约内存
    private int contentLength;//下载文件的大小
    private int mThreadCount = 3;//将下载文件分成几份，也就是多线程现在的几个线程
    private Context context;

    public void download(final String downLoadUrl) {

        try {
            final URL url = new URL(downLoadUrl);
            sExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection connection = null;
                    try {
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setReadTimeout(5000);
                        connection.setRequestMethod("GET");
                        int code = connection.getResponseCode();
                        if (code == 200) {
                            //获取下载文件大小
                            contentLength = connection.getContentLength();
                            //在客户端创建一个临时文件
                            RandomAccessFile raf = new RandomAccessFile("mDownload.rar", "rwd");
                            raf.setLength(contentLength);
                            raf.close();
                            //利用多线程进行下载
                            //平均每个线程下载的文件大小
                            //将文件大小分成mThreadCount份
                            int block = contentLength / mThreadCount;
                            for (int i = 0; i < mThreadCount; i++) {
                                // 划分每个线程开始下载和结束下载的位置
                                //线程开始的位置
                                int start = i * block;
                                //线程结束的位置
                                int end = (i + 1) * block - 1;
                                if (i == mThreadCount - 1) {
                                    end = contentLength - 1;
                                }
                                //每次都开启一个线程
                                sExecutorService.execute(new MyRunnable(i, downLoadUrl, start, end));
                            }
                        }
                        Log.i("absolutePath", "   contentLength     " + contentLength);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    String filePath = "dsadasdsad";

    private class MyRunnable implements Runnable {
        private int id;
        private String downLoadUrl;
        private int startIndex;
        private int endIndex;

        public MyRunnable(int id, String downLoadUrl, int startIndex, int endIndex) {
            this.id = id;
            this.downLoadUrl = downLoadUrl;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        @Override
        public void run() {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(downLoadUrl).openConnection();
                //读取文件断点位置curIndex
                int curIndex = 0;
                File file = new File(context.getExternalCacheDir(), "mDownload.rar");
                if (file.exists() && file.length() > 0) {
                    FileInputStream fis = new FileInputStream(file);
                    BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                    curIndex = Integer.valueOf(br.readLine());
                    startIndex += curIndex;
                    fis.close();
                }

                // 2.从startIndex位置下载文件, 并从startIndex位置写入raf文件
                conn.setRequestProperty("Range", "bytes=" + startIndex + "-" + endIndex);
                InputStream is = conn.getInputStream();
                RandomAccessFile raf = new RandomAccessFile(new File(filePath), "rwd");
                raf.seek(startIndex);
                int len = 0;
                byte[] buffer = new byte[1024 * 1024];
                while ((len = is.read(buffer)) != -1) {
                    raf.write(buffer, 0, len);
                    // 更新断点位置
                    RandomAccessFile indexRaf = new RandomAccessFile("", "rwd");
                    curIndex += len;
                    indexRaf.write(String.valueOf(curIndex).getBytes());
                    indexRaf.close();
                }
                is.close();
                raf.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // 3.所有线程下载完,删除断点位置文件indexFile
                if (mThreadCount == 0) {
                    for (int i = 0; i < mThreadCount; i++) {
                        new File("" + i).delete();
                    }
                }
            }
        }
    }
}
