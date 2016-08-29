package com.yuzhai.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/21.
 */
public class OrderAcceptedFragment extends Fragment implements AdapterView.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mAcceptedSrl;
    private ListView mAcceptedLv;
    private Activity mMainActivity;

    private CustomApplication mCustomApplication;
    private RequestQueue mRequestQueue;
    private String mOrdersJson;
    private List<Map<String, Object>> mOrders;
    private final String COOKIE = "cookie";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.order_viewpager_accepted_layout, container, false);
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
        mAcceptedSrl = (SwipeRefreshLayout) mMainActivity.findViewById(R.id.accepted_order_refresh);
        mAcceptedLv = (ListView) mMainActivity.findViewById(R.id.accepted_listview);

        //设置下拉刷新监听
        mAcceptedSrl.setOnRefreshListener(this);
        //设置刷新样式
        mAcceptedSrl.setColorSchemeResources(R.color.mainColor);
        //设置ListView的子项点击监听
        mAcceptedLv.setOnItemClickListener(this);
    }

    /**
     * 初始化数据
     */
    public void initData() {
        //初始化时默认显示刷新状态
        mAcceptedSrl.setRefreshing(true);
        sendAcceptedOrderRequest();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.accepted_listview:
                break;
        }
    }

    @Override
    public void onRefresh() {
        sendAcceptedOrderRequest();
    }

    public void sendAcceptedOrderRequest() {
        //创建查看已接收订单请求
        //TODO URL和请求参数尚未填写
        CommonRequest acceptedOrderRequest = new CommonRequest(null,
                generateHeaders(),
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
