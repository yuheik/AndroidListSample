package com.sample.myapplication.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.sample.myapplication.R;
import com.sample.myapplication.Utils.LogUtil;

public abstract class BaseFragment extends Fragment {
    private static double LOAD_NEXT_THRESHOLD = 0.6;

    protected SwipeRefreshLayout  swipeRefreshLayout;
    protected RecyclerView        recyclerView;
    protected RecyclerViewAdapter recyclerViewAdapter;
    protected ProgressBar         progressBar;
    protected LinearLayout        emptyView;

    private boolean onLoadingData = false;

    abstract protected boolean isListView();
    /** must override refreshData() when using SwipeRefresh */
    abstract protected boolean useSwipeRefresh();
    abstract protected int getLayoutId();
    abstract protected RecyclerViewAdapter getRecyclerViewAdapter();
    abstract protected void setData();

    protected void setupView(View rootView) {}
    protected void loadNextData() {}
    protected void refreshData() {}

    /**
     * set data loading status with below APIs.
     * loadNextData() will be called only when isDataloading() returns false;
     */
    final protected void startDataLoading() {
        startDataLoading(true);
    }

    final protected void startDataLoading(boolean showProgressBar) {
        onLoadingData = true;
        if (showProgressBar) {
            progressBar.setVisibility(View.VISIBLE);
        }
        swipeRefreshLayout.setEnabled(false);
    }

    final protected void finishDataLoading() {
        onLoadingData = false;
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(useSwipeRefresh());
    }

    final protected boolean isDataLoading() {
        return onLoadingData;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerViewAdapter = getRecyclerViewAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutId(), container, false);
        swipeRefreshLayout = setupSwipeRefreshLayout(rootView);
        progressBar = setupProgressBar(rootView);
        recyclerView = setupRecyclerView(rootView);
        emptyView = setupEmptyView(rootView);

        recyclerViewAdapter.setEmptyView(emptyView);

        setupView(rootView);

        return rootView;
    }

    private SwipeRefreshLayout setupSwipeRefreshLayout(View rootView) {
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.RED); // todo Not sure what the second color is.
        swipeRefreshLayout.setEnabled(useSwipeRefresh());

        return swipeRefreshLayout;
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

    private ProgressBar setupProgressBar(View rootView) {
        ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        return progressBar;
    }

    private LinearLayout setupEmptyView(View rootView) {
        LinearLayout emptyView = (LinearLayout) rootView.findViewById(R.id.empty_view);
        emptyView.setVisibility(View.GONE);
        return emptyView;
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
