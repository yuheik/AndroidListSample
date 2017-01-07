package com.sample.myapplication;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.sample.myapplication.Utils.LogUtil;
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        Picasso.with(this).load(this.uri).into(this.image);
    }
}
