package com.sample.myapplication.Fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class ItemViewAdapter<T> extends RecyclerViewAdapter<ItemViewHolder, T> {
    private int listLayoutId;
    private int gridLayoutId;
    private BaseFragment.DisplayType displayType;

    abstract protected ItemViewHolder getItemViewHolder(View itemView, Context context);

    public ItemViewAdapter(int listLayoutId, int gridLayoutId) {
        this.listLayoutId = listLayoutId;
        this.gridLayoutId = gridLayoutId;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        View itemView = LayoutInflater.from(context)
                                      .inflate(getLayoutId(), parent, false);
        return getItemViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.bind(data.get(position), position);
    }

    public void setDisplayType(BaseFragment.DisplayType displayType) {
        this.displayType = displayType;
    }

    private int getLayoutId() {
        return (this.displayType == BaseFragment.DisplayType.GRID)
               ? this.gridLayoutId : this.listLayoutId;
    }
}
