package com.yuzhai.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuzhai.yuzhaiwork.R;

public class SetUpActivity extends AppCompatActivity {

    private ImageView mBackImage;
    private TextView mTitleText;
    private TextView mFeedBackText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);
        mBackImage = (ImageView) findViewById(R.id.back_image);
        if (mBackImage != null) {
            mBackImage.setImageResource(R.drawable.back);
        }
        mBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTitleText = (TextView) findViewById(R.id.title_text);
        if (mTitleText != null) {
            mTitleText.setText("设置");
        }

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
