package com.yuzhai.bean.requestBean;

/**
 * Created by Administrator on 2016/7/19.
 */
public class UserAlterPswBean {
    private String oldPswd;
    private String newPswd;

    public UserAlterPswBean() {
    }

    public UserAlterPswBean(String oldPswd, String newPswd) {
        this.oldPswd = oldPswd;
        this.newPswd = newPswd;
    }

    public String getNewPswd() {
        return newPswd;
    }

    public void setNewPswd(String newPswd) {
        this.newPswd = newPswd;
    }

    public String getOldPswd() {
        return oldPswd;
    }

    public void setOldPswd(String oldPswd) {
        this.oldPswd = oldPswd;
    }
}
