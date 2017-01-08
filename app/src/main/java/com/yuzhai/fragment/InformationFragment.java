package com.yuzhai.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/24.
 */
public class InformationFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        AdapterView.OnItemClickListener {
    private SwipeRefreshLayout mInfoSrl;
    private ListView mInfoLv;
    private Activity mMainActivity;

    private CustomApplication mCustomApplication;
    private RequestQueue mRequestQueue;
    private String mInfoResponse;
    private List<Map<String, Object>> mOrders;
    private String mType = "";
    private final static String TYPE = "type";

    /**
     * 获取InformationFragment实例
     *
     * @return InformationFragment实例
     */
    public static InformationFragment newInstance(String type) {
        Bundle data = new Bundle();
        data.putString(TYPE, type);
        InformationFragment fragment = new InformationFragment();
        fragment.setArguments(data);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.category_viewpager_info_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainActivity = getActivity();
        mCustomApplication = (CustomApplication) mMainActivity.getApplication();
        mRequestQueue = RequestQueueSingleton.getRequestQueue(mMainActivity);
        mType = getArguments().getString(TYPE);
        //初始化控件
        initViews();
        //初始化数据
        initData();
    }

    /**
     * 初始化组件
     */
    public void initViews() {
        mInfoSrl = (SwipeRefreshLayout) getView().findViewById(R.id.info_refresh);
        mInfoLv = (ListView) getView().findViewById(R.id.info_listview);

        //设置下拉刷新监听
        mInfoSrl.setOnRefreshListener(this);
        //设置刷新样式
        mInfoSrl.setColorSchemeResources(R.color.mainColor);
        //设置ListView的子项点击监听
        mInfoLv.setOnItemClickListener(this);
    }

    /**
     * 初始化数据
     */
    public void initData() {
        setRefreshState(true);
//        sendInfoByTypeRequest(TypeUtil.getTypeText(mType));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onRefresh() {
        setRefreshState(true);
    }

    public void sendInfoByTypeRequest(String type) {
        //创建通过类型获取资讯请求
        CommonRequest infoByTypeRequest = new CommonRequest(null,
                null,
                null,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resp) {
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
        mRequestQueue.add(infoByTypeRequest);
    }

    /**
     * 设置是否显示刷新状态
     *
     * @param state 刷新状态
     */
    public void setRefreshState(Boolean state) {
        mInfoSrl.setRefreshing(state);
    }
}
