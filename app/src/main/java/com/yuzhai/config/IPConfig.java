package com.yuzhai.config;

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
    String DesAddress = "192.168.135.1:8088";

    /**
     * 主机名称
     */
    String hostName = "jfinal_yzgz";

    /**
     * 地址前缀
     */
    String addressPrefix = protocol + "://" + DesAddress + "/" + hostName;

    /**
     * 获取验证码地址
     */
    String verifyAddress = addressPrefix + "/login/verify";

    /**
     * 用户登录地址
     */
    String loginAddress = addressPrefix + "/login/login";

    /**
     * 用户注册地址
     */
    String registerAddress = addressPrefix + "/login/register";

    /**
     * 修改用户密码地址
     */
    String alterPswAddress = addressPrefix + "/login/alter_psw";

    /**
     * 忘记密码修改地址
     */
    String forgetPswAddress = addressPrefix + "/login/forget_psw";

    /**
     * 发布需求地址
     */
    String publishOrderAddress = addressPrefix + "/publish/save";

    /**
     * 上传用户头像地址
     */
    String uploadHeadAddress = addressPrefix + "/headupload/uploadUserHead";

    /**
     * 重命名用户名地址
     */
    String reNameAddress = addressPrefix + "/rename";

    /**
     * 通过需求类型查看项目订单地址
     */
    String ordersByTypeAddress = addressPrefix + "/selectOrderByType";

    /**
     * 申请接收订单地址
     */
    String applyOrderAddress = addressPrefix + "/apply";

    /**
     * 查看已发布订单地址
     */
    String orderPublishedAddress = addressPrefix + "/selectOrderByType/personalPublish";

    /**
     * 取消已发布订单地址
     */
    String cancelPublishedOrderAddress = addressPrefix + "/cancelPublish";
}
