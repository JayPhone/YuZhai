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
import com.yuzhai.util.TypeUtil;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/24.
 */
public class ResumeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        AdapterView.OnItemClickListener {
    private SwipeRefreshLayout mResumeSrl;
    private ListView mResumeLv;
    private Activity mMainActivity;

    private CustomApplication mCustomApplication;
    private RequestQueue mRequestQueue;
    private String mResumeResponse;
    private List<Map<String, Object>> mOrders;
    private int mType;
    private final static String TYPE = "type";
    private final String COOKIE = "cookie";

    /**
     * 获取ResumeFragment实例
     *
     * @return ResumeFragment实例
     */
    public static ResumeFragment newInstance() {
        Bundle data = new Bundle();
//        data.putInt(TYPE, type);
        ResumeFragment fragment = new ResumeFragment();
        fragment.setArguments(data);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.category_viewpager_resume_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainActivity = getActivity();
        mCustomApplication = (CustomApplication) mMainActivity.getApplication();
        mRequestQueue = RequestQueueSingleton.getInstance(mMainActivity).getRequestQueue();
        mType = getArguments().getInt(TYPE);
        //初始化控件
        initViews();
        //初始化数据
        initData();
    }

    /**
     * 初始化组件
     */
    public void initViews() {
        mResumeSrl = (SwipeRefreshLayout) mMainActivity.findViewById(R.id.resume_refresh);
        mResumeLv = (ListView) mMainActivity.findViewById(R.id.resume_listview);

        //设置下拉刷新监听
        mResumeSrl.setOnRefreshListener(this);
        //设置刷新样式
        mResumeSrl.setColorSchemeResources(R.color.mainColor);
        //设置ListView的子项点击监听
        mResumeLv.setOnItemClickListener(this);
    }

    /**
     * 初始化数据
     */
    public void initData() {
        setRefreshState(true);
        sendResumeByTypeRequest(TypeUtil.getTypeText(mType));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onRefresh() {
        setRefreshState(true);
    }

    public void sendResumeByTypeRequest(String type) {
        //创建通过类型获取简历请求
        CommonRequest resumeByTypeRequest = new CommonRequest(null,
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
                        UnRepeatToast.showToast(mMainActivity, "服务器睡着了");
                    }
                });

        //添加到请求队列
        mRequestQueue.add(resumeByTypeRequest);
    }

    /**
     * 设置是否显示刷新状态
     *
     * @param state 刷新状态
     */
    public void setRefreshState(Boolean state) {
        mResumeSrl.setRefreshing(state);
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
