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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yuzhai.config.IPConfig;
import com.yuzhai.dao.JsonUtil;
import com.yuzhai.entry.UserPhone;
import com.yuzhai.entry.UserReg;
import com.yuzhai.util.CheckData;
import com.yuzhai.yuzhaiwork.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 账户注册界面
 */
public class RegisterActivity extends AppCompatActivity {
    //组件引用
    private EditText phoneNumEdit = null;
    private EditText passwordEdit = null;
    private EditText confirmPswdEdit = null;
    private EditText codeEdit = null;
    private Button codeButton = null;
    private Button registerButton = null;
    private TextView loginTextView = null;
    private TextView homeTextView = null;

    //其他引用
    private UserPhone userPhone = null;
    private UserReg userReg = null;
    private RequestQueue requestQueue = null;
    private StringRequest verifyRequest = null;
    private StringRequest registerRequest = null;
    private boolean paramPhoneCheck = false;
    private boolean paramAllDataCheck = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_register);
        //加载界面的各个组件
        inflate_phoneNum_EditText();
        inflate_code_EditText();
        inflate_password_EditText();
        inflate_confirmPswd_EditText();
        inflate_code_button(RegisterActivity.this);
        inflate_register_button(RegisterActivity.this);
        inflate_buttom_textview();
    }

    public void inflate_code_EditText() {
        codeEdit = (EditText) findViewById(R.id.code_field);
    }

    public void inflate_phoneNum_EditText() {
        phoneNumEdit = (EditText) findViewById(R.id.phone_num);
    }

    public void inflate_password_EditText() {
        passwordEdit = (EditText) findViewById(R.id.password);
    }

    public void inflate_confirmPswd_EditText() {
        confirmPswdEdit = (EditText) findViewById(R.id.affirmPsw);
    }

    public void inflate_code_button(final Context context) {
        codeButton = (Button) findViewById(R.id.code_button);
        codeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //验证手机号码合法性
                //当返回true时表示填写的手机号码符合格式
                paramPhoneCheck = checkPhoneNum(context);
                if (paramPhoneCheck == true) {
                    verifyRequest = new StringRequest(Request.Method.POST, IPConfig.verifyAddress, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Log.i("Respone", s);
                            String respone = JsonUtil.decodeJson(s, "code");
                            Log.i("Code", respone);
                            if (respone.equals("1")) {
                                Toast.makeText(RegisterActivity.this, "获取验证码成功,请注意查收", Toast.LENGTH_SHORT).show();
                            } else if (respone.equals("-1")) {
                                Toast.makeText(RegisterActivity.this, "获取验证码失败,请稍后重试", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(RegisterActivity.this, "网络异常,请检测网络后重试", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("userPhone", userPhone.getUserPhone());
                            return params;
                        }
                    };
                    requestQueue.add(verifyRequest);
                }
            }
        });
    }

    public void inflate_register_button(final Context context) {
        registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paramAllDataCheck = checkData(context);
                if (paramAllDataCheck == true) {
                    registerRequest = new StringRequest(Request.Method.POST, IPConfig.registerAddress, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Log.i("Respone", s);
                            String respone = JsonUtil.decodeJson(s, "code");
                            Log.i("Code", respone);
                            if (respone.equals("1")) {
                                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            } else if (respone.equals("-1")) {
                                Toast.makeText(RegisterActivity.this, "用户已存在", Toast.LENGTH_SHORT).show();
                            } else if (respone.equals("0")) {
                                Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                            } else if (respone.equals("2")) {
                                Toast.makeText(RegisterActivity.this, "验证码已过期，请重新获取", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(RegisterActivity.this, "网络异常,请检测网络后重试", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("userPhone", userReg.getUserPhone());
                            params.put("userPsw", userReg.getUserPsw());
                            params.put("temVerify", userReg.getTemVerify());
                            return params;
                        }
                    };
                    requestQueue.add(registerRequest);
                }
            }
        });
    }

    //获取验证码时校验数据
    public Boolean checkPhoneNum(Context context) {
        //获取手机号码
        String userName_str = phoneNumEdit.getText().toString();
        if (userName_str.equals("")) {
            Toast.makeText(context, "手机号码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (userName_str.length() < 11) {
            Toast.makeText(context, "手机号码长度应为11位", Toast.LENGTH_SHORT).show();
            return false;
        }

        //获取手机验证码的手机号
        userPhone = new UserPhone(userName_str);
        return true;
    }

    //账号注册时校验数据
    public boolean checkData(Context context) {
        //获取账号和密码
        String userName_str = phoneNumEdit.getText().toString();
        String identCode_str = codeEdit.getText().toString();
        String pswd_str = passwordEdit.getText().toString();
        String confirmPswd_str = confirmPswdEdit.getText().toString();
        //校验手机号码
        if (CheckData.isEmpty(userName_str)) {
            Toast.makeText(context, "手机号码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!CheckData.matchLength(userName_str, 11)) {
            Toast.makeText(context, "手机号码格式错误", Toast.LENGTH_SHORT).show();
            return false;
        }

        //校验验证码
        if (CheckData.isEmpty(identCode_str)) {
            Toast.makeText(context, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!CheckData.matchLength(identCode_str, 6)) {
            Toast.makeText(context, "验证码格式错误", Toast.LENGTH_SHORT).show();
            return false;
        }

        //校验密码
        if (CheckData.isEmpty(pswd_str)) {
            Toast.makeText(context, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (CheckData.lessLength(pswd_str, 6)) {
            Toast.makeText(context, "密码长度应为6-16位", Toast.LENGTH_SHORT).show();
            return false;
        }

        //校验第二次密码
        if (!CheckData.matchString(pswd_str, confirmPswd_str)) {
            Toast.makeText(context, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return false;
        }

        userReg = new UserReg(userName_str, pswd_str, identCode_str);
        return true;
    }

    public void inflate_buttom_textview() {
        loginTextView = (TextView) findViewById(R.id.login_nav);
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        homeTextView = (TextView) findViewById(R.id.home_page_nav);
        homeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_home = new Intent();
                intent_home.setClass(RegisterActivity.this, MainActivity.class);
                startActivity(intent_home);
                RegisterActivity.this.finish();
            }
        });
    }
}
