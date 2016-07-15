package com.yuzhai.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuzhai.global.CustomApplication;
import com.yuzhai.yuzhaiwork.R;

/**
 * Created by Administrator on 2016/7/8.
 */
public class UserInfoActivity extends AppCompatActivity {
    private TextView changePswd;
    private Button loginExit;
    private ImageView userPicture;
    private ImageView back;
    private RelativeLayout userChangePic;
    private CustomApplication customApplication;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        customApplication = (CustomApplication) getApplication();
        initViews();
    }

    public void initViews() {
        changePswd = (TextView) findViewById(R.id.change_pswd);
        userPicture = (ImageView) findViewById(R.id.userpicture);
        userChangePic = (RelativeLayout) findViewById(R.id.userrela);
        back = (ImageView) findViewById(R.id.back_image);
        loginExit = (Button) findViewById(R.id.exit_login);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        userChangePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddImageDialog();
            }
        });

        loginExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
        changePswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_changePswd = new Intent();
                intent_changePswd.setClass(UserInfoActivity.this, ChangePswdActivity.class);
                startActivity(intent_changePswd);
            }
        });
    }

    //对话框显示图片的添加
    protected void AddImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("添加图片");
        builder.setCancelable(true); //响应back按钮
        builder.setItems(new String[]{"本地相册选择", "手机相机添加"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: //本地相册
                                break;
                            case 1: //手机相机
                                break;
                            default:
                                break;
                        }
                    }
                });
        builder.create().show();
    }
}
