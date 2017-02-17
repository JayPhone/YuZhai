package com.yuzhai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.yuzhai.adapter.CategoryRecyclerViewAdapter;
import com.yuzhai.adapter.CategoryViewPagerAdapter;
import com.yuzhai.fragment.ResumeFragment;
import com.yuzhai.fragment.WorkFragment;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/4.
 */
public class WorkCategoryActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    private Toolbar mToolbar;
    private ViewPager mCategoryViewPager;
    private CategoryViewPagerAdapter mCategoryViewPagerAdapter;
    private TabLayout mTabLayout;
    private String mType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        //获取项目的类型
        mType = getIntent().getStringExtra(CategoryRecyclerViewAdapter.TITLE);
        //初始化组件
        initViews();
    }

    /**
     * 初始化组件
     */
    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.category_toolbar);
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setTitle(mType);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.inflateMenu(R.menu.category_menu);
        mToolbar.setOnMenuItemClickListener(this);

        //工作Fragment
        WorkFragment workFragment = WorkFragment.newInstance(mType);
        //简历Fragment
        ResumeFragment resumeFragment = ResumeFragment.newInstance(mType);

        //添加viewPager的页面布局到List<View>里
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(workFragment);
        fragmentList.add(resumeFragment);

        //创建viewPager的适配器并设置
        mCategoryViewPager = (ViewPager) findViewById(R.id.category_viewPager);
        mCategoryViewPager.setOffscreenPageLimit(2);
        mCategoryViewPagerAdapter = new CategoryViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        mCategoryViewPager.setAdapter(mCategoryViewPagerAdapter);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (mTabLayout != null) {
            mTabLayout.setupWithViewPager(mCategoryViewPager);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Intent search = new Intent(this, SearchActivity.class);
                startActivity(search);
                break;
        }
        return false;
    }
}
