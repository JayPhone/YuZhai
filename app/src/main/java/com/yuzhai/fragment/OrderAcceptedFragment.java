package com.yuzhai.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.yuzhai.activity.OrdersAcceptedActivity;
import com.yuzhai.adapter.AcceptedRecyclerViewAdapter;
import com.yuzhai.bean.innerBean.FragmentUserVisibleBean;
import com.yuzhai.bean.innerBean.LoginToOrderBean;
import com.yuzhai.bean.responseBean.OrderAcceptedBean;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.IPConfig;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/21.
 */
public class OrderAcceptedFragment extends BaseLazyLoadFragment implements
        SwipeRefreshLayout.OnRefreshListener,
        AcceptedRecyclerViewAdapter.OnAcceptedItemClickListener {
    private static final String TAG = "OrderAcceptedFragment";

    private SwipeRefreshLayout mAcceptedSrl;
    private RecyclerView mAcceptedRv;
    private AcceptedRecyclerViewAdapter mAdapter;
    private Activity mMainActivity;

    private CustomApplication mCustomApplication;
    private RequestQueue mRequestQueue;
    private List<OrderAcceptedBean.OrderBean> mInitOrders = new ArrayList<>();
    public final static String ORDER_ID = "order_id";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_viewpager_accepted_layout, container, false);
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
        mAcceptedSrl = (SwipeRefreshLayout) getView().findViewById(R.id.accepted_order_refresh);
        //设置下拉刷新监听
        mAcceptedSrl.setOnRefreshListener(this);
        //设置刷新样式
        mAcceptedSrl.setColorSchemeResources(R.color.mainColor);

        mAcceptedRv = (RecyclerView) getView().findViewById(R.id.accepted_recyclerView);
        mAcceptedRv.setLayoutManager(new LinearLayoutManager(mMainActivity));
        mAcceptedRv.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new AcceptedRecyclerViewAdapter(this, mInitOrders);
        mAdapter.setOnAcceptedItemClickListener(this);
        mAcceptedRv.setAdapter(mAdapter);
    }

    /**
     * 初始化数据
     */
    private void intData() {
        if (CustomApplication.isConnect) {
            setRefreshState(true);
            sendAcceptedOrderRequest();
        }
    }

    /**
     * 懒加载数据
     */
    @Override
    protected void lazyLoadData() {
        super.lazyLoadData();
        if (isViewCreated) {
            intData();
        }
    }

    @Override
    public void onRefresh() {
        if (CustomApplication.isConnect) {
            sendAcceptedOrderRequest();
        } else {
            setRefreshState(false);
        }
    }

    /**
     * 更新订单数据
     *
     * @param newOrders 新获取的订单数据集
     */
    public void updateData(List<OrderAcceptedBean.OrderBean> newOrders) {
        for (OrderAcceptedBean.OrderBean order : newOrders) {
            //将获取的新数据插入到数据集
            mInitOrders.add(order);
        }
        //通知recyclerView插入数据
        mAdapter.notifyItemRangeInserted(0, newOrders.size());
        //recyclerView滚动到顶部
        mAcceptedRv.smoothScrollToPosition(0);

    }

    /**
     * 清空所有数据
     */
    public void deleteAll() {
        mAdapter.notifyItemRangeRemoved(0, mInitOrders.size());
        mInitOrders.clear();
    }

    public void sendAcceptedOrderRequest() {
        //获取查看个人已发布订单请求的参数集
//        Map<String, String> params = ParamsGenerateUtil.generateAcceptedOrderParam();

        //创建查看已接收订单请求
        CommonRequest acceptedOrderRequest = new CommonRequest(getContext(),
                IPConfig.orderAcceptedAddress,
                mCustomApplication.generateHeaderMap(),
                null,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resp) {
                        Log.i("order_accept_resp", resp);
                        if (!JsonUtil.decodeByJsonObject(resp, "code").equals("overdue")) {
                            //解析获取到的订单数据
                            updateData(JsonUtil.decodeByGson(resp, OrderAcceptedBean.class).getOrders());
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
//        mRequestQueue.add(acceptedOrderRequest);
    }

    /**
     * 设置是否显示刷新状态
     *
     * @param state 刷新状态
     */
    public void setRefreshState(Boolean state) {
        mAcceptedSrl.setRefreshing(state);
    }

//    /**
//     * 通过EventBus传递的数据判断消息并作出回应
//     */
//    @Subscribe(threadMode = ThreadMode.POSTING)
//    public void onEventUserLogin(LoginToOrderBean loginToOrderBean) {
//        if (loginToOrderBean.isLogin()) {
//            deleteAll();
//            sendAcceptedOrderRequest();
//        } else if (!loginToOrderBean.isLogin()) {
//            deleteAll();
//        }
//    }

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

    @Override
    public void onAcceptedItemClick(int position) {
        Intent ordersAccepted = new Intent(mMainActivity, OrdersAcceptedActivity.class);
        mMainActivity.startActivity(ordersAccepted);
    }
}
