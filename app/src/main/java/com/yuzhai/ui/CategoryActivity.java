package com.yuzhai.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuzhai.adapter.CategoryRecyclerViewAdapter;
import com.yuzhai.adapter.CategoryViewPagerAdapter;
import com.yuzhai.fragment.InformationFragment;
import com.yuzhai.fragment.ResumeFragment;
import com.yuzhai.fragment.WorkFragment;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/4.
 */
public class CategoryActivity extends AppCompatActivity {

    private ViewPager mCategoryViewPager;
    private CategoryViewPagerAdapter mCategoryViewPagerAdapter;
    private TabLayout mTabLayout;
    private ImageView mBackIcon;
    private TextView mTitle;
    private String mProjectType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        //获取项目的类型
        mProjectType = getIntent().getStringExtra(CategoryRecyclerViewAdapter.TITLE);
        //初始化组件
        initViews();
    }

    /**
     * 初始化组件
     */
    public void initViews() {
        mBackIcon = (ImageView) findViewById(R.id.back_image);
        if (mBackIcon != null) {
            mBackIcon.setImageResource(R.drawable.back);
            mBackIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        mTitle = (TextView) findViewById(R.id.title_text);
        if (mTitle != null) {
            mTitle.setText(mProjectType);
        }

        //工作Fragment
        WorkFragment workFragment = WorkFragment.newInstance();
        //资讯Fragment
        InformationFragment informationFragment = InformationFragment.newInstance();
        //简历Fragment
        ResumeFragment resumeFragment = ResumeFragment.newInstance();

        //添加viewPager的页面布局到List<View>里
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(workFragment);
        fragmentList.add(informationFragment);
        fragmentList.add(resumeFragment);

        //创建viewPager的适配器并设置
        mCategoryViewPager = (ViewPager) findViewById(R.id.category_viewPager);
        mCategoryViewPagerAdapter = new CategoryViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        mCategoryViewPager.setAdapter(mCategoryViewPagerAdapter);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (mTabLayout != null) {
            mTabLayout.setupWithViewPager(mCategoryViewPager);
        }
    }
}
