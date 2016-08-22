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

    /**
     * 重命名用户名参数
     */
    public class ReNameParam {
        /**
         * 新用户名
         */
        public final static String NEWNAME = "newname";
    }

    /**
     * 忘记密码参数
     */
    public class ForgetPswParam {
        /**
         * 用户手机号码
         */
        public final static String USERPHONE = "userPhone";
        /**
         * 验证码
         */
        public final static String TEMVERIFY = "temVerify";
        /**
         * 新密码
         */
        public final static String USERPSW = "userPsw";
    }

    /**
     * 修改密码参数
     */
    public class AlterPswParam {
        /**
         * 旧密码
         */
        public final static String OLDPSW = "oldPsw";
        /**
         * 新密码
         */
        public final static String USERPSW = "userPsw";
    }

    /**
     * 取消已发布订单参数
     */
    public class CancelPublishedOrderParam {
        /**
         * 发布的订单号
         */
        public final static String PUBLISHID = "publishId";
    }
}
