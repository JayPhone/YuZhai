package com.yuzhai.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bumptech.glide.Glide;
import com.yuzhai.bean.innerBean.BaseUserInfoBean;
import com.yuzhai.bean.innerBean.UserInfoBean;
import com.yuzhai.http.IPConfig;
import com.yuzhai.fragment.ContactFragment;
import com.yuzhai.fragment.HomeFragment;
import com.yuzhai.fragment.OrderFragment;
import com.yuzhai.fragment.PublishFragment;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.view.CircleImageView;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.bmob.newim.notification.BmobNotificationManager;

/**
 * Created by Administrator on 2016/6/10.
 */
public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationBar.OnTabSelectedListener {
    private static final String TAG = "MainActivity";
    private static final String NOTIFICATION = "notification";
    private static final String CONTACT = "contact";

    private CustomApplication mCustomApplication;

    private DrawerLayout mDrawLayout;
    private NavigationView mNavigationView;
    private LinearLayout mUserInfoLayout;
    private CircleImageView mUserHeader;
    private TextView mUserNameTv;
    private Button mLoginAndRegButton;

    private BottomNavigationBar mBottomNavigationBar;

    private HomeFragment mHomeFragment;
    private OrderFragment mOrderFragment;
    private PublishFragment mPublishFragment;
    private ContactFragment mContactFragment;
    private Fragment mCurrentFragment;

    private String mUserHeadURL;
    private String mUserName;
    private long mClickTime = 0;

    //通知打开聊天页面
    private String mNotificationMsg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //注册EventBus
        EventBus.getDefault().register(this);
        mNotificationMsg = getIntent().getStringExtra(NOTIFICATION);
        mCustomApplication = (CustomApplication) getApplication();

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        //初始化组件
        initViews();
        //初始化数据
        initData();
    }

    private void initData() {
        if (mHomeFragment == null) {
            mHomeFragment = HomeFragment.newInstance();
        }

        if (!mHomeFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_content, mHomeFragment)
                    .commit();

            mCurrentFragment = mHomeFragment;

            //打开最近联系页面
            if (mNotificationMsg != null && mNotificationMsg.equals(CONTACT)) {
                clickContactTab();
            }
        }
    }

    public void initViews() {
        mDrawLayout = (DrawerLayout) findViewById(R.id.drawer);
//        mDrawLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//                mUserHeader.postInvalidate();
//            }
//        });

        //侧边导航栏
        mNavigationView = (NavigationView) findViewById(R.id.navigation);
        mNavigationView.setItemIconTintList(null);

        //底部导航栏
        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.main_nav_bar);
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.home, "御宅")
                .setActiveColorResource(R.color.mainColor));
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.order, "订单")
                .setActiveColorResource(R.color.mainColor));
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.publish, "发布")
                .setActiveColorResource(R.color.mainColor));
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.contact, "聊天")
                .setActiveColorResource(R.color.mainColor));

        //如果通过离线消息进入界面，则选择聊天界面
        if (mNotificationMsg != null && mNotificationMsg.equals(CONTACT)) {
            mBottomNavigationBar.setFirstSelectedPosition(3).initialise();
        } else {
            mBottomNavigationBar.setFirstSelectedPosition(0).initialise();
        }
        mBottomNavigationBar.setTabSelectedListener(this);

        //菜单
        View headerView = mNavigationView.inflateHeaderView(R.layout.navigation_menu_header_layout);
        mNavigationView.inflateMenu(R.menu.drawer_login_menu);
        mUserInfoLayout = (LinearLayout) headerView.findViewById(R.id.user_info);

        mUserNameTv = (TextView) headerView.findViewById(R.id.user_name);
        mUserHeader = (CircleImageView) headerView.findViewById(R.id.head_image);
        mUserHeader.setOnClickListener(this);

        mLoginAndRegButton = (Button) headerView.findViewById(R.id.menu_login_register);
        mLoginAndRegButton.setOnClickListener(this);

        mNavigationView.setNavigationItemSelectedListener(this);

        //加载侧滑菜单布局
        initMenuLayout();
    }

    private void initMenuLayout() {
        //已成功登录
        if (mCustomApplication.isLogin()) {
            mLoginAndRegButton.setVisibility(View.INVISIBLE);
            mUserHeader.setVisibility(View.VISIBLE);
            mUserInfoLayout.setVisibility(View.VISIBLE);
            mNavigationView.getMenu().clear();
            mNavigationView.inflateMenu(R.menu.drawer_login_menu);
            //设置用户名
            setUserName(mUserName);
            //设置用户头像
            setUserHeader(mUserHeadURL);

        } else {
            //未成功登陆
            mUserHeader.setVisibility(View.INVISIBLE);
            mUserInfoLayout.setVisibility(View.INVISIBLE);
            mLoginAndRegButton.setVisibility(View.VISIBLE);
            mNavigationView.getMenu().clear();
            mNavigationView.inflateMenu(R.menu.drawer_no_login_menu);
        }
    }

    @Override
    public void onTabSelected(int position) {
        switch (position) {
            case 0:
                clickHomeTab();
                break;
            case 1:
                clickOrderTab();
                break;
            case 2:
                clickPublishTab();
                break;
            case 3:
                clickContactTab();
                break;
        }
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    private void clickHomeTab() {
        if (mHomeFragment == null) {
            mHomeFragment = HomeFragment.newInstance();
        }

        showFragment(mHomeFragment);
    }

    private void clickOrderTab() {
        if (mOrderFragment == null) {
            mOrderFragment = OrderFragment.newInstance();
        }

        showFragment(mOrderFragment);
    }

    private void clickPublishTab() {
        if (mPublishFragment == null) {
            mPublishFragment = PublishFragment.newInstance();
        }

        showFragment(mPublishFragment);
    }

    private void clickContactTab() {
        if (mContactFragment == null) {
            mContactFragment = ContactFragment.newInstance();
        }

        showFragment(mContactFragment);
    }


    /**
     * 点击下方导航后，显示对应的Fragment
     *
     * @param fragment 要显示的Fragment
     */
    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mCurrentFragment == fragment) {
            return;
        }

        if (!fragment.isAdded()) {
            transaction.hide(mCurrentFragment)
                    .add(R.id.main_content, fragment).commit();
        } else {
            transaction.hide(mCurrentFragment)
                    .show(fragment).commit();
        }
        //设置前一个Fragment不显示在用户界面
        mCurrentFragment.setUserVisibleHint(false);
        mCurrentFragment = fragment;
        //设置当前Fragment显示在用户界面
        mCurrentFragment.setUserVisibleHint(true);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.collection:
                Intent collection = new Intent(this, CollectionActivity.class);
                startActivity(collection);
                break;
            case R.id.resume:
                Intent resume = new Intent(this, SendResumeActivity.class);
                startActivity(resume);
                break;
            case R.id.realName:
                Intent identityAuthen = new Intent(this, IdentityAuthenActivity.class);
                startActivity(identityAuthen);
                break;
            case R.id.setting:
                Intent setUp = new Intent(this, SetUpActivity.class);
                startActivity(setUp);
                break;
            case R.id.about:
                break;
            case R.id.exit:
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_image:

                Intent userInfo = new Intent(MainActivity.this, UserInfoActivity.class);
                startActivity(userInfo);

                //通过EventBus发送登录后返回的个人信息到UserInfoActivity
                EventBus.getDefault().postSticky(new UserInfoBean(
                        mUserHeadURL,
                        mUserName,
                        mCustomApplication.getUserPhone(),
                        null,
                        mCustomApplication.isLogin()));
                break;

            case R.id.menu_login_register:
                Intent login_register = new Intent(MainActivity.this, LoginAndRegisterActivity.class);
                startActivity(login_register);
                break;
        }
    }


    /**
     * 通过EventBus传递的数据判断消息并作出回应
     *
     * @param baseUserInfoBean
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventUserLogin(BaseUserInfoBean baseUserInfoBean) {
        //替换为未登录界面
        if (baseUserInfoBean.getUserHeadUrl() == null && baseUserInfoBean.getUserName() == null && !baseUserInfoBean.isLogin()) {
            initMenuLayout();
        }
        //替换为登录界面
        else if (baseUserInfoBean.getUserName() != null && baseUserInfoBean.isLogin()) {
            mUserHeadURL = baseUserInfoBean.getUserHeadUrl();
            mUserName = baseUserInfoBean.getUserName();
            initMenuLayout();
        }
        //修改用户名后改变用户名
        else if (baseUserInfoBean.getUserName() != null && baseUserInfoBean.getUserHeadUrl() == null && baseUserInfoBean.isLogin()) {
            mUserName = baseUserInfoBean.getUserName();
            setUserName(mUserName);
        }
        //修改用户头像后改变头像
        else if (baseUserInfoBean.getUserHeadUrl() != null && baseUserInfoBean.getUserName() == null && baseUserInfoBean.isLogin()) {
            mUserHeadURL = baseUserInfoBean.getUserHeadUrl();
            setUserHeader(mUserHeadURL);
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onAutoUserLogin(BaseUserInfoBean baseUserInfoBean) {
        if (baseUserInfoBean.getUserName() != null && baseUserInfoBean.isLogin()) {
            mUserHeadURL = baseUserInfoBean.getUserHeadUrl();
            mUserName = baseUserInfoBean.getUserName();
        }
    }

    public void setUserHeader(String userHeadUrl) {
        //通过返回的用户头像地址获取用户头像
        if (userHeadUrl != null) {
            Log.i("user_header_url", IPConfig.image_addressPrefix + "/" + userHeadUrl);
            Glide.with(this)
                    .load(IPConfig.image_addressPrefix + "/" + userHeadUrl)
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .into(mUserHeader);
        }
    }

    public void setUserName(String userName) {
        //通过返回的用户头像地址获取用户头像
        if (userName != null) {
            mUserNameTv.setText(userName);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPublishFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        //点击返回键关闭滑动菜单
        if (mDrawLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawLayout.closeDrawer(Gravity.LEFT);
        } else {
            if (System.currentTimeMillis() - mClickTime > 2000) {
                UnRepeatToast.showToast(this, "再按一次退出程序");
                mClickTime = System.currentTimeMillis();
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //进入应用后，通知栏应取消
        BmobNotificationManager.getInstance(this).cancelNotification();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除注册EventBus
        EventBus.getDefault().unregister(this);
    }
}
