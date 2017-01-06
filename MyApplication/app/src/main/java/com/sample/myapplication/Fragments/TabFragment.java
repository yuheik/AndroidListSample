package com.sample.myapplication.Fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sample.myapplication.R;


public abstract class TabFragment extends Fragment {
    private TabAdapter tabAdapter;

    abstract protected TabAdapter getTabAdapter(FragmentManager fragmentManager);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        tabAdapter = getTabAdapter(getChildFragmentManager());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab, container, false);

        ViewPager viewPager = setupViewPager(rootView);
        TabLayout tabLayout = setupTabLayout(rootView, viewPager);

        tabLayout.getTabAt(0).select();

        return rootView;
    }

    private ViewPager setupViewPager(View rootView) {
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        viewPager.setAdapter(tabAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });

        return viewPager;
    }

    private TabLayout setupTabLayout(View rootView, ViewPager viewPager) {
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        return tabLayout;
    }

    public static class TabItem {
        private Class clz;
        private String title;

        public TabItem(Class clz, String title) {
            this.clz = clz;
            this.title = title;
        }
    }

    public static abstract class TabAdapter extends FragmentStatePagerAdapter {
        abstract protected TabItem[] getTabItems();

        public TabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return getTabItems().length;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            try {
                fragment = (Fragment) getTabItems()[position].clz.newInstance();
            } catch (java.lang.InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getTabItems()[position].title;
        }
    }
}
