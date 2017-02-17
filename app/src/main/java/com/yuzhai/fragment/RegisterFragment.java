package com.yuzhai.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuzhai.bean.requestBean.UserLoginBean;
import com.yuzhai.bean.requestBean.UserPhoneBean;
import com.yuzhai.bean.responseBean.RegisterRespBean;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.IPConfig;
import com.yuzhai.http.ParamsGenerateUtil;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import java.util.Map;

import cn.bmob.newsmssdk.BmobSMS;
import cn.bmob.newsmssdk.exception.BmobException;
import cn.bmob.newsmssdk.listener.RequestSMSCodeListener;
import cn.bmob.newsmssdk.listener.VerifySMSCodeListener;

/**
 * Created by 35429 on 2017/2/16.
 */

public class RegisterFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "RegisterFragment";

    private TextInputLayout regPhoneLayout;
    private TextInputLayout checkCodeLayout;
    private TextInputLayout pswLayout;
    private TextInputLayout cfmPswLayout;
    private TextInputEditText regPhoneEdit;
    private TextInputEditText checkCodeEdit;
    private TextInputEditText pswEdit;
    private TextInputEditText cfmPswEdit;
    private Button checkCodeButton;
    private Button registerButton;
    private Button loginButton;

    private UserPhoneBean userPhoneBean;
    private UserLoginBean userBean;
    private RequestQueue requestQueue;
    private CustomApplication mCustomApplication;
    private OnLoginClickListener mOnLoginClickListener;

    public static RegisterFragment newInstance() {
        Bundle args = new Bundle();
        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnLoginClickListener(RegisterFragment.OnLoginClickListener onLoginClickListener) {
        mOnLoginClickListener = onLoginClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //获取查看系统权限状态权限
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        } else {
            //初始化短信接口
            initBmobSMS();
        }

        mCustomApplication = (CustomApplication) getActivity().getApplication();
        //初始化控件
        initViews();
        //获取请求队列
        requestQueue = RequestQueueSingleton.getRequestQueue(getActivity());
    }

    /**
     * //初始化短信接口
     */
    private void initBmobSMS() {
        BmobSMS.initialize(getActivity(), CustomApplication.BOMB_APP_ID);
    }

    /**
     * 初始化控件实例，并设置相应的点击事件
     */
    public void initViews() {
        regPhoneLayout = (TextInputLayout) getView().findViewById(R.id.user_name_layout);
        checkCodeLayout = (TextInputLayout) getView().findViewById(R.id.verify_code_layout);
        pswLayout = (TextInputLayout) getView().findViewById(R.id.psw_layout);
        cfmPswLayout = (TextInputLayout) getView().findViewById(R.id.confirm_psw_layout);

        regPhoneEdit = (TextInputEditText) getView().findViewById(R.id.user_name);
        checkCodeEdit = (TextInputEditText) getView().findViewById(R.id.verify_code);
        checkCodeButton = (Button) getView().findViewById(R.id.verify_button);
        pswEdit = (TextInputEditText) getView().findViewById(R.id.password);
        cfmPswEdit = (TextInputEditText) getView().findViewById(R.id.confirm_psw);
        registerButton = (Button) getView().findViewById(R.id.register_button);
        loginButton = (Button) getView().findViewById(R.id.login_nav);

        regPhoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 11) {
                    regPhoneLayout.setError("手机号码不能小于11位");
                } else {
                    regPhoneLayout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        checkCodeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 6) {
                    checkCodeLayout.setError("验证码长度为6位");
                } else {
                    checkCodeLayout.setError("");
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

        cfmPswEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!pswEdit.getText().toString().equals(s.toString())) {
                    cfmPswLayout.setError("两次密码不相同");
                } else {
                    cfmPswLayout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        checkCodeButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //点击获取验证码按钮
            case R.id.verify_button:
                //发送获取验证码请求
                sendVerifyRequest(regPhoneEdit.getText().toString());
                break;

            //点击注册按钮
            case R.id.register_button:
                //发送校验验证码请求
                sendVerifySmsCodeRequest(regPhoneEdit.getText().toString(),
                        checkCodeEdit.getText().toString());
                break;

            //点击登录导航文本
            case R.id.login_nav:
                if (mOnLoginClickListener != null) {
                    mOnLoginClickListener.onLoginClick();
                }
                break;
        }
    }

    /**
     * 发送获取验证码请求
     *
     * @param regPhone 获取验证码的号码
     */
    private void sendVerifyRequest(final String regPhone) {
        //校验注册号码是否合法
        if (checkRegPhone(regPhone)) {
            //发送获取短信验证码请求
            BmobSMS.requestSMSCode(getActivity(), regPhone, "御宅工作", new RequestSMSCodeListener() {
                @Override
                public void done(Integer smsId, BmobException e) {
                    if (e == null) {//验证码发送成功
                        Log.i(TAG, "短信id：" + smsId);//用于查询本次短信发送详情
                    }
                }
            });
        }
    }

    /**
     * 发送验证验证码请求
     *
     * @param registerPhone 注册手机号码
     * @param verifyCode    验证码
     */
    private void sendVerifySmsCodeRequest(String registerPhone, String verifyCode) {
        //检验注册号码和验证码是否合法
        if (checkRegPhone(registerPhone) && checkVerifyCode(verifyCode)) {
            //发送验证验证码请求
            BmobSMS.verifySmsCode(getActivity(), registerPhone, verifyCode, new VerifySMSCodeListener() {
                @Override
                public void done(BmobException ex) {
                    if (ex == null) {//短信验证码已验证成功
                        Log.i(TAG, "验证通过");
                        //发送注册请求
                        sendRegisterRequest(regPhoneEdit.getText().toString(),
                                pswEdit.getText().toString(),
                                cfmPswEdit.getText().toString());
                    } else {
                        Log.i(TAG, "验证失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
                        UnRepeatToast.showToast(getActivity(), "验证码错误，请重新输入");
                    }
                }
            });
        }
    }

    /**
     * 发送注册请求
     *
     * @param regPhone 填写的注册号码
     * @param psw      填写的密码
     * @param cfmPsw   重复密码
     */
    private void sendRegisterRequest(String regPhone,
                                     String psw,
                                     String cfmPsw) {
        if (checkRegData(regPhone,
                psw,
                cfmPsw)) {

            //生成注册请求参数
            Map<String, String> params = ParamsGenerateUtil.generateRegisterParams(userBean.getUserPhone(),
                    userBean.getUserPsw());

            //创建注册请求
            CommonRequest regRequest = new CommonRequest(getActivity(),
                    IPConfig.registerAddress,
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
                                UnRepeatToast.showToast(getActivity(), "用户已存在");
                            }

                            if (respCode != null && respCode.equals("1")) {
                                Log.i("register_resp", "注册成功");
                                getActivity().finish();
                            }

                            if (respCode != null && respCode.equals("2")) {
                                UnRepeatToast.showToast(getActivity(), "验证码已过期，请重新获取");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            UnRepeatToast.showToast(getActivity(), "服务器不务正业中");
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
            UnRepeatToast.showToast(getActivity(), "手机号码不能为空");
            return false;
        }

        if (phoneNumber.length() != 11) {
            UnRepeatToast.showToast(getActivity(), "手机号码长度应为11位");
            return false;
        }

        //校验成功后，保存手机号
        userPhoneBean = new UserPhoneBean(phoneNumber);
        return true;
    }

    /**
     * 校验验证码有效性
     *
     * @param verifyCode
     * @return
     */
    private boolean checkVerifyCode(String verifyCode) {

        //校验验证码
        if (verifyCode.equals("")) {
            UnRepeatToast.showToast(getActivity(), "验证码不能为空");
            return false;
        }

        if (verifyCode.length() != 6) {
            UnRepeatToast.showToast(getActivity(), "验证码长度应为6位");
            return false;
        }
        return true;
    }

    /**
     * 用于校验注册操作的数据
     *
     * @param regPhoneText 填写的注册号码
     * @param pswText      填写的密码
     * @param cfmPswText   重复密码
     * @return 如果填写的数据其中一项或全部不正确，返回false，否则返回true
     */
    public boolean checkRegData(String regPhoneText,
                                String pswText,
                                String cfmPswText) {

        //校验手机号码
        if (!checkRegPhone(regPhoneText)) {
            return false;
        }

        //校验密码
        if (pswText.equals("")) {
            UnRepeatToast.showToast(getActivity(), "密码不能为空");
            return false;
        }

        if (pswText.length() < 6) {
            UnRepeatToast.showToast(getActivity(), "密码长度不能小于6位");
            return false;
        }

        //校验两次密码是否一致
        if (!cfmPswText.equals(pswText)) {
            UnRepeatToast.showToast(getActivity(), "两次密码不一致");
            return false;
        }

        //校验成功后，保存填写的用户名和密码
        userBean = new UserLoginBean(regPhoneText, pswText);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //初始化短信接口
                    initBmobSMS();
                } else {
                    UnRepeatToast.showToast(getActivity(), "需要开启权限才能获取验证码");
                }
                break;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i(TAG, "UserVisible:" + isVisibleToUser);
    }

    //点击登录按钮时回调
    public interface OnLoginClickListener {
        void onLoginClick();
    }
}
