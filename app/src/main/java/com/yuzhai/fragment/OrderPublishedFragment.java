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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuzhai.activity.OrdersPublishedActivity;
import com.yuzhai.adapter.PublishedRecyclerViewAdapter;
import com.yuzhai.bean.responseBean.OrderPublishedBean;
import com.yuzhai.config.IPConfig;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import java.util.List;

/**
 * Created by Administrator on 2016/8/21.
 */
public class OrderPublishedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, PublishedRecyclerViewAdapter.OnPublishedItemClickListener {

    private SwipeRefreshLayout mPublishedSrl;
    private RecyclerView mPublishedRv;
    private PublishedRecyclerViewAdapter mAdapter;
    private Activity mMainActivity;
    private Drawable mDivider;

    private CustomApplication mCustomApplication;
    private RequestQueue mRequestQueue;
    private List<OrderPublishedBean.OrderBean> mOrders;
    public final static String DATA = "data";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.order_viewpager_published_layout, container, false);
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
        mPublishedSrl = (SwipeRefreshLayout) getView().findViewById(R.id.published_order_refresh);
        //设置下拉刷新监听
        mPublishedSrl.setOnRefreshListener(this);
        //设置刷新样式
        mPublishedSrl.setColorSchemeResources(R.color.mainColor);

        mPublishedRv = (RecyclerView) getView().findViewById(R.id.published_recyclerView);
        mPublishedRv.setLayoutManager(new LinearLayoutManager(mMainActivity));
        mPublishedRv.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new PublishedRecyclerViewAdapter(mMainActivity);
        mAdapter.setOnPublishedItemClickListener(this);
    }

    /**
     * 初始化数据
     */
    public void initData() {
        //测试代码
        mPublishedRv.setAdapter(mAdapter);

        //正常代码
        //初始化时默认显示刷新状态
//        mPublishedSrl.setRefreshing(true);
//        sendPublishedOrderRequest();
    }

    @Override
    public void onRefresh() {
        //显示刷新状态
        setRefreshState(true);
        //发送查看已发布订单请求
        sendPublishedOrderRequest();
    }


    /**
     * 发送查看已发布订单请求
     */
    public void sendPublishedOrderRequest() {
        //创建查看已发布订单请求
        CommonRequest publishedOrderRequest = new CommonRequest(IPConfig.orderPublishedAddress,
                null,
                null,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resp) {
                        Log.i("order_publish_resp", resp);
                        //解析获取到的订单数据
                        mOrders = JsonUtil.decodeByGson(resp, OrderPublishedBean.class).getOrder();
                        //设置数据到ListView
//                        mPublishedRv.setAdapter(new PublishedListViewAdapter(mMainActivity, mOrders));
                        //关闭刷新
                        setRefreshState(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        UnRepeatToast.showToast(mMainActivity, "服务器不务正业中");
                        Log.i("error", volleyError.getMessage(), volleyError);
                        setRefreshState(false);
                    }
                });

        //添加到请求队列
        mRequestQueue.add(publishedOrderRequest);
    }

    /**
     * 设置是否显示刷新状态
     *
     * @param state 刷新状态
     */
    public void setRefreshState(Boolean state) {
        mPublishedSrl.setRefreshing(state);
    }

    @Override
    public void onPublishedItemClick(int position) {
        Intent orderPublishedDetail = new Intent(mMainActivity, OrdersPublishedActivity.class);
        mMainActivity.startActivity(orderPublishedDetail);
    }
}
