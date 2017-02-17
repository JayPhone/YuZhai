package com.yuzhai.http;

/**
 * 创建时间：2016/8/18
 * 作者:HJF
 * 主要功能:各种请求的参数信息
 */
public class RequestParamsNameConfig {
    public class BaseParam {
    }

    /**
     * 登录参数
     */
    public class LoginParam extends BaseParam {
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
    public class VerifyParam extends BaseParam {
        /**
         * 获取验证码的手机号: USERPHONE
         */
        public final static String USERPHONE = "userPhone";
    }

    /**
     * 注册参数
     */
    public class RegisterParam extends BaseParam {
        /**
         * 注册号码
         */
        public final static String USERPHONE = "userPhone";
        /**
         * 注册密码
         */
        public final static String USERPSW = "userPsw";
    }

    /**
     * 重命名用户名参数
     */
    public class ReNameParam extends BaseParam {
        /**
         * 新用户名
         */
        public final static String NAME = "name";
    }

    /**
     * 忘记密码参数
     */
    public class ForgetPswParam extends BaseParam {
        /**
         * 用户手机号码
         */
        public final static String USERPHONE = "userPhone";
        /**
         * 新密码
         */
        public final static String USERPSW = "userPsw";
    }

    /**
     * 修改密码参数
     */
    public class AlterPswParam extends BaseParam {
        /**
         * 旧密码
         */
        public final static String OLDPSW = "password1";
        /**
         * 新密码
         */
        public final static String NEWPSW = "password2";
        /**
         * 确认密码
         */
        public final static String COMPSW = "password3";
    }

    /**
     * 取消已发布订单参数
     */
    public class CancelPublishedOrderParam extends BaseParam {
        /**
         * 发布的订单号
         */
        public final static String ORDER_ID = "orderID";
    }

    /**
     * 通过类型查询订单参数
     */
    public class OrdersByTypeParam extends BaseParam {
        /**
         * 项目类型
         */
        public final static String ITEMTYPE = "itemType";
        /**
         * 是否第一次访问
         */
        public final static String FIRST = "first";
    }

    /**
     * 申请接收订单参数
     */
    public class ApplyOrderParam extends BaseParam {
        /**
         * 订单ID
         */
        public final static String ORDERID = "orderID";
    }

    /**
     * 发布订单参数
     */
    public class PublishOrderParam extends BaseParam {
        /**
         * 需求标题
         */
        public final static String TITLE = "title";

        /**
         * 需求内容
         */
        public final static String DESCRIPTION = "description";

        /**
         * 需求类型
         */
        public final static String TYPE = "type";

        /**
         * 需求期限
         */
        public final static String DEADLINE = "deadline";

        /**
         * 联系电话
         */
        public final static String TEL = "tel";

        /**
         * 项目金额
         */
        public final static String REWARD = "reward";
    }

    /**
     * 查看详细订单参数
     */
    public class OrderDetailParam extends BaseParam {
        /**
         * 订单ID
         */
        public final static String ORDERID = "orderID";
    }

    /**
     * 投递简历参数
     */
    public class SendResumeParam extends BaseParam {
        public final static String NAME = "name";
        public final static String SEX = "sex";
        public final static String MODULE = "module";
        public final static String EDUCATION = "education";
        public final static String CONTACT_NUMBER = "contactNumber";
        public final static String EDUCATION_EXPERIENCE = "educationExperience";
        public final static String SKILL = "skill";
        public final static String WORK_EXPERIENCE = "workExperience";
        public final static String SELF_EVALUATION = "selfEvaluation";
    }

    /**
     * 通过类型查询简历参数
     */
    public class ResumesByTypeParam extends BaseParam {
        public final static String TYPE = "type";
    }

    /**
     * 通过类型查询简历参数
     */
    public class DetailResumeParam extends BaseParam {
        public final static String USER_PHONE = "userPhone";
    }

    /**
     * 查询个人已发布订单参数
     */
    public class PublishedOrderParam extends BaseParam {
        public final static String FIRST = "first";
    }
}
