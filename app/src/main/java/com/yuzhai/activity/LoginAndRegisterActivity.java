package com.yuzhai.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yuzhai.fragment.ForgetPswFragment;
import com.yuzhai.fragment.LoginFragment;
import com.yuzhai.fragment.RegisterFragment;
import com.yuzhai.yuzhaiwork.R;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by 35429 on 2017/2/16.
 */

public class LoginAndRegisterActivity extends AppCompatActivity implements
        LoginFragment.OnRegisterClickListener,
        LoginFragment.OnForgetPswClickListener,
        RegisterFragment.OnLoginClickListener,
        ForgetPswFragment.OnLoginClickListener {
    private static final String TAG = "LoginAndRegisterActivit";

    private LoginFragment mloginFragment;
    private RegisterFragment mRegisterFragment;
    private ForgetPswFragment mForgetPswFragment;
    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        initData();
    }

    private void initData() {
        if (mloginFragment == null) {
            mloginFragment = LoginFragment.newInstance();
            mloginFragment.setOnRegisterClickListener(this);
            mloginFragment.setOnForgetPswClickListener(this);
        }

        if (!mloginFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.login_register_content, mloginFragment)
                    .commit();
        }

        mCurrentFragment = mloginFragment;
    }

    private void clickLoginButton() {
        if (mloginFragment == null) {
            mloginFragment = LoginFragment.newInstance();
            mloginFragment.setOnRegisterClickListener(this);
            mloginFragment.setOnForgetPswClickListener(this);
        }

        showFragment(mloginFragment);
    }

    private void clickRegisterButton() {
        if (mRegisterFragment == null) {
            mRegisterFragment = RegisterFragment.newInstance();
            mRegisterFragment.setOnLoginClickListener(this);
        }

        showFragment(mRegisterFragment);
    }

    private void clickForgetPswButton() {
        if (mForgetPswFragment == null) {
            mForgetPswFragment = mForgetPswFragment.newInstance();
            mForgetPswFragment.setOnLoginClickListener(this);

        }

        showFragment(mForgetPswFragment);
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mCurrentFragment == fragment) {
            return;
        }

        if (!fragment.isAdded()) {
            transaction.hide(mCurrentFragment)
                    .add(R.id.login_register_content, fragment)
                    .commit();
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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    //点击注册导航
    @Override
    public void onRegisterClick() {
        clickRegisterButton();
    }

    //点击登陆导航
    @Override
    public void onLoginClick() {
        clickLoginButton();
    }

    @Override
    public void onForgetPswClick() {
        clickForgetPswButton();
    }

    @Override
    public void onLoginNavClick() {
        clickLoginButton();
    }
}
