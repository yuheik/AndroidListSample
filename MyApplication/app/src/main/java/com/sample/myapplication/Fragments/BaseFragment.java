package com.sample.myapplication.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sample.myapplication.R;

public abstract class BaseFragment extends Fragment {
    protected RecyclerViewAdapter recyclerViewAdapter;

    abstract protected boolean isListView();
    abstract protected int getLayoutId();
    abstract protected RecyclerViewAdapter getRecyclerViewAdapter();
    abstract protected void setData();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerViewAdapter = getRecyclerViewAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutId(), container, false);
        RecyclerView recyclerView = setupRecyclerView(rootView);

        return rootView;
    }

    private RecyclerView setupRecyclerView(View rootView) {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        if (isListView()) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        }
        recyclerView.setAdapter(recyclerViewAdapter);

        return recyclerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerViewAdapter.setActivity(this.getActivity());
        setData();
    }
}
