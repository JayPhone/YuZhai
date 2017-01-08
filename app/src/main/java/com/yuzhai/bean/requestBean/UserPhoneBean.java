package com.yuzhai.bean.requestBean;

/**
 * 创建时间：2016/5/14
 * 作者：HJF
 * 主要功能：用户信息类
 */
public class UserPhoneBean {
    private String userPhone = null;

    public UserPhoneBean() {
    }

    public UserPhoneBean(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

}
