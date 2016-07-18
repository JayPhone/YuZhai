package com.yuzhai.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuzhai.yuzhaiwork.R;

public class SetUpActivity extends AppCompatActivity {

    private ImageView backImage;
    private TextView feedBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);
        backImage = (ImageView) findViewById(R.id.back_image);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        feedBack = (TextView) findViewById(R.id.feed_back);
        feedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_feedBack = new Intent();
                intent_feedBack.setClass(SetUpActivity.this, FeedBackActivity.class);
                startActivity(intent_feedBack);
            }
        });
    }
}
