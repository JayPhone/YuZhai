package com.yuzhai.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuzhai.config.IPConfig;
import com.yuzhai.config.RespParamsNameConfig;
import com.yuzhai.entry.UserLogin;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.ParamsGenerateUtil;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import java.util.Map;

/**
 * 账户登录界面
 */
public class LoginActivity extends AppCompatActivity implements
        Response.Listener<String>,
        View.OnClickListener {

    /**
     * 账号输入框
     */
    private EditText userPhoneEdit;

    /**
     * 密码输入框
     */
    private EditText passwordEdit;

    /**
     * 登录按钮
     */
    private Button loginButton;

    /**
     * 注册导航文本
     */
    private TextView registerTextView;

    /**
     * 忘记密码导航文本
     */
    private TextView forgetPswdTextView;

    /**
     * 自定义的全局Application
     */
    private CustomApplication customApplication;

    /**
     * 请求队列
     */
    private RequestQueue requestQueue;

    /**
     * 登录请求
     */
    private CommonRequest loginRequest;

    /**
     * 记录登录信息的实体
     */
    private UserLogin userLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //初始化控件
        initViews();
        //获取全局的Applicant对象
        customApplication = (CustomApplication) getApplication();
        //获取请求队列
        requestQueue = RequestQueueSingleton.getInstance(this).getRequestQueue();
    }

    /**
     * 初始化控件实例，并设置相应的点击事件
     */
    public void initViews() {
        userPhoneEdit = (EditText) findViewById(R.id.username);
        passwordEdit = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.login_login);
        registerTextView = (TextView) findViewById(R.id.register_nav);
        forgetPswdTextView = (TextView) findViewById(R.id.forget_pswd_nav);

        loginButton.setOnClickListener(this);
        registerTextView.setOnClickListener(this);
        forgetPswdTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //点击登陆按钮
            case R.id.login_login:
                //发送登录请求
                sendLoginRequest();
                break;

            //点击注册导航文字
            case R.id.register_nav:
                Intent register = new Intent(this, RegisterActivity.class);
                startActivity(register);
                break;

            //点击忘记密码导航文字
            case R.id.forget_pswd_nav:
                Intent forgetPsw = new Intent(this, ForgetPswdActivity.class);
                startActivity(forgetPsw);
                break;
        }
    }

    /**
     * 点击登陆按钮后,校验输入的信息,发送登录请求
     */
    public void sendLoginRequest() {
        if (checkData(userPhoneEdit.getText().toString(), passwordEdit.getText().toString())) {
            //生成登录请求参数
            Map<String, String> params = ParamsGenerateUtil.generateLoginParams(
                    userLogin.getUserPhone(),
                    userLogin.getUserPsw());

            //创建登录请求
            loginRequest = new CommonRequest(IPConfig.loginAddress,
                    null,
                    params,
                    LoginActivity.this,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            UnRepeatToast.showToast(LoginActivity.this, "服务器睡着了");
                        }
                    });

            //添加登录请求到请求队列
            requestQueue.add(loginRequest);
        }
    }

    /**
     * 校验数据:
     * 如果填写的数据其中一项或全部不正确，返回false;
     * 如果填写的数据全部都正确，返回true.
     */
    public boolean checkData(String userPhoneText, String userPswText) {
        if (userPhoneText.equals("")) {
            UnRepeatToast.showToast(this, "手机号码不能为空");
            return false;
        }

        if (userPhoneText.length() != 11) {
            UnRepeatToast.showToast(this, "手机号码长度应为11位");
            return false;
        }

        if (userPswText.equals("")) {
            UnRepeatToast.showToast(this, "密码不能为空");
            return false;
        }

        //校验成功后，保存填写的用户名和密码
        userLogin = new UserLogin(userPhoneText, userPswText);
        return true;
    }

    @Override
    public void onResponse(String response) {
        Log.i("response", response);
        String responseCode = JsonUtil.decodeJson(response, RespParamsNameConfig.LoginRespParam.CODE);

        if (responseCode.equals("-1")) {
            UnRepeatToast.showToast(this, "账号或密码错误");
        }

        if (responseCode.equals("0")) {
            UnRepeatToast.showToast(this, "账号不存在");
        }

        if (responseCode.equals("1")) {
            //用户头像路径
            String userHead = JsonUtil.decodeJson(response, RespParamsNameConfig.LoginRespParam.USERHEAD);
            //用户名
            String userName = JsonUtil.decodeJson(response, RespParamsNameConfig.LoginRespParam.USERNAME);
            //保存登陆成功的手机号和密码
            customApplication.addUserInfo(userLogin.getUserPhone(), userLogin.getUserPsw());
            //保存登陆成功的账号的cookie
            customApplication.addCookie(loginRequest.getResponseCookie());
            //设置为登录状态
            customApplication.setLoginState(true);
            //替换为登录的界面
            replaceMenuUI(userHead, userName);
            finish();
        }
    }

    /**
     * 替换侧滑菜单的UI为登录状态
     *
     * @param userHead 用户头像路径
     * @param userName 用户名
     */
    public void replaceMenuUI(String userHead, String userName) {
        Intent replaceFragment = new Intent();
        replaceFragment.putExtra("userHead", userHead);
        replaceFragment.putExtra("userName", userName);
        replaceFragment.setAction("yzgz.broadcast.replace.fragment");
        sendBroadcast(replaceFragment);
    }
}
