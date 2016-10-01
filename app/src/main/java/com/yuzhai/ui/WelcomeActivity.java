package com.yuzhai.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuzhai.config.IPConfig;
import com.yuzhai.entry.responseBean.LoginRespBean;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.ParamsGenerateUtil;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.util.BitmapUtil;
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

    /**
     * 显示logo的ImageView
     */
    private ImageView mLogoImage;

    /**
     * Logo
     */
    private Bitmap mBitmap;

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

        //显示Logo的Bitmap
        mLogoImage = (ImageView) findViewById(R.id.logo_image);

        //视图加载完毕后，监听并获取ImageView的尺寸
        final ViewTreeObserver viewTreeObserver = mLogoImage.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mLogoImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        mBitmap = BitmapUtil.decodeSampledBitmapFromResource(getResources(),
                                R.drawable.welcome_logo,
                                mLogoImage.getWidth(),
                                mLogoImage.getHeight());
                        mLogoImage.setImageBitmap(mBitmap);
                    }
                });

        //如果用户曾经登录过，将自动登陆
        if (customApplication.getUserPhone() != null && customApplication.getPassword() != null) {
            //发送登录请求
            sendLoginRequest(customApplication.getUserPhone(), customApplication.getPassword());

        } else {
            Log.i("auto_login", "fail");
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
                        Log.i("welcome_resp", resp);

                        //获取返回码
                        LoginRespBean loginRespBean = JsonUtil.decodeByGson(resp, LoginRespBean.class);
                        String respCode = loginRespBean.getCode();
                        Log.i("welcome_resp_code", respCode);

                        if (respCode != null && respCode.equals("1")) {
                            //用户头像路径
                            String userHead = loginRespBean.getUserHead();
                            Log.i("welcome_resp_userHead", userHead);
                            //用户名
                            String userName = loginRespBean.getUserName();
                            Log.i("welcome_resp_userName", userName);
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
                        UnRepeatToast.showToast(WelcomeActivity.this, "服务器不务正业中");
                        Log.i("auto_login", "fail");
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
