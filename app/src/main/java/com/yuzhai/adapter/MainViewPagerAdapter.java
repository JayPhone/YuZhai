package com.yuzhai.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuzhai.yuzhaiwork.R;

import java.util.List;

/**
 * Created by Administrator on 2016/10/7.
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private List<Fragment> mFragmentList;
    private String[] titles = new String[]{"御宅", "订单", "发布", "聊天"};
    public int[] icons = new int[]{R.drawable.home, R.drawable.order, R.drawable.publish, R.drawable.contact};
    public int[] focusIcons = new int[]{R.drawable.home_click, R.drawable.order_click, R.drawable.publish_click, R.drawable.contact_click};

    public MainViewPagerAdapter(Context context, FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        mContext = context;
        mFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public View getTabView(int position) {
        View tabView = LayoutInflater.from(mContext).inflate(R.layout.main_tab_item_layout, null);
        ImageView tab_icon = (ImageView) tabView.findViewById(R.id.tab_image);
        tab_icon.setImageResource(icons[position]);
        TextView tab_text = (TextView) tabView.findViewById(R.id.tab_text);
        tab_text.setText(titles[position]);
        return tabView;
    }
}
