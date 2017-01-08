package com.yuzhai.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuzhai.yuzhaiwork.R;

public class FeedBackActivity extends AppCompatActivity {
    private ImageView mBackImage;
    private TextView mTitleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
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
            mTitleText.setText("意见反馈");
        }
    }
}
