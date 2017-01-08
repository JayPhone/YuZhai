package com.yuzhai.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.yuzhai.bean.BaseUserInfoBean;
import com.yuzhai.bean.UserInfoBean;
import com.yuzhai.config.IPConfig;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.FileUploadRequest;
import com.yuzhai.http.ParamsGenerateUtil;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.util.BitmapUtil;
import com.yuzhai.util.FileUtil;
import com.yuzhai.util.GetPathUtil;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.Map;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by Administrator on 2016/7/8.
 */
@RuntimePermissions
public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView changePsw;
    private TextView userName;
    private TextView userPhone;
    private TextView titleText;
    private Button loginExit;
    private ImageView headerImage;
    private ImageView backImage;
    private RelativeLayout changeHeaderImage;
    private RelativeLayout changeUserName;

    private String imageCameraPath;
    private CustomApplication mCustomApplication;
    private RequestQueue requestQueue;
    private final int CAMERA_PEQUEST = 1;
    private final int IMAGEPICK_PEQUEST = 2;

    private String userHeadUrl;
    private String userNameStr;
    private String userPhoneStr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        //注册EventBus
        EventBus.getDefault().register(this);
        mCustomApplication = (CustomApplication) getApplication();
        requestQueue = RequestQueueSingleton.getRequestQueue(this);

        //初始化组件
        initViews();
        //初始化数据
        initData();
    }

    /**
     * 初始化组件
     */
    public void initViews() {
        backImage = (ImageView) findViewById(R.id.back_image);
        if (backImage != null) {
            backImage.setImageResource(R.drawable.back);
        }
        titleText = (TextView) findViewById(R.id.title_text);
        if (titleText != null) {
            titleText.setText("账户管理");
        }
        changeHeaderImage = (RelativeLayout) findViewById(R.id.change_image_layout);
        headerImage = (ImageView) findViewById(R.id.header_image);
        changePsw = (TextView) findViewById(R.id.change_pswd);
        changeUserName = (RelativeLayout) findViewById(R.id.change_user_name_layout);
        userName = (TextView) findViewById(R.id.user_name);
        userPhone = (TextView) findViewById(R.id.user_name);
        loginExit = (Button) findViewById(R.id.exit_login);

        //设置监听器
        backImage.setOnClickListener(this);
        changeHeaderImage.setOnClickListener(this);
        headerImage.setOnClickListener(this);
        changePsw.setOnClickListener(this);
        changeUserName.setOnClickListener(this);
        loginExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击回退按钮
            case R.id.back_image:
                this.finish();
                break;

            //点击左边选择头像
            case R.id.change_image_layout:
                //打开选择头像对话框
                this.changeImageDialog();
                break;

            //点击右边的头像
            case R.id.header_image:
                //打开查看头像界面
//                Intent intent = new Intent();
//                intent.setAction(android.content.Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.fromFile(new File(imageCameraPath)), "image/*");
//                startActivity(intent);
                break;

            //点击修改密码
            case R.id.change_pswd:
                //打开修改密码界面
                Intent changePsw = new Intent(this, AlterPswActivity.class);
                startActivity(changePsw);
                break;

            //点击修改用户名
            case R.id.change_user_name_layout:
                //打开修改用户名对话框
                changeUserNameDialog();
                break;

            //点击退出登录按钮
            case R.id.exit_login:
                //发送退出登录请求
                sendExitLoginRequest();
                break;
        }
    }

    /**
     * 设置头像，用户名和手机号等数据
     */
    public void initData() {
        if (userHeadUrl != null) {
            Glide.with(this)
                    .load(userHeadUrl)
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .into(headerImage);
            Log.i("url", userHeadUrl);
        }

        if (userNameStr != null) {
            userName.setText(userNameStr);
        }

        if (userPhoneStr != null) {
            userPhone.setText(userPhoneStr);
        }
    }

    /**
     * 显示图片添加方式的对话框
     */
    public void changeImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("上传头像");
        builder.setItems(
                new String[]{
                        "手机相机添加",
                        "本地相册选择"
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                //打开相机
                                UserInfoActivityPermissionsDispatcher.showCameraWithCheck(UserInfoActivity.this);
                                break;

                            case 1:
                                //打开图片选择器
                                showImagePick();
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    /**
     * 启动相机
     */
    @NeedsPermission(Manifest.permission.CAMERA)
    public void showCamera() {
        //生成路径
        imageCameraPath = FileUtil.createFilePath(FileUtil.HEAD_IMAGE);
        //启动相机
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(imageCameraPath)));
        startActivityForResult(camera, CAMERA_PEQUEST);
    }

    /**
     * 启动图片选择器
     */
    public void showImagePick() {
        Intent intent_imagePick = new Intent(Intent.ACTION_PICK);
        intent_imagePick.setType("image/*");
        //是否按比例缩放
        intent_imagePick.putExtra("scale", "true");
        //图片输出大小
        intent_imagePick.putExtra("outputX", 640);
        intent_imagePick.putExtra("outputY", 640);
        //图片输出格式
        intent_imagePick.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent_imagePick.putExtra("return-data", false);
        startActivityForResult(intent_imagePick, IMAGEPICK_PEQUEST);
    }

    /**
     * 修改用户名对话框
     */
    public void changeUserNameDialog() {
        //获取重命名对话框的输入框实例
        final View chanNameView = getLayoutInflater().inflate(R.layout.userinfo_change_username_layout, null);
        final EditText changeUserName = (EditText) chanNameView.findViewById(R.id.change_user_name_edit);

        //创建重命名对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("输入用户名");
        builder.setView(chanNameView);
        builder.setPositiveButton("修改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!changeUserName.getText().toString().equals("")) {
                    sendReNameRequest(changeUserName.getText().toString(),
                            mCustomApplication.getToken());
                }
            }
        });
        builder.setNegativeButton("取消", null).create().show();
    }

    /**
     * 发送重命名请求
     *
     * @param newName 新用户名
     */
    public void sendReNameRequest(final String newName, String token) {
        //生成请求参数
        Map<String, String> param = ParamsGenerateUtil.generateReNameParam(newName, token);

        //创建重命名请求
        CommonRequest reNameRequest = new CommonRequest(IPConfig.reNameAddress,
                null,
                param,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resp) {
                        Log.i("response", resp);
                        //TODO resp只有1,没有key
                        //设置新用户名
                        userName.setText(newName);
                        //使用Event发送替换侧滑菜单用户名的消息到MainActivity
                        EventBus.getDefault().post(new BaseUserInfoBean(
                                null,
                                newName,
                                mCustomApplication.isLogin()));

                        UnRepeatToast.showToast(UserInfoActivity.this, "用户名修改成功");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        UnRepeatToast.showToast(UserInfoActivity.this, "服务器不务正业中");
                    }
                });

        //添加请求到请求队列
        requestQueue.add(reNameRequest);
    }

    /**
     * 发送退出登录请求
     */
    public void sendExitLoginRequest() {
        //生成请求参数
        Map<String, String> param = ParamsGenerateUtil.generateExitLoginParam(mCustomApplication.getToken());

        //创建退出登录请求
        CommonRequest exitLoginRequest = new CommonRequest(IPConfig.exitLoginAddress,
                mCustomApplication.generateCookieMap(),
                param,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //清除登录的手机号和密码
                        mCustomApplication.clearUserInfo();
                        //清除Cookie
                        mCustomApplication.clearCookie();
                        //退出登录
                        mCustomApplication.setLoginState(false);
                        //使用Event发送替换个人信息界面为未登录的消息到MainActivity
                        EventBus.getDefault().post(new BaseUserInfoBean(null, null, mCustomApplication.isLogin()));
                        UnRepeatToast.showToast(UserInfoActivity.this, "退出成功");
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        UnRepeatToast.showToast(UserInfoActivity.this, "服务器不务正业中");
                    }
                });

        requestQueue.add(exitLoginRequest);
    }

    /**
     * 当用户选择不再提示后的处理
     */
    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void onCameraNeverAskAgain() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("权限申请");
        builder.setMessage("请到设置 - 应用 - 御宅工作 - 权限中开启相机权限，以正常使用头像上传，需求图片上传等功能");
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent getPermission = new Intent(Settings.ACTION_SETTINGS);
                startActivity(getPermission);
            }
        });
        builder.setNegativeButton("取消", null).create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //处理相机返回结果
            case CAMERA_PEQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    headerImage.setImageBitmap(BitmapUtil.decodeSampledBitmapFromFile(imageCameraPath, 100, 100));
                    sendUploadHeaderRequest(new File(imageCameraPath));
                }
                break;

            //处理图片选择器返回结果
            case IMAGEPICK_PEQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    //将获取到的图片URI传给图片裁剪
                    Uri uri = data.getData();
                    imageCameraPath = GetPathUtil.getImageAbsolutePath(this, uri);
                    headerImage.setImageBitmap(BitmapUtil.decodeSampledBitmapFromFile(imageCameraPath, 100, 100));
                    sendUploadHeaderRequest(new File(imageCameraPath));
                }
                break;
        }
    }

    /**
     * 发送上传头像请求
     *
     * @param file 图片路径
     */
    public void sendUploadHeaderRequest(File file) {
        //创建上传头像请求
        FileUploadRequest headerUploadRequest = new FileUploadRequest(IPConfig.uploadHeadAddress,
                null,
                null,
                "image",
                file,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resp) {
                        Log.i("response", resp);
                        //使用EventBus发送替换侧滑菜单头像Url的消息到MainActivity
                        EventBus.getDefault().post(new BaseUserInfoBean(resp, null, mCustomApplication.isLogin()));
                        UnRepeatToast.showToast(UserInfoActivity.this, "头像上传成功");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        UnRepeatToast.showToast(UserInfoActivity.this, "服务器睡着了");
                    }
                }
        );

        //添加到请求队列
        requestQueue.add(headerUploadRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: 将权限处理委托给生成的方法
        UserInfoActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 获取EventBus传递过来的头像路径，用户名，手机号
     *
     * @param userInfoBean
     */
    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onEventMainActivity(UserInfoBean userInfoBean) {
        userHeadUrl = userInfoBean.getUserHeadUrl();
        userNameStr = userInfoBean.getUserName();
        userPhoneStr = userInfoBean.getUserPhone();
    }
}
