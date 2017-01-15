package com.sample.myapplication;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sample.myapplication.Utils.LogUtil;
import com.sample.myapplication.Utils.OnSwipeTouchListener;
import com.squareup.picasso.Picasso;

public class ViewerActivity extends AppCompatActivity {
    Uri uri;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);

        this.uri = getIntent().getData();
        LogUtil.debug("uri : " + uri);
        this.image = (ImageView) findViewById(R.id.viewer_image);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_viewer);
        relativeLayout.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeDown() {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Picasso.with(this).load(this.uri).into(this.image);
    }
}
