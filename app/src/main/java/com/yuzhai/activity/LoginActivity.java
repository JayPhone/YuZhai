package com.yuzhai.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuzhai.bean.innerBean.BaseUserInfoBean;
import com.yuzhai.bean.innerBean.LoginToOrderBean;
import com.yuzhai.bean.requestBean.UserLoginBean;
import com.yuzhai.bean.responseBean.LoginRespBean;
import com.yuzhai.http.IPConfig;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.ParamsGenerateUtil;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

/**
 * 账户登录界面
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        Response.Listener<String> {
    private TextInputLayout userPhoneLayout;
    private TextInputEditText userPhoneEdit;
    private TextInputLayout pswLayout;
    private TextInputEditText pswEdit;
    private Button loginButton;
    private Button registerButton;
    private TextView forgetPswdTextView;
    private CustomApplication customApplication;
    private RequestQueue requestQueue;
    private CommonRequest loginRequest;
    //记录登录信息的实体
    private UserLoginBean userLoginBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //初始化控件
        initViews();
        //获取全局的Applicant对象
        customApplication = (CustomApplication) getApplication();
        //获取请求队列
        requestQueue = RequestQueueSingleton.getRequestQueue(this);
    }

    /**
     * 初始化控件实例，并设置相应的点击事件
     */
    public void initViews() {
        userPhoneLayout = (TextInputLayout) findViewById(R.id.user_name_layout);
        userPhoneEdit = (TextInputEditText) findViewById(R.id.user_name);
        pswLayout = (TextInputLayout) findViewById(R.id.psw_layout);
        pswEdit = (TextInputEditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.login_btn);
        registerButton = (Button) findViewById(R.id.register_nav);
        forgetPswdTextView = (TextView) findViewById(R.id.forget_pswd_nav);

        userPhoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 11) {
                    userPhoneLayout.setError("手机号码长度不能小于11位");
                } else {
                    userPhoneLayout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pswEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 6) {
                    pswLayout.setError("密码长度不能小于6位");
                } else {
                    pswLayout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        forgetPswdTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //点击登陆按钮
            case R.id.login_btn:
                //发送登录请求
                sendLoginRequest(userPhoneEdit.getText().toString(),
                        pswEdit.getText().toString(),
                        customApplication.getToken());
                break;

            //点击注册导航文字
            case R.id.register_nav:
                Intent register = new Intent(this, RegisterActivity.class);
                startActivity(register);
                break;

            //点击忘记密码导航文字
            case R.id.forget_pswd_nav:
                Intent forgetPsw = new Intent(this, ForgetPswActivity.class);
                startActivity(forgetPsw);
                break;
        }
    }

    /**
     * 发送登录请求
     *
     * @param userPhone 用户名
     * @param psw       密码
     */
    public void sendLoginRequest(String userPhone, String psw, String token) {
        if (checkData(userPhone, psw)) {
            //生成登录请求参数
            Map<String, String> params = ParamsGenerateUtil.generateLoginParams(
                    userLoginBean.getUserPhone(),
                    userLoginBean.getUserPsw(),
                    token);
            Log.i("param", params.toString());

            //创建登录请求
            loginRequest = new CommonRequest(IPConfig.loginAddress,
                    null,
                    params,
                    LoginActivity.this,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            UnRepeatToast.showToast(LoginActivity.this, "服务器不务正业中");
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
        userLoginBean = new UserLoginBean(userPhoneText, userPswText);
        return true;
    }

    /**
     * 对服务器响应的数据进行处理
     *
     * @param resp 响应数据
     */
    @Override
    public void onResponse(String resp) {
        Log.i("login_resp", resp);
        LoginRespBean loginRespBean = JsonUtil.decodeByGson(resp, LoginRespBean.class);

        //返回的响应码
        String respCode = loginRespBean.getCode();
        Log.i("login_resp_code", loginRespBean.getCode());

        if (respCode != null && respCode.equals("-1")) {
            UnRepeatToast.showToast(this, "账号或密码错误");
        }

        if (respCode != null && respCode.equals("0")) {
            UnRepeatToast.showToast(this, "账号不存在");
        }

        if (respCode != null && respCode.equals("1")) {
            //用户头像路径
            String userHead = loginRespBean.getUserHeadUrl();
            Log.i("userHead", loginRespBean.getUserHeadUrl());
            //用户名
            String userName = loginRespBean.getUserName();
            Log.i("userName", loginRespBean.getUserName());
            //保存登陆成功的手机号和密码
            customApplication.addUserInfo(userLoginBean.getUserPhone(), userLoginBean.getUserPsw());
            //设置为登录状态
            customApplication.setLoginState(true);
            //保存cookie
            customApplication.setCookie(loginRequest.getResponseCookie());
            //使用EventBus发送替换为登录的界面的消息到MainActivity
            EventBus.getDefault().post(new BaseUserInfoBean(userHead, userName, customApplication.isLogin()));
            //刷新个人订单界面
            EventBus.getDefault().post(new LoginToOrderBean(customApplication.isLogin()));
            finish();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
