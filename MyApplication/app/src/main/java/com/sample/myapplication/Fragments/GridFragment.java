package com.sample.myapplication.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sample.myapplication.FlickrManager;
import com.sample.myapplication.GridItemAdapter;
import com.sample.myapplication.R;

public class GridFragment extends BaseFragment {

    public static GridFragment newInstance() {
        GridFragment fragment = new GridFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected RecyclerViewAdapter getRecyclerViewAdapter() {
        return new GridItemAdapter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.grid;
    }

    @Override
    protected boolean isListView() {
        return false;
    }

    protected void setData() {
        FlickrManager.search("Android", new FlickrManager.PhotosListener() {
            @Override
            public void get(@Nullable FlickrManager.Photos photos) {
                recyclerViewAdapter.setData(photos.getPhotos());
            }
        });
    }
}
