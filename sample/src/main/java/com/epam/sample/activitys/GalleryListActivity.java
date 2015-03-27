package com.epam.sample.activitys;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.epam.sample.R;
import com.epam.sample.adapters.GalleryListAdapter;

import butterknife.InjectView;


public class GalleryListActivity extends BaseActivity {

    @InjectView(R.id.gallery_list_rcv_list)
    RecyclerView imagesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imagesRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        imagesRecyclerView.setAdapter(new GalleryListAdapter(mContext));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_gallery_list;
    }
}
