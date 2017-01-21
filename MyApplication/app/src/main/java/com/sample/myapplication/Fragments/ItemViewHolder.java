package com.sample.myapplication.Fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public abstract class ItemViewHolder<T> extends RecyclerView.ViewHolder {
    protected Context context;

    public ItemViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
    }

    public abstract void bind(T t, int position);

    protected void setImage(ImageView view, String url) {
        if (view != null) {
            Picasso.with(context).load(url).into(view);
        }
    }

    protected void setText(TextView view, String text) {
        if (view != null) {
            view.setText(text);
        }
    }

    protected void setClickListener(View view, View.OnClickListener clickListener) {
        if (view != null) {
            view.setOnClickListener(clickListener);
        }
    }
}
