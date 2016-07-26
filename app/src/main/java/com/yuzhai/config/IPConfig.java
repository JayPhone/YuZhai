package com.yuzhai.config;

/**
 * 创建时间：2016/5/14
 * 作者：HJF
 * 主要功能：IP地址相关的信息
 */
public interface IPConfig {
    final static String addressPrefix = "http://192.168.191.1:8080/jfinal_yzgz";
    final static String verifyAddress = "http://192.168.191.1:8080/jfinal_yzgz/login/verify";
    final static String loginAddress = "http://192.168.191.1:8080/jfinal_yzgz/login/login";
    final static String registerAddress = "http://192.168.191.1:8080/jfinal_yzgz/login/register";
    final static String publishAddress = "http://192.168.191.1:8080/jfinal_yzgz/publish/save";
    final static String uploadHeadAddress = "http://192.168.191.1:8080/jfinal_yzgz/headupload/uploadUserHead";
    final static String renameAddress = "http://192.168.191.1:8080/jfinal_yzgz/rename";
    final static String ordersAddress = "http://192.168.191.1:8080/jfinal_yzgz/selectOrderByType";
    final static String applyAddress = "http://192.168.191.1:8080/jfinal_yzgz/apply";
    final static String orderPublishedAddress = "http://192.168.191.1:8080/jfinal_yzgz/selectOrderByType/personalPublish";
}
