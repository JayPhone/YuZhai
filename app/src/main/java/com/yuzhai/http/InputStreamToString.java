package com.yuzhai.http;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 创建时间:2016/5/18
 * 作者：HJF
 * 主要功能：主要将HttpUtil返回的InputStream转化成String
 */
public class InputStreamToString {

    public static String praseToString(String path) {
        return InputStreamToString.praseToString(path, 3000, "post", false);
    }

    public static String praseToString(String path, int timeout) {
        return InputStreamToString.praseToString(path, timeout, "post", false);
    }

    public static String praseToString(String path, int timeout, String method) {
        return InputStreamToString.praseToString(path, timeout, method, false);
    }

    /*
     * 主要功能:完成将HttpUtil返回的InputStream转化成String
     * @param path       Url路径
     * @param timeout    超时时间
     * @param method     提交方式
     * @param openOutput 是否打开输出流
     * @return
     */
    public static String praseToString(String path, int timeout, String method, boolean openOutput) {
        String stringData = null;
        InputStream inputStreamData;
        inputStreamData = HttpUtil.getInputStream(path, timeout, method, openOutput);
        if (inputStreamData != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] byteData = new byte[1024];
            int length = 0;
            try {
                while ((length = inputStreamData.read(byteData)) != -1) {
                    byteArrayOutputStream.write(byteData, 0, length);
                }
                stringData = byteArrayOutputStream.toString();
                return stringData;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("yuzhai_error", "inputStreamData返回null");
        }
        return null;
    }
}
