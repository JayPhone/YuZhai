package com.yuzhai.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * 创建时间：2016/05/14
 * 作者：HJF
 * 主要功能：实现Http连接的初始化
 */
public class BaseHttpUtil {
    /*
    *主要功能：完成HttpURLConnection的初始化,此方法为重载方法，默认输入流和输出流关闭,超时时间为3000毫秒,请求方式为"post"
    * @param path Url路径
    * @param method 超时时间
    * @return 配置成功返回HttpURLConnection，否则返回Null
    */
    public static HttpURLConnection ConnectInit(String path) {
        return BaseHttpUtil.ConnectInit(path, 3000, "post", false, false);
    }

    /*
     *主要功能：完成HttpURLConnection的初始化,此方法为重载方法，默认输入流和输出流关闭,超时时间为3000毫秒
     * @param path Url路径
     * @param method 超时时间
     * @return 配置成功返回HttpURLConnection，否则返回Null
     */
    public static HttpURLConnection ConnectInit(String path, String method) {
        return BaseHttpUtil.ConnectInit(path, 3000, method, false, false);
    }

    /*
    * 主要功能：完成HttpURLConnection的初始化,此方法为重载方法，默认输入流和输出流关闭,请求方法为"post"
    * @param path Url路径
    * @param timeout 超时时间
    * @return 配置成功返回HttpURLConnection，否则返回Null
    */
    public static HttpURLConnection ConnectInit(String path, int timeout) {
        return BaseHttpUtil.ConnectInit(path, timeout, "post", false, false);
    }

    /*
     * 主要功能：完成HttpURLConnection的初始化,此方法为重载方法，默认输入流和输出流关闭
     * @param path    Url路径
     * @param timeout 超时时间
     * @param method  提交方式
     * @return 配置成功返回HttpURLConnection，否则返回Null
     */
    public static HttpURLConnection ConnectInit(String path, int timeout, String method) {
        return BaseHttpUtil.ConnectInit(path, timeout, method, false, false);
    }

    /*
     *主要功能：完成HttpURLConnection的初始化
     * @param path Url路径
     * @param timeout 超时时间
     * @param method 提交方式
     * @param openInput 是否打开输入流
     * @param openOutput 是否打开输出流
     * @return 配置成功返回HttpURLConnection，否则返回Null
     */
    public static HttpURLConnection ConnectInit(String path, int timeout, String method, boolean openInput, boolean openOutput) {
        try {
            URL url = new URL(path);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(timeout);
            httpURLConnection.setRequestMethod(method);
            httpURLConnection.setDoInput(openInput);
            httpURLConnection.setDoOutput(openOutput);
            return httpURLConnection;
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
