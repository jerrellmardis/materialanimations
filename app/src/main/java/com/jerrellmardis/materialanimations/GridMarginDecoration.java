package com.jerrellmardis.materialanimations;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class GridMarginDecoration extends RecyclerView.ItemDecoration {

    private int margin;

    public GridMarginDecoration(int margin) {
        this.margin = margin;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = margin;
        outRect.top = margin;
        outRect.right = margin;
        outRect.bottom = margin;
    }
}
