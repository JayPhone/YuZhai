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
import com.yuzhai.entry.requestBean.UserPhone;
import com.yuzhai.entry.requestBean.UserReg;
import com.yuzhai.entry.responseBean.RegisterRespBean;
import com.yuzhai.entry.responseBean.VerifyRespBean;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.ParamsGenerateUtil;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import java.util.Map;

/**
 * 账户注册界面
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 账号输入框
     */
    private EditText regPhoneEdit;

    /**
     * 验证码输入框
     */
    private EditText checkCodeEdit;

    /**
     * 密码输入框
     */
    private EditText pswEdit;

    /**
     * 确认密码输入框
     */
    private EditText cfmPswEdit;

    /**
     * 获取验证码按钮
     */
    private Button checkCodeButton;

    /**
     * 注册按钮
     */
    private Button registerButton;

    /**
     * 登录导航文本
     */
    private TextView loginTextView;

    /**
     * 记录获取验证码信息的实体
     */
    private UserPhone userPhone;

    /**
     * 记录注册信息的实体
     */
    private UserReg userReg;

    /**
     * 请求队列
     */
    private RequestQueue requestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        View decorView = getWindow().getDecorView();
//        if (Build.VERSION.SDK_INT >= 21) {
//            //让主体内容占用上方状态栏和下方导航栏的位置，同时设置状态栏和导航栏为透明色
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
//                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
//            decorView.setSystemUiVisibility(option);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
        //初始化控件
        initViews();
        //获取请求队列
        requestQueue = RequestQueueSingleton.getInstance(this).getRequestQueue();
    }

    /**
     * 初始化控件实例，并设置相应的点击事件
     */
    public void initViews() {
        regPhoneEdit = (EditText) findViewById(R.id.phone_num);
        checkCodeEdit = (EditText) findViewById(R.id.code_field);
        checkCodeButton = (Button) findViewById(R.id.code_button);
        pswEdit = (EditText) findViewById(R.id.password);
        cfmPswEdit = (EditText) findViewById(R.id.affirmPsw);
        registerButton = (Button) findViewById(R.id.register_button);
        loginTextView = (TextView) findViewById(R.id.login_nav);

        checkCodeButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        loginTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //点击获取验证码按钮
            case R.id.code_button:
                //发送获取验证码请求
                sendVerifyRequest(regPhoneEdit.getText().toString());
                break;

            //点击注册按钮
            case R.id.register_button:
                //发送注册请求
                sendRegisterRequest(regPhoneEdit.getText().toString(),
                        checkCodeEdit.getText().toString(),
                        pswEdit.getText().toString(),
                        cfmPswEdit.getText().toString());
                break;

            //点击登录导航文本
            case R.id.login_nav:
                Intent login = new Intent(this, LoginActivity.class);
                startActivity(login);
                break;
        }
    }

    /**
     * 发送获取验证码请求
     *
     * @param regPhone 获取验证码的号码
     */
    public void sendVerifyRequest(final String regPhone) {
        if (checkRegPhone(regPhone)) {
            //生成获取验证码请求参数
            Map<String, String> params = ParamsGenerateUtil.generateVerifyParams(userPhone.getUserPhone());

            //创建获取验证码请求
            final CommonRequest verifyRequest = new CommonRequest(IPConfig.verifyAddress,
                    null,
                    params,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String resp) {
                            Log.i("verify_resp", resp);
                            VerifyRespBean verifyRespBean = JsonUtil.decodeByGson(resp, VerifyRespBean.class);

                            //返回的响应码
                            String respCode = verifyRespBean.getCode();
                            Log.i("verify_resp_code", respCode);

                            if (respCode != null && respCode.equals("1")) {
                                UnRepeatToast.showToast(RegisterActivity.this, "验证码发射成功,请注意捕获");
                            }

                            if (respCode != null && respCode.equals("-1")) {
                                UnRepeatToast.showToast(RegisterActivity.this, "验证码发射失败,请稍后再来");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            UnRepeatToast.showToast(RegisterActivity.this, "服务器不务正业中");
                        }
                    });

            //添加获取验证码请求到请求队列
            requestQueue.add(verifyRequest);
        }
    }

    /**
     * 发送注册请求
     *
     * @param regPhone  填写的注册号码
     * @param checkCode 填写的验证码
     * @param psw       填写的密码
     * @param cfmPsw    重复密码
     */
    public void sendRegisterRequest(String regPhone,
                                    String checkCode,
                                    String psw,
                                    String cfmPsw) {
        if (checkRegData(regPhone,
                checkCode,
                psw,
                cfmPsw)) {

            //生成注册请求参数
            Map<String, String> params = ParamsGenerateUtil.generateRegisterParams(userReg.getUserPhone(),
                    userReg.getTemVerify(),
                    userReg.getUserPsw());

            //创建注册请求
            CommonRequest regRequest = new CommonRequest(IPConfig.registerAddress,
                    null,
                    params,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String resp) {
                            Log.i("register_resp", resp);

                            //获取返回码
                            RegisterRespBean registerRespBean = JsonUtil.decodeByGson(resp, RegisterRespBean.class);
                            String respCode = registerRespBean.getCode();
                            Log.i("register_resp_code", respCode);

                            if (respCode != null && respCode.equals("-1")) {
                                UnRepeatToast.showToast(RegisterActivity.this, "用户已存在");
                            }

                            if (respCode != null && respCode.equals("0")) {
                                UnRepeatToast.showToast(RegisterActivity.this, "验证码错误");
                            }

                            if (respCode != null && respCode.equals("1")) {
                                Log.i("register_resp", "register success");
                                finish();
                            }

                            if (respCode != null && respCode.equals("2")) {
                                UnRepeatToast.showToast(RegisterActivity.this, "验证码已过期，请重新获取");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            UnRepeatToast.showToast(RegisterActivity.this, "服务器不务正业中");
                        }
                    });

            //添加注册请求到请求队列
            requestQueue.add(regRequest);
        }
    }

    /**
     * 用于校验获取验证码操作的数据
     *
     * @param phoneNumber 获取验证码的手机号码
     * @return 如果填写的数据其中一项或全部不正确，返回false，否则返回true
     */
    public Boolean checkRegPhone(String phoneNumber) {
        if (phoneNumber.equals("")) {
            UnRepeatToast.showToast(this, "手机号码不能为空");
            return false;
        }

        if (phoneNumber.length() != 11) {
            UnRepeatToast.showToast(this, "手机号码长度应为11位");
            return false;
        }

        //校验成功后，保存手机号
        userPhone = new UserPhone(phoneNumber);
        return true;
    }

    /**
     * 用于校验注册操作的数据
     *
     * @param regPhoneText  填写的注册号码
     * @param checkCodeText 填写的验证码
     * @param pswText       填写的密码
     * @param cfmPswText    重复密码
     * @return 如果填写的数据其中一项或全部不正确，返回false，否则返回true
     */
    public boolean checkRegData(String regPhoneText,
                                String checkCodeText,
                                String pswText,
                                String cfmPswText) {

        //校验手机号码
        if (!checkRegPhone(regPhoneText)) {
            return false;
        }

        //校验验证码
        if (checkCodeText.equals("")) {
            UnRepeatToast.showToast(this, "验证码不能为空");
            return false;
        }

        if (checkCodeText.length() != 6) {
            UnRepeatToast.showToast(this, "验证码长度应为6位");
            return false;
        }

        //校验密码
        if (pswText.equals("")) {
            UnRepeatToast.showToast(this, "密码不能为空");
            return false;
        }

        if (pswText.length() < 6) {
            UnRepeatToast.showToast(this, "密码长度不能小于6位");
            return false;
        }

        //校验两次密码是否一致
        if (!cfmPswText.equals(pswText)) {
            UnRepeatToast.showToast(this, "两次密码不一致");
            return false;
        }

        //校验成功后，保存填写的用户名和密码和验证码
        userReg = new UserReg(regPhoneText, pswText, checkCodeText);
        return true;
    }
}
