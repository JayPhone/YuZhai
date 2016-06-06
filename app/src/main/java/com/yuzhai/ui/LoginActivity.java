package com.yuzhai.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yuzhai.config.IPConfig;
import com.yuzhai.dao.JsonUtil;
import com.yuzhai.entry.UserLogin;
import com.yuzhai.http.HttpUtil;
import com.yuzhai.util.CheckData;
import com.yuzhai.util.InputStreamToString;
import com.yuzhai.yuzhaiwork.R;

import java.io.InputStream;

public class LoginActivity extends AppCompatActivity {
    //组件引用
    EditText userNameEdit = null;
    EditText passwordEdit = null;
    Button loginButton = null;
    TextView registerTextView = null;

    //其他引用
    String params = null;
    InputStream returnStream = null;
    String jsonString = null;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == -1) {
                Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 1) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            } else if (msg.what == 0) {
                Toast.makeText(LoginActivity.this, "网络出现异常", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //加载用户名
        inflate_phoneNum_EditText();
        //加载密码
        inflate_pswd_EditText();
        //加载登录按钮
        inflate_login_button();
        //加载下方的文本
        inflate_buttom_textview();
    }

    public void inflate_phoneNum_EditText() {
        userNameEdit = (EditText) findViewById(R.id.username);
    }

    public void inflate_pswd_EditText() {
        passwordEdit = (EditText) findViewById(R.id.password);
    }

    public void inflate_login_button() {
        loginButton = (Button) findViewById(R.id.login_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params = checkData(LoginActivity.this);
                if (params != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            returnStream = HttpUtil.doPost(IPConfig.loginAddress, 5000, params);
                            if (returnStream != null) {
                                jsonString = InputStreamToString.praseToString(returnStream);
                                String code = JsonUtil.decodeJson(jsonString, "code");
                                if (code.equals("-1")) {
                                    handler.sendEmptyMessage(-1);
                                } else if (code.equals("1")) {
                                    handler.sendEmptyMessage(1);
                                }
                            } else {
                                handler.sendEmptyMessage(0);
                            }
                        }
                    }).start();
                }
            }
        });
    }

    //校验数据
    public String checkData(Context context) {
        //获取账号和密码
        String userName_str = userNameEdit.getText().toString();
        String pswd_str = passwordEdit.getText().toString();
        if (CheckData.isEmpty(userName_str)) {
            Toast.makeText(context, "手机号码不能为空", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (!CheckData.matchLength(userName_str, 11)) {
            Toast.makeText(context, "手机号码长度应为11位", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (CheckData.isEmpty(pswd_str)) {
            Toast.makeText(context, "密码不能为空", Toast.LENGTH_SHORT).show();
            return null;
        }

        UserLogin userLogin = new UserLogin(userName_str, pswd_str);
        String jsonString = JsonUtil.gsonToString(userLogin);
        return jsonString;
    }

    public void inflate_buttom_textview() {
        registerTextView = (TextView) findViewById(R.id.login_register);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_register = new Intent();
                intent_register.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent_register);
            }
        });
    }
}
