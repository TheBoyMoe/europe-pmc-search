package com.example.downloaderdemo.util;

/**
 * Reference
 * From _The Busy Coder's Guide to Android Development  https://commonsware.com/Android (p1221-24)
 */
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class HorizontalDivider extends RecyclerView.ItemDecoration{

    private Drawable mItemDivider;

    public HorizontalDivider(Drawable drawable) {
        mItemDivider = drawable;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {

        View view;
        RecyclerView.LayoutParams params;
        int left, top, bottom, right;
        int itemNumber = parent.getChildCount();

        left = parent.getPaddingLeft();
        right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < itemNumber; i++) {
            view = parent.getChildAt(i);
            params = (RecyclerView.LayoutParams) view.getLayoutParams();
            top = view.getBottom() + params.bottomMargin;
            bottom = mItemDivider.getIntrinsicHeight() + top;
            mItemDivider.setBounds(left, top, right, bottom);
            mItemDivider.draw(canvas);
        }
    }


}
