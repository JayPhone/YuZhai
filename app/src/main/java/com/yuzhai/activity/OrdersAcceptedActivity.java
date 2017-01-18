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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.yuzhai.adapter.OrdersAcceptedViewPagerAdapter;
import com.yuzhai.bean.responseBean.OrderAcceptedDetailBean;
import com.yuzhai.http.IPConfig;
import com.yuzhai.fragment.OrderAcceptedDetailFragment;
import com.yuzhai.fragment.OrderAcceptedFragment;
import com.yuzhai.fragment.OrderAcceptedProcessFragment;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.ParamsGenerateUtil;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.view.CircleImageView;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/14.
 */

public class OrdersAcceptedActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {
    private Toolbar mToolbar;
    private CircleImageView mPublisherHeader;
    private TabLayout mTabLayout;
    private ViewPager mAcceptedViewPager;
    private OrdersAcceptedViewPagerAdapter mAcceptedViewPagerAdapter;
    private FloatingActionButton mCancelFab;

    private CustomApplication mCustomApplication;
    private RequestQueue mRequestQueue;
    private OrderAcceptedDetailBean.OrderInfoBean mOrder;
    private String mOrderId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_accepted);

        mRequestQueue = RequestQueueSingleton.getRequestQueue(this);
        mCustomApplication = (CustomApplication) getApplication();

        mOrderId = getIntent().getStringExtra(OrderAcceptedFragment.ORDER_ID);

        initData();
    }

    private void initData() {
        if (CustomApplication.isConnect) {
            sendAcceptedOrderDetailRequest(mOrderId, mCustomApplication.getToken());
        } else {
            initViews(null);
        }
    }

    private void initViews(String order) {
        mToolbar = (Toolbar) findViewById(R.id.accepted_toolbar);
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.inflateMenu(R.menu.accepted_detail_menu);
        mToolbar.setOnMenuItemClickListener(this);
        mPublisherHeader = (CircleImageView) findViewById(R.id.publisher_header);

        //订单进度Fragment
        OrderAcceptedProcessFragment orderAcceptedProcessFragment = OrderAcceptedProcessFragment.newInstance(order);
        //订单详情Fragment
        OrderAcceptedDetailFragment orderAcceptedDetailFragment = OrderAcceptedDetailFragment.newInstance(order);

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

    private void updateData(OrderAcceptedDetailBean.OrderInfoBean order) {
        mToolbar.setTitle("发布者");
        mToolbar.setSubtitle(order.getPublisher());
        Glide.with(this)
                .load(IPConfig.image_addressPrefix + "/" + order.getPublisherAvatar())
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(mPublisherHeader);
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

    /**
     * 发送查看详细订单的请求
     *
     * @param orderId
     * @param token
     */
    public void sendAcceptedOrderDetailRequest(String orderId, String token) {
        //生成详细订单的参数集
        Map<String, String> params = ParamsGenerateUtil.generateOrderDetailParam(orderId, token);

        CommonRequest orderDetailRequest = new CommonRequest(IPConfig.orderDetailAddress,
                mCustomApplication.generateCookieMap(),
                params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resp) {
                        Log.i("accepted_detail_resp", resp);
                        //初始化界面引用
                        initViews(resp);
                        //解析数据
                        mOrder = JsonUtil.decodeByGson(resp, OrderAcceptedDetailBean.class).getDetailedOrder();
                        //更新数据
                        updateData(mOrder);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        UnRepeatToast.showToast(OrdersAcceptedActivity.this, "服务器不务正业中");
                    }
                });

        //添加到请求队列
        mRequestQueue.add(orderDetailRequest);
    }
}
