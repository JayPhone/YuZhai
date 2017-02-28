package com.yuzhai.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
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
import com.xiaomi.mipush.sdk.MiPushClient;
import com.yuzhai.bean.innerBean.BaseUserInfoBean;
import com.yuzhai.bean.requestBean.UserLoginBean;
import com.yuzhai.bean.responseBean.LoginRespBean;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.IPConfig;
import com.yuzhai.http.ParamsGenerateUtil;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by 35429 on 2017/2/16.
 */

public class LoginFragment extends Fragment implements View.OnClickListener,
        Response.Listener<String> {
    private static final String TAG = "LoginFragment";

    private TextInputLayout userPhoneLayout;
    private TextInputEditText userPhoneEdit;
    private TextInputLayout pswLayout;
    private TextInputEditText pswEdit;
    private Button loginButton;
    private Button registerButton;
    private Button forgetPswButton;
    private CustomApplication customApplication;
    private RequestQueue requestQueue;
    private CommonRequest loginRequest;
    //记录登录信息的实体
    private UserLoginBean userLoginBean;
    private OnRegisterClickListener mOnRegisterClickListener;
    private OnForgetPswClickListener mOnForgetPswClickListener;

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnRegisterClickListener(OnRegisterClickListener onRegisterClickListener) {
        mOnRegisterClickListener = onRegisterClickListener;
    }

    public void setOnForgetPswClickListener(OnForgetPswClickListener onForgetPswClickListener) {
        mOnForgetPswClickListener = onForgetPswClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化控件
        initViews();
        //获取全局的Applicant对象
        customApplication = (CustomApplication) getActivity().getApplication();
        //获取请求队列
        requestQueue = RequestQueueSingleton.getRequestQueue(getActivity());
    }

    /**
     * 初始化控件实例，并设置相应的点击事件
     */
    public void initViews() {
        userPhoneLayout = (TextInputLayout) getView().findViewById(R.id.user_name_layout);
        userPhoneEdit = (TextInputEditText) getView().findViewById(R.id.user_name);
        pswLayout = (TextInputLayout) getView().findViewById(R.id.psw_layout);
        pswEdit = (TextInputEditText) getView().findViewById(R.id.new_psw);
        loginButton = (Button) getView().findViewById(R.id.login_btn);
        registerButton = (Button) getView().findViewById(R.id.register_nav);
        forgetPswButton = (Button) getView().findViewById(R.id.forget_pswd_nav);

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
        forgetPswButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //点击登陆按钮
            case R.id.login_btn:
                //发送登录请求
                sendLoginRequest(userPhoneEdit.getText().toString(),
                        pswEdit.getText().toString(),
                        MiPushClient.getRegId(getActivity()));
                break;

            //点击注册导航文字
            case R.id.register_nav:
                if (mOnRegisterClickListener != null) {
                    mOnRegisterClickListener.onRegisterClick();
                }
                break;

            //点击忘记密码导航文字
            case R.id.forget_pswd_nav:
                if (mOnForgetPswClickListener != null) {
                    mOnForgetPswClickListener.onForgetPswClick();
                }
                break;
        }
    }

    /**
     * 发送登录请求
     *
     * @param userPhone 用户名
     * @param psw       密码
     */
    public void sendLoginRequest(String userPhone, String psw, String regId) {
        if (checkData(userPhone, psw)) {
            //生成登录请求参数
            Map<String, String> params = ParamsGenerateUtil.generateLoginParams(
                    userLoginBean.getUserPhone(),
                    userLoginBean.getUserPsw(),
                    regId);
            Log.i(TAG, "login_url:" + IPConfig.loginAddress);
            Log.i(TAG, "login_param:" + params.toString());

            //创建登录请求
            loginRequest = new CommonRequest(getActivity(),
                    IPConfig.loginAddress,
                    null,
                    params,
                    LoginFragment.this,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            UnRepeatToast.showToast(getActivity(), "服务器不务正业中");
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
            UnRepeatToast.showToast(getActivity(), "手机号码不能为空");
            return false;
        }

        if (userPhoneText.length() != 11) {
            UnRepeatToast.showToast(getActivity(), "手机号码长度应为11位");
            return false;
        }

        if (userPswText.equals("")) {
            UnRepeatToast.showToast(getActivity(), "密码不能为空");
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
        Log.i(TAG, "login_resp:" + resp);
        LoginRespBean loginRespBean = JsonUtil.decodeByGson(resp, LoginRespBean.class);

        //返回的响应码
        String respCode = loginRespBean.getCode();
        Log.i(TAG, "login_resp_code:" + loginRespBean.getCode());

        if (respCode != null && respCode.equals("-1")) {
            UnRepeatToast.showToast(getActivity(), "密码错误");
        }

        if (respCode != null && respCode.equals("0")) {
            UnRepeatToast.showToast(getActivity(), "账号不存在");
        }

        if (respCode != null && respCode.equals("1")) {
            //用户头像路径
            String userHead = loginRespBean.getUser_head_url();
            Log.i(TAG, "login_resp_userHead:" + loginRespBean.getUser_head_url());
            //用户名
            String userName = loginRespBean.getUser_name();
            Log.i(TAG, "login_resp_userName:" + loginRespBean.getUser_name());
            //保存登陆成功的手机号和密码
            customApplication.addUserInfo(userLoginBean.getUserPhone(), userLoginBean.getUserPsw());
            //设置为登录状态
            customApplication.setLoginState(true);
            //即时聊天连接
            BmobIM.connect(customApplication.getUserPhone(), new ConnectListener() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        Log.i(TAG, "连接成功");
                        //监听连接状态
                        BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
                            @Override
                            public void onChange(ConnectionStatus status) {
                                Log.i(TAG, "连接状态:" + status.getMsg());
                            }
                        });
                    } else {
                        Log.e(TAG, e.getErrorCode() + "/" + e.getMessage());
                    }
                }
            });
            //使用EventBus发送替换为登录的界面的消息到MainActivity
            EventBus.getDefault().post(new BaseUserInfoBean(userHead, userName, customApplication.isLogin()));
            getActivity().finish();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i(TAG, "UserVisible:" + isVisibleToUser);
    }

    //点击注册按钮时回调
    public interface OnRegisterClickListener {
        void onRegisterClick();
    }

    //点击忘记密码导航时回掉
    public interface OnForgetPswClickListener {
        void onForgetPswClick();
    }
}
