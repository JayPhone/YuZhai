package com.yuzhai.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.yuzhai.adapter.WorkRecyclerViewAdapter;
import com.yuzhai.config.IPConfig;
import com.yuzhai.entry.responseBean.OrderByTypeBean;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.ParamsGenerateUtil;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.recyclerview.DividerItemDecoration;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.util.TypeUtil;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/22.
 */
public class WorkFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mWorkSrl;
    private RecyclerView mWorkRv;
    private WorkRecyclerViewAdapter mAdapter;
    private Activity mMainActivity;

    private CustomApplication mCustomApplication;
    private RequestQueue mRequestQueue;
    private List<OrderByTypeBean.OrderBean> mOrders;
    private int mType = 0;
    private final static String TYPE = "type";

    /**
     * 获取WorkFragment实例
     *
     * @return WorkFragment实例
     */
    public static WorkFragment newInstance() {
        Bundle data = new Bundle();
//        data.putInt(TYPE, type);
        WorkFragment workFragment = new WorkFragment();
        workFragment.setArguments(data);
        return workFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.category_viewpager_work_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainActivity = getActivity();
        mCustomApplication = (CustomApplication) mMainActivity.getApplication();
        mRequestQueue = RequestQueueSingleton.getInstance(mMainActivity).getRequestQueue();
//        mType = getArguments().getInt(TYPE);
        //初始化控件
        initViews();
        intData();
    }


    /**
     * 初始化组件
     */
    public void initViews() {
        mWorkSrl = (SwipeRefreshLayout) mMainActivity.findViewById(R.id.work_refresh);
        //设置下拉刷新监听
        mWorkSrl.setOnRefreshListener(this);
        //设置刷新样式
        mWorkSrl.setColorSchemeResources(R.color.mainColor);

        mWorkRv = (RecyclerView) mMainActivity.findViewById(R.id.work_recycler_view);
        mWorkRv.setLayoutManager(new LinearLayoutManager(mMainActivity));
        mWorkRv.addItemDecoration(new DividerItemDecoration(mMainActivity, DividerItemDecoration.VERTICAL_LIST));
        mWorkRv.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new WorkRecyclerViewAdapter(mMainActivity);
        mWorkRv.setAdapter(mAdapter);
    }

    private void intData() {
        sendOrdersByTypeRequest(TypeUtil.getTypeText(mType));
    }


    @Override
    public void onRefresh() {
        sendOrdersByTypeRequest(TypeUtil.getTypeText(mType));
    }

    /**
     * 发送通过类型查询订单请求
     *
     * @param type 项目类型
     */
    public void sendOrdersByTypeRequest(String type) {
        //获取通过类型查询订单请求的参数集
        Map<String, String> params = ParamsGenerateUtil.generateOrdersByTypeParams(type);

        //创建通过类型查询订单请求
        CommonRequest ordersByTypeRequest = new CommonRequest(IPConfig.ordersByTypeAddress,
                null,
                params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resp) {
                        Log.i("work_fragment_resp", resp);
                        mOrders = JsonUtil.decodeByGson(resp, OrderByTypeBean.class).getOrder();
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
     * 生成请求头参数集
     *
     * @return 返回请求头参数集
     */
}
