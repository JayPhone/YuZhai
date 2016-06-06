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
     * 对HttpUrlConnection进行初始化
     * @param 请求的路径
     * @return 返回初始化的HttpUrlConnection
     */
    public static HttpURLConnection ConnectInit(String path) {
        //Url连接
        HttpURLConnection httpURLConnection;
        try {
            URL url = new URL(path);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            //设置请求头字段
            httpURLConnection.setRequestProperty("accept", "*/*");
            httpURLConnection.setRequestProperty("accept-charset", "UTF-8");
            httpURLConnection.setRequestProperty("charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
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
