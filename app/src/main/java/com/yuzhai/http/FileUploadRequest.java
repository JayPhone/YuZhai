package com.yuzhai.http;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/16.
 */
public class FileUploadRequest extends Request<String> {

    private MultipartEntity entity = new MultipartEntity();
    private Response.Listener<String> mListener;
    private List<File> mFileParts;
    private String mFilePartName;
    private Map<String, String> mParams;
    private Map<String, String> mHeaders;

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
    public FileUploadRequest(String url,
                             Map<String, String> headers,
                             Map<String, String> params,
                             String filePartName,
                             File file,
                             Response.Listener<String> listener,
                             Response.ErrorListener errorListener) {

        super(Method.POST, url, errorListener);
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
    public FileUploadRequest(String url,
                             Map<String, String> headers,
                             Map<String, String> params,
                             String filePartName,
                             List<File> files,
                             Response.Listener<String> listener,
                             Response.ErrorListener errorListener) {

        super(Method.POST, url, errorListener);
        mFilePartName = filePartName;
        mListener = listener;
        mFileParts = files;
        mParams = params;
        mHeaders = headers;
        buildMultipartEntity();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed,
                HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (getmHeaders() != null) {
            return getmHeaders();
        }
        return super.getHeaders();
    }


    public Map<String, String> getmHeaders() {
        return mHeaders;
    }

    public void setHeaders(Map<String, String> mHeaders) {
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
