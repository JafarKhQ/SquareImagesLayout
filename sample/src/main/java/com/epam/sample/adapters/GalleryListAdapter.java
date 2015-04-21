package com.epam.sample.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epam.sample.R;
import com.epam.sample.models.DayImages;
import com.epam.sample.widget.MySquareImagesView;
import com.epam.widget.SquareImagesLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

public class GalleryListAdapter extends RecyclerView.Adapter<GalleryListAdapter.ViewHolder> {

    private int mScreenWidth;
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<DayImages> mDaysImages;

    private static final boolean USE_LAYOUT = true;

    public GalleryListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;

//        Picasso.with(mContext).setLoggingEnabled(true);
    }

    public void setList(ArrayList<DayImages> mergeCursor) {
        if (mergeCursor == mDaysImages) {
            return;
        }

        mDaysImages = mergeCursor;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (null == mDaysImages) {
            return 0;
        }

        return mDaysImages.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;
        if (USE_LAYOUT) {
            v = mInflater.inflate(R.layout.list_item_gallery_list_layout, viewGroup, false);
        } else {
            v = mInflater.inflate(R.layout.list_item_gallery_list_view, viewGroup, false);
        }

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final DayImages dayImages = mDaysImages.get(i);
        int imagesNum = dayImages.getUriCount();

        int colNum = (int) Math.ceil(Math.sqrt(imagesNum));
        int rowNum = (int) Math.floor(Math.sqrt(imagesNum));
        while (colNum * rowNum < imagesNum) {
            rowNum++;
        }

        viewHolder.txvTitle.setText(dayImages.getDayDate() + " - (" + imagesNum + ")");

        if (null != viewHolder.silImages) {
            viewHolder.silImages.setNumberOfRows(rowNum);
            viewHolder.silImages.setNumberOfColumns(colNum);

            int contentPadding = viewHolder.silImages.getContentPadding() * (colNum - 1);
            int viewPadding = viewHolder.silImages.getPaddingLeft() + viewHolder.silImages.getPaddingRight();
            int availableWidth = mScreenWidth - (contentPadding + viewPadding);
            int imageSize = availableWidth / colNum;

            for (int k = 0; k < imagesNum && k < viewHolder.silImages.getChildCount(); k++) {
                Picasso.with(mContext)
                        .load("file://" + dayImages.getUriAt(k).toString())
                        .resize(imageSize, imageSize)
                        .centerCrop()
                        .into(viewHolder.silImages.getChildAt(k));
            }
        } else {
            viewHolder.sivImages.setTag(i);
            viewHolder.sivImages.clearImages(false);

            viewHolder.sivImages.setNumberOfRows(rowNum);
            viewHolder.sivImages.setNumberOfColumns(colNum);

            int contentPadding = viewHolder.sivImages.getContentPadding() * (colNum - 1);
            int viewPadding = viewHolder.sivImages.getPaddingLeft() + viewHolder.sivImages.getPaddingRight();
            int availableWidth = mScreenWidth - (contentPadding + viewPadding);
            int imageSize = availableWidth / colNum;

            for (int k = 0; k < dayImages.getUriCount(); k++) {
                Picasso.with(mContext)
                        .load("file://" + dayImages.getUriAt(k).toString())
                        .resize(imageSize, imageSize)
                        .centerCrop()
                        .into(viewHolder.sivImages);
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.gallery_list_txv_title)
        TextView txvTitle;
        @Optional
        @InjectView(R.id.gallery_list_sil_images)
        SquareImagesLayout silImages;
        @Optional
        @InjectView(R.id.gallery_list_siv_images)
        MySquareImagesView sivImages;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.inject(this, itemView);
        }
    }
}
