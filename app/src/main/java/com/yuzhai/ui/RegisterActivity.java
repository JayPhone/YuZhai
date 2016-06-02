package com.yuzhai.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.yuzhai.yuzhaiwork.R;

/**
 * Created by Administrator on 2016/5/23.
 */
public class RegisterActivity extends AppCompatActivity {
    TextView login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        inflate_buttom_textview();
    }

    public void inflate_buttom_textview() {
        login = (TextView) findViewById(R.id.register_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
