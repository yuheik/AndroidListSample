package com.sample.myapplication;

import android.content.Context;
import android.view.View;

import com.sample.myapplication.Fragments.ItemViewAdapter;
import com.sample.myapplication.Fragments.ItemViewHolder;

public class PhotoItemViewAdapter extends ItemViewAdapter<FlickrManager.Photo> {
    private FlickrManager.Type type;

    public PhotoItemViewAdapter(FlickrManager.Type type,
                                int listLayoutId,
                                int gridLayoutId) {
        super(listLayoutId, gridLayoutId);
        this.type = type;
    }

    @Override
    protected ItemViewHolder getItemViewHolder(View itemView, Context context) {
        return new PhotoItemViewHolder(itemView, context, this.type);
    }

    @Override
    protected boolean isDataEqual(FlickrManager.Photo lhs, FlickrManager.Photo rhs) {
        return lhs.getId().equals(rhs.getId());
    }
}
