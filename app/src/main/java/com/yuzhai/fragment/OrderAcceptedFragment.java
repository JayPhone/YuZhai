package com.yuzhai.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuzhai.activity.OrdersAcceptedActivity;
import com.yuzhai.adapter.AcceptedRecyclerViewAdapter;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/21.
 */
public class OrderAcceptedFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener, AcceptedRecyclerViewAdapter.OnAcceptedItemClickListener {
    private SwipeRefreshLayout mAcceptedSrl;
    private RecyclerView mAcceptedRv;
    private AcceptedRecyclerViewAdapter mAdapter;
    private Activity mMainActivity;
    private Drawable mDivider;

    private CustomApplication mCustomApplication;
    private RequestQueue mRequestQueue;
    private String mOrdersJson;
    private List<Map<String, Object>> mOrders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.order_viewpager_accepted_layout, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mMainActivity = getActivity();
        mCustomApplication = (CustomApplication) mMainActivity.getApplication();
        mRequestQueue = RequestQueueSingleton.getRequestQueue(mMainActivity);
        mDivider = ContextCompat.getDrawable(mMainActivity, R.drawable.order_recyclerview_divider);
        //初始化控件
        initViews();
        //初始化数据
        initData();
    }

    /**
     * 初始化控件
     */
    public void initViews() {
        mAcceptedSrl = (SwipeRefreshLayout) getView().findViewById(R.id.accepted_order_refresh);
        //设置下拉刷新监听
        mAcceptedSrl.setOnRefreshListener(this);
        //设置刷新样式
        mAcceptedSrl.setColorSchemeResources(R.color.mainColor);

        mAcceptedRv = (RecyclerView) getView().findViewById(R.id.accepted_recyclerView);
        mAcceptedRv.setLayoutManager(new LinearLayoutManager(mMainActivity));
        mAcceptedRv.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new AcceptedRecyclerViewAdapter(mMainActivity);
        mAdapter.setOnAcceptedItemClickListener(this);
    }

    /**
     * 初始化数据
     */
    public void initData() {
        //测试代码
        mAcceptedRv.setAdapter(mAdapter);

//        //初始化时默认显示刷新状态
//        mAcceptedSrl.setRefreshing(true);
//        sendAcceptedOrderRequest();
    }

    @Override
    public void onRefresh() {
        sendAcceptedOrderRequest();
    }

    public void sendAcceptedOrderRequest() {
        //创建查看已接收订单请求
        //TODO URL和请求参数尚未填写
        CommonRequest acceptedOrderRequest = new CommonRequest(null,
                null,
                null,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        setRefreshState(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        UnRepeatToast.showToast(mMainActivity, "服务器不务正业中");
                        setRefreshState(false);
                    }
                });

        //添加到请求队列
        mRequestQueue.add(acceptedOrderRequest);
    }

    /**
     * 设置是否显示刷新状态
     *
     * @param state 刷新状态
     */
    public void setRefreshState(Boolean state) {
        mAcceptedSrl.setRefreshing(state);
    }

    @Override
    public void onAcceptedItemClick(int position) {
        Intent ordersAccepted = new Intent(mMainActivity, OrdersAcceptedActivity.class);
        mMainActivity.startActivity(ordersAccepted);
    }
}
