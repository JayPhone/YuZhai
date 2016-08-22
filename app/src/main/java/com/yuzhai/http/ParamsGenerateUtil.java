package com.yuzhai.http;

import com.yuzhai.config.ParamsNameConfig;

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
     * @param userPawd  登录的密码
     * @return 返回登陆的请求参数集
     */
    public static Map<String, String> generateLoginParams(String userPhone,
                                                          String userPawd) {
        Map<String, String> params = new HashMap<>();
        params.put(ParamsNameConfig.LoginParam.USERPHONE, userPhone);
        params.put(ParamsNameConfig.LoginParam.USERPSWD, userPawd);
        return params;
    }

    /**
     * 生成获取验证码请求的参数集
     *
     * @param phoneNum 获取验证码的手机号
     * @return 返回获取验证码的请求参数集
     */
    public static Map<String, String> generateVerifyParams(String phoneNum) {
        Map<String, String> params = new HashMap<>();
        params.put(ParamsNameConfig.VerifyParam.USERPHONE, phoneNum);
        return params;
    }

    /**
     * 生成注册请求的参数集
     *
     * @param regPhone  注册号码
     * @param checkCode 验证码
     * @param regPsw    注册密码
     * @return 返回注册请求的参数集
     */
    public static Map<String, String> generateRegisterParams(String regPhone,
                                                             String checkCode,
                                                             String regPsw) {
        Map<String, String> params = new HashMap<>();
        params.put(ParamsNameConfig.RegisterParam.USERPHONE, regPhone);
        params.put(ParamsNameConfig.RegisterParam.TEMVERIFY, checkCode);
        params.put(ParamsNameConfig.RegisterParam.USERPSW, regPsw);
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
        params.put(ParamsNameConfig.ReNameParam.NEWNAME, newName);
        return params;
    }

    /**
     * 生成忘记密码请求的参数集
     *
     * @param regPhone  用户手机号码
     * @param checkCode 验证码
     * @param regPsw    新密码
     * @return 返回忘记密码请求的参数集
     */
    public static Map<String, String> generateForgetPswParams(String regPhone,
                                                              String checkCode,
                                                              String regPsw) {
        Map<String, String> params = new HashMap<>();
        params.put(ParamsNameConfig.ForgetPswParam.USERPHONE, regPhone);
        params.put(ParamsNameConfig.ForgetPswParam.TEMVERIFY, checkCode);
        params.put(ParamsNameConfig.ForgetPswParam.USERPSW, regPsw);
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
                                                             String newPsw) {
        Map<String, String> params = new HashMap<>();
        params.put(ParamsNameConfig.AlterPswParam.OLDPSW, oldPsw);
        params.put(ParamsNameConfig.AlterPswParam.USERPSW, newPsw);
        return params;
    }

    /**
     * 生成取消已发布订单请求的参数集
     *
     * @param publishId 已发布的订单号
     * @return 返回取消已发布订单请求的参数集
     */
    public static Map<String, String> generateCancelPublishedOrderParams(String publishId) {
        Map<String, String> params = new HashMap<>();
        params.put(ParamsNameConfig.CancelPublishedOrderParam.PUBLISHID, publishId);
        return params;
    }
}
