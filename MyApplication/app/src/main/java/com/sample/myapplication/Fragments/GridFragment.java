package com.sample.myapplication.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sample.myapplication.FlickrManager;
import com.sample.myapplication.PhotoViewHolder;
import com.sample.myapplication.R;
import com.sample.myapplication.Utils.LogUtil;

public class GridFragment extends Fragment {
    GridAdapter gridAdapter;

    public static GridFragment newInstance() {
        GridFragment fragment = new GridFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        gridAdapter = new GridAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.grid, container, false);
        RecyclerView recyclerView = setupRecyclerView(rootView);

        return rootView;
    }


    private RecyclerView setupRecyclerView(View rootView) {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(gridAdapter);

        return recyclerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        gridAdapter.setActivity(this.getActivity());
        setData();
    }

    private void setData() {
        FlickrManager.search("Android", new FlickrManager.PhotosListener() {
            @Override
            public void get(@Nullable FlickrManager.Photos photos) {
                LogUtil.debug("photos.size : " + photos.getPhotos().size());
                gridAdapter.setData(photos.getPhotos());
            }
        });
    }

    class GridAdapter extends RecyclerViewAdapter<ItemViewHolder, FlickrManager.Photo> {
        Context context;

        @Override
        protected boolean isDataEqual(FlickrManager.Photo lhs, FlickrManager.Photo rhs) {
            return lhs.getId().equals(rhs.getId());
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            context = parent.getContext();

            View itemView = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
            return new PhotoViewHolder(itemView, getContext());
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            LogUtil.debug(data.get(position).getTitle() + " " + data.get(position).getUrl());
            holder.bind(data.get(position));
        }
    }
}
