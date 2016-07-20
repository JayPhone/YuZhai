package com.yuzhai.entry;

/**
 * Created by Administrator on 2016/7/19.
 */
public class UserForgetPswd extends UserLogin {
    private String temVerify;

    public UserForgetPswd() {
        super();
    }

    public UserForgetPswd(String userPhone, String userPsw, String verifyValue) {
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
