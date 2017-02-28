package com.yuzhai.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.yuzhai.bean.innerBean.BaseUserInfoBean;
import com.yuzhai.bean.responseBean.LoginRespBean;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.IPConfig;
import com.yuzhai.http.ParamsGenerateUtil;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by Administrator on 2016/7/11.
 */
public class WelcomeActivity extends AppCompatActivity {
    private static final String TAG = "WelcomeActivity";
    private CustomApplication customApplication;
    private RequestQueue requestQueue;
    private CommonRequest loginRequest;
    //请求获取读写外存权限请求码
    private static final int STORAGE_PERMISSION = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        checkStoragePermission();
    }

    /**
     * 检测是否拥有读写外存权限
     */
    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
        } else {
            initData();
        }
    }

    /**
     * 初始化数据，包括用户自动登陆等
     */
    public void initData() {
        customApplication = (CustomApplication) getApplication();

        //获取请求队列
        requestQueue = RequestQueueSingleton.getRequestQueue(this);

        //如果用户曾经登录过，将自动登陆
        if (customApplication.getUserPhone() != null && customApplication.getPassword() != null) {
            //发送登录请求
            sendLoginRequest(customApplication.getUserPhone(),
                    customApplication.getPassword(),
                    MiPushClient.getRegId(this));

        } else {
            Log.i(TAG, "auto_login_fail");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                        Intent main = new Intent(WelcomeActivity.this, MainActivity.class);
                        startActivity(main);
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    /**
     * 发送登录请求
     *
     * @param userPhone 登录的用户名
     * @param userPsw   登录密码
     */
    public void sendLoginRequest(String userPhone, String userPsw, String regId) {
        //生成登录请求参数
        Map<String, String> params = ParamsGenerateUtil.generateLoginParams(
                userPhone,
                userPsw,
                regId);
        Log.v(TAG, "login_params:" + params.toString());

        //创建登录请求
        loginRequest = new CommonRequest(this,
                IPConfig.loginAddress,
                null,
                params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resp) {
                        Log.i(TAG, "login_resp:" + resp);
                        //获取返回码
                        LoginRespBean loginRespBean = JsonUtil.decodeByGson(resp, LoginRespBean.class);
                        String respCode = loginRespBean.getCode();
                        Log.i(TAG, "login_resp_code:" + respCode);

                        if (respCode != null && respCode.equals("1")) {
                            //用户头像路径
                            Log.i(TAG, "login_resp_userHead:" + loginRespBean.getUser_head_url());
                            //用户名
                            Log.i(TAG, "login_resp_userName:" + loginRespBean.getUser_name());
                            //设置为登录状态
                            customApplication.setLoginState(true);

                            //即时聊天连接
                            BmobIM.connect(customApplication.getUserPhone(), new ConnectListener() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null) {
                                        Log.i(TAG, "连接成功");
                                        //监听连接状态
                                        BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
                                            @Override
                                            public void onChange(ConnectionStatus status) {
                                                Log.i(TAG, "连接状态:" + status.getMsg());
                                            }
                                        });
                                    } else {
                                        Log.e(TAG, e.getErrorCode() + "/" + e.getMessage());
                                    }
                                }
                            });

                            Intent main = new Intent(WelcomeActivity.this, MainActivity.class);
                            startActivity(main);

                            //使用EventBus将loginRespBean传递到主界面
                            EventBus.getDefault().postSticky(new BaseUserInfoBean(
                                    loginRespBean.getUser_head_url(),
                                    loginRespBean.getUser_name()
                                    , customApplication.isLogin()));

                            WelcomeActivity.this.finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        UnRepeatToast.showToast(WelcomeActivity.this, "服务器不务正业中");
                        Log.i(TAG, "auto_login_fail");
                        Intent main = new Intent(WelcomeActivity.this, MainActivity.class);
                        startActivity(main);
                        finish();
                    }
                });

        //添加登录请求到请求队列
        requestQueue.add(loginRequest);
    }

    /**
     * 当获取读写存储器的权限拒绝时，显示一个对话框让用户开启
     */
    public void showOnDenienStoragePermission() {
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
        switch (requestCode) {
            case STORAGE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initData();
                } else {
                    showOnDenienStoragePermission();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_CANCELED) {
            finish();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
