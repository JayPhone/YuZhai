package com.yuzhai.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuzhai.yuzhaiwork.R;

/**
 * Created by Administrator on 2016/7/26.
 */
public class IdentityAuthenActivity extends AppCompatActivity {
    private ImageView mBackImage;
    private TextView mTitleText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity_authen);
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
            mTitleText.setText("实名认证");
        }
    }
}
