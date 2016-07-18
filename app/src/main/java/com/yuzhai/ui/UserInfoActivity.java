package com.yuzhai.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuzhai.global.CustomApplication;
import com.yuzhai.util.BitmapUtil;
import com.yuzhai.yuzhaiwork.R;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

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
    private Button loginExit;
    private ImageView headerImage;
    private ImageView back;
    private RelativeLayout changeHeaderImage;
    private RelativeLayout changeUserName;

    private CustomApplication customApplication;
    private String imagePath;
    private int CAMERA_PEQUEST = 1;
    private int GALLERY_PEQUEST = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        customApplication = (CustomApplication) getApplication();
        initViews();
    }

    public void initViews() {
        back = (ImageView) findViewById(R.id.back_image);
        changeHeaderImage = (RelativeLayout) findViewById(R.id.change_image_layout);
        headerImage = (ImageView) findViewById(R.id.header_image);
        changePswd = (TextView) findViewById(R.id.change_pswd);
        changeUserName = (RelativeLayout) findViewById(R.id.change_user_name_layout);
        userName = (TextView) findViewById(R.id.user_name);
        loginExit = (Button) findViewById(R.id.exit_login);
        //设置监听器
        back.setOnClickListener(this);
        changeHeaderImage.setOnClickListener(this);
        headerImage.setOnClickListener(this);
        changePswd.setOnClickListener(this);
        changeUserName.setOnClickListener(this);
        loginExit.setOnClickListener(this);
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
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(imagePath)), "image/*");
                startActivity(intent);
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

    //修改用户名对话框
    public void changeUserNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("输入用户名");
        builder.setView(getLayoutInflater().inflate(R.layout.userinfo_change_username_layout, null));
        builder.setPositiveButton("修改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

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
                                showGallery();
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    //执行需要权限的操作。如果这是由permissionsdispatcher执行，许可将被授予
    @NeedsPermission(Manifest.permission.CAMERA)
    public void showCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //保存图片的路径
            String out_file_path = Environment.getExternalStorageDirectory() + "/YuZhai/headerImage";
            //创建图片路径文件夹
            File dir = new File(out_file_path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            imagePath = out_file_path + "/hdIMG_" + new DateFormat().format("yyyyMMdd_HHmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
            Log.i("path", imagePath);
            intent_camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(imagePath)));
            startActivityForResult(intent_camera, CAMERA_PEQUEST);
        } else {
            Log.i("错误", "请确认已经插入SD卡");
        }
    }

    public void showGallery() {
        Intent intent_gallery = new Intent(Intent.ACTION_PICK);
        intent_gallery.setType("image/*");
        startActivityForResult(intent_gallery, GALLERY_PEQUEST);
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
        if (requestCode == CAMERA_PEQUEST && resultCode == Activity.RESULT_OK) {
            headerImage.setImageBitmap(BitmapUtil.decodeSampledBitmapFromFile(imagePath, 200, 200));
        } else if (requestCode == GALLERY_PEQUEST && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                headerImage.setImageBitmap(BitmapUtil.decodeSampledBitmapFromFile(path, 70, 70));
            }
        }
    }

    //权限许可情况调用
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: 将权限处理委托给生成的方法
        UserInfoActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
