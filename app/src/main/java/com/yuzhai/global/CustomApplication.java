package com.yuzhai.global;

import android.app.Application;

import com.yuzhai.dao.CookieOperate;
import com.yuzhai.dao.UserInfoOperate;

/**
 * Created by Administrator on 2016/7/9.
 */
public class CustomApplication extends Application {
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

    //登录操作
    public boolean isLogin() {
        return login;
    }

    public void setLoginState(boolean loginState) {
        this.login = loginState;
    }
}
