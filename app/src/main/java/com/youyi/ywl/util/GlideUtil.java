package com.youyi.ywl.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.youyi.ywl.R;

import java.io.File;

/**
 * Created by Administrator on 2018/9/21.
 * Glide加载图片工具类
 */

public class GlideUtil {

    /**
     * Glide封装加载网络图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadNetImageView(Context context, String url, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions().placeholder(R.mipmap.img_loading).error(R.mipmap.img_load_error);
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(imageView);


    }

    /**
     * Glide封装加载文件图片
     * @param context
     * @param file
     * @param imageView
     */
    public static void loadFileImageView(Context context, File file,ImageView imageView){
        RequestOptions requestOptions = new RequestOptions().placeholder(R.mipmap.img_loading).error(R.mipmap.img_load_error);
        Glide.with(context)
                .load(file)
                .apply(requestOptions)
                .into(imageView);
    }

    /**
     * Glide封装加载本地图片(不带加载中和加载失败的逻辑)
     * @param context
     * @param resId
     * @param imageView
     */
    public static void loadLocalImageView(Context context,int resId,ImageView imageView){
        Glide.with(context)
                .load(resId)
                .into(imageView);
    }
}

