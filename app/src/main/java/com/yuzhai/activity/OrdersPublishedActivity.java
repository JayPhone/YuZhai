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
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuzhai.adapter.OrdersPublishedViewPagerAdapter;
import com.yuzhai.bean.responseBean.CancelPublishedRespBean;
import com.yuzhai.bean.responseBean.OrderPublishedDetailBean;
import com.yuzhai.fragment.OrderPublishedDetailFragment;
import com.yuzhai.fragment.OrderPublishedFragment;
import com.yuzhai.fragment.OrderPublishedProcessFragment;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.IPConfig;
import com.yuzhai.http.ParamsGenerateUtil;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/14.
 */

public class OrdersPublishedActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mPublishedViewPager;
    private OrdersPublishedViewPagerAdapter mPublishedViewPagerAdapter;
    private FloatingActionButton mCancelFab;
    private FloatingActionButton mFinishFab;

    private CustomApplication mCustomApplication;
    private RequestQueue mRequestQueue;
    private OrderPublishedDetailBean.OrderInfoBean mOrder;
    private String mOrderId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_published);
        mRequestQueue = RequestQueueSingleton.getRequestQueue(this);
        mCustomApplication = (CustomApplication) getApplication();

        mOrderId = getIntent().getStringExtra(OrderPublishedFragment.ORDER_ID);

        initData();
    }

    private void initData() {
        if (CustomApplication.isConnect) {
            sendPublishedOrderDetailRequest(mOrderId);
        } else {
            initViews(null);
        }
    }

    private void initViews(String order) {
        mToolbar = (Toolbar) findViewById(R.id.published_toolbar);
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //订单进度Fragment
        OrderPublishedProcessFragment orderPublishedProcessFragment = OrderPublishedProcessFragment.newInstance(order);
        //订单详情Fragment
        OrderPublishedDetailFragment orderPublishedDetailFragment = OrderPublishedDetailFragment.newInstance(order);

        //添加viewPager的页面布局到List<View>里
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(orderPublishedProcessFragment);
        fragmentList.add(orderPublishedDetailFragment);

        //创建viewPager的适配器并设置
        mPublishedViewPager = (ViewPager) findViewById(R.id.published_viewPager);
        mPublishedViewPagerAdapter = new OrdersPublishedViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        mPublishedViewPager.setAdapter(mPublishedViewPagerAdapter);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mPublishedViewPager);

        mCancelFab = (FloatingActionButton) findViewById(R.id.cancel_order_fab);
        mCancelFab.setOnClickListener(this);

        mFinishFab = (FloatingActionButton) findViewById(R.id.finish_order_fab);
        mFinishFab.setOnClickListener(this);

    }

    private void updateData(OrderPublishedDetailBean.OrderInfoBean order) {
        mToolbar.setTitle("订单状态");
        mToolbar.setSubtitle(order.getStatus());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_order_fab:
                showCancelPublishedOrderDialog();
                break;
            case R.id.finish_order_fab:
                showConfirmFinishDialog();
                break;
        }
    }

    private void showCancelPublishedOrderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("取消发单");
        builder.setMessage("您确定要取消订单，如果该订单已经被接收，则需要赔付一定的金额，您还要继续吗?");
        builder.setPositiveButton("我要退", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //发送取消已发布订单请求
                if (CustomApplication.isConnect) {
                    sendCancelPublishedRequest(mOrder.getOrderID());
                }
            }
        });
        builder.setNegativeButton("先不退", null);
        builder.create().show();
    }

    private void showConfirmFinishDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确认完成");
        builder.setMessage("点击确认订单后，你交付在本平台上的项目金额将转账到对方账号，请谨慎操作！");
        builder.setPositiveButton("我确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //正常代码
                //发送取消已发布订单请求
//                sendCancelPublishedRequest(mData.get(mPosition).getPublish().getPublishId());
//                Intent pay = new Intent(OrdersPublishedActivity.this, PayActivity.class);
//                startActivity(pay);

            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    /**
     * 发送取消已发布订单请求
     */
    private void sendCancelPublishedRequest(String publishId) {
        //获取取消已发布订单请求的参数集
        Map<String, String> params = ParamsGenerateUtil.generateCancelPublishedOrderParams(publishId);

        //创建取消已发布订单请求
        CommonRequest cancelPublishedRequest = new CommonRequest(this,
                IPConfig.cancelPublishedOrderAddress,
                mCustomApplication.generateHeaderMap(),
                params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resp) {
                        Log.i("cancel_publish_resp", resp);
                        if (JsonUtil.decodeByGson(resp, CancelPublishedRespBean.class).equals("1")) {
                            UnRepeatToast.showToast(OrdersPublishedActivity.this, "取消发布订单成功");
                        }

//                        if (respCode != null && respCode.equals("-1")) {
//                            UnRepeatToast.showToast(OrdersPublishedActivity.this, "退单失败,请稍后再试");
//                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        UnRepeatToast.showToast(OrdersPublishedActivity.this, "服务器不务正业中");
                    }
                });

        //添加到请求队列
        mRequestQueue.add(cancelPublishedRequest);
    }

    /**
     * 发送查看详细订单的请求
     *
     * @param orderId
     */
    public void sendPublishedOrderDetailRequest(String orderId) {
        //生成详细订单的参数集
        Map<String, String> params = ParamsGenerateUtil.generateOrderDetailParam(orderId);

        CommonRequest orderDetailRequest = new CommonRequest(this,
                IPConfig.orderDetailAddress,
                mCustomApplication.generateHeaderMap(),
                params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resp) {
                        Log.i("published_detail_resp", resp);
                        //初始化界面引用
                        initViews(resp);
                        //解析数据
                        mOrder = JsonUtil.decodeByGson(resp, OrderPublishedDetailBean.class).getDetailedOrder();
                        //更新数据
                        updateData(mOrder);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        UnRepeatToast.showToast(OrdersPublishedActivity.this, "服务器不务正业中");
                    }
                });

        //添加到请求队列
        mRequestQueue.add(orderDetailRequest);
    }
}
