package com.yuzhai.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.yuzhai.adapter.CategoryViewPagerAdapter;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/4.
 */
public class CategoryActivity extends AppCompatActivity {

    private List<View> categoryViews;

    private ViewPager categoryViewPager;
    private CategoryViewPagerAdapter categoryViewPagerAdapter;

    private TextView titleWork;
    private TextView titleInfo;
    private TextView titleResume;
    private TextView categoryTitle;
    private ImageView cursorImageView;
    private ImageView backImage;

    private SwipeRefreshLayout workRefresh;
    private SwipeRefreshLayout infoRefresh;
    private SwipeRefreshLayout resumeRefresh;
    private ListView workListView;
    private ListView infoListView;
    private ListView resumeListView;
    private View workView;
    private View infoView;
    private View resumeView;

    private Bitmap cursor;
    private int currentItem;
    private Animation translateAnimation;
    private int offSet;
    private int cursorWidth;
    private Matrix matrix;

    private List<Map<String, Object>> works;
    private List<Map<String, Object>> infos;
    private List<Map<String, Object>> resumnes;

    private int title;
    private String[] categoryTexts = new String[]{"软件IT", "音乐制作", "平面设计", "视频拍摄", "游戏研发", "文案撰写", "金融会计"};
    //订单图标类型
    private int[] typeImages = new int[]{R.drawable.it, R.drawable.music, R.drawable.design, R.drawable.movie, R.drawable.game, R.drawable.write, R.drawable.calculate};
    //订单日期
    private String[] dates = new String[]{"2016-07-14", "2016-07-14", "2016-07-14", "2016-07-14", "2016-07-14", "2016-07-14", "2016-07-14"};
    //订单名称
    private String[] names = new String[]{
            "【LOGO设计】千树设计主管标志设计商标设计/设计名称",
            "【LOGO设计】千树设计主管标志设计商标设计/设计名称",
            "【LOGO设计】千树设计主管标志设计商标设计/设计名称",
            "【LOGO设计】千树设计主管标志设计商标设计/设计名称",
            "【LOGO设计】千树设计主管标志设计商标设计/设计名称",
            "【LOGO设计】千树设计主管标志设计商标设计/设计名称",
            "【LOGO设计】千树设计主管标志设计商标设计/设计名称"
    };
    //订单金额
    private String[] prices = new String[]{"100", "150", "200", "250", "300", "350", "400"};
    //期限
    private String[] limits = new String[]{"7天", "15天", "30天", "半年", "一年", "半年", "一年"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        title = getIntent().getIntExtra("title", -1);
        initViews();
        initViewPagerView();
        initCursor();
    }

    //初始化组件
    public void initViews() {
        cursorImageView = (ImageView) findViewById(R.id.cursor);
        backImage = (ImageView) findViewById(R.id.back_image);
        categoryViewPager = (ViewPager) findViewById(R.id.category_viewPager);
        titleWork = (TextView) findViewById(R.id.title_work);
        titleInfo = (TextView) findViewById(R.id.title_info);
        titleResume = (TextView) findViewById(R.id.title_resume);
        categoryTitle = (TextView) findViewById(R.id.category_title);
        titleWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryViewPager.setCurrentItem(0);
            }
        });
        titleInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryViewPager.setCurrentItem(1);
            }
        });
        titleResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryViewPager.setCurrentItem(2);
            }
        });
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (title != -1) {
            categoryTitle.setText(categoryTexts[title]);
        }
    }

    //添加viewPager的页面
    public void initViewPagerView() {
        //添加viewPager的页面
        categoryViews = new ArrayList<>();
        workView = getLayoutInflater().inflate(R.layout.category_viewpager_work_layout, null);
        infoView = getLayoutInflater().inflate(R.layout.category_viewpager_info_layout, null);
        resumeView = getLayoutInflater().inflate(R.layout.category_viewpager_resume_layout, null);
        categoryViews.add(workView);
        categoryViews.add(infoView);
        categoryViews.add(resumeView);
        //创建viewPager的适配器
        categoryViewPagerAdapter = new CategoryViewPagerAdapter(categoryViews);
        categoryViewPager.setAdapter(categoryViewPagerAdapter);
        categoryViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        });
        initWorkPage();
        initInfoPage();
        initResumePage();
    }

    //初始化游标
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

    private void initWorkPage() {
        workRefresh = (SwipeRefreshLayout) workView.findViewById(R.id.work_refresh);
        workListView = (ListView) workView.findViewById(R.id.work_listview);
        works = new ArrayList<>();
        for (int i = 0; i < typeImages.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("image", typeImages[i]);
            item.put("date", dates[i]);
            item.put("name", names[i]);
            item.put("price", prices[i]);
            item.put("limit", limits[i]);
            works.add(item);
        }
        final SimpleAdapter adapter = new SimpleAdapter(
                this,
                works,
                R.layout.category_work_listview_item_layout,
                new String[]{"date", "image", "name", "price", "limit"},
                new int[]{R.id.date, R.id.type_image, R.id.name, R.id.price, R.id.limit}
        );
        workListView.setAdapter(adapter);
        workRefresh.setColorSchemeResources(R.color.mainColor);
        workRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                workListView.setAdapter(adapter);
                workRefresh.setRefreshing(false);
            }
        });

        workListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent_detail = new Intent();
                intent_detail.setClass(CategoryActivity.this, DetailActivity.class);
                startActivity(intent_detail);
            }
        });
    }

    private void initInfoPage() {

    }

    private void initResumePage() {
    }
}
