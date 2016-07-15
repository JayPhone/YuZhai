package com.yuzhai.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2016/7/14.
 */
public class CategoryViewPagerAdapter extends PagerAdapter {
    List<View> viewLists;

    public CategoryViewPagerAdapter(List<View> lists) {
        viewLists = lists;
    }

    @Override
    public int getCount() {                                                                 //获得size
        return viewLists.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup viewGroup, int position, Object object) {
        ((ViewPager) viewGroup).removeView(viewLists.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int position) {
        ((ViewPager) viewGroup).addView(viewLists.get(position), 0);
        return viewLists.get(position);
    }
}
