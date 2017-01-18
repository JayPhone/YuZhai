package com.yuzhai.bean.innerBean;

/**
 * Created by Administrator on 2016/11/23.
 */

public class BaseUserInfoBean {
    private boolean isLogin = true;
    private String userHeadUrl;
    private String userName;

    public BaseUserInfoBean(String userHeadUrl, String userName, boolean isLogin) {
        this.userHeadUrl = userHeadUrl;
        this.userName = userName;
        this.isLogin = isLogin;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getUserHeadUrl() {
        return userHeadUrl;
    }

    public void setUserHeadUrl(String userHeadUrl) {
        this.userHeadUrl = userHeadUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
