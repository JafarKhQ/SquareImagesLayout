package com.epam.sample.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epam.sample.R;
import com.epam.sample.models.DayImages;
import com.epam.widget.SquareImagesLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GalleryListAdapter extends RecyclerView.Adapter<GalleryListAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<DayImages> mDaysImages;

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
        View v = mInflater.inflate(R.layout.list_item_gallery_list, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final DayImages dayImages = mDaysImages.get(i);
        int imagesNum = dayImages.getUriCount();

        int colNum = (int) Math.ceil(Math.sqrt(imagesNum));
        int rowNum = (int) Math.ceil(Math.sqrt(imagesNum));

        viewHolder.txvTitle.setText(dayImages.getDayDate());

        viewHolder.silImages.setNumberOfRows(rowNum);
        viewHolder.silImages.setNumberOfColumns(colNum);

        for (int x = 0; x < imagesNum && x < viewHolder.silImages.getChildCount(); x++) {
            //Picasso.with(mContext)
                   // .load(dayImages.getUriAt(x))
                            //.resize(viewHolder.silImages.getImageSize(), viewHolder.silImages.getImageSize())
                  //  .into(viewHolder.silImages.getChildAt(x));
            viewHolder.silImages.getChildAt(x).setImageURI(dayImages.getUriAt(x));
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.gallery_list_txv_title)
        TextView txvTitle;
        @InjectView(R.id.gallery_list_sil_images)
        SquareImagesLayout silImages;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.inject(this, itemView);
        }
    }
}
