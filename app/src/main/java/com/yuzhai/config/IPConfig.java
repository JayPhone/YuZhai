package com.yuzhai.config;

/**
 * 创建时间：2016/5/14
 * 作者：HJF
 * 主要功能：IP地址相关的信息
 */
public interface IPConfig {
    final static String IPAddress = "119.29.176.249:8080";
    final static String addressPrefix = "http://" + IPAddress + "/jfinal_yzgz";
    final static String verifyAddress = "http://" + IPAddress + "/jfinal_yzgz/login/verify";
    final static String loginAddress = "http://" + IPAddress + "/jfinal_yzgz/login/login";
    final static String registerAddress = "http://" + IPAddress + "/jfinal_yzgz/login/register";
    final static String publishAddress = "http://" + IPAddress + "/jfinal_yzgz/publish/save";
    final static String uploadHeadAddress = "http://" + IPAddress + "/jfinal_yzgz/headupload/uploadUserHead";
    final static String renameAddress = "http://" + IPAddress + "/jfinal_yzgz/rename";
    final static String ordersAddress = "http://" + IPAddress + "/jfinal_yzgz/selectOrderByType";
    final static String applyAddress = "http://" + IPAddress + "/jfinal_yzgz/apply";
    final static String orderPublishedAddress = "http://" + IPAddress + "/jfinal_yzgz/selectOrderByType/personalPublish";
    final static String alterPswdAddress = "http://" + IPAddress + "/jfinal_yzgz/login/alter_psw";
    final static String forgetPswdAddress = "http://" + IPAddress + "/jfinal_yzgz/login/forget_psw";
    final static String cancelPublishedOrderAddress = "http://" + IPAddress + "/jfinal_yzgz/cancelPublish";
}
