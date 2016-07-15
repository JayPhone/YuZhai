package com.yuzhai.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2016/7/13.
 */
public class OrderViewPager extends ViewPager {

    private boolean isCanScroll = false;

    public OrderViewPager(Context context) {
        super(context);
    }

    public OrderViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (isCanScroll) {
            return super.onTouchEvent(motionEvent);
        } else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (isCanScroll) {
            return super.onInterceptTouchEvent(motionEvent);
        } else {
            return false;
        }
    }
}
