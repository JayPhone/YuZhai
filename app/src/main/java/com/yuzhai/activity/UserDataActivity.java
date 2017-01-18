package com.yuzhai.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.yuzhai.yuzhaiwork.R;

/**
 * Created by Administrator on 2017/1/8.
 */

public class UserDataActivity extends AppCompatActivity {
    private Toolbar mUserDataToolbar;
    private AppBarLayout mAppBarLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private TextView mAgreeAccept;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_user_data);

        initViews();
    }

    private void initViews() {
        mUserDataToolbar = (Toolbar) findViewById(R.id.user_data_toolbar);
        mUserDataToolbar.setNavigationIcon(R.drawable.back);
        mUserDataToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mAgreeAccept = (TextView) findViewById(R.id.agree_accept_text);
        mAgreeAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPayDialog();
            }
        });
    }

    private void showPayDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("同意申请");
        builder.setMessage("同意申请者接单意味着订单正式开始，你需要缴纳订单总额以及保证金，缴纳成功后，订单开始计时");
        builder.setPositiveButton("我要支付", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent pay = new Intent(UserDataActivity.this, PayActivity.class);
                startActivity(pay);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }
}
