package com.yuzhai.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuzhai.adapter.OrderViewPagerAdapter;
import com.yuzhai.view.OrderViewPager;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/10.
 */
public class OrderFragment extends Fragment {
    private Activity mMainActivity;

    private OrderViewPager mViewPager;
    private OrderViewPagerAdapter mPagerAdapter;
    private TabLayout mTabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //获取Activity的实例
        mMainActivity = getActivity();
        //初始化组件
        initViews();
        //初始化ViewPager
        initViewPager();
    }


    /**
     * 初始化组件
     */
    public void initViews() {
    }


    /**
     * 添加viewPager的页面，已发布订单的界面和已接收订单的界面
     */
    public void initViewPager() {
        //已发布订单Fragment
        OrderPublishedFragment publishedFragment = new OrderPublishedFragment();
        //己接收订单Fragment
        OrderAcceptedFragment acceptedFragment = new OrderAcceptedFragment();

        //添加viewPager的页面布局到List<View>里
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(publishedFragment);
        fragmentList.add(acceptedFragment);

        //创建viewPager的适配器并设置
        mViewPager = (OrderViewPager) mMainActivity.findViewById(R.id.order_viewPager);
        mPagerAdapter = new OrderViewPagerAdapter(getChildFragmentManager(), fragmentList);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout = (TabLayout) mMainActivity.findViewById(R.id.tab_layout);
        if (mTabLayout != null) {
            mTabLayout.setupWithViewPager(mViewPager);
        }
    }
}
