package com.yuzhai.config;

/**
 * Created by Administrator on 2016/8/18.
 */
public class RespParamsNameConfig {
    /**
     * 登录返回参数
     */
    public class LoginRespParam {
        /**
         * 返回码: CODE
         */
        public final static String CODE = "code";
        /**
         * 用户头像路径:  USERHEAD
         */
        public final static String USERHEAD = "userHead";
        /**
         * 用户名:USERNAME
         */
        public final static String USERNAME = "userName";
    }

    /**
     * 获取验证码返回参数
     */
    public class VerifyResParam {
        /**
         * 返回码: CODE
         */
        public final static String CODE = "code";
    }

    /**
     * 注册返回参数
     */
    public class RegisterParam {
        /**
         * 返回码: CODE
         */
        public final static String CODE = "code";
    }

}
