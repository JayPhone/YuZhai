package com.yuzhai.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yuzhai.config.IPConfig;
import com.yuzhai.http.BaseHttpUtil;
import com.yuzhai.util.InputStreamToString;
import com.yuzhai.yuzhaiwork.R;

import java.io.InputStream;

public class LoginActivity extends AppCompatActivity {
    //组件引用
    TextView register;
    Button login;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(LoginActivity.this, msg.getData().getString("str"), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inflate_buttom_textview();
        login = (Button) findViewById(R.id.login_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        InputStream inputStream = BaseHttpUtil.ConnectInit(IPConfig.address2, 3000, "POST", true, true);
                        String str = InputStreamToString.praseToString(inputStream);
                        Log.i("str", str);
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("str", str);
                        message.setData(bundle);
                        handler.sendMessageAtTime(message, 0);
                    }
                }
                ).start();
            }
        });
    }

    public void inflate_buttom_textview() {
        register = (TextView) findViewById(R.id.login_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_register = new Intent();
                intent_register.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent_register);
            }
        });
    }
}
