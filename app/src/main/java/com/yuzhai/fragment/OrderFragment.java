package com.yuzhai.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuzhai.adapter.OrderViewPagerAdapter;
import com.yuzhai.view.OrderViewPager;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/10.
 */
public class OrderFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private Activity mMainActivity;

    private TextView mPublishedTitle;
    private TextView mAcceptedTitle;
    private OrderViewPager mViewPager;
    private OrderViewPagerAdapter mPagerAdapter;

    private ImageView cursorImageView;
    private Bitmap cursor;
    private int currentItem;
    private int offSet;
    private int cursorWidth;
    private Matrix matrix;

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
        //初始化上方的切换游标
        initCursor();
    }


    /**
     * 初始化组件
     */
    public void initViews() {
        cursorImageView = (ImageView) mMainActivity.findViewById(R.id.cursor);
        mViewPager = (OrderViewPager) mMainActivity.findViewById(R.id.order_viewPager);
        mPublishedTitle = (TextView) mMainActivity.findViewById(R.id.title_published);
        mAcceptedTitle = (TextView) mMainActivity.findViewById(R.id.title_accepted);

        mPublishedTitle.setOnClickListener(this);
        mAcceptedTitle.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击发布标题导航
            case R.id.title_published:
                mViewPager.setCurrentItem(0);
                break;

            //点击接收标题导航
            case R.id.title_accepted:
                mViewPager.setCurrentItem(1);
                break;
        }
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
        mPagerAdapter = new OrderViewPagerAdapter(getChildFragmentManager(), fragmentList);
        mViewPager.setAdapter(mPagerAdapter);

        //设置页面切换监听
        mViewPager.addOnPageChangeListener(this);
    }

    /**
     * 初始化上方的切换游标
     */
    public void initCursor() {
        matrix = new Matrix();
        cursor = BitmapFactory.decodeResource(getResources(), R.drawable.line);
        cursorWidth = cursor.getWidth();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        offSet = (displayMetrics.widthPixels - 2 * cursorWidth) / 4;
        matrix.setTranslate(offSet, 0);
        cursorImageView.setImageMatrix(matrix);
        currentItem = 0;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Animation translateAnim;
        switch (position) {
            case 0:
                if (currentItem == 1) {
                    translateAnim = new TranslateAnimation(offSet * 2 + cursorWidth, 0, 0, 0);
                    translateAnim.setDuration(150);
                    translateAnim.setFillAfter(true);
                    cursorImageView.startAnimation(translateAnim);
                }
                break;
            case 1:
                if (currentItem == 0) {
                    translateAnim = new TranslateAnimation(0, offSet * 2 + cursorWidth, 0, 0);
                    translateAnim.setDuration(150);
                    translateAnim.setFillAfter(true);
                    cursorImageView.startAnimation(translateAnim);
                }
                break;
        }
        currentItem = position;
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
