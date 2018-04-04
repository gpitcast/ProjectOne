package com.feature.projectone.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.inter.OnTvCheckedChangedListener;

/**
 * Created by Administrator on 2018/4/3.
 * 自定义 类似checkedBox的TextView 并能记录选中状态
 */

@SuppressLint("AppCompatCustomView")
public class ShaixuanTextView extends TextView {
    private OnTvCheckedChangedListener listener;

    public ShaixuanTextView(Context context) {
        super(context);
    }

    public ShaixuanTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ShaixuanTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private boolean isChecked;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isChecked == false) {
                    this.setBackgroundResource(R.drawable.bg_shaixuan_checked);
                    this.setTextColor(Color.WHITE);
                    isChecked = true;
                } else {
                    this.setBackgroundResource(R.drawable.bg_shaixuan_normal);
                    this.setTextColor(getResources().getColor(R.color.normal_black3));
                    isChecked = false;
                }
                listener.onCheckedChange(isChecked);
                break;
        }
        return super.onTouchEvent(event);
    }

    public boolean isChecked() {
        return isChecked();
    }


    public void setOnCheckedChangedListener(OnTvCheckedChangedListener listener) {
        this.listener = listener;
    }
}
