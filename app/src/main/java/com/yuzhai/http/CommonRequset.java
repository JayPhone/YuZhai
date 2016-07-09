package com.yuzhai.http;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.yuzhai.global.Login;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/9.
 */
public class CommonRequset extends StringRequest {
    private Map<String, String> mParams;

    public CommonRequset(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    //重写获取参数方法
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return getmParams();
    }

    //设置参数
    public void setParams(Map<String, String> params) {
        mParams = params;
    }

    //获取参数
    public Map<String, String> getmParams() {
        return mParams;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            Map<String, String> responseHeaders = response.headers;
            Login.setCOOKIE(responseHeaders.get("Set-Cookie"));
            String dataString = new String(response.data, "UTF-8");
            return Response.success(dataString, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (Login.getCOOKIE() != null) {
            HashMap headers = new HashMap();
            headers.put("Cookie", Login.getCOOKIE());
            return headers;
        }
        return super.getHeaders();
    }

    public String getCookie() {
        return Login.getCOOKIE();
    }
}
