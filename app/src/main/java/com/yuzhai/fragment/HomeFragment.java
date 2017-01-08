package com.yuzhai.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuzhai.activity.SearchActivity;
import com.yuzhai.adapter.CategoryRecyclerViewAdapter;
import com.yuzhai.recyclerview.DividerGridItemDecoration;
import com.yuzhai.recyclerview.ItemTouchHelperCallback;
import com.yuzhai.view.IndicatedViewFlipper;
import com.yuzhai.view.TranslucentScrollView;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/10.
 */
public class HomeFragment extends Fragment {
    private Activity mainActivity;
    //搜索框
    private TextView searchView;
    //用于弹出个人信息面板
    private ImageView navigationImage;
    //滚动面板
    private IndicatedViewFlipper indicatedFlipper;
    //类别面板
    private RecyclerView categoryRecyclerView;
    //类别面板设配器
    private CategoryRecyclerViewAdapter categoryAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private ItemTouchHelperCallback mItemTouchHelperCallback;

    private RelativeLayout toolbar;
    private TranslucentScrollView translucentScrollView;

    public static HomeFragment newInstance() {
//        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化数据
        initViews();
    }

    private void initViews() {
        mainActivity = getActivity();
        //初始化标题栏
        initToolbar();
        //初始化焦点图
        initBanner();
        //初始化分类面板
        initCategory();
    }

    //初始化标题栏
    public void initToolbar() {
        toolbar = (RelativeLayout) getView().findViewById(R.id.home_toolbar);
        toolbar.getBackground().mutate().setAlpha(0);
        navigationImage = (ImageView) getView().findViewById(R.id.menu_image);
        navigationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = (DrawerLayout) mainActivity.findViewById(R.id.drawer);
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        searchView = (TextView) getView().findViewById(R.id.search_view);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search_intent = new Intent();
                search_intent.setClass(mainActivity, SearchActivity.class);
                startActivity(search_intent);
            }
        });

        translucentScrollView = (TranslucentScrollView) getView().findViewById(R.id.home_scroll);
        translucentScrollView.setOnScrollingListener(new TranslucentScrollView.OnScrollingListener() {
            @Override
            public void onTranslucent(int h, int v, int oldH, int oldV) {
                if ((float) v <= indicatedFlipper.getMeasuredHeight() - toolbar.getMeasuredHeight()) {
                    toolbar.getBackground().mutate().setAlpha(
                            (int) ((float) v / (indicatedFlipper.getMeasuredHeight() - toolbar.getMeasuredHeight()) * 255));
                } else if (oldV > indicatedFlipper.getMeasuredHeight() - toolbar.getMeasuredHeight()) {
                    toolbar.getBackground().mutate().setAlpha(255);
                }
            }
        });
    }

    //初始化焦点图
    public void initBanner() {
        //初始化焦点图面板
        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.test1);
        list.add(R.drawable.test2);
        list.add(R.drawable.test3);
        list.add(R.drawable.test4);
        list.add(R.drawable.test1);
        list.add(R.drawable.test2);
        list.add(R.drawable.test3);
        list.add(R.drawable.test4);
        indicatedFlipper = (IndicatedViewFlipper) mainActivity.findViewById(R.id.indicated_flipper);
        indicatedFlipper.setDisallowInterceptViewGroup((ViewGroup) indicatedFlipper.getParent().getParent().getParent().getParent());
        indicatedFlipper.setFlipperImageResources(list);
    }

    //初始化分类面板
    public void initCategory() {
        categoryAdapter = new CategoryRecyclerViewAdapter(mainActivity);
        categoryRecyclerView = (RecyclerView) getView().findViewById(R.id.category);
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(mainActivity, 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        categoryRecyclerView.setAdapter(categoryAdapter);
        categoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        categoryRecyclerView.addItemDecoration(new DividerGridItemDecoration(mainActivity));
        mItemTouchHelperCallback = new ItemTouchHelperCallback(categoryAdapter);
        mItemTouchHelper = new ItemTouchHelper(mItemTouchHelperCallback);
        mItemTouchHelper.attachToRecyclerView(categoryRecyclerView);
    }
}
