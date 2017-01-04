package com.sample.myapplication;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sample.myapplication.Fragments.ItemViewHolder;

public class PhotoViewHolder extends ItemViewHolder<FlickrManager.Photo> {
    private ImageView image;
    private TextView title;
    private TextView subTitle;
    private TextView description;
    private TextView date;
    private TextView rankingItemOrder;

    public PhotoViewHolder(View itemView, Context context) {
        super(itemView, context);

        this.image = (ImageView) itemView.findViewById(R.id.item_image);
        this.title = (TextView) itemView.findViewById(R.id.item_title);
        this.subTitle = (TextView) itemView.findViewById(R.id.item_sub_title);
        this.description = (TextView) itemView.findViewById(R.id.item_description);
        this.date = (TextView) itemView.findViewById(R.id.item_date);
        this.rankingItemOrder = (TextView) itemView.findViewById(R.id.ranking_item_order);
    }

    @Override
    public void bind(FlickrManager.Photo photo) {
        setImage(image, photo.getUrl());
        setText(title, photo.getTitle());
        setText(subTitle, photo.getOwner());
        setText(description, photo.getSecret());
        setText(date, photo.getUrl());
    }
}
