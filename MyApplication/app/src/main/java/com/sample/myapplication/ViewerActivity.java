package com.sample.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.sample.myapplication.Flickr.FlickrManager;
import com.sample.myapplication.Flickr.FlickrPhoto;
import com.sample.myapplication.Utils.LogUtil;

import java.util.ArrayList;

public class ViewerActivity extends AppCompatActivity {
    public static final String FLICKER_ACTION_TYPE = "FlickerActionType";
    public static final String FLICKER_DATA_POSITION = "FlickerDataPosition";

    ViewPager viewPager;
    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);

        FlickrManager.Type type = (FlickrManager.Type) getIntent().getSerializableExtra(FLICKER_ACTION_TYPE);
        LogUtil.debug("type : " + type);

        int position = getIntent().getIntExtra(FLICKER_DATA_POSITION, 0);
        LogUtil.debug("position : " + position);

        this.viewPager = (ViewPager) findViewById(R.id.viewer_pager);
        this.pagerAdapter = new ViewerPagerAdapter(getSupportFragmentManager(), type);
        this.viewPager.setAdapter(this.pagerAdapter);
        this.viewPager.setCurrentItem(position);
    }

    static class ViewerPagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<FlickrPhoto> data;

        public ViewerPagerAdapter(FragmentManager fm, FlickrManager.Type type) {
            super(fm);
            this.data = FlickrManager.getResult(type);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Fragment getItem(int position) {
            return ImagerFragment.newInstance(data.get(position));
        }
    }
}
