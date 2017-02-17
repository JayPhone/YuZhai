package com.yuzhai.http;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.yuzhai.dao.CookieOperate;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/16.
 */
public class FileUploadRequest extends Request<String> {
    private Context mContext;
    private MultipartEntity entity = new MultipartEntity();
    private Response.Listener<String> mListener;
    private List<File> mFileParts;
    private String mFilePartName;
    private Map<String, String> mParams;
    private Map<String, String> mHeaders;
    private String mSetCookie;

    /**
     * 单个文件＋参数 上传
     *
     * @param url
     * @param listener
     * @param errorListener
     * @param filePartName
     * @param file
     * @param params
     */
    public FileUploadRequest(Context context,
                             String url,
                             Map<String, String> headers,
                             Map<String, String> params,
                             String filePartName,
                             File file,
                             Response.Listener<String> listener,
                             Response.ErrorListener errorListener) {

        super(Method.POST, url, errorListener);
        mContext = context;
        mFileParts = new ArrayList<>();
        if (file != null && file.exists()) {
            mFileParts.add(file);
        } else {
            VolleyLog.e("MultipartRequest---文件不存在");
        }
        mFilePartName = filePartName;
        mListener = listener;
        mParams = params;
        mHeaders = headers;
        buildMultipartEntity();
    }

    /**
     * 多个文件＋参数上传
     *
     * @param url
     * @param listener
     * @param errorListener
     * @param filePartName
     * @param files
     * @param params
     */
    public FileUploadRequest(Context context,
                             String url,
                             Map<String, String> headers,
                             Map<String, String> params,
                             String filePartName,
                             List<File> files,
                             Response.Listener<String> listener,
                             Response.ErrorListener errorListener) {

        super(Method.POST, url, errorListener);
        mContext = context;
        mFilePartName = filePartName;
        mListener = listener;
        mFileParts = files;
        mParams = params;
        mHeaders = headers;
        buildMultipartEntity();
    }

    public String getResponseCookie() {
        return mSetCookie;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String responseData = new String(response.data, "UTF-8");
            Log.i("common_request", "get headers in parseNetworkResponse" + response.headers.toString());

            //获取服务器返回的Cookie值
            Map<String, String> headers = response.headers;
            if (headers.containsKey("Set-Cookie")) {
                //获取Cookie的JessionId的值
                mSetCookie = headers.get("Set-Cookie").split(";")[0];
                Log.i("common_request", "cookie from server " + mSetCookie);

                //持久化Cookie
                CookieOperate cookieOperate = new CookieOperate(mContext);
                cookieOperate.setCookie(mSetCookie);
            }
            return Response.success(responseData, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        if (getRequestHeaders() != null) {
            Map<String, String> cookieMap = getRequestHeaders();
            for (String key : cookieMap.keySet()) {
                header.put(key, cookieMap.get(key));
            }
        }
        return header;
    }


    private Map<String, String> getRequestHeaders() {
        return mHeaders;
    }

    public void setRequestHeaders(Map<String, String> mHeaders) {
        this.mHeaders = mHeaders;
    }

    @Override
    public String getBodyContentType() {
        return entity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            entity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("数据写出异常");
        }
        return bos.toByteArray();
    }

    private void buildMultipartEntity() {
        if (mFileParts != null && mFileParts.size() > 0) {
            for (File file : mFileParts) {
                entity.addPart(mFilePartName, new FileBody(file));
            }
            long l = entity.getContentLength();
            Log.i("图片数量", mFileParts.size() + "个，长度：" + l);
        }

        try {
            if (mParams != null && mParams.size() > 0) {
                for (Map.Entry<String, String> entry : mParams.entrySet()) {
                    entity.addPart(
                            entry.getKey(),
                            new StringBody(entry.getValue(), Charset
                                    .forName("UTF-8")));
                }
            }
        } catch (UnsupportedEncodingException e) {
            VolleyLog.e("未知编码异常");
        }
    }

}
