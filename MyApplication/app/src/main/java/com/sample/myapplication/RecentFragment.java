package com.sample.myapplication;

import android.support.annotation.Nullable;

import com.sample.myapplication.Fragments.GridFragment;
import com.sample.myapplication.Fragments.RecyclerViewAdapter;

public class RecentFragment extends GridFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.grid;
    }

    @Override
    protected RecyclerViewAdapter getRecyclerViewAdapter() {
        return new GridItemAdapter();
    }

    @Override
    protected void setData() {
        FlickrManager.recent(new FlickrManager.PhotosListener() {
            @Override
            public void get(@Nullable FlickrManager.Photos photos) {
                recyclerViewAdapter.setData(photos.getPhotos());
            }
        });
    }
}
