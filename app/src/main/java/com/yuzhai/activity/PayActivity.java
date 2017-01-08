package com.yuzhai.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.yuzhai.yuzhaiwork.R;

/**
 * Created by Administrator on 2016/12/26.
 */

public class PayActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private RelativeLayout aliPayLayout;
    private RelativeLayout wxPayLayout;
    private RadioButton aliPayRadio;
    private RadioButton wxPayRadio;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        mToolbar = (Toolbar) findViewById(R.id.pay_toolbar);
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.setTitle("订单支付");

        aliPayRadio = (RadioButton) findViewById(R.id.choice_alipay);
        aliPayRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wxPayRadio.setChecked(false);
            }
        });

        wxPayRadio = (RadioButton) findViewById(R.id.choice_wxpay);
        wxPayRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aliPayRadio.setChecked(false);
            }
        });

        aliPayLayout = (RelativeLayout) findViewById(R.id.alipay_layout);
        aliPayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aliPayRadio.setChecked(true);
                wxPayRadio.setChecked(false);
            }
        });

        wxPayLayout = (RelativeLayout) findViewById(R.id.wxpay_layout);
        wxPayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aliPayRadio.setChecked(false);
                wxPayRadio.setChecked(true);
            }
        });
    }
}
