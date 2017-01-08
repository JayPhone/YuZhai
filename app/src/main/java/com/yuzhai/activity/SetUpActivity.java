package com.yuzhai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.yuzhai.yuzhaiwork.R;

public class SetUpActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView mFeedBackText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);

        mToolbar = (Toolbar) findViewById(R.id.setup_toolbar);
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.setTitle("软件设置");

        mFeedBackText = (TextView) findViewById(R.id.feed_back);
        mFeedBackText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_feedBack = new Intent();
                intent_feedBack.setClass(SetUpActivity.this, FeedBackActivity.class);
                startActivity(intent_feedBack);
            }
        });
    }
}
