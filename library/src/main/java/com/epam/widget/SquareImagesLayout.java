package com.epam.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class SquareImagesLayout extends ViewGroup {

    public SquareImagesLayout(Context context) {
        this(context, null);
    }

    public SquareImagesLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareImagesLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
