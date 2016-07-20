package com.yuzhai.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuzhai.config.IPConfig;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.yuzhaiwork.R;

import java.util.HashMap;
import java.util.Map;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by Administrator on 2016/7/11.
 */
@RuntimePermissions
public class WelcomeActivity extends AppCompatActivity {
    private CustomApplication customApplication;
    private RequestQueue requestQueue;
    private CommonRequest loginRequest;
    private Map<String, String> params;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //获取权限
        WelcomeActivityPermissionsDispatcher.showWriteExternalStorageWithCheck(WelcomeActivity.this);
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void showWriteExternalStorage() {
        customApplication = (CustomApplication) getApplication();
        requestQueue = customApplication.getRequestQueue();
        if (customApplication.getUserPhone() != null && customApplication.getPassword() != null) {
            loginRequest = new CommonRequest(Request.Method.POST, IPConfig.loginAddress, new Response.Listener<String>() {
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

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void rejectWriteExternalStorage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("权限申请");
        builder.setMessage("请到设置 - 应用 - 御宅工作 - 权限中开启读取内部存储权限，以正常使用御宅工作的功能");
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent_getPermission = new Intent(Settings.ACTION_SETTINGS);
                startActivityForResult(intent_getPermission, 1);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).create().show();
    }

    //权限许可情况调用
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: 将权限处理委托给生成的方法
        WelcomeActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_CANCELED) {
            finish();
        }
    }
}
