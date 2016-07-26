package com.yuzhai.ui;

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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.yuzhai.config.IPConfig;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.FileUploadRequest;
import com.yuzhai.util.BitmapCache;
import com.yuzhai.util.BitmapUtil;
import com.yuzhai.util.CheckData;
import com.yuzhai.util.FileUtil;
import com.yuzhai.util.GetPathUtil;
import com.yuzhai.yuzhaiwork.R;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by Administrator on 2016/7/8.
 */
@RuntimePermissions
public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView changePswd;
    private TextView userName;
    private TextView userPhone;
    private Button loginExit;
    private ImageView headerImage;
    private ImageView back;
    private RelativeLayout changeHeaderImage;
    private RelativeLayout changeUserName;

    private CustomApplication customApplication;
    private String imageCameraPath;
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
        customApplication = (CustomApplication) getApplication();
        requestQueue = customApplication.getRequestQueue();
        if (getIntent().getStringExtra("userHead") != null) {
            userHeadUrl = getIntent().getStringExtra("userHead");
        }
        if (getIntent().getStringExtra("userName") != null) {
            userNameStr = getIntent().getStringExtra("userName");
        }
        if (getIntent().getStringExtra("userPhone") != null) {
            userPhoneStr = getIntent().getStringExtra("userPhone");
        }
        initViews();
    }

    public void initViews() {
        back = (ImageView) findViewById(R.id.back_image);
        changeHeaderImage = (RelativeLayout) findViewById(R.id.change_image_layout);
        headerImage = (ImageView) findViewById(R.id.header_image);
        changePswd = (TextView) findViewById(R.id.change_pswd);
        changeUserName = (RelativeLayout) findViewById(R.id.change_user_name_layout);
        userName = (TextView) findViewById(R.id.user_name);
        userPhone = (TextView) findViewById(R.id.phone_num);
        loginExit = (Button) findViewById(R.id.exit_login);
        //设置监听器
        back.setOnClickListener(this);
        changeHeaderImage.setOnClickListener(this);
        headerImage.setOnClickListener(this);
        changePswd.setOnClickListener(this);
        changeUserName.setOnClickListener(this);
        loginExit.setOnClickListener(this);

        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_image:
                //结束当前页面
                this.finish();
                break;
            case R.id.change_image_layout:
                //打开选择头像对话框
                this.changeImageDialog();
                break;
            case R.id.header_image:
                //打开查看头像界面
//                Intent intent = new Intent();
//                intent.setAction(android.content.Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.fromFile(new File(imageCameraPath)), "image/*");
//                startActivity(intent);
                break;
            case R.id.change_pswd:
                //打开修改密码界面
                Intent intent_changePswd = new Intent();
                intent_changePswd.setClass(UserInfoActivity.this, ChangePswdActivity.class);
                startActivity(intent_changePswd);
                break;
            case R.id.change_user_name_layout:
                changeUserNameDialog();
                break;
            case R.id.exit_login:
                //清除登录的手机号和密码
                customApplication.clearUserInfo();
                //清除cookie
                customApplication.clearCookie();
                //退出登录
                customApplication.setLOGIN(false);
                //替换个人信息界面为未登录
                Intent replaceFragment = new Intent();
                replaceFragment.setAction("yzgz.broadcast.replace.fragment");
                sendBroadcast(replaceFragment);
                finish();
                break;
        }
    }

    public void initData() {
        if (userHeadUrl != null) {
            ImageLoader imageLoader = new ImageLoader(requestQueue, new BitmapCache());
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(headerImage, R.drawable.head_default, R.drawable.head_default);
            imageLoader.get(userHeadUrl, listener, 200, 200);
        }

        if (userNameStr != null) {
            userName.setText(userNameStr);
        }

        if (userPhoneStr != null) {
            userPhone.setText(userPhoneStr);
        }
    }

    //修改用户名对话框
    public void changeUserNameDialog() {
        final View view = getLayoutInflater().inflate(R.layout.userinfo_change_username_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("输入用户名");
        builder.setView(view);
        builder.setPositiveButton("修改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final EditText changeUserName = (EditText) view.findViewById(R.id.change_user_name_edit);
                if (!CheckData.isEmpty(changeUserName.getText().toString())) {
                    CommonRequest commonRequest = new CommonRequest(Request.Method.POST, IPConfig.renameAddress, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Log.i("response", s);
                            //设置用户名
                            userName.setText(changeUserName.getText().toString());
                            Toast.makeText(UserInfoActivity.this, "用户名修改成功", Toast.LENGTH_SHORT).show();
                            Intent replace_name = new Intent();
                            replace_name.putExtra("userName", changeUserName.getText().toString());
                            replace_name.setAction("yzgz.broadcast.replace.name");
                            sendBroadcast(replace_name);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(UserInfoActivity.this, "网络异常,请检测网络后重试", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("newname", changeUserName.getText().toString());
                    commonRequest.setParams(params);
                    commonRequest.setmHeaders(createHeaders());
                    requestQueue.add(commonRequest);
                }
            }
        });
        builder.setNegativeButton("取消", null).create().show();
    }

    //对话框显示图片的添加方式
    public void changeImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("上传头像");
        builder.setItems(new String[]{"手机相机添加", "本地相册选择"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                //将权限处理委托给生成的方法
                                UserInfoActivityPermissionsDispatcher.showCameraWithCheck(UserInfoActivity.this);
                                break;
                            case 1:
                                showImagePick();
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    //执行需要权限的操作。如果这是由permissionsdispatcher执行，许可将被授予
    @NeedsPermission(Manifest.permission.CAMERA)
    public void showCamera() {
        //生成路径
        imageCameraPath = FileUtil.createFilePath(FileUtil.HEAD_IMAGE);
        //启动相机
        Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent_camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(imageCameraPath)));
        startActivityForResult(intent_camera, CAMERA_PEQUEST);
    }

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

    //当用户选择不再提示后的处理
    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void onCameraNeverAskAgain() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("权限申请");
        builder.setMessage("请到设置 - 应用 - 御宅工作 - 权限中开启相机权限，以正常使用头像上传，需求图片上传等功能");
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent_getPermission = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent_getPermission);
            }
        });
        builder.setNegativeButton("取消", null).create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_PEQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    headerImage.setImageBitmap(BitmapUtil.decodeSampledBitmapFromFile(imageCameraPath, 100, 100));
                    uploadHeaderImage(new File(imageCameraPath));
                }
                break;
            case IMAGEPICK_PEQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    //将获取到的图片URI传给图片裁剪
                    Uri uri = data.getData();
                    imageCameraPath = GetPathUtil.getImageAbsolutePath(this, uri);
                    headerImage.setImageBitmap(BitmapUtil.decodeSampledBitmapFromFile(imageCameraPath, 100, 100));
                    uploadHeaderImage(new File(imageCameraPath));
                }
                break;
        }
    }

    public void uploadHeaderImage(File file) {
        FileUploadRequest fileUploadRequest = new FileUploadRequest(IPConfig.uploadHeadAddress, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i("response", s);
                Toast.makeText(UserInfoActivity.this, "头像上传成功", Toast.LENGTH_SHORT).show();
                Intent replace_head = new Intent();
                replace_head.putExtra("userHead", s);
                replace_head.setAction("yzgz.broadcast.replace.head");
                sendBroadcast(replace_head);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(UserInfoActivity.this, "网络异常,请检测网络后重试", Toast.LENGTH_SHORT).show();
            }
        }, "image", file, null);
        fileUploadRequest.setmHeaders(createHeaders());
        requestQueue.add(fileUploadRequest);
    }

    public Map<String, String> createHeaders() {
        //设置请求参数
        Map<String, String> headers = new HashMap<>();
        headers.put("cookie", customApplication.getCookie());
        return headers;
    }

    //权限许可情况调用
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: 将权限处理委托给生成的方法
        UserInfoActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
