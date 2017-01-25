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
import com.sample.myapplication.Utils.LogUtil;

public abstract class BaseFragment extends Fragment {
    private static double LOAD_NEXT_THRESHOLD = 0.6;

    protected RecyclerView recyclerView;
    protected RecyclerViewAdapter recyclerViewAdapter;

    private boolean onLoadingData = false;

    abstract protected boolean isListView();
    abstract protected int getLayoutId();
    abstract protected RecyclerViewAdapter getRecyclerViewAdapter();
    abstract protected void setData();

    protected void setupView(View rootView) {}
    protected void loadNextData() {}

    /**
     * set data loading status with below APIs.
     * loadNextData() will be called only when isDataloading() returns false;
     */
    final protected void startDataLoading() { onLoadingData = true; }
    final protected void finishDataLoading() { onLoadingData = false; }
    final protected boolean isDataLoading() { return onLoadingData; }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerViewAdapter = getRecyclerViewAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutId(), container, false);
        recyclerView = setupRecyclerView(rootView);

        setupView(rootView);

        return rootView;
    }

    private RecyclerView setupRecyclerView(View rootView) {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        if (isListView()) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                                                                  LinearLayoutManager.VERTICAL,
                                                                  false));
        }
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (! isDataLoading() &&
                    (recyclerViewAdapter.getItemCount() * LOAD_NEXT_THRESHOLD) < getLastVisibleItemPosition()) {
                    // dumpStatus();
                    loadNextData();
                }
            }
        });

        return recyclerView;
    }

    private int getLastVisibleItemPosition() {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        // todo should check (lauoutManager instanceof LinearLayoutManager);
        return ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerViewAdapter.setActivity(this.getActivity());
        setData();
    }

    private void dumpStatus() {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        LogUtil.debug("data num: " + layoutManager.getItemCount());
        LogUtil.debug("firstVisibleItem: " + ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition());
        LogUtil.debug("lastVisibleItem: " + ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition());
        LogUtil.debug("shown item num: " + recyclerView.getChildCount());
    }
}
