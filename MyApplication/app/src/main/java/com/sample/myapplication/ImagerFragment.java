package com.sample.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.sample.myapplication.Utils.OnSwipeTouchListener;
import com.squareup.picasso.Picasso;


public class ImagerFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    private String uri;

    public static ImagerFragment newInstance(String param1) {
        ImagerFragment fragment = new ImagerFragment();
        Bundle         args     = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uri = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.imager_fragment, container, false);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
        Picasso.with(this.getContext()).load(this.uri).into(imageView);

        final Activity parent = this.getActivity();

        FrameLayout frameLayout = (FrameLayout) rootView.findViewById(R.id.imager_fragment);
        frameLayout.setOnTouchListener(new OnSwipeTouchListener(parent) {
            @Override
            public void onSwipeDown() {
                parent.finish();
            }
        });

        return rootView;
    }
}
