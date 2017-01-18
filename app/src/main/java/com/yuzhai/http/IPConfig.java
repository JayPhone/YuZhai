package com.yuzhai.http;

/**
 * 创建时间：2016/5/14
 * 作者：HJF
 * 主要功能：IP地址相关的信息
 */
public interface IPConfig {
    /**
     * 协议名称
     */
    String protocol = "http";

    /**
     * 目标地址和端口
     */
    String DesAddress = "172.16.178.5:8088";

    /**
     * 主机名称
     */
    String hostName = "yzgz1.2mobile/servlet";

    /**
     * 图片主机名称
     */
    String image_hostName = "yzgz1.2mobile";

    /**
     * 地址前缀
     */
    String addressPrefix = protocol + "://" + DesAddress + "/" + hostName;

    /**
     * 图片地址前缀
     */
    String image_addressPrefix = protocol + "://" + DesAddress + "/" + image_hostName;

    /**
     * 获取验证码地址
     */
    String verifyAddress = addressPrefix + "/login/verify";

    /**
     * 用户登录地址
     */
    String loginAddress = addressPrefix + "/login";

    /**
     * 用户注册地址
     */
    String registerAddress = addressPrefix + "/login/register";

    /**
     * 修改用户密码地址
     */
    String alterPswAddress = addressPrefix + "/repassword";

    /**
     * 忘记密码修改地址
     */
    String forgetPswAddress = addressPrefix + "/login/forget_psw";

    /**
     * 发布需求地址
     */
    String publishOrderAddress = addressPrefix + "/publishtask";

    /**
     * 上传用户头像地址
     */
    String uploadHeadAddress = addressPrefix + "/changeavatar";

    /**
     * 重命名用户名地址
     */
    String reNameAddress = addressPrefix + "/rename";

    /**
     * 通过需求类型查看项目订单地址
     */
    String ordersByTypeAddress = addressPrefix + "/lookupbytype";

    /**
     * 申请接收订单地址
     */
    String applyOrderAddress = addressPrefix + "/applyorder";

    /**
     * 查看已发布订单地址
     */
    String orderPublishedAddress = addressPrefix + "/lookuppublished";

    /**
     * 查看以接收订单地址
     */
    String orderAcceptedAddress = addressPrefix + "/lookupreceived";

    /**
     * 取消已发布订单地址
     */
    String cancelPublishedOrderAddress = addressPrefix + "/cancelpublish";

    /**
     * 查看详细订单地址
     */
    String orderDetailAddress = addressPrefix + "/detailedorder";

    /**
     * 退出登录地址
     */
    String exitLoginAddress = addressPrefix + "/quit";

    /**
     * 投递简历地址
     */
    String sendResumeAddress = addressPrefix + "/launchresume";

    /**
     * 通过需求类型查看简历订单地址
     */
    String resumesByTypeAddress = addressPrefix + "/resumes";

    /**
     * 查看详细简历地址
     */
    String resumeDetailAddress = addressPrefix + "/detailedresumes";

    /**
     * 查看个人已发布简历
     */
    String personalResumeAddress = addressPrefix + "/selfdetailedresume";
}
