package com.epam.sample.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epam.sample.R;
import com.epam.widget.SquareImagesLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GalleryListAdapter extends RecyclerView.Adapter<GalleryListAdapter.ViewHolder> {

    private LayoutInflater mInflater;

    public GalleryListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = mInflater.inflate(R.layout.list_item_gallery_list, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

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
