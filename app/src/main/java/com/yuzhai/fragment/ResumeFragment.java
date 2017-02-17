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
import com.yuzhai.activity.ResumeDetailActivity;
import com.yuzhai.adapter.ResumeRecyclerViewAdapter;
import com.yuzhai.bean.responseBean.SimpleResumeByTypeBean;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.IPConfig;
import com.yuzhai.http.ParamsGenerateUtil;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/24.
 */
public class ResumeFragment extends BaseLazyLoadFragment implements SwipeRefreshLayout.OnRefreshListener,
        ResumeRecyclerViewAdapter.OnResumeItemClickListener {
    private static final String TAG = "ResumeFragment";

    private SwipeRefreshLayout mResumeSrl;
    private RecyclerView mResumeRv;
    private ResumeRecyclerViewAdapter mAdapter;
    private Activity mMainActivity;

    private CustomApplication mCustomApplication;
    private RequestQueue mRequestQueue;
    private List<SimpleResumeByTypeBean.SimpleResumeBean> mInitResumes = new ArrayList<>();
    private List<SimpleResumeByTypeBean.SimpleResumeBean> mNewResumes;

    private String mType = "";
    private final static String TYPE = "type";
    public final static String RESUME_ID = "resume_id";

    /**
     * 获取ResumeFragment实例
     *
     * @return ResumeFragment实例
     */
    public static ResumeFragment newInstance(String type) {
        Bundle data = new Bundle();
        data.putString(TYPE, type);
        ResumeFragment fragment = new ResumeFragment();
        fragment.setArguments(data);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_viewpager_resume_layout, container, false);
        isViewCreated = true;
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainActivity = getActivity();
        mCustomApplication = (CustomApplication) mMainActivity.getApplication();
        mRequestQueue = RequestQueueSingleton.getRequestQueue(mMainActivity);
        mType = getArguments().getString(TYPE);

        initViews();
    }

    /**
     * 初始化组件
     */
    public void initViews() {
        mResumeSrl = (SwipeRefreshLayout) getView().findViewById(R.id.resume_refresh);
        //设置下拉刷新监听
        mResumeSrl.setOnRefreshListener(this);
        //设置刷新样式
        mResumeSrl.setColorSchemeResources(R.color.mainColor);

        mResumeRv = (RecyclerView) getView().findViewById(R.id.resume_recycler_view);
        mResumeRv.setLayoutManager(new LinearLayoutManager(mMainActivity));
        mResumeRv.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ResumeRecyclerViewAdapter(this, mInitResumes);
        mAdapter.setOnResumeItemClickListener(ResumeFragment.this);
        mResumeRv.setAdapter(mAdapter);
    }

    /**
     * 初始化数据
     */
    public void initData() {
        if (CustomApplication.isConnect) {
            setRefreshState(true);
            sendResumeByTypeRequest(mType);
        }
    }

    @Override
    protected void lazyLoadData() {
        super.lazyLoadData();
        if (isViewCreated) {
            initData();
        }
    }

    /**
     * 更新订单数据
     *
     * @param newResumes 新获取的订单数据集
     */
    public void updateData(List<SimpleResumeByTypeBean.SimpleResumeBean> newResumes) {
        for (SimpleResumeByTypeBean.SimpleResumeBean resume : newResumes) {
            //将获取的新数据插入到数据集
            mInitResumes.add(resume);
        }
        //通知recyclerView插入数据
        mAdapter.notifyItemRangeInserted(0, newResumes.size());
        //recyclerView滚动到顶部
        mResumeRv.smoothScrollToPosition(0);

    }

    /**
     * 清空所有数据
     */
    public void deleteAll() {
        mAdapter.notifyItemRangeRemoved(0, mInitResumes.size());
        mInitResumes.clear();
    }

    @Override
    public void onRefresh() {
        if (CustomApplication.isConnect) {
            sendResumeByTypeRequest(mType);
        } else {
            setRefreshState(false);
        }
    }

    public void sendResumeByTypeRequest(String type) {
        //获取通过类型查询订单请求的参数集
        Map<String, String> params = ParamsGenerateUtil.generateResumesByTypeParams(type);
        Log.i("resume_params", params.toString());

        //创建通过类型获取简历请求
        CommonRequest resumeByTypeRequest = new CommonRequest(getContext(),
                IPConfig.resumesByTypeAddress,
                mCustomApplication.generateHeaderMap(),
                params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resp) {
                        Log.i("resume_fragment_resp", resp);
                        //解析数据
                        mNewResumes = JsonUtil.decodeByGson(resp, SimpleResumeByTypeBean.class).getResumes();
                        //更新数据
                        updateData(mNewResumes);
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
//        mRequestQueue.add(resumeByTypeRequest);
    }

    /**
     * 设置是否显示刷新状态
     *
     * @param state 刷新状态
     */
    public void setRefreshState(Boolean state) {
        mResumeSrl.setRefreshing(state);
    }

    @Override
    public void onResumeItemClick(int position) {
        Intent resumeDetail = new Intent(mMainActivity, ResumeDetailActivity.class);
        if (CustomApplication.isConnect) {
            resumeDetail.putExtra(RESUME_ID, mInitResumes.get(position).getUserPhone());
        }
        startActivity(resumeDetail);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i(TAG, "UserVisible:" + isVisibleToUser);
    }
}
