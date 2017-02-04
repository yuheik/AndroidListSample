package com.sample.myapplication;

import android.support.annotation.Nullable;

import com.sample.myapplication.Fragments.GridFragment;
import com.sample.myapplication.Fragments.RecyclerViewAdapter;

import java.util.ArrayList;

public class RecentFragment extends GridFragment {
    private int page = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.grid;
    }

    @Override
    protected RecyclerViewAdapter getRecyclerViewAdapter() {
        return new GridItemAdapter(FlickrManager.Type.RECENT);
    }

    @Override
    protected void setData() {
        page = 1;
        loadRecentData(page);
    }

    @Override
    protected void loadNextData() {
        // note: this implementation is hard to find out whether if it reaches the end.
        page++;
        loadRecentData(page);
    }

    private void loadRecentData(int pageIndex) {
        startDataLoading();
        FlickrManager.recent(pageIndex, new FlickrManager.PhotosListener() {
            @Override
            public void get(@Nullable ArrayList<FlickrManager.Photo> photos) {
                recyclerViewAdapter.setData(photos);
                finishDataLoading();
            }
        });
    }
}
