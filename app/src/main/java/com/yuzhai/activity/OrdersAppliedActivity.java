package com.yuzhai.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.yuzhai.adapter.OrdersAppliedViewPagerAdapter;
import com.yuzhai.bean.responseBean.CancelAppliedRespBean;
import com.yuzhai.bean.responseBean.OrderAcceptedDetailBean;
import com.yuzhai.bean.responseBean.OrderAppliedDetailBean;
import com.yuzhai.fragment.OrderAcceptedDetailFragment;
import com.yuzhai.fragment.OrderAcceptedProcessFragment;
import com.yuzhai.fragment.OrderAppliedDetailFragment;
import com.yuzhai.fragment.OrderAppliedFragment;
import com.yuzhai.fragment.OrderAppliedProcessFragment;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.IPConfig;
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
 * Created by 35429 on 2017/2/20.
 */

public class OrdersAppliedActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {
    private static final String TAG = "OrdersAppliedActivity";
    private static final String AVATAR = "avatar";

    private Toolbar mToolbar;
    private CircleImageView mPublisherHeader;
    private TabLayout mTabLayout;
    private ViewPager mAppliedViewPager;
    private OrdersAppliedViewPagerAdapter mOrdersAppliedViewPagerAdapter;
    private FloatingActionButton mCancelFab;

    private CustomApplication mCustomApplication;
    private RequestQueue mRequestQueue;
    private OrderAppliedDetailBean.OrderInfoBean mOrder;
    private String mOrderId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_applied);

        mRequestQueue = RequestQueueSingleton.getRequestQueue(this);
        mCustomApplication = (CustomApplication) getApplication();

        mOrderId = getIntent().getStringExtra(OrderAppliedFragment.ORDER_ID);
        Log.i(TAG, "order_id:" + mOrderId);

        initData();
    }

    private void initData() {
        if (CustomApplication.isConnect) {
            sendAppliedOrderDetailRequest(mOrderId);
        } else {
            initViews(null);
        }
    }

    private void initViews(String order) {
        mToolbar = (Toolbar) findViewById(R.id.applied_toolbar);
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.inflateMenu(R.menu.applied_detail_menu);
        mToolbar.setOnMenuItemClickListener(this);
        mPublisherHeader = (CircleImageView) findViewById(R.id.publisher_header);
        mPublisherHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userData = new Intent(OrdersAppliedActivity.this, UserDataActivity.class);
                userData.putExtra(AVATAR, mOrder.getPublisherAvatar());
                startActivity(userData);
            }
        });

        //订单进度Fragment
        OrderAppliedProcessFragment orderAppliedProcessFragment = OrderAppliedProcessFragment.newInstance(order);
        //订单详情Fragment
        OrderAppliedDetailFragment orderAppliedDetailFragment = OrderAppliedDetailFragment.newInstance(order);

        //添加viewPager的页面布局到List<View>里
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(orderAppliedProcessFragment);
        fragmentList.add(orderAppliedDetailFragment);

        //创建viewPager的适配器并设置
        mAppliedViewPager = (ViewPager) findViewById(R.id.applied_viewPager);
        mOrdersAppliedViewPagerAdapter = new OrdersAppliedViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        mAppliedViewPager.setAdapter(mOrdersAppliedViewPagerAdapter);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (mTabLayout != null) {
            mTabLayout.setupWithViewPager(mAppliedViewPager);
        }

        mCancelFab = (FloatingActionButton) findViewById(R.id.cancel_order_fab);
        mCancelFab.setOnClickListener(this);
    }

    private void updateData(OrderAppliedDetailBean.OrderInfoBean order) {
        mToolbar.setTitle("发布者");
        mToolbar.setSubtitle(order.getPublisher());
        Glide.with(this)
                .load(IPConfig.image_addressPrefix + order.getPublisherAvatar())
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(mPublisherHeader);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_order_fab:
                showCancelAppliedOrderDialog();
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

    private void showCancelAppliedOrderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("取消申请");
        builder.setMessage("您确定要取消申请订单吗?");
        builder.setPositiveButton("取消申请", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendCancelAppliedRequest(mOrder.getOrderID());
            }
        });
        builder.setNegativeButton("先不取消", null);
        builder.create().show();
    }

    /**
     * 发送查看详细订单的请求
     *
     * @param orderId
     */
    public void sendAppliedOrderDetailRequest(String orderId) {
        //生成详细订单的参数集
        Map<String, String> params = ParamsGenerateUtil.generateOrderDetailParam(orderId);

        CommonRequest orderDetailRequest = new CommonRequest(this,
                IPConfig.orderDetailAddress,
                mCustomApplication.generateHeaderMap(),
                params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resp) {
                        Log.i(TAG, "applied_detail_resp:" + resp);
                        //初始化界面引用
                        initViews(resp);
                        //解析数据
                        mOrder = JsonUtil.decodeByGson(resp, OrderAppliedDetailBean.class).getDetailedOrder();
                        //更新数据
                        updateData(mOrder);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        UnRepeatToast.showToast(OrdersAppliedActivity.this, "服务器不务正业中");
                    }
                });

        //添加到请求队列
        mRequestQueue.add(orderDetailRequest);
    }

    /**
     * 发送取消申请订单的请求
     *
     * @param orderId
     */
    public void sendCancelAppliedRequest(String orderId) {
        //生成取消申请订单的请求参数集
        Map<String, String> params = ParamsGenerateUtil.generateCancelAppliedOrderParams(orderId);

        CommonRequest cancelAppliedOrder = new CommonRequest(this,
                IPConfig.cancelAppliedOrderAddress,
                mCustomApplication.generateHeaderMap(),
                params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resp) {
                        Log.i(TAG, "cancel_applied_resp:" + resp);
                        //解析数据
                        String code = JsonUtil.decodeByGson(resp, CancelAppliedRespBean.class).getCode();
                        if (code.equals("1")) {
                            UnRepeatToast.showToast(OrdersAppliedActivity.this, "取消申请订单成功");
                        }
                        if (code.equals("-1")) {
                            UnRepeatToast.showToast(OrdersAppliedActivity.this, "取消申请失败,请稍后再试");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        UnRepeatToast.showToast(OrdersAppliedActivity.this, "服务器不务正业中");
                    }
                });

        //添加到请求队列
        mRequestQueue.add(cancelAppliedOrder);
    }
}
