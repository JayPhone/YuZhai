package com.yuzhai.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuzhai.recyclerview.DividerGridItemDecoration;
import com.yuzhai.recyclerview.ItemTouchHelperCallback;
import com.yuzhai.adapter.CategoryRecyclerViewAdapter;
import com.yuzhai.ui.AdvertiseActivity;
import com.yuzhai.ui.MainActivity;
import com.yuzhai.ui.SearchActivity;
import com.yuzhai.util.BitmapUtil;
import com.yuzhai.view.PointViewFlipper;
import com.yuzhai.view.TranslucentScrollView;
import com.yuzhai.yuzhaiwork.R;

/**
 * Created by Administrator on 2016/6/10.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    private Activity mainActivity;
    //滚动面板下方的圆点面板
    private LinearLayout pointPanel;
    //滚动面板下方的圆点
    private TextView point_1, point_2, point_3, point_4;
    //圆点数组
    private TextView[] points;
    //搜索框
    private TextView searchView;
    //滚动面板的图片
    private ImageView image_1, image_2, image_3, image_4;
    //用于弹出个人信息面板
    private ImageView personImage;
    //滚动面板
    private PointViewFlipper picturePanel;
    //类别面板
    private RecyclerView category;
    //类别面板设配器
    private CategoryRecyclerViewAdapter categoryAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private ItemTouchHelperCallback mItemTouchHelperCallback;

    private int imageWidth;
    private int imageHeight;

    private RelativeLayout relativeLayout;
    private TranslucentScrollView translucentScrollView;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化数据
        initValues();
        MainActivity.menu.addIgnoredView(picturePanel);
    }

    private void initValues() {
        mainActivity = getActivity();
        //初始化标题栏
        initToolbar(mainActivity);
        //初始化焦点图
        initBanner(mainActivity);
        //初始化分类面板
        initCategory(mainActivity);
    }

    //初始化标题栏
    public void initToolbar(final Activity mainActivity) {
//        relativeLayout = (RelativeLayout) mainActivity.findViewById(R.id.home_toolbar);
//        relativeLayout.setAlpha(0);
        personImage = (ImageView) mainActivity.findViewById(R.id.person_image);
        personImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.menu.showMenu();
            }
        });
        searchView = (TextView) mainActivity.findViewById(R.id.search_view);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search_intent = new Intent();
                search_intent.setClass(mainActivity, SearchActivity.class);
                startActivity(search_intent);
            }
        });
//        translucentScrollView = (TranslucentScrollView) mainActivity.findViewById(R.id.home_scroll);
//        translucentScrollView.setOnScrollingListener(new TranslucentScrollView.OnScrollingListener() {
//            @Override
//            public void onTranslucent(int h, int v, int oldH, int oldV) {
//                float scrollY = v;
//                relativeLayout.setAlpha(scrollY / 300);
//            }
//        });
    }

    //初始化焦点图
    public void initBanner(Activity mainActivity) {
        //初始化下面的圆点面板
        pointPanel = (LinearLayout) mainActivity.findViewById(R.id.point_panel);
        point_1 = (TextView) pointPanel.findViewById(R.id.point_1);
        point_2 = (TextView) pointPanel.findViewById(R.id.point_2);
        point_3 = (TextView) pointPanel.findViewById(R.id.point_3);
        point_4 = (TextView) pointPanel.findViewById(R.id.point_4);
        points = new TextView[]{point_1, point_2, point_3, point_4};

        //初始化焦点图面板
        picturePanel = (PointViewFlipper) mainActivity.findViewById(R.id.fli);
        picturePanel.setInAnimation(getActivity(), android.R.anim.fade_in);
        picturePanel.setOutAnimation(getActivity(), android.R.anim.fade_out);
        picturePanel.setOnFlipListener(flipListener);
        picturePanel.getViewTreeObserver().
                addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                picturePanel.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                imageWidth = picturePanel.getWidth();
                                imageHeight = picturePanel.getHeight();
                                Log.i("width", imageWidth + "");
                                Log.i("height", imageHeight + "");

                                image_1.setImageBitmap(BitmapUtil.decodeSampledBitmapFromResource(
                                        getResources(), R.drawable.test1, imageWidth, imageHeight));
                                image_2.setImageBitmap(BitmapUtil.decodeSampledBitmapFromResource(
                                        getResources(), R.drawable.test2, imageWidth, imageHeight));
                                image_3.setImageBitmap(BitmapUtil.decodeSampledBitmapFromResource(
                                        getResources(), R.drawable.test3, imageWidth, imageHeight));
                                image_4.setImageBitmap(BitmapUtil.decodeSampledBitmapFromResource(
                                        getResources(), R.drawable.test4, imageWidth, imageHeight));
                            }
                        });

        //初始化图片内容
        image_1 = (ImageView) mainActivity.findViewById(R.id.image_1);
        image_2 = (ImageView) mainActivity.findViewById(R.id.image_2);
        image_3 = (ImageView) mainActivity.findViewById(R.id.image_3);
        image_4 = (ImageView) mainActivity.findViewById(R.id.image_4);

        image_1.setOnClickListener(this);
        image_2.setOnClickListener(this);
        image_3.setOnClickListener(this);
        image_4.setOnClickListener(this);

        point_1.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.buttom_style_choose));
    }

    //初始化分类面板
    public void initCategory(Activity mMainActivity) {
        categoryAdapter = new CategoryRecyclerViewAdapter(mMainActivity);
        category = (RecyclerView) mMainActivity.findViewById(R.id.category);
        category.setLayoutManager(new GridLayoutManager(mMainActivity, 3));
        category.setAdapter(categoryAdapter);
        category.setItemAnimator(new DefaultItemAnimator());
        category.addItemDecoration(new DividerGridItemDecoration(mMainActivity));
        mItemTouchHelperCallback = new ItemTouchHelperCallback(categoryAdapter);
        mItemTouchHelper = new ItemTouchHelper(mItemTouchHelperCallback);
        mItemTouchHelper.attachToRecyclerView(category);
    }

    @Override
    public void onClick(View v) {
        int viewID = v.getId();
        switch (viewID) {
            case R.id.image_1:
                Intent intent_advertise1 = new Intent(getActivity(), AdvertiseActivity.class);
                startActivity(intent_advertise1);
                break;
            case R.id.image_2:
                Intent intent_advertise2 = new Intent(getActivity(), AdvertiseActivity.class);
                startActivity(intent_advertise2);
                break;
            case R.id.image_3:
                Intent intent_advertise3 = new Intent(getActivity(), AdvertiseActivity.class);
                startActivity(intent_advertise3);
                break;
            case R.id.image_4:
                Intent intent_advertise4 = new Intent(getActivity(), AdvertiseActivity.class);
                startActivity(intent_advertise4);
                break;
        }
    }

    //图片切换监听器
    private PointViewFlipper.OnFlipListener flipListener = new PointViewFlipper.OnFlipListener() {
        //图片切换到下一张
        @Override
        public void onShowPrevious(PointViewFlipper flipper) {
            int id = flipper.getDisplayedChild();
            points[id].setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.buttom_style_choose));
            for (int i = 0; i < points.length; i++) {
                if (id == i)
                    continue;
                else
                    points[i].setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.buttom_style));
            }
        }

        //图片切换到上一张
        @Override
        public void onShowNext(PointViewFlipper flipper) {
            int id = flipper.getDisplayedChild();
            points[id].setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.buttom_style_choose));
            for (int i = 0; i < points.length; i++) {
                if (id == i)
                    continue;
                else
                    points[i].setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.buttom_style));
            }
        }
    };

}
