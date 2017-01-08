package com.yuzhai.bean.requestBean;

/**
 * 创建时间：2016/5/14.
 * 作者：HJF
 * 主要功能：用户注册信息类
 */
public class UserRegBeanBean extends UserLoginBean {
    private String temVerify;

    public UserRegBeanBean() {
        super();
    }

    public UserRegBeanBean(String userPhone, String userPsw, String verifyValue) {
        super(userPhone, userPsw);
        this.temVerify = verifyValue;
    }

    public String getTemVerify() {
        return temVerify;
    }

    public void setTemVerify(String temVerify) {
        this.temVerify = temVerify;
    }
}
