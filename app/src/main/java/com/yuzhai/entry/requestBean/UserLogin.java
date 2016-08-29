package com.yuzhai.entry.requestBean;

/**
 * 创建时间：2016/5/14
 * 作者：HJF
 * 主要功能：用户登录信息类
 */
public class UserLogin extends UserPhone {
    private String userPsw = null;

    public UserLogin() {
        super();
    }

    public UserLogin(String userPhone, String userPsw) {
        super(userPhone);
        this.userPsw = userPsw;
    }

    public String getUserPsw() {
        return userPsw;
    }

    public void setUserPsw(String userPsw) {
        this.userPsw = userPsw;
    }

}
