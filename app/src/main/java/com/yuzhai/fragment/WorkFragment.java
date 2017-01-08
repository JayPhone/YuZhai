package com.yuzhai.fragment;

import android.app.Activity;
import android.content.Intent;
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
import com.yuzhai.activity.WorkDetailActivity;
import com.yuzhai.adapter.WorkRecyclerViewAdapter;
import com.yuzhai.bean.responseBean.SimpleOrderByTypeBean;
import com.yuzhai.config.IPConfig;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.ParamsGenerateUtil;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/22.
 */
public class WorkFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, WorkRecyclerViewAdapter.OnWorkItemClickListener {
    private SwipeRefreshLayout mWorkSrl;
    private RecyclerView mWorkRv;
    private WorkRecyclerViewAdapter mAdapter;
    private Activity mMainActivity;

    private CustomApplication mCustomApplication;
    private RequestQueue mRequestQueue;
    private List<SimpleOrderByTypeBean.SimpleOrderBean> mInitOrders;
    private List<SimpleOrderByTypeBean.SimpleOrderBean> mNewOrders;

    private String mType = "";
    private final static String TYPE = "type";
    public final static String ORDER_ID = "order_id";

    /**
     * 测试数据
     */
    private List<Map<String, Object>> mData;
    private String[] titles = new String[]{
            "帮我做一个很厉害的APP，记住，是很厉害的，普通厉害的不要!!",
            "帮我做一个很厉害的APP，记住，是很厉害的，普通厉害的不要!!",
            "帮我做一个很厉害的APP，记住，是很厉害的，普通厉害的不要!!",
            "帮我做一个很厉害的APP，记住，是很厉害的，普通厉害的不要!!"};
    private String[] dates = new String[]{"16-10-02", "16-10-03", "16-10-04", "16-10-05"};
    private String[] limits = new String[]{"5天", "15天", "20天", "50天"};
    private String[] prices = new String[]{"200", "300", "50", "100"};
    private int[] images = new int[]{R.drawable.test1, R.drawable.test2, R.drawable.test3, R.drawable.test4};
    /**
     * 测试数据
     */

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
        return inflater.inflate(R.layout.category_viewpager_work_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainActivity = getActivity();
        mCustomApplication = (CustomApplication) getActivity().getApplication();
        mRequestQueue = RequestQueueSingleton.getRequestQueue(mMainActivity);
        mType = getArguments().getString(TYPE);
        //初始化控件
        intData();
        initViews();
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
//        mAdapter = new WorkRecyclerViewAdapter(mMainActivity, mData);
    }

    private void intData() {
        /*正常代码*/
        //初始化订单数据集
        mInitOrders = new ArrayList<>();
        sendOrdersByTypeRequest(mType, mCustomApplication.getToken());
        /*正常代码*/

        /*测试代码*/
//        if (!CustomApplication.isConnect) {
//            /*测试代码*/
//            mData = new ArrayList<>();
//            for (int i = 0; i < titles.length; i++) {
//                Map<String, Object> map = new HashMap<>();
//                map.put("title", titles[i]);
//                map.put("date", dates[i]);
//                map.put("limit", limits[i]);
//                map.put("price", prices[i]);
//                map.put("image", images[i]);
//                mData.add(map);
//            }
//        }
        /*测试代码*/
    }

    @Override
    public void onRefresh() {
        /*正常代码*/
        if (mCustomApplication.isConnect) {
            sendOrdersByTypeRequest(mType, mCustomApplication.getToken());
        }

        /*测试代码*/
//        if (!mCustomApplication.isConnect) {
//            for (int i = 0; i < titles.length; i++) {
//                Map<String, Object> map = new HashMap<>();
//                map.put("title", titles[i]);
//                map.put("date", dates[i]);
//                map.put("limit", limits[i]);
//                map.put("price", prices[i]);
//                map.put("image", images[i]);
//                mData.add(map);
//            }
//            mAdapter.notifyItemRangeInserted(0, titles.length);
//            mWorkRv.smoothScrollToPosition(0);
//            setRefreshState(false);
//        }
        /*测试代码*/
    }

    /**
     * 更新订单数据
     *
     * @param newOrders 新获取的订单数据集
     */
    public void updateData(List<SimpleOrderByTypeBean.SimpleOrderBean> newOrders) {
        if (null == mAdapter) {
            mAdapter = new WorkRecyclerViewAdapter(this, newOrders);
            mAdapter.setOnWorkItemClickListener(WorkFragment.this);
            mWorkRv.setAdapter(mAdapter);
        } else {
            for (SimpleOrderByTypeBean.SimpleOrderBean order : newOrders) {
                //将获取的新数据插入到数据集
                mInitOrders.add(order);
            }
            //通知recyclerView插入数据
            mAdapter.notifyItemRangeInserted(0, newOrders.size());
            //recyclerView滚动到顶部
            mWorkRv.smoothScrollToPosition(0);
        }
    }

    /**
     * 发送通过类型查询订单请求
     *
     * @param type 项目类型
     */
    public void sendOrdersByTypeRequest(String type, String token) {
        //获取通过类型查询订单请求的参数集
        Map<String, String> params = ParamsGenerateUtil.generateOrdersByTypeParams(type, token);
        Log.i("work_params", params.toString());

        //创建通过类型查询订单请求
        CommonRequest ordersByTypeRequest = new CommonRequest(IPConfig.ordersByTypeAddress,
                mCustomApplication.generateCookieMap(),
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
        startActivity(workDetail);
    }
}
