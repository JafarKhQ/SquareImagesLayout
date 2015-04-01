package com.epam.sample.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epam.sample.R;
import com.epam.sample.models.DayImages;
import com.epam.widget.SquareImagesLayout;
import com.epam.widget.SquareImagesView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

public class GalleryListAdapter extends RecyclerView.Adapter<GalleryListAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<DayImages> mDaysImages;

    private static final boolean USE_LAYOUT = false;

    public GalleryListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
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
        int rowNum = (int) Math.ceil(Math.sqrt(imagesNum));

        viewHolder.txvTitle.setText(dayImages.getDayDate());

        if (null != viewHolder.silImages) {
            viewHolder.silImages.setNumberOfRows(rowNum);
            viewHolder.silImages.setNumberOfColumns(colNum);
        } else {
            viewHolder.sivImages.setNumberOfRows(rowNum);
            viewHolder.sivImages.setNumberOfColumns(colNum);
        }

        if (null != viewHolder.silImages) {
            for (int x = 0; x < imagesNum && x < viewHolder.silImages.getChildCount(); x++) {
                viewHolder.silImages.getChildAt(x).setImageURI(dayImages.getUriAt(x));
            }
        } else {
            Uri[] array = new Uri[imagesNum];
            dayImages.getUris().toArray(array);
            viewHolder.sivImages.addImage(array);
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
        SquareImagesView sivImages;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.inject(this, itemView);
        }
    }
}
