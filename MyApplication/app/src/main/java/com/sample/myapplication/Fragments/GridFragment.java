package com.sample.myapplication.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sample.myapplication.FlickrManager;
import com.sample.myapplication.PhotoViewHolder;
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
        return new GridAdapter();
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

    class GridAdapter extends RecyclerViewAdapter<ItemViewHolder, FlickrManager.Photo> {
        Context context;

        @Override
        protected boolean isDataEqual(FlickrManager.Photo lhs, FlickrManager.Photo rhs) {
            return lhs.getId().equals(rhs.getId());
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            context = parent.getContext();

            View itemView = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
            return new PhotoViewHolder(itemView, getContext());
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            holder.bind(data.get(position));
        }
    }
}
