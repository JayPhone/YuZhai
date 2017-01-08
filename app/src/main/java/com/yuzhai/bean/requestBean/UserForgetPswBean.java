package com.yuzhai.bean.requestBean;

/**
 * Created by Administrator on 2016/7/19.
 */
public class UserForgetPswBean extends UserLoginBean {
    private String temVerify;

    public UserForgetPswBean() {
        super();
    }

    public UserForgetPswBean(String userPhone, String userPsw, String verifyValue) {
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
