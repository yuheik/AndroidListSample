package com.sample.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sample.myapplication.Flickr.FlickrPhoto;
import com.sample.myapplication.Utils.OnSwipeTouchListener;
import com.squareup.picasso.Picasso;


public class ImagerFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    private FlickrPhoto photo;

    public static ImagerFragment newInstance(FlickrPhoto photo) {
        ImagerFragment fragment = new ImagerFragment();
        Bundle         args     = new Bundle();
        args.putSerializable(ARG_PARAM1, photo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            photo = (FlickrPhoto) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.imager_fragment, container, false);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
        Picasso.with(this.getContext()).load(this.photo.getUrl()).into(imageView);

        final Activity parent = this.getActivity();

        LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.imager_fragment);
        linearLayout.setOnTouchListener(new OnSwipeTouchListener(parent) {
            @Override
            public void onSwipeDown() {
                parent.finish();
            }
        });

        ((TextView) rootView.findViewById(R.id.title)).setText(this.photo.getTitle());
        ((TextView) rootView.findViewById(R.id.author)).setText("by " + this.photo.getOwner());

        return rootView;
    }
}
