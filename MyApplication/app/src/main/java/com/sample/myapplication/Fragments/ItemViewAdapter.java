package com.sample.myapplication.Fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class ItemViewAdapter<T> extends RecyclerViewAdapter<ItemViewHolder, T> {
    abstract protected int getLayoutId();
    abstract protected ItemViewHolder getItemViewHolder(View itemView, Context context);

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        View itemView = LayoutInflater.from(context).inflate(getLayoutId(), parent, false);
        return getItemViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.bind(data.get(position));
    }
}
