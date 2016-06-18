package com.yuzhai.fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.yuzhai.listener.OnGestureAll;
import com.yuzhai.view.CategoryGridView;
import com.yuzhai.view.PointViewFlipper;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/10.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    //滚动显示和隐藏menu时，手指滑动需要达到的速度。
    public static final int SNAP_VELOCITY = 200;
    //屏幕宽度值。
    private int screenWidth;
    // menu最多可以滑动到的左边缘。值由menu布局的宽度来定，marginLeft到达此值之后，不能再减少。
    private int leftEdge;
    //menu最多可以滑动到的右边缘。值恒为0，即marginLeft到达0之后，不能增加。
    private int rightEdge = 0;
    //menu完全显示时，留给content的宽度值。
    private int menuPadding = 150;
    //主内容的布局。
    private View content;
    //menu的布局。
    private View menu;
    // menu布局的参数，通过此参数来更改leftMargin的值。
    private LinearLayout.LayoutParams menuParams;
    //记录手指按下时的横坐标。
    private float xDown;
    // 记录手指移动时的横坐标。
    private float xMove;
    //记录手机抬起时的横坐标。
    private float xUp;
    // menu当前是显示还是隐藏。只有完全显示或隐藏menu时才会更改此值，滑动过程中此值无效。
    private boolean isMenuVisible;
    // 用于计算手指滑动的速度。
    private VelocityTracker mVelocityTracker;
    //滚动面板下方的圆点面板
    private LinearLayout pointPanel;
    //滚动面板下方的圆点
    private TextView point_1, point_2, point_3, point_4;
    //圆点数组
    private TextView[] points;
    //滚动面板的图片
    private ImageView image_1, image_2, image_3, image_4;
    //滚动面板
    private PointViewFlipper picturePanel;
    //类别面板
    private CategoryGridView category;
    //手势检测器
    private GestureDetector detectorBanner, detectorAll;
    //窗口管理器
    private WindowManager window;
    //类别面板设配器
    private SimpleAdapter categoryAdapter;
    //类别面板数据容器
    private List<Map<String, Object>> categoryData;
    //类别面板分类图片
    private int[] imageId = new int[]{R.drawable.it, R.drawable.it, R.drawable.it, R.drawable.movie, R.drawable.it, R.drawable.it, R.drawable.calculate};
    //类别面板分类标题
    private String[] categoryTexts = new String[]{"软件IT", "音乐制作", "平面设计", "视频拍摄", "游戏研发", "文案撰写", "金融会计"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //初始化数据
        initValues(view);
        return view;
    }

    /**
     * 初始化一些关键性数据。包括获取屏幕的宽度，给content布局重新设置宽度，给menu布局重新设置宽度和偏移距离等。
     */
    private void initValues(View view) {
        window = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        initWindow(view);
        initBanner(view);
        initCategory(view);
    }

    //初始化侧滑菜单和主界面布局
    public void initWindow(View view) {
        Point point = new Point();
        window.getDefaultDisplay().getSize(point);
        screenWidth = point.x;
        content = view.findViewById(R.id.content_layout);
        menu = view.findViewById(R.id.menu_layout);
        menuParams = (LinearLayout.LayoutParams) menu.getLayoutParams();
        // 将menu的宽度设置为屏幕宽度减去menuPadding
        menuParams.width = screenWidth - menuPadding;
        // 左边缘的值赋值为menu宽度的负数
        leftEdge = -menuParams.width;
        // menu的leftMargin设置为左边缘的值，这样初始化时menu就变为不可见.
        menuParams.leftMargin = leftEdge;
        // 将content的宽度设置为屏幕宽度
        content.getLayoutParams().width = screenWidth;
        //设置监听器
//        content.setOnTouchListener(this);
//        menu.setOnTouchListener(this);

        //初始化手势检测
        detectorAll = new GestureDetector(getActivity(), new OnGestureAll(getActivity()));
    }

    //初始化焦点图
    public void initBanner(View view) {
        //初始化下面的圆点面板
        pointPanel = (LinearLayout) view.findViewById(R.id.point_panel);
        point_1 = (TextView) pointPanel.findViewById(R.id.point_1);
        point_2 = (TextView) pointPanel.findViewById(R.id.point_2);
        point_3 = (TextView) pointPanel.findViewById(R.id.point_3);
        point_4 = (TextView) pointPanel.findViewById(R.id.point_4);
        points = new TextView[]{point_1, point_2, point_3, point_4};

        //初始化焦点图面板
        picturePanel = (PointViewFlipper) view.findViewById(R.id.fli);
        picturePanel.setInAnimation(getActivity(), android.R.anim.fade_in);
        picturePanel.setOutAnimation(getActivity(), android.R.anim.fade_out);
//        picturePanel.setOnTouchListener(this);
        picturePanel.setOnFlipListener(flipListener);

        //初始化图片内容
        image_1 = (ImageView) view.findViewById(R.id.image_1);
        image_2 = (ImageView) view.findViewById(R.id.image_2);
        image_3 = (ImageView) view.findViewById(R.id.image_3);
        image_4 = (ImageView) view.findViewById(R.id.image_4);

        image_1.setImageResource(R.drawable.test1);
        image_2.setImageResource(R.drawable.test2);
        image_3.setImageResource(R.drawable.test3);
        image_4.setImageResource(R.drawable.test4);

        image_1.setOnClickListener(this);
        image_2.setOnClickListener(this);
        image_3.setOnClickListener(this);
        image_4.setOnClickListener(this);

        point_1.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.buttom_style));
    }

    //初始化分类面板
    public void initCategory(View view) {
        category = (CategoryGridView) view.findViewById(R.id.category);
        categoryData = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("image", imageId[i]);
            map.put("text", categoryTexts[i]);
            categoryData.add(map);
        }
        categoryAdapter = new SimpleAdapter(getActivity(), categoryData, R.layout.home_category_cell_layout, new String[]{"image", "text"}, new int[]{R.id.category_image, R.id.category_text});
        category.setAdapter(categoryAdapter);
    }

    @Override
    public void onClick(View v) {
        int viewID = v.getId();
        switch (viewID) {
            case R.id.image_1:
                Toast.makeText(getActivity(), "图片1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.image_2:
                Toast.makeText(getActivity(), "图片2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.image_3:
                Toast.makeText(getActivity(), "图片3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.image_4:
                Toast.makeText(getActivity(), "图片4", Toast.LENGTH_SHORT).show();
                break;
        }
    }

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        if (v.getId() == R.id.fli) {
//            detectorBanner.onTouchEvent(event);
//        } else if (v.getId() != R.id.fli) {
////            detectorAll.onTouchEvent(event);
//            createVelocityTracker(event);
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    // 手指按下时，记录按下时的横坐标
//                    xDown = event.getRawX();
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    // 手指移动时，对比按下时的横坐标，计算出移动的距离，来调整menu的leftMargin值，从而显示和隐藏menu
//                    xMove = event.getRawX();
//                    int distanceX = (int) (xMove - xDown);
//                    if (isMenuVisible) {
//                        menuParams.leftMargin = distanceX;
//                    } else {
//                        menuParams.leftMargin = leftEdge + distanceX;
//                    }
//                    if (menuParams.leftMargin < leftEdge) {
//                        menuParams.leftMargin = leftEdge;
//                    } else if (menuParams.leftMargin > rightEdge) {
//                        menuParams.leftMargin = rightEdge;
//                    }
//                    menu.setLayoutParams(menuParams);
//                    break;
//                case MotionEvent.ACTION_UP:
//                    // 手指抬起时，进行判断当前手势的意图，从而决定是滚动到menu界面，还是滚动到content界面
//                    xUp = event.getRawX();
//                    if (wantToShowMenu()) {
//                        if (shouldScrollToMenu()) {
//                            scrollToMenu();
//                        } else {
//                            scrollToContent();
//                        }
//                    } else if (wantToShowContent()) {
//                        if (shouldScrollToContent()) {
//                            scrollToContent();
//                        } else {
//                            scrollToMenu();
//                        }
//                    }
//                    recycleVelocityTracker();
//                    break;
//            }
//        }
//        return true;
//    }


    /**
     * 判断当前手势的意图是不是想显示content。如果手指移动的距离是负数，且当前menu是可见的，则认为当前手势是想要显示content。
     *
     * @return 当前手势想显示content返回true，否则返回false。
     */
    private boolean wantToShowContent() {
        return xUp - xDown < 0 && isMenuVisible;
    }

    /**
     * 判断当前手势的意图是不是想显示menu。如果手指移动的距离是正数，且当前menu是不可见的，则认为当前手势是想要显示menu。
     *
     * @return 当前手势想显示menu返回true，否则返回false。
     */
    private boolean wantToShowMenu() {
        return xUp - xDown > 0 && !isMenuVisible;
    }

    /**
     * 判断是否应该滚动将menu展示出来。如果手指移动距离大于屏幕的1/2，或者手指移动速度大于SNAP_VELOCITY，
     * 就认为应该滚动将menu展示出来。
     *
     * @return 如果应该滚动将menu展示出来返回true，否则返回false。
     */
    private boolean shouldScrollToMenu() {
        return xUp - xDown > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;
    }

    /**
     * 判断是否应该滚动将content展示出来。如果手指移动距离加上menuPadding大于屏幕的1/2，
     * 或者手指移动速度大于SNAP_VELOCITY， 就认为应该滚动将content展示出来。
     *
     * @return 如果应该滚动将content展示出来返回true，否则返回false。
     */
    private boolean shouldScrollToContent() {
        return xDown - xUp + menuPadding > screenWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;
    }

    /**
     * 将屏幕滚动到menu界面，滚动速度设定为30.
     */
    private void scrollToMenu() {
//        all.setAnimation(AnimationUtils.loadAnimation(this, R.anim.left_enter));
//        content.setAnimation(AnimationUtils.loadAnimation(this, R.anim.left_enter));
        menuParams.leftMargin = 0;
        menu.setLayoutParams(menuParams);
        isMenuVisible = true;
    }

    /**
     * 将屏幕滚动到content界面，滚动速度设定为-30.
     */
    private void scrollToContent() {
//        all.setAnimation(AnimationUtils.loadAnimation(this, R.anim.right_enter));
//        content.setAnimation(AnimationUtils.loadAnimation(this, R.anim.right_enter));
        menuParams.leftMargin = leftEdge;
        menu.setLayoutParams(menuParams);
        isMenuVisible = false;
    }

    /**
     * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。
     *
     * @param event content界面的滑动事件
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 获取手指在content界面滑动的速度。
     *
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }

    /**
     * 回收VelocityTracker对象。
     */
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }


    private PointViewFlipper.OnFlipListener flipListener = new PointViewFlipper.OnFlipListener() {
        @Override
        public void onShowPrevious(PointViewFlipper flipper) {
            int id = flipper.getDisplayedChild();
            points[id].setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.buttom_style));
            for (int i = 0; i < points.length; i++) {
                if (id == i)
                    continue;
                else
                    points[i].setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.buttom_style_choose));
            }
        }

        @Override
        public void onShowNext(PointViewFlipper flipper) {
            int id = flipper.getDisplayedChild();
            points[id].setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.buttom_style));
            for (int i = 0; i < points.length; i++) {
                if (id == i)
                    continue;
                else
                    points[i].setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.buttom_style_choose));
            }
        }
    };

}
