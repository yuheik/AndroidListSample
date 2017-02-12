package com.sample.myapplication.Fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class ItemViewAdapter<T> extends RecyclerViewAdapter<ItemViewHolder, T> {
    private int listLayoutId;
    private int gridLayoutId;

    abstract protected ItemViewHolder getItemViewHolder(View itemView, Context context);

    public ItemViewAdapter(int listLayoutId, int gridLayoutId) {
        this.listLayoutId = listLayoutId;
        this.gridLayoutId = gridLayoutId;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        View itemView = LayoutInflater.from(context)
                                      .inflate(this.gridLayoutId, parent, false);
        return getItemViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.bind(data.get(position), position);
    }
}
