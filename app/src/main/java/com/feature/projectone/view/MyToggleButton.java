package com.feature.projectone.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ToggleButton;

import com.feature.projectone.R;

/**
 * Created by Administrator on 2018/3/29.
 *  自定义toggleButton
 */

public class MyToggleButton extends View {

    private View teacherView;
    private View studentView;
    private Bitmap teacherBitmap;
    private Bitmap studentBitmap;
    private Bitmap teacher;
    private Bitmap student;

    //准备
    public MyToggleButton(Context context) {
        super(context);
        teacherView = LayoutInflater.from(context).inflate(R.layout.layout_teacher, null);
        studentView = LayoutInflater.from(context).inflate(R.layout.layout_student, null);

    }

    public MyToggleButton(Context context, @Nullable AttributeSet attrs) {
        this(context);
    }

    public MyToggleButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    //测量
    ////java代码中创建时使用这个构造方法
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        setMeasuredDimension();
    }

    //排版
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    //绘制
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    //滑动事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return false;
    }

    public Bitmap getViewBitmap(View v) {
        v.setPressed(false);
        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);
        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);
        return bitmap;
    }
}
