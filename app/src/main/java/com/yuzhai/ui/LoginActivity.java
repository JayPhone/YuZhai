package com.yuzhai.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuzhai.config.IPConfig;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.entry.UserLogin;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.util.CheckData;
import com.yuzhai.yuzhaiwork.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 账户登录界面
 */
public class LoginActivity extends AppCompatActivity {
    //组件引用
    private EditText userNameEdit = null;
    private EditText passwordEdit = null;
    private Button loginButton = null;
    private TextView registerTextView = null;
    private TextView forgetPswdTextView = null;

    //其他引用
    private CustomApplication customApplication;
    private RequestQueue requestQueue = null;
    private CommonRequest loginRequest = null;
    private UserLogin userLogin = null;
    private boolean paramsCheck = false;
    private Map<String, String> params = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取全局请求队列
        customApplication = (CustomApplication) getApplication();
        requestQueue = customApplication.getRequestQueue();
        setContentView(R.layout.activity_login);
        //加载用户名
        inflate_phoneNum_EditText();
        //加载密码框
        inflate_pswd_EditText();
        //加载登录按钮
        inflate_login_button();
        //加载下方的两个导航
        inflate_buttom_textview();
    }

    //加载用户名框
    public void inflate_phoneNum_EditText() {
        userNameEdit = (EditText) findViewById(R.id.username);
    }

    //加载密码框
    public void inflate_pswd_EditText() {
        passwordEdit = (EditText) findViewById(R.id.password);
    }

    //加载登录按钮
    public void inflate_login_button() {
        loginButton = (Button) findViewById(R.id.login_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //校验数据
                //当返回true时表示所有填写的参数均符合格式
                paramsCheck = checkData(LoginActivity.this);
                if (paramsCheck == true) {
                    loginRequest = new CommonRequest(LoginActivity.this, Request.Method.POST, IPConfig.loginAddress, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Log.i("Response", s);
                            String response = JsonUtil.decodeJson(s, "code");
                            Log.i("Code", response);
                            if (response.equals("1")) {
                                //保存登陆成功的手机号和密码
                                customApplication.addUserInfo(userLogin.getUserPhone(), userLogin.getUserPsw());
                                //保存登陆成功的账号的cookie
                                customApplication.addCookie(loginRequest.getResponseCookie());
                                //设置为登录状态
                                customApplication.setLOGIN(true);
                                //替换为登录的界面
                                Intent replaceFragment = new Intent();
                                replaceFragment.setAction("yzgz.broadcast.replace.fragment");
                                sendBroadcast(replaceFragment);
                                finish();
                            } else if (response.equals("-1")) {
                                Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(LoginActivity.this, "网络异常,请检测网络后重试", Toast.LENGTH_SHORT).show();
                        }
                    });
                    params = new HashMap<String, String>();
                    params.put("userPhone", userLogin.getUserPhone());
                    params.put("userPsw", userLogin.getUserPsw());
                    loginRequest.setParams(params);
                    requestQueue.add(loginRequest);
                }
            }
        });
    }

    /*校验数据
     *如果填写的数据不正确，返回false
     * 如果填写的数据全部都正确，返回true
     */
    public boolean checkData(Context context) {
        //获取账号和密码
        String userName_str = userNameEdit.getText().toString();
        String pswd_str = passwordEdit.getText().toString();

        /*CheckData类用于校验填写的数据*/
        if (CheckData.isEmpty(userName_str)) {
            Toast.makeText(context, "手机号码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!CheckData.matchLength(userName_str, 11)) {
            Toast.makeText(context, "手机号码长度应为11位", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (CheckData.isEmpty(pswd_str)) {
            Toast.makeText(context, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        //校验成功后，保存填写的用户名和密码
        userLogin = new UserLogin(userName_str, pswd_str);
        return true;
    }

    //加载下方的两个导航
    public void inflate_buttom_textview() {
        registerTextView = (TextView) findViewById(R.id.register_nav);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_register = new Intent();
                intent_register.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent_register);
            }
        });

        forgetPswdTextView = (TextView) findViewById(R.id.forget_pswd_nav);
        forgetPswdTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_forget_pswd = new Intent();
                intent_forget_pswd.setClass(LoginActivity.this, ForgetPswdActivity.class);
                startActivity(intent_forget_pswd);
            }
        });
    }
}
