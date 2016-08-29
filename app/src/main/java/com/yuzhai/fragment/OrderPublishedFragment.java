package com.yuzhai.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuzhai.adapter.PublishedListViewAdapter;
import com.yuzhai.config.IPConfig;
import com.yuzhai.entry.responseBean.OrderPublishedBean;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.ui.DetailWorkActivity;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/21.
 */
public class OrderPublishedFragment extends Fragment implements AdapterView.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mPublishedSrl;
    private ListView mPublishedLv;
    private Activity mMainActivity;

    private CustomApplication mCustomApplication;
    private RequestQueue mRequestQueue;
    private List<OrderPublishedBean.OrderBean> mOrders;
    private final String COOKIE = "cookie";
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
        mRequestQueue = RequestQueueSingleton.getInstance(mMainActivity).getRequestQueue();
        //初始化控件
        initViews();
        //初始化数据
        initData();
    }

    /**
     * 初始化控件
     */
    public void initViews() {
        mPublishedSrl = (SwipeRefreshLayout) mMainActivity.findViewById(R.id.published_order_refresh);
        mPublishedLv = (ListView) mMainActivity.findViewById(R.id.published_listview);

        //设置下拉刷新监听
        mPublishedSrl.setOnRefreshListener(this);
        //设置刷新样式
        mPublishedSrl.setColorSchemeResources(R.color.mainColor);
        //设置ListView的子项点击监听
        mPublishedLv.setOnItemClickListener(this);
    }

    /**
     * 初始化数据
     */
    public void initData() {
        //初始化时默认显示刷新状态
        mPublishedSrl.setRefreshing(true);
        sendPublishedOrderRequest();
    }

    @Override
    public void onRefresh() {
        //显示刷新状态
        setRefreshState(true);
        //发送查看已发布订单请求
        sendPublishedOrderRequest();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.published_listview:
                //获取当前项订单数据
                String orderJson = JsonUtil.codeByGson(mOrders.get(position));
                Log.i("publish_order", orderJson);

                //跳转到订单详情界面并传递订单数据
                Intent detailWork = new Intent(mMainActivity, DetailWorkActivity.class);
                detailWork.putExtra(DATA, orderJson);
                startActivity(detailWork);
                break;
        }
    }

    /**
     * 发送查看已发布订单请求
     */
    public void sendPublishedOrderRequest() {
        //创建查看已发布订单请求
        CommonRequest publishedOrderRequest = new CommonRequest(IPConfig.orderPublishedAddress,
                generateHeaders(),
                null,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resp) {
                        Log.i("order_publish_resp", resp);
                        //解析获取到的订单数据
                        mOrders = JsonUtil.decodeByGson(resp, OrderPublishedBean.class).getOrder();
                        //设置数据到ListView
                        mPublishedLv.setAdapter(new PublishedListViewAdapter(mMainActivity, mOrders));
                        //关闭刷新
                        setRefreshState(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        UnRepeatToast.showToast(mMainActivity, "服务器不务正业中");
                        Log.i("error", volleyError.getMessage(), volleyError);
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

    /**
     * 生成请求头参数集
     *
     * @return 返回请求头参数集
     */
    public Map<String, String> generateHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put(COOKIE, mCustomApplication.getCookie());
        return headers;
    }
}
