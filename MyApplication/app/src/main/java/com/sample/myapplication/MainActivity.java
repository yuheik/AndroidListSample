package com.sample.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.sample.myapplication.Fragments.TabFragment;
import com.sample.myapplication.Utils.LogUtil;

public class MainActivity extends AppCompatActivity {

    Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_activitiy);

        setUpToolBar();
        setUpTabView();
    }

    private void setUpToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        } else {
            LogUtil.error("actionBar is null");
        }
    }

    private void setUpTabView() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TabFragment.class.getName());
        if (fragment == null) {
            fragment = MainTabFragment.newInstance();
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (currentFragment != null) {
            fragmentTransaction.detach(currentFragment);
        }

        currentFragment = fragment;
        fragmentTransaction.replace(R.id.main_content,
                                    currentFragment,
                                    TabFragment.class.getName())
                           .attach(currentFragment)
                           .commit();
    }
}
