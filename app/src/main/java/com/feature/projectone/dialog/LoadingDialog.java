package com.feature.projectone.dialog;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.feature.projectone.R;

/**
 * Created by Administrator on 2018/3/13.
 */

public class LoadingDialog extends Dialog {
    private Context context;
    private ImageView imgLoading;

    public LoadingDialog(@NonNull Context context) {
        this(context, R.style.mDialog);
        this.context = context;
    }

    public LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        imgLoading = view.findViewById(R.id.imgLoading);
        Animation animation = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(3000);
        animation.setRepeatCount(ValueAnimator.INFINITE);
//        animation.setRepeatCount(8);//动画的重复次数
        animation.setFillAfter(true);//设置为true，动画转化结束后被应用
        imgLoading.startAnimation(animation);
        setContentView(view);
    }
}
