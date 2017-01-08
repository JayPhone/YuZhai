package com.yuzhai.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.yuzhai.adapter.MainViewPagerAdapter;
import com.yuzhai.bean.BaseUserInfoBean;
import com.yuzhai.bean.UserInfoBean;
import com.yuzhai.config.IPConfig;
import com.yuzhai.fragment.ContactFragment;
import com.yuzhai.fragment.HomeFragment;
import com.yuzhai.fragment.OrderFragment;
import com.yuzhai.fragment.PublishFragment;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/10.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private CustomApplication mCustomApplication;
    private RequestQueue mRequestQueue;

    private DrawerLayout mDrawLayout;
    private NavigationView mNavigationView;
    private LinearLayout mUserInfoLayout;
    private ImageView mUserHeader;
    private TextView mUserNameTv;
    private Button mLoginAndRegButton;

    private HomeFragment mHomeFragment;
    private OrderFragment mOrderFragment;
    private PublishFragment mPublishFragment;
    private ContactFragment mContactFragment;

    private String mUserHeadURL;
    private String mUserName;
    private long mClickTime = 0;

    private ViewPager mViewPager;
    private MainViewPagerAdapter mViewPagerAdapter;
    private TabLayout mTabLayout;
    private List<Fragment> mFragmentList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //注册EventBus
        EventBus.getDefault().register(this);
        mCustomApplication = (CustomApplication) getApplication();
        mRequestQueue = RequestQueueSingleton.getRequestQueue(this);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        //初始化组件
        initViews();
    }

    public void initViews() {
        mDrawLayout = (DrawerLayout) findViewById(R.id.drawer);
        mNavigationView = (NavigationView) findViewById(R.id.navigation);
        mNavigationView.setItemIconTintList(null);

        mFragmentList = new ArrayList<>();
        mHomeFragment = new HomeFragment();
        mOrderFragment = new OrderFragment();
        mPublishFragment = new PublishFragment();
        mContactFragment = new ContactFragment();

        mFragmentList.add(mHomeFragment);
        mFragmentList.add(mOrderFragment);
        mFragmentList.add(mPublishFragment);
        mFragmentList.add(mContactFragment);

        mViewPager = (ViewPager) findViewById(R.id.main_view_pager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeTabFocus(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPagerAdapter = new MainViewPagerAdapter(this, getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mViewPagerAdapter);
        mTabLayout = (TabLayout) findViewById(R.id.main_tab);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setSelectedTabIndicatorHeight(0);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(mViewPagerAdapter.getTabView(i));
            }
        }
        changeTabFocus(0);

        //菜单
        View headerView = mNavigationView.inflateHeaderView(R.layout.navigation_menu_header_layout);
        mNavigationView.inflateMenu(R.menu.drawer_login_menu);
        mUserInfoLayout = (LinearLayout) headerView.findViewById(R.id.user_info);

        mUserNameTv = (TextView) headerView.findViewById(R.id.user_name);
        mUserHeader = (ImageView) headerView.findViewById(R.id.head_image);
        mUserHeader.setOnClickListener(this);

        mLoginAndRegButton = (Button) headerView.findViewById(R.id.menu_login_register);
        mLoginAndRegButton.setOnClickListener(this);

        mNavigationView.setNavigationItemSelectedListener(this);

        //加载侧滑菜单布局
        initMenuLayout();
    }

    public void changeTabFocus(int position) {
        ImageView tabImage;
        TextView tabText;

        for (int tabIndex = 0; tabIndex < mTabLayout.getTabCount(); tabIndex++) {
            tabImage = (ImageView) mTabLayout.getTabAt(tabIndex).getCustomView().findViewById(R.id.tab_image);
            tabText = (TextView) mTabLayout.getTabAt(tabIndex).getCustomView().findViewById(R.id.tab_text);
            if (tabIndex == position) {
                Glide.with(this).load(mViewPagerAdapter.focusIcons[tabIndex]).into(tabImage);
                tabText.setTextColor(ContextCompat.getColor(this, R.color.mainColor));
            } else {
                Glide.with(this).load(mViewPagerAdapter.icons[tabIndex]).into(tabImage);
                tabText.setTextColor(ContextCompat.getColor(this, R.color.color_616161));
            }
        }
    }

    //初始化布局的事件和数据
    public void initMenuLayout() {
        //已成功登录
        if (mCustomApplication.isLogin()) {
            mLoginAndRegButton.setVisibility(View.INVISIBLE);
            mUserHeader.setVisibility(View.VISIBLE);
            mUserInfoLayout.setVisibility(View.VISIBLE);
            mNavigationView.getMenu().clear();
            mNavigationView.inflateMenu(R.menu.drawer_login_menu);
            //设置用户头像
            setUserHeader(mUserHeadURL);
            //设置用户名
            setUserName(mUserName);

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
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.collection:
                Intent collection = new Intent(this, CollectionActivity.class);
                startActivity(collection);
                break;
            case R.id.resume:
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
                        IPConfig.addressPrefix + mUserHeadURL,
                        mUserName,
                        mCustomApplication.getUserPhone(),
                        null,
                        mCustomApplication.isLogin()));
                break;

            case R.id.menu_login_register:
                Intent login = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(login);
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
        else if (baseUserInfoBean.getUserName() == null && baseUserInfoBean.getUserHeadUrl() != null && baseUserInfoBean.isLogin()) {
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
            Glide.with(this)
                    .load(userHeadUrl)
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
    protected void onDestroy() {
        super.onDestroy();
        //解除注册EventBus
        EventBus.getDefault().unregister(this);
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
}
