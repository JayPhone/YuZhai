package com.yuzhai.entry;

/**
 * 创建时间：2016/5/14.
 * 作者：HJF
 * 主要功能：用户注册信息类
 */
public class UserReg extends User {
    private String identCode;

    public UserReg() {
        super();
        this.identCode = "";
    }

    public UserReg(String acount, String pawd, String identCode) {
        super(acount, pawd);
        this.identCode = identCode;
    }

    public String getIdentCode() {
        return identCode;
    }

    public void setIdentCode(String identCode) {
        this.identCode = identCode;
    }
}
