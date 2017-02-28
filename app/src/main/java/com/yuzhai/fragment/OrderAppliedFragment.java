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
import com.yuzhai.activity.OrdersAppliedActivity;
import com.yuzhai.activity.OrdersPublishedActivity;
import com.yuzhai.adapter.AppliedRecyclerViewAdapter;
import com.yuzhai.bean.innerBean.FragmentUserVisibleBean;
import com.yuzhai.bean.responseBean.OrderAppliedBean;
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
 * Created by 35429 on 2017/2/20.
 */

public class OrderAppliedFragment extends BaseLazyLoadFragment implements
        SwipeRefreshLayout.OnRefreshListener,
        AppliedRecyclerViewAdapter.OnAppliedItemClickListener {
    private static final String TAG = "OrderAppliedFragment";

    private SwipeRefreshLayout mAppliedSrl;
    private RecyclerView mAppliedRv;
    private AppliedRecyclerViewAdapter mAdapter;
    private Activity mMainActivity;

    private CustomApplication mCustomApplication;
    private RequestQueue mRequestQueue;
    private List<OrderAppliedBean.OrderBean> mInitOrders = new ArrayList<>();

    public final static String ORDER_ID = "order_id";
    private final static String IS_FIRST_TIME = "yes";
    private final static String NOT_FIRST_TIME = "no";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_viewpager_applied_layout, container, false);
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
    }

    /**
     * 初始化控件
     */
    public void initViews() {
        mAppliedSrl = (SwipeRefreshLayout) getView().findViewById(R.id.applied_order_refresh);
        //设置下拉刷新监听
        mAppliedSrl.setOnRefreshListener(this);
        //设置刷新样式
        mAppliedSrl.setColorSchemeResources(R.color.mainColor);

        mAppliedRv = (RecyclerView) getView().findViewById(R.id.applied_recyclerView);
        mAppliedRv.setLayoutManager(new LinearLayoutManager(mMainActivity));
        mAppliedRv.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new AppliedRecyclerViewAdapter(this, mInitOrders);
        mAdapter.setOnAppliedItemClickListener(this);
        mAppliedRv.setAdapter(mAdapter);
    }

    /**
     * 初始化数据
     */
    private void intData() {
        if (CustomApplication.isConnect) {
            setRefreshState(true);
            sendAppliedOrderRequest(IS_FIRST_TIME);
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
    public void updateData(List<OrderAppliedBean.OrderBean> newOrders) {
        Collections.reverse(newOrders);
        mInitOrders.addAll(0, newOrders);
        //通知recyclerView插入数据
        mAdapter.notifyDataSetChanged();
        //recyclerView滚动到顶部
        mAppliedRv.smoothScrollToPosition(0);
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
            sendAppliedOrderRequest(NOT_FIRST_TIME);
        } else {
            setRefreshState(false);
        }
    }


    /**
     * 发送查看已申请订单请求
     */
    public void sendAppliedOrderRequest(String isFirstTime) {
        //获取查看个人已申请订单请求的参数集
        Map<String, String> params = ParamsGenerateUtil.generateAppliedOrderParam(isFirstTime);

        //创建查看已申请订单请求
        CommonRequest appliedOrderRequest = new CommonRequest(getContext(),
                IPConfig.orderAppliedAddress,
                mCustomApplication.generateHeaderMap(),
                params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resp) {
                        Log.i(TAG, "order_apply_resp:" + resp);
                        if (!JsonUtil.decodeByJsonObject(resp, "code").equals("overdue")) {
                            //解析获取到的订单数据
                            updateData(JsonUtil.decodeByGson(resp, OrderAppliedBean.class).getOrders());
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
        mRequestQueue.add(appliedOrderRequest);
    }

    /**
     * 设置是否显示刷新状态
     *
     * @param state 刷新状态
     */
    public void setRefreshState(Boolean state) {
        mAppliedSrl.setRefreshing(state);
    }

    @Override
    public void onAppliedItemClick(int position) {
        Intent orderAppliedDetail = new Intent(mMainActivity, OrdersAppliedActivity.class);
        if (CustomApplication.isConnect) {
            orderAppliedDetail.putExtra(ORDER_ID, mInitOrders.get(position).getOrderId());
        }
        mMainActivity.startActivity(orderAppliedDetail);
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
