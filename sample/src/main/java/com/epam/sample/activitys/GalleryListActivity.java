package com.epam.sample.activitys;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.epam.sample.R;
import com.epam.sample.adapters.GalleryListAdapter;
import com.epam.sample.models.DayImages;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import butterknife.InjectView;


public class GalleryListActivity extends BaseActivity {

    @InjectView(R.id.gallery_list_rcv_list)
    RecyclerView imagesRecyclerView;

    private GalleryListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new GalleryListAdapter(mContext);
        imagesRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        imagesRecyclerView.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(0, null, loaderCallbacks);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_gallery_list;
    }

    private LoaderManager.LoaderCallbacks<ArrayList<DayImages>> loaderCallbacks
            = new LoaderManager.LoaderCallbacks<ArrayList<DayImages>>() {

        @Override
        public Loader<ArrayList<DayImages>> onCreateLoader(int id, Bundle args) {
            return new ImagesLoader(mContext);
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<DayImages>> loader, ArrayList<DayImages> data) {
            mAdapter.setList(data);
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<DayImages>> loader) {
            mAdapter.setList(null);
            loader.abandon();
        }
    };

    private static class ImagesLoader extends AsyncTaskLoader<ArrayList<DayImages>> {
        final ForceLoadContentObserver mObserver;

        private ArrayList<DayImages> mDaysImages;

        public ImagesLoader(Context context) {
            super(context);
            mObserver = new ForceLoadContentObserver();
        }

        @Override
        protected void onStartLoading() {
            if (null != mDaysImages) {
                deliverResult(mDaysImages);
            }

            if (takeContentChanged() || mDaysImages == null) {
                forceLoad();
            }
        }

        @Override
        public ArrayList<DayImages> loadInBackground() {
            ContentResolver cr = getContext().getContentResolver();

            Cursor internal = cr.query(MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                    null, null, null,
                    MediaStore.Images.Media.DEFAULT_SORT_ORDER);
            Cursor external = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, null, null,
                    MediaStore.Images.Media.DEFAULT_SORT_ORDER);

            MergeCursor mergeCursor = new MergeCursor(new Cursor[]
                    {
                            internal,
                            external
                    });

            // Ensure the cursor window is filled
            mergeCursor.getCount();
            mergeCursor.registerContentObserver(mObserver);

            HashMap<Long, ArrayList<Uri>> daysImagesMap = new HashMap<>();
            if (mergeCursor.moveToFirst()) {
                final int colIndexUri = mergeCursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                final int colIndexDateAdd = mergeCursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_ADDED);
                final int colIndexDateTaken = mergeCursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN);

                Calendar cal = Calendar.getInstance();
                do {
                    long date = mergeCursor.getLong(colIndexDateTaken);
                    if (date < 0) {
                        // Invalid taken date, try to get created date
                        date = mergeCursor.getLong(colIndexDateAdd);
                        if (date < 0) {
                            // Set date to Zero if not valid
                            date = 0;
                        }
                    }

                    // calender have a  shity performance
                    cal.setTimeInMillis(date);
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);

                    final long dayDate = cal.getTimeInMillis();
                    ArrayList<Uri> dayUris = daysImagesMap.get(dayDate);
                    if (null == dayUris) {
                        dayUris = new ArrayList<>();
                        daysImagesMap.put(dayDate, dayUris);
                    }

                    dayUris.add(Uri.parse(mergeCursor.getString(colIndexUri)));

                } while (mergeCursor.moveToNext());
            }
            mergeCursor.close();

            SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM, yyyy", Locale.ENGLISH);

            Set<Map.Entry<Long, ArrayList<Uri>>> entrySet = daysImagesMap.entrySet();
            ArrayList<DayImages> daysImages = new ArrayList<>(entrySet.size());

            for (Map.Entry<Long, ArrayList<Uri>> entry : entrySet) {
                DayImages dayImages = new DayImages();
                dayImages.setDayDate(
                        dateFormat.format(
                                new Date(entry.getKey())
                        )
                );
                dayImages.setUris(entry.getValue());

                daysImages.add(dayImages);
            }

            return daysImages;
        }

        @Override
        public void deliverResult(ArrayList<DayImages> mergeCursor) {
            if (isReset()) {
                return;
            }

            mDaysImages = mergeCursor;
            if (isStarted()) {
                super.deliverResult(mergeCursor);
            }
        }

        @Override
        protected void onReset() {
            super.onReset();

            // Ensure the loader is stopped
            onStopLoading();

            mDaysImages = null;
        }

        @Override
        protected void onStopLoading() {
            // Attempt to cancel the current load task if possible.
            cancelLoad();
        }
    }
}
