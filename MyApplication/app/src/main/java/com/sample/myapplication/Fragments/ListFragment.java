package com.sample.myapplication.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sample.myapplication.FlickrManager;
import com.sample.myapplication.ListItemAdapter;
import com.sample.myapplication.R;

public class ListFragment extends BaseFragment {

    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected RecyclerViewAdapter getRecyclerViewAdapter() {
        return new ListItemAdapter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.list;
    }

    @Override
    protected boolean isListView() {
        return true;
    }

    protected void setData() {
        FlickrManager.search("Apple", new FlickrManager.PhotosListener() {
            @Override
            public void get(@Nullable FlickrManager.Photos photos) {
                recyclerViewAdapter.setData(photos.getPhotos());
            }
        });
    }
}
