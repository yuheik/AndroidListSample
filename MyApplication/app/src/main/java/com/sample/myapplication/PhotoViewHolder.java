package com.sample.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sample.myapplication.Fragments.ItemViewHolder;
import com.sample.myapplication.Utils.LogUtil;

public class PhotoViewHolder extends ItemViewHolder<FlickrManager.Photo> {
    private FlickrManager.Type type;

    private ImageView image;
    private TextView title;
    private TextView subTitle;
    private TextView description;
    private TextView date;
    private TextView rankingItemOrder;

    public PhotoViewHolder(View itemView, Context context, FlickrManager.Type type) {
        super(itemView, context);

        this.type = type;

        this.image = (ImageView) itemView.findViewById(R.id.item_image);
        this.title = (TextView) itemView.findViewById(R.id.item_title);
        this.subTitle = (TextView) itemView.findViewById(R.id.item_sub_title);
        this.description = (TextView) itemView.findViewById(R.id.item_description);
        this.date = (TextView) itemView.findViewById(R.id.item_date);
        this.rankingItemOrder = (TextView) itemView.findViewById(R.id.ranking_item_order);
    }

    @Override
    public void bind(final FlickrManager.Photo photo) {
        setImage(image, photo.getUrl());
        setText(title, photo.getTitle());
        setText(subTitle, photo.getOwner());
        setText(description, photo.getSecret());
        setText(date, photo.getUrl());

        setClickListener(image, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtil.debug(photo.getTitle());
                Intent intent = new Intent(context, ViewerActivity.class);
                intent.setData(Uri.parse(photo.getUrl()));
                intent.putExtra(ViewerActivity.FLICKER_ACTION_TYPE, type);
                context.startActivity(intent);
            }
        });
    }
}
