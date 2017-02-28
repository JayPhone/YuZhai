package com.yuzhai.http;

import com.yuzhai.bean.requestBean.SendResumeBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/17.
 */
public class ParamsGenerateUtil {

    /**
     * 生成登录请求的参数集
     *
     * @param userPhone 登录的用户名
     * @param userPsw   登录的密码
     * @param regId     手机标识号
     * @return 返回登陆的请求参数集
     */
    public static Map<String, String> generateLoginParams(String userPhone,
                                                          String userPsw,
                                                          String regId) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.LoginParam.USERPHONE, userPhone);
        params.put(RequestParamsNameConfig.LoginParam.USERPSWD, userPsw);
        params.put(RequestParamsNameConfig.LoginParam.REGID, regId);
        return params;
    }

    /**
     * 生成获取验证码请求的参数集
     *
     * @param userPhone 获取验证码的手机号
     * @return 返回获取验证码的请求参数集
     */
    public static Map<String, String> generateVerifyParams(String userPhone) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.VerifyParam.USERPHONE, userPhone);
        return params;
    }

    /**
     * 生成注册请求的参数集
     *
     * @param regPhone 注册号码
     * @param regPsw   注册密码
     * @return 返回注册请求的参数集
     */
    public static Map<String, String> generateRegisterParams(String regPhone,
                                                             String regPsw) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.RegisterParam.USERPHONE, regPhone);
        params.put(RequestParamsNameConfig.RegisterParam.USERPSW, regPsw);
        return params;
    }

    /**
     * 生成重命名用户名的参数集
     *
     * @param newName 新用户名
     * @return 返回重命名用户名请求的参数集
     */
    public static Map<String, String> generateReNameParam(String newName) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.ReNameParam.NAME, newName);
        return params;
    }

    /**
     * 生成忘记密码请求的参数集
     *
     * @param regPhone 用户手机号码
     * @param newPsw   新密码
     * @return 返回忘记密码请求的参数集
     */
    public static Map<String, String> generateForgetPswParams(String regPhone,
                                                              String newPsw,
                                                              String comPsw) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.ForgetPswParam.USERPHONE, regPhone);
        params.put(RequestParamsNameConfig.ForgetPswParam.NEWPSW, newPsw);
        params.put(RequestParamsNameConfig.ForgetPswParam.COMPSW, comPsw);
        return params;
    }

    /**
     * 生成修改密码请求的参数集
     *
     * @param oldPsw 新密码
     * @param newPsw 旧密码
     * @return 返回修改密码请求的参数集
     */
    public static Map<String, String> generateAlterPswParams(String oldPsw,
                                                             String newPsw,
                                                             String comPsw) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.AlterPswParam.OLDPSW, oldPsw);
        params.put(RequestParamsNameConfig.AlterPswParam.NEWPSW, newPsw);
        params.put(RequestParamsNameConfig.AlterPswParam.COMPSW, comPsw);
        return params;
    }

    /**
     * 生成取消已发布订单请求的参数集
     *
     * @param publishedId 已发布的订单号
     * @return 返回取消已发布订单请求的参数集
     */
    public static Map<String, String> generateCancelPublishedOrderParams(String publishedId) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.CancelPublishedOrderParam.ORDER_ID, publishedId);
        return params;
    }

    /**
     * 生成取消已申请订单请求的参数集
     *
     * @param appliedId 已申请的订单号
     * @return 返回取消已申请订单请求的参数集
     */
    public static Map<String, String> generateCancelAppliedOrderParams(String appliedId) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.CancelAppliedOrderParam.ORDER_ID, appliedId);
        return params;
    }

    /**
     * 生成取消已接收订单请求的参数集
     *
     * @param acceptedId 已接收的订单号
     * @return 返回取消已接收订单请求的参数集
     */
    public static Map<String, String> generateCancelAcceptedOrderParams(String acceptedId) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.CancelAcceptedOrderParam.ORDER_ID, acceptedId);
        return params;
    }

    /**
     * 生成通过类型查询订单请求的参数集
     *
     * @param itemType    需求类型
     * @param isFirstTime 是否第一次访问
     * @return 返回通过类型查询订单请求的参数集
     */
    public static Map<String, String> generateOrdersByTypeParams(String itemType, String isFirstTime) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.OrdersByTypeParam.ITEMTYPE, itemType);
        params.put(RequestParamsNameConfig.OrdersByTypeParam.FIRST, isFirstTime);
        return params;
    }

    /**
     * 生成申请接收订单请求的参数集
     *
     * @param orderId 订单ID
     * @return 返回申请接收订单请求的参数集
     */
    public static Map<String, String> generateApplyOrderParams(String orderId) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.ApplyOrderParam.ORDERID, orderId);
        return params;
    }

    /**
     * 生成发布订单请求的参数集
     *
     * @param title   需求标题
     * @param content 需求内容
     * @param type    需求类型
     * @param date    需求期限
     * @param contact 联系电话
     * @param money   项目金额
     * @return 返回发布订单请求的参数集
     */
    public static Map<String, String> generatePublishOrderParams(String title,
                                                                 String content,
                                                                 String type,
                                                                 String date,
                                                                 String contact,
                                                                 String money) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.PublishOrderParam.TITLE, title);
        params.put(RequestParamsNameConfig.PublishOrderParam.DESCRIPTION, content);
        params.put(RequestParamsNameConfig.PublishOrderParam.TYPE, type);
        params.put(RequestParamsNameConfig.PublishOrderParam.DEADLINE, date);
        params.put(RequestParamsNameConfig.PublishOrderParam.TEL, contact);
        params.put(RequestParamsNameConfig.PublishOrderParam.REWARD, money);
        return params;
    }

    /**
     * 生成查看详细订单请求的参数集
     *
     * @param orderId
     * @return
     */
    public static Map<String, String> generateOrderDetailParam(String orderId) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.OrderDetailParam.ORDERID, orderId);
        return params;
    }

    /**
     * 生成退出登录请求的参数集
     *
     * @return
     */
