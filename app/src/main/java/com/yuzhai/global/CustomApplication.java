package com.yuzhai.global;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.yuzhai.dao.CookieOperate;
import com.yuzhai.dao.UserInfoOperate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/9.
 */
public class CustomApplication extends Application {
    private UserInfoOperate mUserInfoOperate;
    private CookieOperate mCookieOperate;
    private boolean login = true;
    public static boolean isConnect = false;
    private final String COOKIE = "cookie";
    private final String IMEI = "IMEI";

    @Override
    public void onCreate() {
        super.onCreate();
        mUserInfoOperate = new UserInfoOperate(this);
        mCookieOperate = new CookieOperate(this);
    }

    //用户信息操作
    public String getUserPhone() {
        return mUserInfoOperate.getUserPhone();
    }

    public String getPassword() {
        return mUserInfoOperate.getPassword();
    }

    public void addUserInfo(String userPhone, String password) {
        mUserInfoOperate.addUserInfo(userPhone, password);
    }

    public void clearUserInfo() {
        mUserInfoOperate.clearUserInfo();
    }

    public void setCookie(String cookie) {
        mCookieOperate.setCookie(cookie);
    }

    public String getCookie() {
        return mCookieOperate.getCookie();
    }

    public void clearCookie() {
        mCookieOperate.clearCookie();
    }

    public Map<String, String> generateCookieMap() {
        Map<String, String> cookie = new HashMap<>();
        cookie.put(COOKIE, getCookie());
        return cookie;
    }

    public String getToken() {
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        Log.i(IMEI, telephonyManager.getDeviceId());
        return telephonyManager.getDeviceId();
    }

    //登录操作
    public boolean isLogin() {
        return login;
    }

    public void setLoginState(boolean loginState) {
        this.login = loginState;
    }
}
