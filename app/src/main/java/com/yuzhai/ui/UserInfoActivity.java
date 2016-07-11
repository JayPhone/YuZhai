package com.yuzhai.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.yuzhai.yuzhaiwork.R;

import java.io.File;

/**
 * Created by Administrator on 2016/7/8.
 */
public class UserInfoActivity extends AppCompatActivity {
    private ImageView userPicture, out;
    private RelativeLayout userChangePic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        //获取id
        userPicture = (ImageView) findViewById(R.id.userpicture);
        userChangePic = (RelativeLayout) findViewById(R.id.userrela);
        out = (ImageView) findViewById(R.id.out);

        out.setOnClickListener(new View.OnClickListener() {
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
