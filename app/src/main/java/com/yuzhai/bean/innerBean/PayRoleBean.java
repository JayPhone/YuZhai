package com.yuzhai.bean.innerBean;

/**
 * Created by 35429 on 2017/2/23.
 */

public class PayRoleBean {
    private String pay_role;

    public PayRoleBean(String pay_role) {
        this.pay_role = pay_role;
    }

    public String getPayRole() {
        return pay_role;
    }

    public void setPayRole(String pay_role) {
        this.pay_role = pay_role;
    }

    @Override
    public String toString() {
        return "PayRoleBean{" +
                "pay_role='" + pay_role + '\'' +
                '}';
    }
}
