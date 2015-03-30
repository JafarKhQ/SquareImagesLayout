package com.epam.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
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

    private int mImageSize;
    private int mNumberOfRows;
    private int mNumberOfColumns;
    private int mContentPadding;
    private ImageView.ScaleType mScaleType;

    private boolean mFinishCreating;

    public SquareImagesLayout(Context context) {
        this(context, null);
    }

    public SquareImagesLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareImagesLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setSaveEnabled(true);

        final TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.SquareImagesLayout,
                defStyleAttr, 0);

        try {
            final int scaleType = a.getInt(R.styleable.SquareImagesLayout_android_scaleType, NONE);
            if (NONE == scaleType || scaleType < 0 || scaleType >= SCALE_TYPES.length) {
                setScaleType(DEFAULT_SCALE_TYPE);
            } else {
                setScaleType(SCALE_TYPES[scaleType]);
            }

            setNumberOfRows(a.getInteger(R.styleable.SquareImagesLayout_numRows,
                    DEFAULT_NUMBER_OF_ROWS));
            setNumberOfColumns(a.getInteger(R.styleable.SquareImagesLayout_numColumns,
                    DEFAULT_NUMBER_OF_COLUMNS));
            setContentPadding(a.getDimensionPixelSize(R.styleable.SquareImagesLayout_contentPadding,
                    DEFAULT_CONTENT_PADDING));
        } finally {
            a.recycle();
        }

        mFinishCreating = true;
        prepareChildViews();
    }

    public int getImageSize() {
        return mImageSize;
    }

    public void setNumberOfRows(final int numberOfRows) {
        if (numberOfRows < MIN_NUMBER_OF_ROWS) {
            throw new IllegalArgumentException("Number Of Rows ("
                    + numberOfRows
                    + ") cant be less than " + MIN_NUMBER_OF_ROWS);
        }

        if (numberOfRows == mNumberOfRows) {
            return;
        }

        mNumberOfRows = numberOfRows;
        prepareChildViews();
    }

    public int getNumberOfRows() {
        return mNumberOfRows;
    }

    public void setNumberOfColumns(final int numberOfColumns) {
        if (numberOfColumns < MIN_NUMBER_OF_COLUMNS) {
            throw new IllegalArgumentException("Number Of Columns ("
                    + numberOfColumns
                    + ") cant be less than " + MIN_NUMBER_OF_COLUMNS);
        }

        if (numberOfColumns == mNumberOfColumns) {
            return;
        }

        mNumberOfColumns = numberOfColumns;
        prepareChildViews();
    }

    public int getNumberOfColumns() {
        return mNumberOfColumns;
    }

    public void setContentPadding(final int contentPadding) {
        if (contentPadding == mContentPadding) {
            return;
        }

        mContentPadding = contentPadding;

        requestLayout();
        invalidate();
    }

    public int getContentPadding() {
        return mContentPadding;
    }

    public void setScaleType(ImageView.ScaleType scaleType) {
        if (scaleType == null) {
            throw new NullPointerException();
        }

        if (scaleType != mScaleType) {
            mScaleType = scaleType;

            final int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                getChildAt(i).setScaleType(mScaleType);
            }
        }
    }

    public ImageView.ScaleType getScaleType() {
        return mScaleType;
    }

    public void setImages(int[] resIds) {
        if (null == resIds || resIds.length < 1) {
            Log.w(TAG, "setImagesResource: empty array of ids");
            return;
        }

        final int childCount = getChildCount();
        for (int i = 0; i < childCount && i < resIds.length; i++) {
            getChildAt(i).setImageResource(resIds[i]);
        }
    }

    public void setImage(int resId, int index) {
        checkValidImageIndex(index);
        getChildAt(index).setImageResource(resId);
    }

    public void setImages(Drawable[] drawables) {
        if (null == drawables || drawables.length < 1) {
            Log.w(TAG, "setImagesDrawable: empty array of drawables");
            return;
        }

        final int childCount = getChildCount();
        for (int i = 0; i < childCount && i < drawables.length; i++) {
            getChildAt(i).setImageDrawable(drawables[i]);
        }
    }

    public void setImage(Drawable drawable, int index) {
        checkValidImageIndex(index);
        getChildAt(index).setImageDrawable(drawable);
    }

    public void setImages(Bitmap[] bitmaps) {
        if (null == bitmaps || bitmaps.length < 1) {
            Log.w(TAG, "setImagesDrawable: empty array of drawables");
            return;
        }

        final int childCount = getChildCount();
        for (int i = 0; i < childCount && i < bitmaps.length; i++) {
            getChildAt(i).setImageBitmap(bitmaps[i]);
        }
    }

    public void setImage(Bitmap bitmap, int index) {
        checkValidImageIndex(index);
        getChildAt(index).setImageBitmap(bitmap);
    }

    @Override
    public ImageView getChildAt(int index) {
        return (ImageView) super.getChildAt(index);
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

        mImageSize = (availableContentWidth - totalHorizontalContentPadding) / mNumberOfColumns;
        final int layoutHeight = (mImageSize * mNumberOfRows)
                + getPaddingTop() + getPaddingBottom()
                + totalVerticalContentPadding;

        for (int currentChild = 0; currentChild < getChildCount(); currentChild++) {
            final View v = getChildAt(currentChild);

            LayoutParams lp = (LayoutParams) v.getLayoutParams();

            int colIndex = currentChild % mNumberOfColumns;
            int rowIndex = currentChild / mNumberOfColumns;

            lp.x = (colIndex * mImageSize) + (colIndex * mContentPadding) + getPaddingLeft();
            lp.y = (rowIndex * mImageSize) + (rowIndex * mContentPadding) + getPaddingTop();
            lp.width = lp.height = mImageSize;

            v.measure(MeasureSpec.makeMeasureSpec(mImageSize, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(mImageSize, MeasureSpec.EXACTLY));
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
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        ss.scaleType = mScaleType;
        ss.numberOfRows = mNumberOfRows;
        ss.contentPadding = mContentPadding;
        ss.numberOfColumns = mNumberOfColumns;

        final int childCount = getChildCount();
        ss.imagesDrawable = new ArrayList<>(childCount);

        for (int i = 0; i < childCount; i++) {
            ImageView imv = getChildAt(i);
            ss.imagesDrawable.add(imv.getDrawable());
        }

        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        setScaleType(ss.scaleType);
        mNumberOfRows = ss.numberOfRows;
        mContentPadding = ss.contentPadding;
        mNumberOfColumns = ss.numberOfColumns;

        prepareChildViews();
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imv = getChildAt(i);
            imv.setImageDrawable(ss.imagesDrawable.get(i));
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

    private void prepareChildViews() {
        if (false == mFinishCreating) {
            return;
        }

        int oldChildCount = getChildCount();
        int targetChildCount = mNumberOfColumns * mNumberOfRows;

        if (targetChildCount == oldChildCount) {
            return;
        }

        // remove children
        while (oldChildCount > targetChildCount) {
            myRemoveViewAt(--oldChildCount);
        }

        // add children
        while (oldChildCount < targetChildCount) {
            LayoutParams lp = new LayoutParams();
            ColorDrawable drawable = new ColorDrawable(getRandomBgColor());

            ImageView imv = new ImageView(getContext());
            imv.setLayoutParams(lp);
            imv.setScaleType(mScaleType);
            imv.setImageDrawable(drawable);

            myAddView(imv);
            oldChildCount++;
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

    private void myRemoveViewAt(int index) {
        super.removeViewAt(index);
    }

    private void checkValidImageIndex(int index) {
        if (false == isValidImageIndex(index)) {
            throw new IndexOutOfBoundsException("Image index ("
                    + index
                    + ") cant exceed the number of Images");
        }
    }

    private boolean isValidImageIndex(int index) {
        int imagesCount = mNumberOfRows * mNumberOfColumns;
        return (index > 0) && (index < imagesCount);
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

    private static class SavedState extends BaseSavedState {

        int numberOfRows;
        int numberOfColumns;
        int contentPadding;
        ImageView.ScaleType scaleType;

        ArrayList<Drawable> imagesDrawable;

        public SavedState(Parcel source) {
            super(source);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }
    }
}
