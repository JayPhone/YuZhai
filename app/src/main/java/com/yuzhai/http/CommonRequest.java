package com.yuzhai.http;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/9.
 */
public class CommonRequest extends StringRequest {

    /**
     * 请求参数
     */
    private Map<String, String> mParams;

    /**
     * 请求头参数
     */
    private Map<String, String> mHeaders;

    /**
     * 返回的Cookie值
     */
    private String mResponseCookie;

    /**
     * 可添加请求参数和请求头参数的构造函数
     *
     * @param method        请求方式
     * @param url           请求URL
     * @param headers       请求头参数
     * @param params        请求参数
     * @param listener
     * @param errorListener
     */
    public CommonRequest(int method, String url,
                         Map<String, String> headers,
                         Map<String, String> params,
                         Response.Listener<String> listener,
                         Response.ErrorListener errorListener) {

        super(method, url, listener, errorListener);
        mHeaders = headers;
        mParams = params;
    }

    /**
     * 默认请求方式为POST
     *
     * @param url           请求URL
     * @param headers       请求头参数
     * @param params        请求参数
     * @param listener
     * @param errorListener
     */
    public CommonRequest(String url,
                         Map<String, String> headers,
                         Map<String, String> params,
                         Response.Listener<String> listener,
                         Response.ErrorListener errorListener) {

        this(Method.POST, url, headers, params, listener, errorListener);
    }

    /**
     * 默认请求方式为GET
     *
     * @param url
     * @param listener
     * @param errorListener
     */
    public CommonRequest(String url,
                         Response.Listener<String> listener,
                         Response.ErrorListener errorListener) {
        this(Method.GET, url, null, null, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = getRequestHeaders();
        if (headers != null) {
            return headers;
        }
        return super.getHeaders();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = getRequestParams();
        if (params != null) {
            return params;
        }
        return super.getParams();
    }

    //获取设置的Headers
    public Map<String, String> getRequestHeaders() {
        return mHeaders;
    }

    //设置请求头参数
    public void setRequestHeaders(Map<String, String> headers) {
        this.mHeaders = headers;
    }

    //设置请求参数
    public void setRequestParams(Map<String, String> params) {
        this.mParams = params;
    }

    //获取设置的请求参数
    public Map<String, String> getRequestParams() {
        return mParams;
    }


    public String getResponseCookie() {
        return mResponseCookie;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            Map<String, String> responseHeaders = response.headers;
            if (responseHeaders.containsKey("Set-Cookie")) {
                mResponseCookie = responseHeaders.get("Set-Cookie");
            }
            String responseData = new String(response.data, "UTF-8");
            return Response.success(responseData, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }
}