//    public static Map<String, String> generateExitLoginParam() {
//        Map<String, String> params = new HashMap<>();
//        return params;
//    }

    /**
     * 生成查看个人已发布的订单请求的参数集
     *
     * @return
     */
    public static Map<String, String> generatePublishedOrderParam(String isFirstTime) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.PublishedOrderParam.FIRST, isFirstTime);
        return params;
    }

    /**
     * 生成查看个人已申请的订单请求的参数集
     *
     * @return
     */
    public static Map<String, String> generateAppliedOrderParam(String isFirstTime) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.AppliedOrderParam.FIRST, isFirstTime);
        return params;
    }

    /**
     * 生成查看个人已接收的订单请求的参数集
     *
     * @return
     */
    public static Map<String, String> generateAcceptedOrderParam(String isFirstTime) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.AppliedOrderParam.FIRST, isFirstTime);
        return params;
    }

    /**
     * 生成投递简历请求的参数
     *
     * @param sendResumeBean
     * @return
     */
    public static Map<String, String> generateSendResumeParam(SendResumeBean sendResumeBean) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.SendResumeParam.NAME, sendResumeBean.getName());
        params.put(RequestParamsNameConfig.SendResumeParam.SEX, sendResumeBean.getSex());
        params.put(RequestParamsNameConfig.SendResumeParam.MODULE, sendResumeBean.getType());
        params.put(RequestParamsNameConfig.SendResumeParam.EDUCATION, sendResumeBean.getEducation());
        params.put(RequestParamsNameConfig.SendResumeParam.CONTACT_NUMBER, sendResumeBean.getTel());
        params.put(RequestParamsNameConfig.SendResumeParam.EDUCATION_EXPERIENCE, sendResumeBean.getEducationalExperience());
        params.put(RequestParamsNameConfig.SendResumeParam.SKILL, sendResumeBean.getSkill());
        params.put(RequestParamsNameConfig.SendResumeParam.WORK_EXPERIENCE, sendResumeBean.getWorkExperience());
        params.put(RequestParamsNameConfig.SendResumeParam.SELF_EVALUATION, sendResumeBean.getSelfEvaluation());
        return params;
    }

    /**
     * 生成通过类型查询简历请求的参数集
     *
     * @param type
     * @param isFirstTime
     * @return
     */
    public static Map<String, String> generateResumesByTypeParams(String type, String isFirstTime) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.ResumesByTypeParam.TYPE, type);
        params.put(RequestParamsNameConfig.ResumesByTypeParam.FIRST, isFirstTime);
        return params;
    }

    /**
     * 生成查看个人简历请求的参数集
     *
     * @return
     */
//    public static Map<String, String> generatePersonalResumeParams() {
//        Map<String, String> params = new HashMap<>();
//        return params;
//    }

    /**
     * 生成查看详细简历请求的参数集
     *
     * @return
     */
    public static Map<String, String> generateDetailResumeParams(String resumeId) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.DetailResumeParam.USER_PHONE, resumeId);
        return params;
    }

    /**
     * 生成查看申请订单用户的信息的参数集
     *
     * @param avatarText
     * @return
     */
    public static Map<String, String> generateApplyUserDataParams(String avatarText) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.ApplyUserDataParam.AVATAR, avatarText);
        return params;
    }

    /**
     * 生成同意用户申请订单的参数集
     *
     * @param orderId
     * @param bidderId
     * @return
     */
    public static Map<String, String> generateAgreeApplyOrderParams(String orderId, String bidderId) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.AgreeApplyOrderParam.ORDERID, orderId);
        params.put(RequestParamsNameConfig.AgreeApplyOrderParam.BIDDERID, bidderId);
        return params;
    }

    /**
     * 查询聊天用户信息的参数集
     *
     * @param userPhone
     * @return
     */
    public static Map<String, String> generateQueryContactUserInfo(String userPhone) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.QueryContactUserInfoParam.USERPHONE, userPhone);
        return params;
    }
}
