package com.yuzhai.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuzhai.config.IPConfig;
import com.yuzhai.entry.UserForgetPswd;
import com.yuzhai.entry.UserPhone;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.util.CheckData;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.yuzhaiwork.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/4.
 */
public class ForgetPswdActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backImage;
    //组件引用
    private EditText phoneNumEdit = null;
    private EditText passwordEdit = null;
    private EditText confirmPswdEdit = null;
    private EditText codeEdit = null;
    private Button codeButton = null;
    private Button changeButton = null;

    private CustomApplication customApplication;
    private RequestQueue requestQueue;
    private UserForgetPswd userForget;
    private UserPhone userPhone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pswd);
        customApplication = (CustomApplication) getApplication();
        requestQueue = customApplication.getRequestQueue();
        initViews();
    }

    public void initViews() {
        //获取组件
        backImage = (ImageView) findViewById(R.id.back_image);
        phoneNumEdit = (EditText) findViewById(R.id.phone_num);
        codeEdit = (EditText) findViewById(R.id.code_field);
        codeButton = (Button) findViewById(R.id.code_button);
        passwordEdit = (EditText) findViewById(R.id.password);
        confirmPswdEdit = (EditText) findViewById(R.id.confirm_password);
        changeButton = (Button) findViewById(R.id.change_button);

        //设置点击监听器
        backImage.setOnClickListener(this);
        codeButton.setOnClickListener(this);
        changeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_image:
                finish();
                break;
            case R.id.code_button:
                if (checkPhoneNum() == true) {
                    CommonRequest verifyRequest = new CommonRequest(Request.Method.POST, IPConfig.verifyAddress, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Log.i("Respone", s);
                            String respone = JsonUtil.decodeJson(s, "code");
                            Log.i("Code", respone);
                            if (respone.equals("1")) {
                                Toast.makeText(ForgetPswdActivity.this, "获取验证码成功,请注意查收", Toast.LENGTH_SHORT).show();
                            } else if (respone.equals("-1")) {
                                Toast.makeText(ForgetPswdActivity.this, "获取验证码失败,请稍后重试", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(ForgetPswdActivity.this, "网络异常,请检测网络后重试", Toast.LENGTH_SHORT).show();
                        }
                    });
                    //设置请求参数
                    verifyRequest.setRequestParams(createFirstParams());
                    //添加请求到请求队列
                    requestQueue.add(verifyRequest);
                }
                break;
            case R.id.change_button:
                if (checkData() == true) {
                    CommonRequest ForgetRequest = new CommonRequest(Request.Method.POST, IPConfig.forgetPswdAddress, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Log.i("Respone", s);
                            String respone = JsonUtil.decodeJson(s, "code");
                            Log.i("Code", respone);
                            if (respone.equals("1")) {
                                Toast.makeText(ForgetPswdActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                Intent intent_login = new Intent(ForgetPswdActivity.this, LoginActivity.class);
                                startActivity(intent_login);
                            } else if (respone.equals("0")) {
                                Toast.makeText(ForgetPswdActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                            } else if (respone.equals("2")) {
                                Toast.makeText(ForgetPswdActivity.this, "验证码已过期，请重新获取", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(ForgetPswdActivity.this, "服务器开小差了", Toast.LENGTH_SHORT).show();
                        }
                    });

                    ForgetRequest.setRequestParams(createSecondParams());
                    //添加请求到请求队列
                    requestQueue.add(ForgetRequest);
                }
                break;
        }
    }

    //获取验证码时校验数据
    public Boolean checkPhoneNum() {
        //获取手机号码
        String userName = phoneNumEdit.getText().toString();
        if (userName.equals("")) {
            Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (userName.length() < 11) {
            Toast.makeText(this, "手机号码长度应为11位", Toast.LENGTH_SHORT).show();
            return false;
        }

        //获取手机验证码的手机号
        userPhone = new UserPhone(userName);
        return true;
    }

    //修改密码时校验数据
    public boolean checkData() {
        //获取账号和密码
        String userName = phoneNumEdit.getText().toString();
        String identCode = codeEdit.getText().toString();
        String pswd = passwordEdit.getText().toString();
        String confirmPswd = confirmPswdEdit.getText().toString();
        //校验手机号码
        if (CheckData.isEmpty(userName)) {
            Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!CheckData.matchLength(userName, 11)) {
            Toast.makeText(this, "手机号码格式错误", Toast.LENGTH_SHORT).show();
            return false;
        }

        //校验验证码
        if (CheckData.isEmpty(identCode)) {
            Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!CheckData.matchLength(identCode, 6)) {
            Toast.makeText(this, "验证码格式错误", Toast.LENGTH_SHORT).show();
            return false;
        }

        //校验密码
        if (CheckData.isEmpty(pswd)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (CheckData.lessLength(pswd, 6)) {
            Toast.makeText(this, "密码长度应为6-16位", Toast.LENGTH_SHORT).show();
            return false;
        }

        //校验第二次密码
        if (!CheckData.matchString(pswd, confirmPswd)) {
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return false;
        }

        userForget = new UserForgetPswd(userName, pswd, identCode);
        return true;
    }

    //生成请求参数
    public Map<String, String> createFirstParams() {
        Map<String, String> params = new HashMap<>();
        params.put("userPhone", userPhone.getUserPhone());
        return params;
    }

    public Map<String, String> createSecondParams() {
        //设置请求参数
        Map<String, String> params = new HashMap<>();
        params.put("userPhone", userForget.getUserPhone());
        params.put("userPsw", userForget.getUserPsw());
        params.put("temVerify", userForget.getTemVerify());
        return params;
    }
}
