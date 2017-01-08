package com.yuzhai.http;

import com.yuzhai.config.RequestParamsNameConfig;

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
     * @param token     标识
     * @return 返回登陆的请求参数集
     */
    public static Map<String, String> generateLoginParams(String userPhone,
                                                          String userPsw,
                                                          String token) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.LoginParam.USERPHONE, userPhone);
        params.put(RequestParamsNameConfig.LoginParam.USERPSWD, userPsw);
        params.put(RequestParamsNameConfig.LoginParam.TOKEN, token);
        return params;
    }

    /**
     * 生成获取验证码请求的参数集
     *
     * @param userPhone 获取验证码的手机号
     * @param token     标识
     * @return 返回获取验证码的请求参数集
     */
    public static Map<String, String> generateVerifyParams(String userPhone,
                                                           String token) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.VerifyParam.USERPHONE, userPhone);
        params.put(RequestParamsNameConfig.VerifyParam.TOKEN, token);
        return params;
    }

    /**
     * 生成注册请求的参数集
     *
     * @param regPhone  注册号码
     * @param checkCode 验证码
     * @param regPsw    注册密码
     * @param token     标识
     * @return 返回注册请求的参数集
     */
    public static Map<String, String> generateRegisterParams(String regPhone,
                                                             String checkCode,
                                                             String regPsw,
                                                             String token) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.RegisterParam.USERPHONE, regPhone);
        params.put(RequestParamsNameConfig.RegisterParam.TEMVERIFY, checkCode);
        params.put(RequestParamsNameConfig.RegisterParam.USERPSW, regPsw);
        params.put(RequestParamsNameConfig.RegisterParam.TOKEN, token);
        return params;
    }

    /**
     * 生成重命名用户名的参数集
     *
     * @param newName 新用户名
     * @param token   标识
     * @return 返回重命名用户名请求的参数集
     */
    public static Map<String, String> generateReNameParam(String newName,
                                                          String token) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.ReNameParam.NEWNAME, newName);
        params.put(RequestParamsNameConfig.ReNameParam.TOKEN, token);
        return params;
    }

    /**
     * 生成忘记密码请求的参数集
     *
     * @param regPhone  用户手机号码
     * @param checkCode 验证码
     * @param regPsw    新密码
     * @param token     标识
     * @return 返回忘记密码请求的参数集
     */
    public static Map<String, String> generateForgetPswParams(String regPhone,
                                                              String checkCode,
                                                              String regPsw,
                                                              String token) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.ForgetPswParam.USERPHONE, regPhone);
        params.put(RequestParamsNameConfig.ForgetPswParam.TEMVERIFY, checkCode);
        params.put(RequestParamsNameConfig.ForgetPswParam.USERPSW, regPsw);
        params.put(RequestParamsNameConfig.ForgetPswParam.TOKEN, token);
        return params;
    }

    /**
     * 生成修改密码请求的参数集
     *
     * @param oldPsw 新密码
     * @param newPsw 旧密码
     * @param token  标识
     * @return 返回修改密码请求的参数集
     */
    public static Map<String, String> generateAlterPswParams(String oldPsw,
                                                             String newPsw,
                                                             String token) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.AlterPswParam.OLDPSW, oldPsw);
        params.put(RequestParamsNameConfig.AlterPswParam.USERPSW, newPsw);
        params.put(RequestParamsNameConfig.AlterPswParam.TOKEN, token);
        return params;
    }

    /**
     * 生成取消已发布订单请求的参数集
     *
     * @param publishId 已发布的订单号
     * @param token     标识
     * @return 返回取消已发布订单请求的参数集
     */
    public static Map<String, String> generateCancelPublishedOrderParams(String publishId,
                                                                         String token) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.CancelPublishedOrderParam.PUBLISHID, publishId);
        params.put(RequestParamsNameConfig.CancelPublishedOrderParam.TOKEN, token);
        return params;
    }

    /**
     * 生成通过类型查询订单请求的参数集
     *
     * @param itemType 需求类型
     * @param token    标识
     * @return 返回通过类型查询订单请求的参数集
     */
    public static Map<String, String> generateOrdersByTypeParams(String itemType,
                                                                 String token) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.OrdersByTypeParam.ITEMTYPE, itemType);
        params.put(RequestParamsNameConfig.OrdersByTypeParam.TOKEN, token);
        return params;
    }

    /**
     * 生成申请接收订单请求的参数集
     *
     * @param orderId 订单ID
     * @param token   标识
     * @return 返回申请接收订单请求的参数集
     */
    public static Map<String, String> generateApplyOrderParams(String orderId,
                                                               String token) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.ApplyOrderParam.ORDERID, orderId);
        params.put(RequestParamsNameConfig.ApplyOrderParam.TOKEN, token);
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
     * @param token
     * @return
     */
    public static Map<String, String> generateOrderDetailParam(String orderId, String token) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.OrderDetailParam.ORDERID, orderId);
        params.put(RequestParamsNameConfig.OrderDetailParam.TOKEN, token);
        return params;
    }

    /**
     * 生成退出登录请求的参数集
     *
     * @param token
     * @return
     */
    public static Map<String, String> generateExitLoginParam(String token) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParamsNameConfig.OrderDetailParam.TOKEN, token);
        return params;
    }
}
