package com.yuzhai.http;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
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
     *主要功能：完成HttpURLConnection的初始化
     * @param path Url路径
     * @param timeout 超时时间
     * @param method 提交方式
     * @param openInput 是否打开输入流
     * @param openOutput 是否打开输出流
     * @return 配置成功返回HttpURLConnection，否则返回Null
     */
    public static InputStream ConnectInit(String path, int timeout, String method, boolean openInput, boolean openOutput) {
        //连接状态码
        int responseCode;
        //Url连接
        HttpURLConnection httpURLConnection;
        try {
            URL url = new URL(path);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(timeout);
            httpURLConnection.setRequestMethod(method);
            httpURLConnection.setDoInput(openInput);
            httpURLConnection.setDoOutput(openOutput);
            responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200) {
                Log.i("responseCode", responseCode + "");
                return httpURLConnection.getInputStream();
            } else {
                Log.i("responseCode", responseCode + "");
            }
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
