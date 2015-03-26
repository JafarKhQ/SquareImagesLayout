package com.epam.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Random;

public class SquareImagesLayout extends ViewGroup {
    private static final String TAG = "SquareImagesLayout";

    private static final int NONE = Integer.MIN_VALUE;

    private static final int MIN_NUMBER_OF_ROWS = 1;
    private static final int MIN_NUMBER_OF_COLUMNS = 1;
    private static final ImageView.ScaleType[] SCALE_TYPES = ImageView.ScaleType.values();

    private static final int DEFAULT_CONTENT_PADDING = 0;
    private static final int DEFAULT_NUMBER_OF_ROWS = 1;
    private static final int DEFAULT_NUMBER_OF_COLUMNS = 1;
    private static final ImageView.ScaleType DEFAULT_SCALE_TYPE = ImageView.ScaleType.CENTER_CROP;

    private int mNumberOfRows;
    private int mNumberOfColumns;
    private int mContentPadding;
    private ImageView.ScaleType mScaleType;

    public SquareImagesLayout(Context context) {
        this(context, null);
    }

    public SquareImagesLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareImagesLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.SquareImagesLayout,
                defStyleAttr, 0);

        try {
            setNumberOfRows(a.getInteger(R.styleable.SquareImagesLayout_numRows,
                    DEFAULT_NUMBER_OF_ROWS));
            setNumberOfColumns(a.getInteger(R.styleable.SquareImagesLayout_numColumns,
                    DEFAULT_NUMBER_OF_COLUMNS));
            setContentPadding(a.getDimensionPixelSize(R.styleable.SquareImagesLayout_contentPadding,
                    DEFAULT_CONTENT_PADDING));

            final int scaleType = a.getInt(R.styleable.SquareImagesLayout_android_scaleType, NONE);
            if (NONE == scaleType || scaleType < 0 || scaleType >= SCALE_TYPES.length) {
                setScaleType(DEFAULT_SCALE_TYPE);
            } else {
                setScaleType(SCALE_TYPES[scaleType]);
            }
        } finally {
            a.recycle();
        }

        final int childCount = mNumberOfColumns * mNumberOfRows;

        for (int i = 0; i < childCount; i++) {
            LayoutParams lp = new LayoutParams();
            ColorDrawable drawable = new ColorDrawable(getRandomBgColor());

            ImageView imv = new ImageView(getContext());
            imv.setLayoutParams(lp);
            imv.setScaleType(mScaleType);
            imv.setImageDrawable(drawable);
            imv.setTag(i);

            myAddView(imv);
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

    public void setImagesResource(int[] resIds) {
        if (null == resIds || resIds.length < 1) {
            Log.w(TAG, "setImagesResource: empty array of ids");
            return;
        }

        final int childCount = getChildCount();
        for (int i = 0; i < childCount && i < resIds.length; i++) {
            ((ImageView) getChildAt(i)).setImageResource(resIds[i]);

        }
    }

    private int getRandomBgColor() {
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);

        return Color.rgb(r, g, b);
    }

    private void myAddView(View v) {
        super.addView(v, -1, v.getLayoutParams());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        final int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        final int availableContentWidth = sizeWidth - (getPaddingLeft() + getPaddingRight());

        final int totalHorizontalContentPadding = mContentPadding * (mNumberOfColumns - 1);
        final int totalVerticalContentPadding = mContentPadding * (mNumberOfRows - 1);

        final int imageViewSize = (availableContentWidth - totalHorizontalContentPadding) / mNumberOfColumns;
        final int layoutHeight = (imageViewSize * mNumberOfRows)
                + getPaddingTop() + getPaddingBottom()
                + totalVerticalContentPadding;

        for (int currentChild = 0; currentChild < getChildCount(); currentChild++) {
            final View v = getChildAt(currentChild);

            LayoutParams lp = (LayoutParams) v.getLayoutParams();

            int colIndex = currentChild % mNumberOfColumns;
            int rowIndex = currentChild / mNumberOfColumns;

            lp.x = (colIndex * imageViewSize) + (colIndex * mContentPadding) + getPaddingLeft();
            lp.y = (rowIndex * imageViewSize) + (rowIndex * mContentPadding) + getPaddingTop();
            lp.width = lp.height = imageViewSize;

            v.measure(MeasureSpec.makeMeasureSpec(imageViewSize, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(imageViewSize, MeasureSpec.EXACTLY));
        }

        setMeasuredDimension(sizeWidth,
                (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight : layoutHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            final View v = getChildAt(i);

            LayoutParams lp = (LayoutParams) v.getLayoutParams();
            v.layout(lp.x, lp.y, lp.x + lp.width, lp.y + lp.height);
        }
    }

    @Override
    public void addView(View child) {
        throw new UnsupportedOperationException("You cant remove or add Views");
    }

    @Override
    public void addView(View child, int index) {
        throw new UnsupportedOperationException("You cant remove or add Views");
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        throw new UnsupportedOperationException("You cant remove or add Views");
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
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

    public static class LayoutParams extends ViewGroup.LayoutParams {

        public int x;
        public int y;

        public LayoutParams() {
            this(0, 0);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }
    }
}
