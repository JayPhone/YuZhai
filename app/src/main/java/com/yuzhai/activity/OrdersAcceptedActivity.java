package com.yuzhai.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.yuzhai.adapter.OrdersAcceptedViewPagerAdapter;
import com.yuzhai.fragment.OrderAcceptedDetailFragment;
import com.yuzhai.fragment.OrderAcceptedProcessFragment;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/14.
 */

public class OrdersAcceptedActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener,View.OnClickListener {
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mAcceptedViewPager;
    private OrdersAcceptedViewPagerAdapter mAcceptedViewPagerAdapter;
    private FloatingActionButton mCancelFab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_accepted);
        initViews();
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.accepted_toolbar);
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.setTitle("发布者");
        mToolbar.setSubtitle("13048119089");
        mToolbar.inflateMenu(R.menu.accepted_detail_menu);
        mToolbar.setOnMenuItemClickListener(this);

        //订单进度Fragment
        OrderAcceptedProcessFragment orderAcceptedProcessFragment = OrderAcceptedProcessFragment.newInstance();
        //订单详情Fragment
        OrderAcceptedDetailFragment orderAcceptedDetailFragment = OrderAcceptedDetailFragment.newInstance();

        //添加viewPager的页面布局到List<View>里
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(orderAcceptedProcessFragment);
        fragmentList.add(orderAcceptedDetailFragment);

        //创建viewPager的适配器并设置
        mAcceptedViewPager = (ViewPager) findViewById(R.id.accepted_viewPager);
        mAcceptedViewPagerAdapter = new OrdersAcceptedViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        mAcceptedViewPager.setAdapter(mAcceptedViewPagerAdapter);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (mTabLayout != null) {
            mTabLayout.setupWithViewPager(mAcceptedViewPager);
        }

        mCancelFab = (FloatingActionButton) findViewById(R.id.cancel_order_fab);
        mCancelFab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_order_fab:
                showCancelAcceptedOrderDialog();
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.call:
                break;
        }
        return true;
    }

    private void showCancelAcceptedOrderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("取消接单");
        builder.setMessage("您确定要取消接收订单，如果该订单已经同意您接收，则需要赔付一定的金额，您还要继续吗?");
        builder.setPositiveButton("我要取消接收", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //正常代码
                //发送取消已发布订单请求
//                sendCancelPublishedRequest(mData.get(mPosition).getPublish().getPublishId());

            }
        });
        builder.setNegativeButton("先不取消", null);
        builder.create().show();
    }
}
