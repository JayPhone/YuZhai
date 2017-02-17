package com.yuzhai.http;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.yuzhai.dao.CookieOperate;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/7/9.
 */
public class CommonRequest extends StringRequest {
    private Context mContext;
    //请求参数
    private Map<String, String> mParams;
    //请求头参数
    private Map<String, String> mHeaders;
    //返回的Cookie值
    private String mSetCookie;

    /**
     * 可添加请求参数和请求头参数的构造函数
     *
     * @param context
     * @param method        请求方式
     * @param url           请求URL
     * @param headers       请求头参数
     * @param params        请求参数
     * @param listener
     * @param errorListener
     */
    public CommonRequest(Context context,
                         int method,
                         String url,
                         Map<String, String> headers,
                         Map<String, String> params,
                         Response.Listener<String> listener,
                         Response.ErrorListener errorListener) {

        super(method, url, listener, errorListener);
        mContext = context;
        mHeaders = headers;
        mParams = params;
    }

    /**
     * 默认请求方式为POST
     *
     * @param context
     * @param url           请求URL
     * @param headers       请求头参数
     * @param params        请求参数
     * @param listener
     * @param errorListener
     */
    public CommonRequest(Context context,
                         String url,
                         Map<String, String> headers,
                         Map<String, String> params,
                         Response.Listener<String> listener,
                         Response.ErrorListener errorListener) {

        this(context, Method.POST, url, headers, params, listener, errorListener);
    }

    /**
     * 默认请求方式为GET
     *
     * @param url
     * @param listener
     * @param errorListener
     */
    public CommonRequest(Context context,
                         String url,
                         Response.Listener<String> listener,
                         Response.ErrorListener errorListener) {
        this(context, Method.GET, url, null, null, listener, errorListener);
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

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = getRequestParams();
        if (null != params) {
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

    //获取返回的Set-Cookie参数值
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
}
