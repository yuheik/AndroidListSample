package com.sample.myapplication;

import android.support.annotation.Nullable;

import com.sample.myapplication.Fragments.GridFragment;
import com.sample.myapplication.Fragments.RecyclerViewAdapter;

import java.util.ArrayList;

public class RecentFragment extends GridFragment {
    private int page = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.recent_fragment;
    }

    @Override
    protected RecyclerViewAdapter getRecyclerViewAdapter() {
        return new GridItemAdapter(FlickrManager.Type.RECENT);
    }

    @Override
    protected boolean useSwipeRefresh() {
        return true;
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

    @Override
    protected void refreshData() {
        page = 1;
        loadRecentData(page);
    }

    private void loadRecentData(final int pageIndex) {
        startDataLoading();
        FlickrManager.recent(pageIndex, new FlickrManager.PhotosListener() {
            @Override
            public void get(@Nullable ArrayList<FlickrManager.Photo> photos) {
                if (pageIndex == 1) {
                    recyclerView.scrollToPosition(0);
                }
                recyclerViewAdapter.setData(photos);
                finishDataLoading();
            }
        });
    }
}
