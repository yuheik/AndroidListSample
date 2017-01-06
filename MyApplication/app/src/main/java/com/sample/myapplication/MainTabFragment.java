package com.sample.myapplication;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.sample.myapplication.Fragments.TabFragment;

public class MainTabFragment extends TabFragment {

    public static MainTabFragment newInstance() {
        Bundle args = new Bundle();

        MainTabFragment fragment = new MainTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected TabAdapter getTabAdapter(FragmentManager fragmentManager) {
        return new MainTabAdapter(fragmentManager);
    }


    public static class MainTabAdapter extends TabFragment.TabAdapter {
        static private TabFragment.TabItem[] tabItems = {
                new TabFragment.TabItem(RecentFragment.class, "RECENT"),
                new TabFragment.TabItem(SearchFragment.class, "SEARCH")
        };

        public MainTabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        protected TabFragment.TabItem[] getTabItems() {
            return tabItems;
        }
    }
}
