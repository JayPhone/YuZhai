package com.yuzhai.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yuzhai.yuzhaiwork.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
    }
}
