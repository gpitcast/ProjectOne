package com.youyi.YWL.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.youyi.YWL.R;

/**
 * Created by Administrator on 2018/8/7.
 *  圆角图片
 */

@SuppressLint("AppCompatCustomView")
public class RoundImageView extends ImageView {

    /*圆角角度*/
    private float roundedCorners;

    /***
     * 圆角ImageView类的实现
     **/
    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context, attrs);
    }

    /**
     * 获取xml里面的圆角角度值
     * 默认10dp
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        /*获取圆角角度*/
        roundedCorners = typedArray.getDimension(R.styleable.RoundImageView_rounded_corners,
                20);
        typedArray.recycle();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        Path clipPath = new Path();
        int w = this.getWidth();
        int h = this.getHeight();

        clipPath.addRoundRect(new RectF(0, 0, w, h),
                roundedCorners,
                roundedCorners,
                Path.Direction.CW);
        canvas.clipPath(clipPath);

        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        super.onDraw(canvas);
    }
}
