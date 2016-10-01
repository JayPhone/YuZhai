package com.yuzhai.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuzhai.adapter.CategoryViewPagerAdapter;
import com.yuzhai.fragment.InformationFragment;
import com.yuzhai.fragment.ResumeFragment;
import com.yuzhai.fragment.WorkFragment;
import com.yuzhai.recyclerview.RecyclerViewAdapter;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/4.
 */
public class CategoryActivity extends AppCompatActivity implements View.OnClickListener,
        ViewPager.OnPageChangeListener {

    private ViewPager categoryViewPager;
    private CategoryViewPagerAdapter categoryViewPagerAdapter;

    private TextView titleWork;
    private TextView titleInfo;
    private TextView titleResume;
    private TextView categoryTitle;
    private ImageView cursorImageView;
    private ImageView backImage;

    private Bitmap cursor;
    private int currentItem;
    private Animation translateAnimation;
    private int offSet;
    private int cursorWidth;
    private Matrix matrix;

    private String mProjectType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        //获取项目的类型
        mProjectType = getIntent().getStringExtra(RecyclerViewAdapter.TITLE);
        //初始化组件
        initViews();
        //初始化ViewPager
        initViewPagerView();
        //初始化上方的切换游标
        initCursor();
    }

    /**
     * 初始化组件
     */
    public void initViews() {
        cursorImageView = (ImageView) findViewById(R.id.cursor);
        backImage = (ImageView) findViewById(R.id.back_image);
        categoryViewPager = (ViewPager) findViewById(R.id.category_viewPager);
        titleWork = (TextView) findViewById(R.id.title_work);
        titleInfo = (TextView) findViewById(R.id.title_info);
        titleResume = (TextView) findViewById(R.id.title_resume);
        categoryTitle = (TextView) findViewById(R.id.category_title);
        categoryTitle.setText(mProjectType);

        titleWork.setOnClickListener(this);
        titleInfo.setOnClickListener(this);
        titleResume.setOnClickListener(this);
        backImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_image:
                finish();
                break;
            case R.id.title_work:
                categoryViewPager.setCurrentItem(0);
                break;
            case R.id.title_info:
                categoryViewPager.setCurrentItem(1);
                break;
            case R.id.title_resume:
                categoryViewPager.setCurrentItem(2);
                break;
        }
    }

    //添加viewPager的页面,工作Fragment、资讯Fragment和简历Fragment
    public void initViewPagerView() {
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
        categoryViewPagerAdapter = new CategoryViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        categoryViewPager.setAdapter(categoryViewPagerAdapter);

        //设置页面切换监听
        categoryViewPager.addOnPageChangeListener(this);
    }

    /**
     * 初始化游标
     */
    public void initCursor() {
        matrix = new Matrix();
        cursor = BitmapFactory.decodeResource(getResources(), R.drawable.line);
        cursorWidth = cursor.getWidth();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        offSet = (displayMetrics.widthPixels - 3 * cursorWidth) / 6;
        matrix.setTranslate(offSet, 0);
        cursorImageView.setImageMatrix(matrix);
        currentItem = 0;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        switch (position) {
            case 0:
                if (currentItem == 1) {
                    translateAnimation = new TranslateAnimation(
                            offSet * 2 + cursorWidth, 0, 0, 0);
                    translateAnimation.setDuration(150);
                    translateAnimation.setFillAfter(true);
                    cursorImageView.startAnimation(translateAnimation);
                } else if (currentItem == 2) {
                    translateAnimation = new TranslateAnimation(offSet * 4 + 2
                            * cursorWidth, 0, 0, 0);
                    translateAnimation.setDuration(150);
                    translateAnimation.setFillAfter(true);
                    cursorImageView.startAnimation(translateAnimation);
                }

                break;
            case 1:
                if (currentItem == 0) {
                    translateAnimation = new TranslateAnimation(0, offSet * 2
                            + cursorWidth, 0, 0);
                    translateAnimation.setDuration(150);
                    translateAnimation.setFillAfter(true);
                    cursorImageView.startAnimation(translateAnimation);
                } else if (currentItem == 2) {
                    translateAnimation = new TranslateAnimation(4 * offSet + 2
                            * cursorWidth, offSet * 2 + cursorWidth, 0, 0);
                    translateAnimation.setDuration(150);
                    translateAnimation.setFillAfter(true);
                    cursorImageView.startAnimation(translateAnimation);
                }

                break;
            case 2:
                if (currentItem == 0) {
                    translateAnimation = new TranslateAnimation(0, 4 * offSet + 2
                            * cursorWidth, 0, 0);
                    translateAnimation.setDuration(150);
                    translateAnimation.setFillAfter(true);
                    cursorImageView.startAnimation(translateAnimation);
                } else if (currentItem == 1) {
                    translateAnimation = new TranslateAnimation(
                            offSet * 2 + cursorWidth, 4 * offSet + 2 * cursorWidth,
                            0, 0);
                    translateAnimation.setDuration(150);
                    translateAnimation.setFillAfter(true);
                    cursorImageView.startAnimation(translateAnimation);
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
