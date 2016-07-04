package com.yuzhai.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuzhai.slidingmenu.SlidingMenu;
import com.yuzhai.fragment.HomeFragment;
import com.yuzhai.fragment.OrderFragment;
import com.yuzhai.fragment.PublishFragment;
import com.yuzhai.fragment.MenuFragment;
import com.yuzhai.yuzhaiwork.R;

/**
 * Created by Administrator on 2016/6/10.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //导航
    private LinearLayout buttomPanel = null;
    private LinearLayout home_icon = null, order_icon = null, publish_icon = null;

    //导航的图标
    private ImageView home_image = null, order_image = null, publish_image = null;

    //导航的文字
    private TextView home_text = null, order_text = null, publish_text = null;

    //侧滑菜单
    private SlidingMenu menu;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化组件
        init();
    }

    public void init() {
        //初始化滑动菜单
        initSlidingMenu();

        buttomPanel = (LinearLayout) findViewById(R.id.buttomPanel);
        //初始化三个导航
        home_icon = (LinearLayout) buttomPanel.findViewById(R.id.home_icon);
        order_icon = (LinearLayout) buttomPanel.findViewById(R.id.order_icon);
        publish_icon = (LinearLayout) buttomPanel.findViewById(R.id.publish_icon);
        //主页被选中
        changeStatus(home_icon, home_image, home_text, R.id.home_image, R.id.home_text, R.drawable.home_click, R.color.mainColor);
        //加载主页布局
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_content, new HomeFragment());
        fragmentTransaction.commit();
        //添加事件监听
        home_icon.setOnClickListener(this);
        order_icon.setOnClickListener(this);
        publish_icon.setOnClickListener(this);
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
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (v.getId()) {
            case R.id.home_icon:
                changeStatus(home_icon, home_image, home_text, R.id.home_image, R.id.home_text, R.drawable.home_click, R.color.mainColor);
                changeStatus(order_icon, order_image, order_text, R.id.order_image, R.id.order_text, R.drawable.order, R.color.color_8c8c8c);
                changeStatus(publish_icon, publish_image, publish_text, R.id.publish_image, R.id.publish_text, R.drawable.publish, R.color.color_8c8c8c);
                fragmentTransaction.replace(R.id.main_content, new HomeFragment());
                break;
            case R.id.order_icon:
                changeStatus(home_icon, home_image, home_text, R.id.home_image, R.id.home_text, R.drawable.home, R.color.color_8c8c8c);
                changeStatus(order_icon, order_image, order_text, R.id.order_image, R.id.order_text, R.drawable.order_click, R.color.mainColor);
                changeStatus(publish_icon, publish_image, publish_text, R.id.publish_image, R.id.publish_text, R.drawable.publish, R.color.color_8c8c8c);
                fragmentTransaction.replace(R.id.main_content, new OrderFragment());
                break;
            case R.id.publish_icon:
                changeStatus(home_icon, home_image, home_text, R.id.home_image, R.id.home_text, R.drawable.home, R.color.color_8c8c8c);
                changeStatus(order_icon, order_image, order_text, R.id.order_image, R.id.order_text, R.drawable.order, R.color.color_8c8c8c);
                changeStatus(publish_icon, publish_image, publish_text, R.id.publish_image, R.id.publish_text, R.drawable.publish_click, R.color.mainColor);
                fragmentTransaction.replace(R.id.main_content, new PublishFragment());
                break;
        }
        fragmentTransaction.commit();
    }

    /**
     * 初始化滑动菜单
     */
    private void initSlidingMenu() {
        // 设置主界面视图
        getFragmentManager().beginTransaction().replace(R.id.main_content, new HomeFragment()).commit();
        // 设置滑动菜单的属性值
        menu = new SlidingMenu(this);
        //设置触摸模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //设置阴影效果的宽度
        menu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
        //设置阴影效果
        menu.setShadowDrawable(R.drawable.shadow);
        //设置主界面显示的宽度
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        //设置滑动时渐入渐出的效果
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        // 设置滑动菜单的视图界面
        menu.setMenu(R.layout.fragment_menu);
        getFragmentManager().beginTransaction().replace(R.id.main_content, new MenuFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        //点击返回键关闭滑动菜单
        if (menu.isMenuShowing()) {
            menu.showContent();
        } else {
            super.onBackPressed();
        }
    }
}
