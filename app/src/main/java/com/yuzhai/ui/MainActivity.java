package com.yuzhai.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuzhai.fragment.HomeFragment;
import com.yuzhai.fragment.MenuFragment;
import com.yuzhai.fragment.OrderFragment;
import com.yuzhai.fragment.PublishFragment;
import com.yuzhai.slidingmenu.SlidingMenu;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

/**
 * Created by Administrator on 2016/6/10.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 底部导航面板
     */
    private FrameLayout mButtomPanel;

    /**
     * 底部3个导航的布局
     */
    private LinearLayout mHomeLayout, mOrderLayout, mPublishLayout;

    /**
     * 底部导航的图标
     */
    private ImageView mHomeIcon, mOrderIcon, mPublishIcon;

    /**
     * 底部导航的文字
     */
    private TextView mHomeText, mOrderText, mPublishText;

    /**
     * 侧滑菜单实例
     */
    public static SlidingMenu menu;

    /**
     * Fragment管理器
     */
    private FragmentManager mFragmentManager;

    /**
     * Fragment事务
     */
    private FragmentTransaction mFragmentTransaction;

    /**
     * 主页Fragment
     */
    private HomeFragment mHomeFragment;

    /**
     * 订单Fragment
     */
    private OrderFragment mOrderFragment;

    /**
     * 发布Fragment
     */
    private PublishFragment mPublishFragment;

    /**
     * 个人信息Fragment
     */
    private MenuFragment mMenuFragment;

    /**
     * 主页Fragment的Tag
     */
    private final String mHomeFragmentTag = "homeFragment";

    /**
     * 发布Fragment的Tag
     */
    private final String mOrderFragmentTag = "orderFragment";

    /**
     * 个人信息Fragment的Tag
     */
    private final String mPublishFragmentTag = "publishFragment";

    /**
     * 用户头像路径
     */
    private String mUserHeadURL;

    /**
     * 用户名
     */
    private String mUserNameText;

    /**
     * 双击退出第一次点击的时间
     */
    private long mClickTime = 0;

    /**
     * 接收替换个人信息界面的广播
     */
    private BroadcastReceiver mReplaceMenuUI;

    /**
     * 替换侧滑菜单布局的Filter
     */
    private IntentFilter mReplaceMenuUiFilter;


    private final String REPLACE_MENU_FILETER = "yzgz.broadcast.replace.fragment";
    private final String USERHEAD = "userHead";
    private final String USERNAME = "userName";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取登录后WelcomeActivity传递的头像路径和用户名
        String headUrl = getIntent().getStringExtra(USERHEAD);
        String userName = getIntent().getStringExtra(USERNAME);
        if (headUrl != null) {
            mUserHeadURL = headUrl;
        }
        if (userName != null) {
            mUserNameText = userName;
        }

        //初始化组件
        initComponent();
    }

    public void initComponent() {
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();

        //初始化滑动菜单
        initSlidingMenu(mFragmentTransaction);
        mButtomPanel = (FrameLayout) findViewById(R.id.buttomPanel);
        //初始化三个导航
        mHomeLayout = (LinearLayout) mButtomPanel.findViewById(R.id.home_icon);
        mOrderLayout = (LinearLayout) mButtomPanel.findViewById(R.id.order_icon);
        mPublishLayout = (LinearLayout) mButtomPanel.findViewById(R.id.publish_icon);
        //主页被选中
        changeStatus(mHomeLayout, mHomeIcon, mHomeText, R.id.home_image, R.id.home_text, R.drawable.home_click, R.color.mainColor);

        //添加事件监听
        mHomeLayout.setOnClickListener(this);
        mOrderLayout.setOnClickListener(this);
        mPublishLayout.setOnClickListener(this);

        //初始化替换个人信息界面的broadcast
        initReplaceReceiver();
    }

    /**
     * 初始化滑动菜单
     */
    private void initSlidingMenu(FragmentTransaction fragmentTransaction) {
        //主界面Fragment，个人信息Fragment
        mMenuFragment = new MenuFragment();
        mHomeFragment = new HomeFragment();

        //把获取到的头像和用户名数据传递给MenuFragment
        Bundle bundle = new Bundle();
        if (mUserHeadURL != null) {
            bundle.putString(USERHEAD, mUserHeadURL);
        }
        if (mUserNameText != null) {
            bundle.putString(USERNAME, mUserNameText);
        }
        mMenuFragment.setArguments(bundle);

        // 设置主界面视图
        fragmentTransaction.replace(R.id.main_content, mHomeFragment);
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
        fragmentTransaction.replace(R.id.menu_content, mMenuFragment);
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
    public void changeStatus(LinearLayout navigator,
                             ImageView imageView,
                             TextView textView,
                             int navigaImageID,
                             int navigaTextID,
                             int image,
                             int color) {

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
        mFragmentTransaction = mFragmentManager.beginTransaction();
        //隐藏所有已有的fragment
        this.hideFragment(mFragmentTransaction);
        //重置所有的导航颜色
        this.resetNavigator();

        switch (v.getId()) {

            case R.id.home_icon:
                //设置点击的导航的图片和文字颜色为高亮
                changeStatus(mHomeLayout,
                        mHomeIcon,
                        mHomeText,
                        R.id.home_image,
                        R.id.home_text,
                        R.drawable.home_click,
                        R.color.mainColor
                );
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    mFragmentTransaction.add(R.id.main_content, mHomeFragment, mHomeFragmentTag);
                } else {
                    mFragmentTransaction.show(mHomeFragment);
                }
                break;

            case R.id.order_icon:
                changeStatus(mOrderLayout,
                        mOrderIcon,
                        mOrderText,
                        R.id.order_image,
                        R.id.order_text,
                        R.drawable.order_click,
                        R.color.mainColor
                );
                if (mOrderFragment == null) {
                    mOrderFragment = new OrderFragment();
                    mFragmentTransaction.add(R.id.main_content, mOrderFragment, mOrderFragmentTag);
                } else {
                    mFragmentTransaction.show(mOrderFragment);
                }
                break;

            case R.id.publish_icon:
                changeStatus(mPublishLayout,
                        mPublishIcon,
                        mPublishText,
                        R.id.publish_image,
                        R.id.publish_text,
                        R.drawable.publish_click,
                        R.color.mainColor
                );
                if (mPublishFragment == null) {
                    mPublishFragment = new PublishFragment();
                    mFragmentTransaction.add(R.id.main_content, mPublishFragment, mPublishFragmentTag);
                } else {
                    mFragmentTransaction.show(mPublishFragment);
                }
                break;
        }

        //提交事务
        mFragmentTransaction.commit();
    }

    /**
     * 将所有的Fragment都置为隐藏状态
     *
     * @param fragmentTransaction fragment事务
     */
    private void hideFragment(FragmentTransaction fragmentTransaction) {
        if (mHomeFragment != null) {
            fragmentTransaction.hide(mHomeFragment);
        }
        if (mOrderFragment != null) {
            fragmentTransaction.hide(mOrderFragment);
        }
        if (mPublishFragment != null) {
            fragmentTransaction.hide(mPublishFragment);
        }
    }

    /**
     * 重置下方导航颜色
     */
    public void resetNavigator() {
        changeStatus(mHomeLayout,
                mHomeIcon,
                mHomeText,
                R.id.home_image,
                R.id.home_text,
                R.drawable.home,
                R.color.color_8c8c8c
        );
        changeStatus(mOrderLayout,
                mOrderIcon,
                mOrderText,
                R.id.order_image,
                R.id.order_text,
                R.drawable.order,
                R.color.color_8c8c8c
        );
        changeStatus(mPublishLayout,
                mPublishIcon,
                mPublishText,
                R.id.publish_image,
                R.id.publish_text,
                R.drawable.publish,
                R.color.color_8c8c8c
        );
    }

    @Override
    public void onBackPressed() {
        //点击返回键关闭滑动菜单
        if (menu.isMenuShowing()) {
            menu.showContent();
        } else {
            if (System.currentTimeMillis() - mClickTime > 2000) {
                UnRepeatToast.showToast(this, "再按一次退出程序");
                mClickTime = System.currentTimeMillis();
            } else {
                finish();
            }
        }
    }

    /**
     * 初始化替换个人信息界面的broadcastReceiver
     */
    public void initReplaceReceiver() {
        mReplaceMenuUiFilter = new IntentFilter(REPLACE_MENU_FILETER);

        mReplaceMenuUI = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //重新创建个人信息Fragment
                mMenuFragment = new MenuFragment();
                //获取发送过来的头像路径和用户名数据
                Bundle bundle = new Bundle();
                if (intent.getStringExtra(USERHEAD) != null && !intent.getStringExtra(USERHEAD).equals("")) {
                    String userHead = intent.getStringExtra(USERHEAD);
                    bundle.putString(USERHEAD, userHead);
                    Log.i(USERHEAD, userHead);
                }

                if (intent.getStringExtra(USERNAME) != null && !intent.getStringExtra(USERNAME).equals("")) {
                    String userName = intent.getStringExtra(USERNAME);
                    bundle.putString(USERNAME, userName);
                    Log.i(USERNAME, userName);
                }
                mMenuFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.menu_content, mMenuFragment).commitAllowingStateLoss();
            }
        };

        //注册广播接收器
        registerReceiver(mReplaceMenuUI, mReplaceMenuUiFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReplaceMenuUI);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFragmentManager.findFragmentByTag(mPublishFragmentTag).onActivityResult(requestCode, resultCode, data);
    }
}
