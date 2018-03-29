package com.feature.projectone.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.feature.projectone.view.ZQImageViewRoundOval;
import com.youth.banner.loader.ImageLoader;


public class GlideOvalImageLoader extends ImageLoader {
    @Override
    public void displayImage( Context context, Object path,  ImageView imageView) {
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
        Glide.with(context.getApplicationContext())
                .load(path)
//                .transform(new CenterCrop(context),new GlideRoundTransform(context,10))
                .into(imageView);
    }

    @Override
    public ImageView createImageView(Context context) {
        //圆角
        return new ZQImageViewRoundOval(context);
    }
}
