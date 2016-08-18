package com.yuzhai.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuzhai.config.IPConfig;
import com.yuzhai.entry.UserChangePswd;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.util.CheckData;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.yuzhaiwork.R;

import java.util.HashMap;
import java.util.Map;

public class ChangePswdActivity extends AppCompatActivity {
    private EditText passwordEdit;
    private EditText newPasswordEdit;
    private EditText confirmNewPasswordEdit;
    private Button changeButton;

    private CustomApplication customApplication;
    private RequestQueue requestQueue;
    private UserChangePswd userChangePswd;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pswd);
        customApplication = (CustomApplication) getApplication();
        requestQueue = customApplication.getRequestQueue();
        initViews();
    }

    public void initViews() {
        //获取组件
        passwordEdit = (EditText) findViewById(R.id.password);
        newPasswordEdit = (EditText) findViewById(R.id.new_password);
        confirmNewPasswordEdit = (EditText) findViewById(R.id.confirm_password);
        changeButton = (Button) findViewById(R.id.change_button);

        //设置监听器
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkData() == true) {
                    CommonRequest changeRequest = new CommonRequest(Request.Method.POST, IPConfig.alterPswdAddress, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Log.i("response_alter_pswd", s);
                            if (JsonUtil.decodeJson(s, "code").equals("1")) {
                                Toast.makeText(ChangePswdActivity.this, "密码修改成功,请重新登陆", Toast.LENGTH_SHORT).show();
                                //设置为没登录
                                customApplication.setLoginState(false);
                                //替换侧滑菜单界面为非登录界面
                                Intent replaceFragment = new Intent();
                                replaceFragment.setAction("yzgz.broadcast.replace.fragment");
                                sendBroadcast(replaceFragment);
                                //启动主界面，由于设置了singleTask模式，上层的activity被弹出
                                Intent intent_main = new Intent(ChangePswdActivity.this, MainActivity.class);
                                startActivity(intent_main);
                            } else if (JsonUtil.decodeJson(s, "code").equals("-1")) {
                                Toast.makeText(ChangePswdActivity.this, "原密码错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(ChangePswdActivity.this, "服务器开小差了", Toast.LENGTH_SHORT).show();
                        }
                    });
                    changeRequest.setRequestParams(createParams());
                    changeRequest.setRequestHeaders(createHeaders());
                    requestQueue.add(changeRequest);
                }
            }
        });
    }

    //修改密码时校验数据
    public boolean checkData() {
        String password = passwordEdit.getText().toString();
        String newPassword = newPasswordEdit.getText().toString();
        String confirmPassword = confirmNewPasswordEdit.getText().toString();
        if (CheckData.isEmpty(password)) {
            Toast.makeText(this, "旧密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (CheckData.isEmpty(newPassword)) {
            Toast.makeText(this, "新密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (CheckData.lessLength(newPassword, 6)) {
            Toast.makeText(this, "新密码长度应为6-16位", Toast.LENGTH_SHORT).show();
            return false;
        }

        //校验第二次密码
        if (!CheckData.matchString(newPassword, confirmPassword)) {
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return false;
        }

        userChangePswd = new UserChangePswd(password, newPassword);
        return true;
    }

    public Map<String, String> createParams() {
        //设置请求参数
        Map<String, String> params = new HashMap<>();
        params.put("oldPsw", userChangePswd.getOldPswd());
        params.put("userPsw", userChangePswd.getNewPswd());
        return params;
    }

    public Map<String, String> createHeaders() {
        //设置请求参数
        Map<String, String> headers = new HashMap<>();
        headers.put("cookie", customApplication.getCookie());
        return headers;
    }
}
