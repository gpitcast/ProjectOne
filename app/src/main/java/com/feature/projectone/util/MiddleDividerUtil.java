package com.feature.projectone.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2018/3/20.
 */

public class MiddleDividerUtil extends RecyclerView.ItemDecoration {

    private int space;
    private int spanCount;
    private int top;
    private int bottom;

    public MiddleDividerUtil(int space, int top, int bottom) {
        this.space = space;
        this.spanCount = spanCount;
        this.top = top;
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = space / 2;
        outRect.right = space / 2;
        outRect.top = top;
        outRect.bottom=bottom;
    }
}
