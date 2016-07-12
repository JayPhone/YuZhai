package com.yuzhai.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuzhai.config.IPConfig;
import com.yuzhai.dao.JsonUtil;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.yuzhaiwork.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/11.
 */
public class WelcomeActivity extends AppCompatActivity {
    private CustomApplication customApplication;
    private RequestQueue requestQueue;
    private CommonRequest loginRequest;
    private Map<String, String> params;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        customApplication = (CustomApplication) getApplication();
        requestQueue = customApplication.getRequestQueue();
        if (customApplication.getUserPhone() != null && customApplication.getPassword() != null) {
            loginRequest = new CommonRequest(this, Request.Method.POST, IPConfig.loginAddress, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    String response = JsonUtil.decodeJson(s, "code");
                    if (response.equals("1")) {
                        //设置为登录状态
                        customApplication.setLOGIN(true);
                        //保存登陆成功的账号的cookie
                        customApplication.addCookie(loginRequest.getResponseCookie());
                        //进入主界面
                        Intent main_intent = new Intent();
                        main_intent.setClass(WelcomeActivity.this, MainActivity.class);
                        startActivity(main_intent);
                        //替换菜单为已登录界面
                        Intent replaceFragment = new Intent();
                        replaceFragment.setAction("yzgz.broadcast.replace.fragment");
                        sendBroadcast(replaceFragment);
                        finish();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Intent main_intent = new Intent();
                    main_intent.setClass(WelcomeActivity.this, MainActivity.class);
                    startActivity(main_intent);
                    finish();
                }
            });
            params = new HashMap<>();
            params.put("userPhone", customApplication.getUserPhone());
            params.put("userPsw", customApplication.getPassword());
            loginRequest.setParams(params);
            requestQueue.add(loginRequest);
        } else {
            Intent main_intent = new Intent();
            main_intent.setClass(this, MainActivity.class);
            startActivity(main_intent);
            finish();
        }
    }
}
