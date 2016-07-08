package com.yuzhai.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yuzhai.fragment.HomeFragment;
import com.yuzhai.fragment.MenuFragment;
import com.yuzhai.fragment.OrderFragment;
import com.yuzhai.fragment.PublishFragment;
import com.yuzhai.slidingmenu.SlidingMenu;
import com.yuzhai.yuzhaiwork.R;

/**
 * Created by Administrator on 2016/6/10.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //导航
    private FrameLayout buttomPanel = null;
    private LinearLayout home_icon = null, order_icon = null, publish_icon = null;

    //导航的图标
    private ImageView home_image = null, order_image = null, publish_image = null;

    //导航的文字
    private TextView home_text = null, order_text = null, publish_text = null;

    //侧滑菜单
    static public SlidingMenu menu;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    //HomeFragment,OrderFragment,PublishFragment
    private HomeFragment homeFragment = null;
    private OrderFragment orderFragment = null;
    private PublishFragment publishFragment = null;
    private MenuFragment menuFragment = null;

    //退出按钮按的次数
    int click_time = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化组件
        initComponent();
    }

    public void initComponent() {

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        //初始化滑动菜单
        initSlidingMenu(fragmentTransaction);

        buttomPanel = (FrameLayout) findViewById(R.id.buttomPanel);
        //初始化三个导航
        home_icon = (LinearLayout) buttomPanel.findViewById(R.id.home_icon);
        order_icon = (LinearLayout) buttomPanel.findViewById(R.id.order_icon);
        publish_icon = (LinearLayout) buttomPanel.findViewById(R.id.publish_icon);
        //主页被选中
        changeStatus(home_icon, home_image, home_text, R.id.home_image, R.id.home_text, R.drawable.home_click, R.color.mainColor);

        //添加事件监听
        home_icon.setOnClickListener(this);
        order_icon.setOnClickListener(this);
        publish_icon.setOnClickListener(this);
    }

    /**
     * 初始化滑动菜单
     */
    private void initSlidingMenu(FragmentTransaction fragmentTransaction) {
        //主界面Fragment，个人信息Fragment
        menuFragment = new MenuFragment();
        homeFragment = new HomeFragment();
        // 设置主界面视图
        fragmentTransaction.replace(R.id.main_content, homeFragment);
        // 设置滑动菜单的属性值
        menu = new SlidingMenu(this);
        //设置触摸模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //设置滑动菜单的位置，左边，右边，左右都有
        menu.setMode(SlidingMenu.LEFT);
        //设置阴影效果的宽度
        menu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
        //设置阴影效果
        menu.setShadowDrawable(R.drawable.shadow);
        //设置主界面显示的宽度
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        //设置滑动时渐入渐出的效果
        menu.setFadeDegree(0.35f);
        // 设置滑动菜单的视图界面
        menu.setMenu(R.layout.activity_main_menu);
        fragmentTransaction.replace(R.id.menu_content, menuFragment);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        fragmentTransaction.commit();
    }

    /*
     * 主要用于改变点击后导航的颜色
     * @param navigator 导航
     * @param imageView 导航图标
     * @param textView  导航文字
     * @param navigaImageID  导航图标的ID
     * @param navigaTextID  导航文字的ID
     * @param image  要设定的导航图片
     * @param color  要设定的导航文字颜色
     */
    public void changeStatus(LinearLayout navigator, ImageView imageView, TextView textView, int navigaImageID, int navigaTextID, int image, int color) {
        if (imageView == null) {
            imageView = (ImageView) navigator.findViewById(navigaImageID);
        }
        imageView.setImageResource(image);

        if (textView == null) {
            textView = (TextView) navigator.findViewById(navigaTextID);
        }
        textView.setTextColor(ContextCompat.getColor(this, color));
    }

    @Override
    public void onClick(View v) {
        //获取fragment事务处理对象
        fragmentTransaction = fragmentManager.beginTransaction();
        //隐藏所有已有的fragment
        this.hideFragment(fragmentTransaction);
        //重置所有的导航颜色
        this.resetNavigator();
        switch (v.getId()) {
            case R.id.home_icon:
                //设置点击的导航的图片和文字颜色为高亮
                changeStatus(home_icon, home_image, home_text, R.id.home_image, R.id.home_text, R.drawable.home_click, R.color.mainColor);
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    fragmentTransaction.add(R.id.main_content, homeFragment);
                } else {
                    fragmentTransaction.show(homeFragment);
                }
                break;
            case R.id.order_icon:
                changeStatus(order_icon, order_image, order_text, R.id.order_image, R.id.order_text, R.drawable.order_click, R.color.mainColor);
                if (orderFragment == null) {
                    orderFragment = new OrderFragment();
                    fragmentTransaction.add(R.id.main_content, orderFragment);
                } else {
                    fragmentTransaction.show(orderFragment);
                }
                break;
            case R.id.publish_icon:
                changeStatus(publish_icon, publish_image, publish_text, R.id.publish_image, R.id.publish_text, R.drawable.publish_click, R.color.mainColor);
                if (publishFragment == null) {
                    publishFragment = new PublishFragment();
                    fragmentTransaction.add(R.id.main_content, publishFragment);
                } else {
                    fragmentTransaction.show(publishFragment);
                }
                break;
        }
        fragmentTransaction.commit();
    }

    //将所有的Fragment都置为隐藏状态
    private void hideFragment(FragmentTransaction fragmentTransaction) {
        if (homeFragment != null) {
            fragmentTransaction.hide(homeFragment);
        }
        if (orderFragment != null) {
            fragmentTransaction.hide(orderFragment);
        }
        if (publishFragment != null) {
            fragmentTransaction.hide(publishFragment);
        }
    }

    //重置下方导航颜色
    public void resetNavigator() {
        changeStatus(home_icon, home_image, home_text, R.id.home_image, R.id.home_text, R.drawable.home, R.color.color_8c8c8c);
        changeStatus(order_icon, order_image, order_text, R.id.order_image, R.id.order_text, R.drawable.order, R.color.color_8c8c8c);
        changeStatus(publish_icon, publish_image, publish_text, R.id.publish_image, R.id.publish_text, R.drawable.publish, R.color.color_8c8c8c);
    }

    @Override
    public void onBackPressed() {
        //点击返回键关闭滑动菜单
        if (menu.isMenuShowing()) {
            menu.showContent();
            click_time = 0;
        } else {
            if (click_time == 1) {
                this.finish();
            } else {
                click_time++;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
