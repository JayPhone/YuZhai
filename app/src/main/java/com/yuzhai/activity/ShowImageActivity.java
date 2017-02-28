package com.yuzhai.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yuzhai.http.IPConfig;
import com.yuzhai.yuzhaiwork.R;

/**
 * Created by Administrator on 2017/1/9.
 */

public class ShowImageActivity extends AppCompatActivity {
    private static final String TAG = "ShowImageActivity";
    private ImageView mShowImage;
    private String mImageUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_show_image);

        mImageUrl = getIntent().getStringExtra(WorkDetailActivity.IMAGE_URL);

        mShowImage = (ImageView) findViewById(R.id.show_image);
        Glide.with(this)
                .load(IPConfig.image_addressPrefix + mImageUrl)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(mShowImage);
        mShowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
