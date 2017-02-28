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
    String DesAddress = "172.16.172.20:8080";

    /**
     * 主机名称
     */
    String hostName = "yzgz1.3mobile/action";

    /**
     * 图片名称
     */
    String imagePrefix = "yzgz1.3mobile";

    /**
     * 地址前缀
     */
    String addressPrefix = protocol + "://" + DesAddress + "/" + hostName;

    /**
     * 图片地址前缀
     */
    String image_addressPrefix = protocol + "://" + DesAddress + "/" + imagePrefix;

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
    String registerAddress = addressPrefix + "/regisuser";

    /**
     * 修改用户密码地址
     */
    String alterPswAddress = addressPrefix + "/repassword";

    /**
     * 忘记密码修改地址
     */
    String forgetPswAddress = addressPrefix + "/resetpsw";

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
     * 查看已接收订单地址
     */
    String orderAcceptedAddress = addressPrefix + "/lookupreceive";

    /**
     * 查看已申请订单地址
     */
    String orderAppliedAddress = addressPrefix + "/lookupapply";

    /**
     * 取消已发布订单地址
     */
    String cancelPublishedOrderAddress = addressPrefix + "/cancelpublish";

    /**
     * 取消已申请订单地址
     */
    String cancelAppliedOrderAddress = addressPrefix + "/cancelapply";

    /**
     * 取消已接收订单地址
     */
    String cancelAcceptedOrderAddress = addressPrefix + "/cancelreceive";

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
    String sendResumeAddress = addressPrefix + "/sendresume";

    /**
     * 通过需求类型查看简历订单地址
     */
    String resumesByTypeAddress = addressPrefix + "/resumes";

    /**
     * 查看详细简历地址
     */
    String resumeDetailAddress = addressPrefix + "/detailedresume";

    /**
     * 查看个人已发布简历地址
     */
    String personalResumeAddress = addressPrefix + "/selfdetailedresume";

    /**
     * 查看申请用户数据地址
     */
    String applyUserDataAddress = addressPrefix + "/lookupbyavatar";

    /**
     * 同意用户申请地址
     */
    String decideBidderAddress = addressPrefix + "/decidebidder";

    /**
     * 获取聊天用户信息地址
     */
    String queryContactUserInfoAddress = addressPrefix + "/getidandavatar";
}
