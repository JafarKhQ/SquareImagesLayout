package com.epam.sample.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

import com.epam.widget.SquareImagesView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class MySquareImagesView extends SquareImagesView implements Target {

    public MySquareImagesView(Context context) {
        super(context);
    }

    public MySquareImagesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySquareImagesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        addImage(bitmap);
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
        Log.w("MySquareImagesView", "onBitmapFailed");
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
    }

    @Override
    public boolean equals(Object o) {
        // work around to force Picasso to call this Target callback multiple times
        return false;
    }
}
