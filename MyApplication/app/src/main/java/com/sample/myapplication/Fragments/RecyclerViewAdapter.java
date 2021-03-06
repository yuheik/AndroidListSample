package com.sample.myapplication.Fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class RecyclerViewAdapter<T extends RecyclerView.ViewHolder, E> extends RecyclerView.Adapter<T> {

    protected List<E> data;

    protected FragmentActivity activity;
    protected int              resourceIdx;
    protected RecyclerView     recyclerView;
    protected View             emptyView;

    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    private void setVisibility(View view, int visible) {
        if (view != null) { view.setVisibility(visible); }
    }
    private void show(View view) { setVisibility(view, View.VISIBLE); }
    private void hide(View view) { setVisibility(view, View.GONE);    }

    /**
     * Set adapter data.
     * @param data @Note. Be sure not to pass the reference of original.
     */
    public void setData(@Nullable List<E> data) {
        if (data == null) {
            hide(recyclerView);
            hide(emptyView);
        } else if (data.size() == 0) {
            hide(recyclerView);
            show(emptyView);
        } else {
            show(recyclerView);
            hide(emptyView);
        }

        if (this.data == null || data == null) {
            this.data = data;
            notifyDataSetChanged();
            return;
        }

        int i = 0;
        while (i < this.data.size() || i < data.size()) {
            if (i >= this.data.size()) { // old items are shorter. now append new items at the end
                this.data.add(data.get(i));
                notifyItemInserted(i);
                i++;
            } else if (i >= data.size()) { // old items are longer. now remove left old items
                this.data.remove(i);
                notifyItemRemoved(i);
            } else {
                E oldItem = this.data.get(i);
                E newItem = data.get(i);
                if (!isDataEqual(oldItem, newItem)) {
                    this.data.set(i, newItem);
                    notifyItemChanged(i);
                }
                i++;
            }
        }
    }

    public void removeItem(E item) {
        if (data == null) {
            return;
        }
        for (int i = 0; i < data.size(); i++) {
            if (isDataEqual(data.get(i), item)) {
                data.remove(i);
                notifyItemRemoved(i);
                if (recyclerView != null && emptyView != null && data.size() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
                return;
            }
        }
    }

    protected abstract boolean isDataEqual(E lhs, E rhs);

    @Override
    public abstract T onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(T holder, int position);

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = null;
    }
}
