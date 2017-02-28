package com.yuzhai.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuzhai.adapter.OrderViewPagerAdapter;
import com.yuzhai.bean.innerBean.FragmentUserVisibleBean;
import com.yuzhai.yuzhaiwork.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Administrator on 2016/6/10.
 */
public class OrderFragment extends Fragment {
    private static final String TAG = "OrderFragment";

    private Activity mMainActivity;

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private OrderViewPagerAdapter mPagerAdapter;
    private TabLayout mTabLayout;

    private OrderPublishedFragment mPublishedFragment;
    private OrderAppliedFragment mAppliedFragment;
    private OrderAcceptedFragment mAcceptedFragment;

    public static OrderFragment newInstance() {

        Bundle args = new Bundle();

        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //获取Activity的实例
        mMainActivity = getActivity();
        //初始化ViewPager
        initView();
    }

    /**
     * 添加viewPager的页面，已发布订单的界面和已接收订单的界面
     */
    public void initView() {
        mToolbar = (Toolbar) getView().findViewById(R.id.order_toolbar);
        mToolbar.setTitle("我的订单");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer);
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        //已发布订单Fragment
        mPublishedFragment = new OrderPublishedFragment();
        //已申请订单Fragment
        mAppliedFragment = new OrderAppliedFragment();
        //己接收订单Fragment
        mAcceptedFragment = new OrderAcceptedFragment();

        //添加viewPager的页面布局到List<View>里
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(mPublishedFragment);
        fragmentList.add(mAppliedFragment);
        fragmentList.add(mAcceptedFragment);

        //创建viewPager的适配器并设置
        mViewPager = (ViewPager) getView().findViewById(R.id.order_viewPager);
        //加载全部的Fragment布局，实现懒加载
        mViewPager.setOffscreenPageLimit(3);
        mPagerAdapter = new OrderViewPagerAdapter(getChildFragmentManager(), fragmentList);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout = (TabLayout) getView().findViewById(R.id.tab_layout);
        if (mTabLayout != null) {
            mTabLayout.setupWithViewPager(mViewPager);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i(TAG, "UserVisible:" + isVisibleToUser);
//        if (mAcceptedFragment != null) {
//            Log.i(TAG, "AcceptedFragment visible:" + mAcceptedFragment.getUserVisibleHint());
//        }
//        if (mPublishedFragment != null) {
//            Log.i(TAG, "PublishedFragment visible:" + mPublishedFragment.getUserVisibleHint());
//        }
        EventBus.getDefault().post(new FragmentUserVisibleBean(isVisibleToUser));
    }
}
