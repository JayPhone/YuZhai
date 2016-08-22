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

    /**
     * 忘记密码返回参数
     */
    public class ForgetPswParam {
        /**
         * 返回码: CODE
         */
        public final static String CODE = "code";
    }

    /**
     * 修改密码返回参数
     */
    public class AlterPswParam {
        /**
         * 返回码: CODE
         */
        public final static String CODE = "code";
    }

    /**
     * 查询已发布订单返回参数
     */
    public class PublishedOrdersParam {
        /**
         * 订单集合
         */
        public final static String ORDER = "order";
        public final static String IMAGE = "image";
    }

    /**
     * 取消已发布订单请求返回的参数
     */
    public class CancelPublishedOrdersParam {
        /**
         * 返回码: CODE
         */
        public final static String CODE = "code";
    }
}
