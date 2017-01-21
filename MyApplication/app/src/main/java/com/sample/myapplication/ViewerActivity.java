package com.sample.myapplication;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.sample.myapplication.Utils.LogUtil;

public class ViewerActivity extends AppCompatActivity {
    public static final String FLICKER_ACTION_TYPE = "FlickerActionType";

    Uri uri;
    FlickrManager.Type type;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);

        this.uri = getIntent().getData();
        LogUtil.debug("uri : " + uri);
        this.type = (FlickrManager.Type) getIntent().getSerializableExtra(FLICKER_ACTION_TYPE);
        LogUtil.debug("type : " + type);

        this.viewPager = (ViewPager) findViewById(R.id.viewer_pager);
        this.pagerAdapter = new ViewerPagerAdapter(getSupportFragmentManager(), this.uri);
        this.viewPager.setAdapter(this.pagerAdapter);
    }

    static class ViewerPagerAdapter extends FragmentStatePagerAdapter {
        String uri;

        public ViewerPagerAdapter(FragmentManager fm, Uri uri) {
            super(fm);
            this.uri = uri.toString();
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            return ImagerFragment.newInstance(this.uri);
        }
    }
}
