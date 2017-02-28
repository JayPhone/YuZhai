package com.yuzhai.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.yuzhai.activity.WorkDetailActivity;
import com.yuzhai.adapter.WorkRecyclerViewAdapter;
import com.yuzhai.bean.responseBean.SimpleOrderByTypeBean;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.IPConfig;
import com.yuzhai.http.ParamsGenerateUtil;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/22.
 */
public class WorkFragment extends BaseLazyLoadFragment implements SwipeRefreshLayout.OnRefreshListener,
        WorkRecyclerViewAdapter.OnWorkItemClickListener {
    private static final String TAG = "WorkFragment";

    private SwipeRefreshLayout mWorkSrl;
    private RecyclerView mWorkRv;
    private WorkRecyclerViewAdapter mAdapter;
    private Activity mMainActivity;

    private CustomApplication mCustomApplication;
    private RequestQueue mRequestQueue;
    private List<SimpleOrderByTypeBean.SimpleOrderBean> mInitOrders = new ArrayList<>();
    private List<SimpleOrderByTypeBean.SimpleOrderBean> mNewOrders;

    //第一次显示时初始化数据
    private Boolean isShowed = false;
    private String mType = "";
    private final static String TYPE = "type";
    public final static String ORDER_ID = "order_id";
    private final static String IS_FIRST_TIME = "yes";
    private final static String NOT_FIRST_TIME = "no";

    /**
     * 获取WorkFragment实例
     *
     * @return WorkFragment实例
     */
    public static WorkFragment newInstance(String type) {
        Bundle data = new Bundle();
        data.putString(TYPE, type);
        WorkFragment workFragment = new WorkFragment();
        workFragment.setArguments(data);
        return workFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_viewpager_work_layout, container, false);
        isViewCreated = true;
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainActivity = getActivity();
        mCustomApplication = (CustomApplication) getActivity().getApplication();
        mRequestQueue = RequestQueueSingleton.getRequestQueue(mMainActivity);
        mType = getArguments().getString(TYPE);

        initViews();
        intData();
    }


    /**
     * 初始化组件
     */
    public void initViews() {
        mWorkSrl = (SwipeRefreshLayout) getView().findViewById(R.id.work_refresh);
        //设置下拉刷新监听
        mWorkSrl.setOnRefreshListener(this);
        //设置刷新样式
        mWorkSrl.setColorSchemeResources(R.color.mainColor);

        mWorkRv = (RecyclerView) getView().findViewById(R.id.work_recycler_view);
        mWorkRv.setLayoutManager(new LinearLayoutManager(mMainActivity));
        mWorkRv.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new WorkRecyclerViewAdapter(this, mInitOrders);
        mAdapter.setOnWorkItemClickListener(WorkFragment.this);
        mWorkRv.setAdapter(mAdapter);
    }

    /**
     * 初始化数据
     */
    private void intData() {
        if (CustomApplication.isConnect) {
            setRefreshState(true);
            sendOrdersByTypeRequest(mType, IS_FIRST_TIME);
            isShowed = true;
        }
    }

    @Override
    protected void lazyLoadData() {
        super.lazyLoadData();
        if (isViewCreated && !isShowed) {
            isShowed = true;
            intData();
        }
    }

    @Override
    public void onRefresh() {
        /*正常代码*/
        if (CustomApplication.isConnect) {
            sendOrdersByTypeRequest(mType, NOT_FIRST_TIME);
        } else {
            setRefreshState(false);
        }
    }

    /**
     * 更新订单数据
     *
     * @param newOrders 新获取的订单数据集
     */
    public void updateData(List<SimpleOrderByTypeBean.SimpleOrderBean> newOrders) {
        Collections.reverse(newOrders);
        mInitOrders.addAll(0, newOrders);
        //通知recyclerView插入数据
        mAdapter.notifyDataSetChanged();
        //recyclerView滚动到顶部
        mWorkRv.smoothScrollToPosition(0);

    }

    /**
     * 清空所有数据
     */
    public void deleteAll() {
        mAdapter.notifyItemRangeRemoved(0, mInitOrders.size());
        mInitOrders.clear();
    }

    /**
     * 发送通过类型查询订单请求
     *
     * @param type 项目类型
     */
    public void sendOrdersByTypeRequest(String type, String isFirstTime) {
        //获取通过类型查询订单请求的参数集
        Map<String, String> params = ParamsGenerateUtil.generateOrdersByTypeParams(type, isFirstTime);
        Log.i("work_params", params.toString());

        //创建通过类型查询订单请求
        CommonRequest ordersByTypeRequest = new CommonRequest(getContext(),
                IPConfig.ordersByTypeAddress,
                mCustomApplication.generateHeaderMap(),
                params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resp) {
                        Log.i("work_fragment_resp", resp);
                        //解析数据
                        mNewOrders = JsonUtil.decodeByGson(resp, SimpleOrderByTypeBean.class).getOrders();
                        //更新数据
                        updateData(mNewOrders);
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
        mRequestQueue.add(ordersByTypeRequest);
    }

    /**
     * 设置是否显示刷新状态
     *
     * @param state 刷新状态
     */
    public void setRefreshState(Boolean state) {
        mWorkSrl.setRefreshing(state);
    }

    /**
     * 当一个子项被点击时触发
     */
    @Override
    public void onWorkItemClick(int position) {
        Intent workDetail = new Intent(mMainActivity, WorkDetailActivity.class);
        if (CustomApplication.isConnect) {
            workDetail.putExtra(ORDER_ID, mInitOrders.get(position).getOrder_id());
        }
        startActivity(workDetail);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i(TAG, "UserVisible:" + isVisibleToUser);
    }
}
