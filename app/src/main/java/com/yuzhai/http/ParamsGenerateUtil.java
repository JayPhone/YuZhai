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
    public static Map<String, String> generateLoginParams(String userPhone, String userPawd) {
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
}
