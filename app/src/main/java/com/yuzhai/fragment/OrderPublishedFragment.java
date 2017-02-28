package com.yuzhai.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.yuzhai.bean.innerBean.FragmentUserVisibleBean;
import com.yuzhai.bean.responseBean.OrderPublishedBean;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.IPConfig;
import com.yuzhai.http.ParamsGenerateUtil;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/21.
 */
public class OrderPublishedFragment extends BaseLazyLoadFragment implements
        SwipeRefreshLayout.OnRefreshListener,
        PublishedRecyclerViewAdapter.OnPublishedItemClickListener {
    private static final String TAG = "OrderPublishedFragment";

    private SwipeRefreshLayout mPublishedSrl;
    private RecyclerView mPublishedRv;
    private PublishedRecyclerViewAdapter mAdapter;
    private Activity mMainActivity;

    private CustomApplication mCustomApplication;
    private RequestQueue mRequestQueue;
    private List<OrderPublishedBean.OrderBean> mInitOrders = new ArrayList<>();

    public final static String ORDER_ID = "order_id";
    private final static String IS_FIRST_TIME = "yes";
    private final static String NOT_FIRST_TIME = "no";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_viewpager_published_layout, container, false);
        //设置当前Fragment的布局已经加载
        isViewCreated = true;
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainActivity = getActivity();
        EventBus.getDefault().register(this);
        mCustomApplication = (CustomApplication) mMainActivity.getApplication();
        mRequestQueue = RequestQueueSingleton.getRequestQueue(mMainActivity);
        initViews();
        intData();
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
        mAdapter = new PublishedRecyclerViewAdapter(this, mInitOrders);
        mAdapter.setOnPublishedItemClickListener(this);
        mPublishedRv.setAdapter(mAdapter);
    }

    /**
     * 初始化数据
     */
    private void intData() {
        if (CustomApplication.isConnect) {
            setRefreshState(true);
            sendPublishedOrderRequest(IS_FIRST_TIME);
        }
    }

    /**
     * 懒加载数据
     */
    @Override
    protected void lazyLoadData() {
        super.lazyLoadData();
        if (isViewCreated) {
            deleteAll();
            intData();
        }
    }

    /**
     * 更新订单数据
     *
     * @param newOrders 新获取的订单数据集
     */
    public void updateData(List<OrderPublishedBean.OrderBean> newOrders) {
        Collections.reverse(newOrders);
        mInitOrders.addAll(0, newOrders);
        //通知recyclerView插入数据
        mAdapter.notifyDataSetChanged();
        //recyclerView滚动到顶部
        mPublishedRv.smoothScrollToPosition(0);
    }

    /**
     * 清空所有数据
     */
    public void deleteAll() {
        mAdapter.notifyItemRangeRemoved(0, mInitOrders.size());
        mInitOrders.clear();
    }

    @Override
    public void onRefresh() {
        if (CustomApplication.isConnect) {
            sendPublishedOrderRequest(NOT_FIRST_TIME);
        } else {
            setRefreshState(false);
        }
    }


    /**
     * 发送查看已发布订单请求
     */
    public void sendPublishedOrderRequest(String isFirstTime) {
        //获取查看个人已发布订单请求的参数集
        Map<String, String> params = ParamsGenerateUtil.generatePublishedOrderParam(isFirstTime);

        //创建查看已发布订单请求
        CommonRequest publishedOrderRequest = new CommonRequest(getContext(),
                IPConfig.orderPublishedAddress,
                mCustomApplication.generateHeaderMap(),
                params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resp) {
                        Log.i("order_publish_resp", resp);
                        if (!JsonUtil.decodeByJsonObject(resp, "code").equals("overdue")) {
                            //解析获取到的订单数据
                            updateData(JsonUtil.decodeByGson(resp, OrderPublishedBean.class).getOrders());
                        } else {
                            deleteAll();
                        }
                        //关闭刷新
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
        if (CustomApplication.isConnect) {
            orderPublishedDetail.putExtra(ORDER_ID, mInitOrders.get(position).getOrder_id());
        }
        mMainActivity.startActivity(orderPublishedDetail);
    }

    //当从别的Tab切换回来的时候重新加载数据
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventOrderFragmentVisible(FragmentUserVisibleBean fragmentUserVisibleBean) {
        if (fragmentUserVisibleBean.isVisible()
                && isViewCreated
                && getUserVisibleHint()) {
            deleteAll();
            intData();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i(TAG, "UserVisible:" + isVisibleToUser);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
