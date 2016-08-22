package com.yuzhai.entry;

/**
 * Created by Administrator on 2016/7/19.
 */
public class UserForgetPsw extends UserLogin {
    private String temVerify;

    public UserForgetPsw() {
        super();
    }

    public UserForgetPsw(String userPhone, String userPsw, String verifyValue) {
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
