package com.sample.myapplication;

import android.content.Context;
import android.view.View;

import com.sample.myapplication.Fragments.ItemViewAdapter;
import com.sample.myapplication.Fragments.ItemViewHolder;

public class GridItemAdapter extends ItemViewAdapter<FlickrManager.Photo> {
    private FlickrManager.Type type;

    public GridItemAdapter(FlickrManager.Type type) {
        super();
        this.type = type;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.grid_item;
    }

    @Override
    protected ItemViewHolder getItemViewHolder(View itemView, Context context) {
        return new PhotoViewHolder(itemView, context, this.type);
    }

    @Override
    protected boolean isDataEqual(FlickrManager.Photo lhs, FlickrManager.Photo rhs) {
        return lhs.getId().equals(rhs.getId());
    }
}
