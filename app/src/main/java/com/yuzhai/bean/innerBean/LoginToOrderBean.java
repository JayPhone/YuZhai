package com.yuzhai.bean.innerBean;

/**
 * Created by Administrator on 2017/1/12.
 */

public class LoginToOrderBean {
    private boolean isLogin;

    public LoginToOrderBean(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
