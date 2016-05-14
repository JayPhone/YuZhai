package com.yuzhai.entry;

/**
 * 创建时间：2016/5/14
 * 作者：HJF
 * 主要功能：用户信息类
 */
public class User {
    private String acount;
    private String pawd;

    public User() {
        this.acount = "";
        this.pawd = "";
    }

    public User(String acount, String pawd) {
        this.acount = acount;
        this.pawd = pawd;
    }

    public String getAcount() {
        return acount;
    }

    public void setAcount(String acount) {
        this.acount = acount;
    }

    public String getPawd() {
        return pawd;
    }

    public void setPawd(String pawd) {
        this.pawd = pawd;
    }
}
