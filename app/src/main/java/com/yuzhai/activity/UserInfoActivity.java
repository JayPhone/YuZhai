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
import android.support.v7.widget.Toolbar;
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
import com.yuzhai.bean.innerBean.BaseUserInfoBean;
import com.yuzhai.bean.innerBean.LoginToOrderBean;
import com.yuzhai.bean.innerBean.UserInfoBean;
import com.yuzhai.bean.responseBean.ReNameBean;
import com.yuzhai.bean.responseBean.UserHeaderUploadBean;
import com.yuzhai.http.IPConfig;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.FileUploadRequest;
import com.yuzhai.http.ParamsGenerateUtil;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.util.FileUtil;
import com.yuzhai.util.GetPathUtil;
import com.yuzhai.util.JsonUtil;
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
    private Toolbar mUserInfoToolbar;
    private TextView mAlterPswText;
    private TextView mUserNameText;
    private TextView mUserPhoneText;
    private Button mLoginExitBtn;
    private ImageView mHeaderImage;
    private RelativeLayout mAlterHeaderImageLayout;
    private RelativeLayout mChangeUserNameLayout;

    private String mImagePath;
    private String mUserHeadUrl;
    private String mUserNameStr;
    private String mUserPhoneStr;

    private CustomApplication mCustomApplication;
    private RequestQueue requestQueue;

    private final int CAMERA_REQUEST = 1;
    private final int IMAGE_PICK_REQUEST = 2;
    public static final String IMAGE_URL = "image_url";

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
        mUserInfoToolbar = (Toolbar) findViewById(R.id.user_info_toolbar);
        mUserInfoToolbar.setNavigationIcon(R.drawable.back);
        mUserInfoToolbar.setTitle("账户管理");
        mUserInfoToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAlterHeaderImageLayout = (RelativeLayout) findViewById(R.id.change_image_layout);
        mHeaderImage = (ImageView) findViewById(R.id.header_image);
        mAlterPswText = (TextView) findViewById(R.id.change_pswd);
        mChangeUserNameLayout = (RelativeLayout) findViewById(R.id.change_user_name_layout);
        mUserNameText = (TextView) findViewById(R.id.user_name);
        mUserPhoneText = (TextView) findViewById(R.id.user_name);
        mLoginExitBtn = (Button) findViewById(R.id.exit_login);

        //设置监听器
        mAlterHeaderImageLayout.setOnClickListener(this);
        mHeaderImage.setOnClickListener(this);
        mAlterPswText.setOnClickListener(this);
        mChangeUserNameLayout.setOnClickListener(this);
        mLoginExitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击左边选择头像
            case R.id.change_image_layout:
                //打开选择头像对话框
                this.changeImageDialog();
                break;

            //点击右边的头像
            case R.id.header_image:
                //打开查看头像界面
                Intent showImage = new Intent(this, ShowImageActivity.class);
                showImage.putExtra(IMAGE_URL, mUserHeadUrl);
                startActivity(showImage);
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
        if (mUserHeadUrl != null) {
            Log.i("user_header_url", mUserHeadUrl);
            setUserHeader(mUserHeadUrl);
        }

        if (mUserNameStr != null) {
            mUserNameText.setText(mUserNameStr);
        }

        if (mUserPhoneStr != null) {
            mUserPhoneText.setText(mUserPhoneStr);
        }
    }

    /**
     * 显示图片添加方式的对话框
     */
    public void changeImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择上传头像方式");
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
        mImagePath = FileUtil.createFilePath(FileUtil.HEAD_IMAGE);
        //启动相机
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mImagePath)));
        startActivityForResult(camera, CAMERA_REQUEST);
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
        startActivityForResult(intent_imagePick, IMAGE_PICK_REQUEST);
    }

    /**
     * 修改用户名对话框
     */
    public void changeUserNameDialog() {
        //获取重命名对话框的输入框实例
        final View chanNameView = getLayoutInflater().inflate(R.layout.userinfo_change_username_layout, null);
        final EditText alterUserName = (EditText) chanNameView.findViewById(R.id.change_user_name_edit);

        //创建重命名对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("输入用户名");
        builder.setView(chanNameView);
        builder.setPositiveButton("修改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!alterUserName.getText().toString().equals("")) {
                    sendReNameRequest(alterUserName.getText().toString(),
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
                        Log.i("re_name_resp", resp);
                        if (JsonUtil.decodeByGson(resp, ReNameBean.class).getCode().equals("1")) {
                            //设置新用户名
                            mUserNameText.setText(newName);
                            //使用Event发送替换侧滑菜单用户名的消息到MainActivity
                            EventBus.getDefault().post(new BaseUserInfoBean(
                                    null,
                                    newName,
                                    mCustomApplication.isLogin()));

                            UnRepeatToast.showToast(UserInfoActivity.this, "用户名修改成功");
                        }
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
                        //通知个人订单界面清空数据
                        EventBus.getDefault().post(new LoginToOrderBean(mCustomApplication.isLogin()));
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
            case CAMERA_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    sendUploadHeaderRequest(new File(mImagePath), mCustomApplication.getToken());
                    Log.i("path", mImagePath);
                }
                break;

            //处理图片选择器返回结果
            case IMAGE_PICK_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    //将获取到的图片URI传给图片裁剪
                    Uri uri = data.getData();
                    mImagePath = GetPathUtil.getImageAbsolutePath(this, uri);
                    sendUploadHeaderRequest(new File(mImagePath), mCustomApplication.getToken());
                    Log.i("path", mImagePath);
                }
                break;
        }
    }

    /**
     * 发送上传头像请求
     *
     * @param file 图片路径
     */
    public void sendUploadHeaderRequest(File file, String token) {

        //创建上传头像请求
        FileUploadRequest headerUploadRequest = new FileUploadRequest(
                IPConfig.uploadHeadAddress + "?token=" + token,
                mCustomApplication.generateCookieMap(),
                null,
                "image",
                file,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resp) {
                        Log.i("upload_header_resp", resp);
                        mUserHeadUrl = JsonUtil.decodeByGson(resp, UserHeaderUploadBean.class).getAvatar();
                        //更新头像
                        setUserHeader(mUserHeadUrl);
                        //使用EventBus发送替换侧滑菜单头像Url的消息到MainActivity
                        EventBus.getDefault().post(new BaseUserInfoBean(mUserHeadUrl, null, mCustomApplication.isLogin()));
                        UnRepeatToast.showToast(UserInfoActivity.this, "头像上传成功");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        UnRepeatToast.showToast(UserInfoActivity.this, "服务器不务正业中");
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
        mUserHeadUrl = userInfoBean.getUserHeadUrl();
        mUserNameStr = userInfoBean.getUserName();
        mUserPhoneStr = userInfoBean.getUserPhone();
    }

    /**
     * 设置用户头像
     *
     * @param userHeadUrl
     */
    public void setUserHeader(String userHeadUrl) {
        //通过返回的用户头像地址获取用户头像
        if (userHeadUrl != null) {
            Log.i("user_header_url", IPConfig.image_addressPrefix + "/" + userHeadUrl);
            Glide.with(this)
                    .load(IPConfig.image_addressPrefix + "/" + userHeadUrl)
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .into(mHeaderImage);
        }
    }
}
