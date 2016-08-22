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
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuzhai.config.IPConfig;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.ParamsGenerateUtil;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import java.util.Map;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by Administrator on 2016/7/11.
 */
@RuntimePermissions
public class WelcomeActivity extends AppCompatActivity {
    /**
     * 全局Applicant对象，用于保存返回的Cookie
     */
    private CustomApplication customApplication;

    /**
     * 请求队列
     */
    private RequestQueue requestQueue;

    /**
     * 登录请求
     */
    private CommonRequest loginRequest;

    private final String CODE = "code";
    private final String USERHEAD = "userHead";
    private final String USERNAME = "userName";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //获取权限
        WelcomeActivityPermissionsDispatcher.showWriteExternalStorageWithCheck(WelcomeActivity.this);
    }

    /**
     * 需要获取到权限才能执行以下方法
     */
    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void showWriteExternalStorage() {
        //获取全局Applicant对象，主要用于保存cookie
        customApplication = (CustomApplication) getApplication();

        //获取请求队列
        requestQueue = RequestQueueSingleton.getInstance(this).getRequestQueue();

        //如果用户曾经登录过，将自动登陆
        if (customApplication.getUserPhone() != null && customApplication.getPassword() != null) {
            //发送登录请求
            sendLoginRequest(customApplication.getUserPhone(), customApplication.getPassword());

        } else {
            Log.i("auto_login", "fail");
            Intent main = new Intent(this, MainActivity.class);
            startActivity(main);
            finish();
        }
    }

    /**
     * 发送登录请求
     *
     * @param userPhone 登录的用户名
     * @param userPsw   登录密码
     */
    public void sendLoginRequest(String userPhone, String userPsw) {
        //生成登录请求参数
        Map<String, String> params = ParamsGenerateUtil.generateLoginParams(
                userPhone,
                userPsw);

        //创建登录请求
        loginRequest = new CommonRequest(IPConfig.loginAddress,
                null,
                params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resp) {
                        String responseCode = JsonUtil.decodeJson(resp, CODE);
                        if (responseCode != null && responseCode.equals("1")) {
                            //用户头像路径
                            String userHead = JsonUtil.decodeJson(resp, USERHEAD);
                            //用户名
                            String userName = JsonUtil.decodeJson(resp, USERNAME);
                            //设置为登录状态
                            customApplication.setLoginState(true);
                            //保存登陆成功的账号的cookie
                            customApplication.addCookie(loginRequest.getResponseCookie());

                            Intent main = new Intent(WelcomeActivity.this, MainActivity.class);
                            //如果用户头像路径和用户名不为空，则传递到主界面
                            if (userHead != null) {
                                main.putExtra(USERHEAD, userHead);
                            }
                            if (userName != null) {
                                main.putExtra(USERNAME, userName);
                            }
                            startActivity(main);
                            WelcomeActivity.this.finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        UnRepeatToast.showToast(WelcomeActivity.this, "服务器睡着了");
                    }
                });

        //添加登录请求到请求队列
        requestQueue.add(loginRequest);
    }

    /**
     * 当获取读写存储器的权限拒绝时，显示一个对话框让用户开启
     */
    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void rejectWriteExternalStorage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("权限申请");
        builder.setMessage("请到设置 - 应用 - 御宅工作 - 权限中开启读取内部存储权限，以正常使用御宅工作的功能");
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent getPermission = new Intent(Settings.ACTION_SETTINGS);
                startActivityForResult(getPermission, 1);
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
