package com.yuzhai.util;

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

    public static String praseToString(InputStream inputStream) {
        String stringData = "";
        if (inputStream != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] byteData = new byte[1024];
            int length = 0;
            try {
                while ((length = inputStream.read(byteData)) != -1) {
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
