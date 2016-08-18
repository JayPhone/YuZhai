package com.yuzhai.config;

/**
 * 创建时间：2016/8/18
 * 作者:HJF
 * 主要功能:各种请求的参数信息
 */
public class ParamsNameConfig {
    /**
     * 登录参数
     */
    public class LoginParam {
        /**
         * 登录手机号: USERPHONE
         */
        public final static String USERPHONE = "userPhone";
        /**
         * 登录密码:  USERPSWD
         */
        public final static String USERPSWD = "userPsw";
    }

    /**
     * 获取验证码参数
     */
    public class VerifyParam {
        /**
         * 获取验证码的手机号: USERPHONE
         */
        public final static String USERPHONE = "userPhone";
    }

    /**
     * 注册参数
     */
    public class RegisterParam {
        /**
         * 注册号码
         */
        public final static String USERPHONE = "userPhone";
        /**
         * 验证码
         */
        public final static String TEMVERIFY = "temVerify";
        /**
         * 注册密码
         */
        public final static String USERPSW = "userPsw";
    }

}
