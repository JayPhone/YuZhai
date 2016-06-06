package com.yuzhai.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/4.
 */
public class HttpUtil {
    /*
     * 以get方法请求数据
     * @path 请求的路径
     * @timeOut 请求超时时间
     * @return 返回输入流
     */
    public static InputStream doGet(String path, int timeOut) {

        HttpURLConnection httpURLConnection;

        if ((httpURLConnection = BaseHttpUtil.ConnectInit(path)) != null) {
            httpURLConnection.setConnectTimeout(timeOut);
            try {
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                //响应正常
                if (httpURLConnection.getResponseCode() == 200) {
                    //返回输入流
                    return httpURLConnection.getInputStream();
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /*
     * 以Post方法请求数据
     * @path 请求的路径
     * @timeOut 请求超时时间
     * @params 请求参数
     * @return 输入流
     */
    public static InputStream doPost(String path, int timeOut, String params) {

        HttpURLConnection httpURLConnection = null;
        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        StringBuilder stringBuilder = null;
        String paramString = null;

        if ((httpURLConnection = BaseHttpUtil.ConnectInit(path)) != null) {
            httpURLConnection.setConnectTimeout(timeOut);
            try {
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                //获取输出流
                outputStream = httpURLConnection.getOutputStream();
                outputStreamWriter = new OutputStreamWriter(outputStream);
                //写出参数
                outputStreamWriter.write(params);
                outputStreamWriter.flush();
                //响应正常
                if (httpURLConnection.getResponseCode() == 200) {
                    Map<String, List<String>> map = httpURLConnection.getHeaderFields();
                    for (String key : map.keySet()) {
                        System.out.println(key + "--->" + map.get(key).toString());
                    }
                    //返回输入流
                    return httpURLConnection.getInputStream();
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
