package com.yuzhai.http;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * 创建时间:2016/5/18
 * 作者：HJF
 * 主要功能：主要将BaseHttpUtil返回的已初始化的HttpURLConnection进行操作，获取输入流或输出流
 */

public class HttpUtil {
    /*
     * 主要功能，利用BaseHttpUtil返回的HttpURLConnection对象获取输出流，默认输人流打开
     *
     * @param path       Url路径
     * @param timeout    超时时间
     * @param method     提交方式
     * @param openOutput 是否打开输出流
     * @return 返回InputStream, 否则为null
     */
    public static InputStream getInputStream(String path, int timeout, String method, boolean openOutput) {
        try {
            //连接状态码
            int responseCode;
            HttpURLConnection httpURLConnection = null;
            //返回HttpURLConnection对象
            httpURLConnection = BaseHttpUtil.ConnectInit(path, timeout, method, true, openOutput);
            responseCode = httpURLConnection.getResponseCode();
            //如果状态正常
            if (httpURLConnection != null) {
                if (responseCode == 200) {
                    return httpURLConnection.getInputStream();
                }
            } else {
                Log.e("yuzhai_error", "httpURLConnection返回null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
