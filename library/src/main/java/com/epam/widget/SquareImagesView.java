package com.epam.widget;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Random;

public class SquareImagesView extends View implements SquareImages {
    private static final String TAG = "SquareImagesView";

    private static final int NONE = Integer.MIN_VALUE;

    private static final int MIN_NUMBER_OF_ROWS = 1;
    private static final int MIN_NUMBER_OF_COLUMNS = 1;

    private static final int DEFAULT_CONTENT_PADDING = 0;
    private static final int DEFAULT_NUMBER_OF_ROWS = 1;
    private static final int DEFAULT_NUMBER_OF_COLUMNS = 1;

    private int mImageSize;
    private int mNumberOfRows;
    private int mNumberOfColumns;
    private int mContentPadding;

    private Paint mPaint;

    private Path mPathNull;
    private Paint mPaintNullFG;
    private Paint mPaintNullBG;
    private ArrayList<SoftReference<Drawable>> mDrawables;

    public SquareImagesView(Context context) {
        this(context, null);
    }

    public SquareImagesView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareImagesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.SquareImagesView,
                defStyleAttr, 0);

        try {
            setNumberOfRows(a.getInteger(R.styleable.SquareImagesView_numRows,
                    DEFAULT_NUMBER_OF_ROWS));
            setNumberOfColumns(a.getInteger(R.styleable.SquareImagesView_numColumns,
                    DEFAULT_NUMBER_OF_COLUMNS));
            setContentPadding(a.getDimensionPixelSize(R.styleable.SquareImagesView_contentPadding,
                    DEFAULT_CONTENT_PADDING));
        } finally {
            a.recycle();
        }

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);

        mPaintNullFG = new Paint();
        mPaintNullFG.setStyle(Paint.Style.STROKE);
        mPaintNullFG.setColor(Color.BLACK);
        mPaintNullFG.setStrokeWidth(20.0f);

        mPaintNullBG = new Paint(mPaint);
        mPaintNullBG.setColor(Color.WHITE);

        mPathNull = new Path();

        mDrawables = new ArrayList<>(getMaxImagesNumber());
    }

    @Override
    public int getImageSize() {
        return mImageSize;
    }

    @Override
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
        requestLayout();
        invalidate();
    }

    @Override
    public int getNumberOfRows() {
        return mNumberOfRows;
    }

    @Override
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
        requestLayout();
        invalidate();
    }

    @Override
    public int getNumberOfColumns() {
        return mNumberOfColumns;
    }

    @Override
    public void setContentPadding(final int contentPadding) {
        if (contentPadding == mContentPadding) {
            return;
        }

        mContentPadding = contentPadding;
        requestLayout();
        invalidate();
    }

    @Override
    public int getContentPadding() {
        return mContentPadding;
    }

    public int getMaxImagesNumber() {
        return mNumberOfColumns * mNumberOfRows;
    }

    public int getImagesCount() {
        return mDrawables.size();
    }

    public void addImage(Drawable... drawables) {
        if (null == drawables) {
            Log.w(TAG, "addImage: null drawable");
            return;
        }

        int i = 0;
        for (; i < drawables.length - 1; i++) {
            addImageInternal(drawables[i], false);
        }

        addImageInternal(drawables[i], true);
    }

    public void addImage(Bitmap... bitmaps) {
        if (null == bitmaps) {
            Log.w(TAG, "addImage: null bitmap");
            return;
        }

        int i = 0;
        for (; i < bitmaps.length - 1; i++) {
            addImageInternal(bitmaps[i], false);
        }

        addImageInternal(bitmaps[i], true);
    }

    public void addImage(int... resIds) {
        if (null == resIds) {
            Log.w(TAG, "addImage: null resId");
            return;
        }

        int i = 0;
        for (; i < resIds.length - 1; i++) {
            addImageInternal(resIds[i], false);
        }

        addImageInternal(resIds[i], true);
    }

    public void addImage(Uri... uris) {
        if (null == uris) {
            Log.w(TAG, "addImage: null uri");
            return;
        }

        int i = 0;
        for (; i < uris.length - 1; i++) {
            addImageInternal(uris[i], false);
        }

        addImageInternal(uris[i], true);
    }

    public void clearImages() {
        clearImages(true);
    }

    public void clearImages(boolean invalidate) {
        mDrawables.clear();

        if (invalidate) {
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        final int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        final int availableContentWidth = sizeWidth - (getPaddingLeft() + getPaddingRight());

        final int totalHorizontalContentPadding = mContentPadding * (mNumberOfColumns - 1);
        final int totalVerticalContentPadding = mContentPadding * (mNumberOfRows - 1);

        mImageSize = (availableContentWidth - totalHorizontalContentPadding) / mNumberOfColumns;
        final int viewHeight = (mImageSize * mNumberOfRows)
                + getPaddingTop() + getPaddingBottom()
                + totalVerticalContentPadding;

        setMeasuredDimension(sizeWidth,
                (MeasureSpec.EXACTLY == modeHeight) ? sizeHeight : viewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int i = 0;
        int imagesCount = getImagesCount();
        if (imagesCount > getMaxImagesNumber()) {
            imagesCount = getMaxImagesNumber();
        }

        for (; i < imagesCount; i++) {
            final Drawable d = mDrawables.get(i).get();
            if (null != d) {
                drawDrawable(d, canvas, i);
            } else {
                Log.w(TAG, "Image number " + i + " is NULL");
                drawNullImage(canvas, i);
            }
        }

        imagesCount = getMaxImagesNumber();
        for (; i < imagesCount; i++) {
            drawRandomColor(canvas, i);
        }
    }

    private void drawDrawable(Drawable d, Canvas canvas, int position) {
        int colIndex = position % mNumberOfColumns;
        int rowIndex = position / mNumberOfColumns;

        int x = (colIndex * mImageSize) + (colIndex * mContentPadding) + getPaddingLeft();
        int y = (rowIndex * mImageSize) + (rowIndex * mContentPadding) + getPaddingTop();

        d.setBounds(x, y, x + mImageSize, y + mImageSize);
        d.draw(canvas);
    }

    private void drawRandomColor(Canvas canvas, int position) {
        drawColor(getRandomBgColor(), canvas, position);
    }

    private void drawNullImage(Canvas canvas, int position) {
        int colIndex = position % mNumberOfColumns;
        int rowIndex = position / mNumberOfColumns;

        int x = (colIndex * mImageSize) + (colIndex * mContentPadding) + getPaddingLeft();
        int y = (rowIndex * mImageSize) + (rowIndex * mContentPadding) + getPaddingTop();

        mPathNull.reset();
        mPathNull.moveTo(x, y);
        mPathNull.lineTo(x + mImageSize, y + mImageSize);
        mPathNull.moveTo(x + mImageSize, y);
        mPathNull.lineTo(x, y + mImageSize);

        canvas.drawRect(x, y, x + mImageSize, y + mImageSize, mPaintNullBG);
        canvas.drawPath(mPathNull, mPaintNullFG);
    }

    private void drawColor(int color, Canvas canvas, int position) {
        mPaint.setColor(color);

        int colIndex = position % mNumberOfColumns;
        int rowIndex = position / mNumberOfColumns;

        int x = (colIndex * mImageSize) + (colIndex * mContentPadding) + getPaddingLeft();
        int y = (rowIndex * mImageSize) + (rowIndex * mContentPadding) + getPaddingTop();

        canvas.drawRect(x, y, x + mImageSize, y + mImageSize, mPaint);
    }

    private void addImageInternal(Bitmap bitmap, boolean invalidate) {
        addImageInternal(new BitmapDrawable(getResources(), bitmap), invalidate);
    }

    private void addImageInternal(int resId, boolean invalidate) {
        addImageInternal(getResources().getDrawable(resId), invalidate);
    }

    private void addImageInternal(Uri uri, boolean invalidate) {
        Drawable d = null;
        String scheme = uri.getScheme();

        if (ContentResolver.SCHEME_ANDROID_RESOURCE.equals(scheme)) {
            // Load drawable through Resources, to get the source density information
            throw new UnsupportedOperationException("Resource URI not supported");
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)
                || ContentResolver.SCHEME_FILE.equals(scheme)) {
            InputStream stream = null;
            try {
                stream = getContext().getContentResolver().openInputStream(uri);
                d = Drawable.createFromStream(stream, null);
            } catch (Exception e) {
                Log.w("ImageView", "Unable to open content: " + uri, e);
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                    }
                }
            }
        } else {
            d = Drawable.createFromPath(uri.toString());
        }

        addImageInternal(d, invalidate);
    }

    private void addImageInternal(Drawable drawable, boolean invalidate) {
        mDrawables.add(new SoftReference<>(drawable));

        if (invalidate) {
            invalidate();
        }
    }

    private int getRandomBgColor() {
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);

        return Color.rgb(r, g, b);
    }
}
