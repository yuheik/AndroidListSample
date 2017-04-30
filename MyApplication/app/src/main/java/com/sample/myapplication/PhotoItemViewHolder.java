package com.sample.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sample.myapplication.Flickr.FlickrManager;
import com.sample.myapplication.Flickr.FlickrPhoto;
import com.sample.myapplication.Fragments.ItemViewHolder;
import com.sample.myapplication.Utils.LogUtil;

public class PhotoItemViewHolder extends ItemViewHolder<FlickrPhoto> {
    private FlickrManager.Type type;

    private ImageView image;
    private TextView  title;
    private TextView  subTitle;
    private TextView  description;
    private TextView  date;
    private TextView  rankingItemOrder;

    public PhotoItemViewHolder(View itemView, Context context, FlickrManager.Type type) {
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
    public void bind(final FlickrPhoto photo, final int position) {
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
                intent.putExtra(ViewerActivity.FLICKER_ACTION_TYPE, type);
                intent.putExtra(ViewerActivity.FLICKER_DATA_POSITION, position);

                context.startActivity(intent);
            }
        });
    }
}
