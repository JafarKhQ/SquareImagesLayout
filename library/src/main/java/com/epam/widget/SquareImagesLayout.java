package com.epam.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class SquareImagesLayout extends ViewGroup {
    private static final int MIN_NUMBER_OF_ROWS = 1;
    private static final int MIN_NUMBER_OF_COLUMNS = 1;
    private static final ImageView.ScaleType[] SCALE_TYPES = ImageView.ScaleType.values();


//    private static final int DEFAULT_CONTENT_PADDING = 0;
//    private static final int DEFAULT_NUMBER_OF_ROWS = 1;
//    private static final int DEFAULT_NUMBER_OF_COLUMNS = 1;

    private int mNumberOfRows;
    private int mNumberOfColumns;
    private int mContentPadding;
    private ImageView.ScaleType mScaleType;

    private ViewGroup.LayoutParams mLayoutParamsImageView;

    public SquareImagesLayout(Context context) {
        this(context, null);
    }

    public SquareImagesLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.SquareImagesLayoutStyle);
    }

    public SquareImagesLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //TODO: default style resource
        final TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.SquareImagesLayout,
                defStyleAttr,
                R.style.SquareImagesLayoutStyleDefault);

        try {
            setNumberOfRows(a.getInteger(R.styleable.SquareImagesLayout_numRows, 0));
            setNumberOfColumns(a.getInteger(R.styleable.SquareImagesLayout_numColumns, 0));
            setContentPadding(a.getDimensionPixelSize(R.styleable.SquareImagesLayout_contentPadding, 0));

            final int scaleType = a.getInt(R.styleable.SquareImagesLayout_android_scaleType, 0);
            setScaleType(SCALE_TYPES[scaleType]);
        } finally {
            a.recycle();
        }

        mLayoutParamsImageView = new ViewGroup.LayoutParams(0, 0);

        final int childCount = mNumberOfColumns * mNumberOfRows;
        for (int i = 0; i < childCount; i++) {
            ImageView imv = new ImageView(getContext());
            imv.setScaleType(mScaleType);
            super.addView(imv);
        }
    }

    public void setNumberOfRows(int numberOfRows) {
        if (numberOfRows < MIN_NUMBER_OF_ROWS) {
            throw new IllegalArgumentException("Number Of Rows ("
                    + numberOfRows
                    + ") cant be less than " + MIN_NUMBER_OF_ROWS);
        }

        mNumberOfRows = numberOfRows;
    }

    public void setNumberOfColumns(int numberOfColumns) {
        if (numberOfColumns < MIN_NUMBER_OF_COLUMNS) {
            throw new IllegalArgumentException("Number Of Columns ("
                    + numberOfColumns
                    + ") cant be less than " + MIN_NUMBER_OF_COLUMNS);
        }

        mNumberOfColumns = numberOfColumns;
    }

    public void setContentPadding(int contentPadding) {
        mContentPadding = contentPadding;
    }

    public void setScaleType(ImageView.ScaleType scaleType) {
        if (scaleType == null) {
            throw new NullPointerException();
        }

        if (scaleType != mScaleType) {
            mScaleType = scaleType;

            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int sizeWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingRight() - getPaddingLeft();
        final int sizeHeight = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();

        final int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        final int totalHorizontalContentPadding = mContentPadding * (mNumberOfColumns - 1);
        final int totalVerticalContentPadding = mContentPadding * (mNumberOfRows - 1);

        final int imageViewSize = (sizeWidth - totalHorizontalContentPadding) / mNumberOfColumns;
        final int layoutHeight = (imageViewSize * mNumberOfRows)
                + getPaddingTop() + getPaddingBottom()
                + totalVerticalContentPadding;

        mLayoutParamsImageView.width = mLayoutParamsImageView.height = imageViewSize;

        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.setLayoutParams(mLayoutParamsImageView);

            v.measure(MeasureSpec.makeMeasureSpec(imageViewSize, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(imageViewSize, MeasureSpec.EXACTLY));
        }

        setMeasuredDimension(sizeWidth,
                (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight : layoutHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);

            v.layout(0, mLayoutParamsImageView.width, 0, mLayoutParamsImageView.height);
        }
    }



    /*
    @Override
    public void addView(View child) {
        throw new UnsupportedOperationException("You cant remove or add Views");
    }

    @Override
    public void addView(View child, int index) {
        throw new UnsupportedOperationException("You cant remove or add Views");
    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        throw new UnsupportedOperationException("You cant remove or add Views");
    }

    @Override
    public void addView(View child, LayoutParams params) {
        throw new UnsupportedOperationException("You cant remove or add Views");
    }

    @Override
    public void addView(View child, int width, int height) {
        throw new UnsupportedOperationException("You cant remove or add Views");
    }

    @Override
    public void removeView(View view) {
        throw new UnsupportedOperationException("You cant remove or add Views");
    }

    @Override
    public void removeViewAt(int index) {
        throw new UnsupportedOperationException("You cant remove or add Views");
    }

    @Override
    public void removeViews(int start, int count) {
        throw new UnsupportedOperationException("You cant remove or add Views");
    }

    @Override
    public void removeAllViews() {
        throw new UnsupportedOperationException("You cant remove or add Views");
    }
    */
}
