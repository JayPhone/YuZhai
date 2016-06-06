package com.yuzhai.entry;

/**
 * 创建时间：2016/5/14.
 * 作者：HJF
 * 主要功能：用户注册信息类
 */
public class UserReg extends UserLogin {
    private String temVerify;

    public UserReg() {
        super();
    }

    public UserReg(String userPhone, String userPsw, String verifyValue) {
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
