package com.yuzhai.global;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.yuzhai.dao.CookieOperate;
import com.yuzhai.dao.UserInfoOperate;

/**
 * Created by Administrator on 2016/7/9.
 */
public class CustomApplication extends Application {
    private RequestQueue mRequestQueue;
    private UserInfoOperate userInfoOperate;
    private CookieOperate cookieOperate;
    private boolean login = false;

    @Override
    public void onCreate() {
        super.onCreate();
        userInfoOperate = new UserInfoOperate(this);
        cookieOperate = new CookieOperate(this);
    }

    //用户信息操作
    public String getUserPhone() {
        return userInfoOperate.getUserPhone();
    }

    public String getPassword() {
        return userInfoOperate.getPassword();
    }

    public void addUserInfo(String userPhone, String password) {
        userInfoOperate.addUserInfo(userPhone, password);
    }

    public void clearUserInfo() {
        userInfoOperate.clearUserInfo();
    }

    //Cookie信息操作
    public String getCookie() {
        return cookieOperate.getCookie();
    }

    public void addCookie(String cookie) {
        cookieOperate.addCookie(cookie);
    }

    public void clearCookie() {
        cookieOperate.clearCookie();
    }

    //获取请求队列
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    //登录操作
    public boolean isLogin() {
        return login;
    }

    public void setLoginState(boolean loginState) {
        this.login = loginState;
    }
}
