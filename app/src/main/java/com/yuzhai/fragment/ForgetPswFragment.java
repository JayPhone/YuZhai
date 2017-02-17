package com.yuzhai.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuzhai.bean.requestBean.UserLoginBean;
import com.yuzhai.bean.requestBean.UserPhoneBean;
import com.yuzhai.bean.responseBean.ForgetPswRespBean;
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

public class ForgetPswFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ForgetPswFragment";
    private EditText mUserPhoneEdit;
    private EditText mPswEdit;
    private EditText mCfmPswEdit;
    private EditText mCheckCodeEdit;
    private Button mCheckCodeButton;
    private Button mAlterBtn;
    private Button mLoginTextView;

    private RequestQueue mRequestQueue;
    private UserLoginBean mUserBean;
    private UserPhoneBean mUserPhoneBean;

    private OnLoginClickListener mOnLoginClickListener;

    public static ForgetPswFragment newInstance() {
        Bundle args = new Bundle();
        ForgetPswFragment fragment = new ForgetPswFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnLoginClickListener(OnLoginClickListener onLoginClickListener) {
        mOnLoginClickListener = onLoginClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forget_psw, container, false);
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

        //获取请求队列
        mRequestQueue = RequestQueueSingleton.getRequestQueue(getActivity());
        //初始化组件
        initViews();
    }

    /**
     * //初始化短信接口
     */
    private void initBmobSMS() {
        BmobSMS.initialize(getActivity(), CustomApplication.BOMB_APP_ID);
    }

    public void initViews() {
        mUserPhoneEdit = (EditText) getView().findViewById(R.id.user_name);
        mCheckCodeEdit = (EditText) getView().findViewById(R.id.verify_code);
        mCheckCodeButton = (Button) getView().findViewById(R.id.verify_button);
        mPswEdit = (EditText) getView().findViewById(R.id.password);
        mCfmPswEdit = (EditText) getView().findViewById(R.id.confirm_password);
        mAlterBtn = (Button) getView().findViewById(R.id.change_button);
        mLoginTextView = (Button) getView().findViewById(R.id.login_nav);

        //设置点击监听器
        mCheckCodeButton.setOnClickListener(this);
        mAlterBtn.setOnClickListener(this);
        mLoginTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击获取验证码按钮
            case R.id.verify_button:
                //发送获取验证码请求
                sendVerifyRequest(mUserPhoneEdit.getText().toString());
                break;

            //点击修改密码按钮
            case R.id.change_button:
                //发送验证验证码请求
                sendVerifySmsCodeRequest(mUserPhoneEdit.getText().toString(),
                        mCheckCodeEdit.getText().toString());
                break;

            //点击登录导航文本
            case R.id.login_nav:
                if (mOnLoginClickListener != null) {
                    mOnLoginClickListener.onLoginNavClick();
                }
                break;
        }
    }

    /**
     * 发送获取验证码请求
     *
     * @param forgetPswPhone 获取验证码的号码
     */
    private void sendVerifyRequest(final String forgetPswPhone) {
        //校验忘记密码号码是否合法
        if (checkForgetPswPhone(forgetPswPhone)) {
            //发送获取短信验证码请求
            BmobSMS.requestSMSCode(getActivity(), forgetPswPhone, "御宅工作", new RequestSMSCodeListener() {
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
     * @param forgetPswPhone 注册手机号码
     * @param verifyCode     验证码
     */
    private void sendVerifySmsCodeRequest(String forgetPswPhone, String verifyCode) {
        //检验注册号码和验证码是否合法
        if (checkForgetPswPhone(forgetPswPhone) && checkVerifyCode(verifyCode)) {
            //发送验证验证码请求
            BmobSMS.verifySmsCode(getActivity(), forgetPswPhone, verifyCode, new VerifySMSCodeListener() {
                @Override
                public void done(BmobException ex) {
                    if (ex == null) {//短信验证码已验证成功
                        Log.i(TAG, "验证通过");
                        //发送忘记密码请求
                        sendForgetPswRequest(mUserPhoneEdit.getText().toString(),
                                mPswEdit.getText().toString(),
                                mCfmPswEdit.getText().toString());
                    } else {
                        Log.i(TAG, "验证失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
                        UnRepeatToast.showToast(getActivity(), "验证码错误，请重新输入");
                    }
                }
            });
        }
    }

    /**
     * 发送忘记密码请求
     *
     * @param userPhone 填写的忘记密码号码
     * @param psw       填写的密码
     * @param cfmPsw    重复密码
     */
    public void sendForgetPswRequest(String userPhone,
                                     String psw,
                                     String cfmPsw) {
        if (checkForgetPswData(userPhone,
                psw,
                cfmPsw)) {

            //生成忘记密码请求参数
            Map<String, String> params = ParamsGenerateUtil.generateForgetPswParams(mUserBean.getUserPhone(),
                    mUserBean.getUserPsw());

            //创建忘记密码请求
            CommonRequest forgetPswRequest = new CommonRequest(getActivity(),
                    IPConfig.forgetPswAddress,
                    null,
                    params,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String resp) {
                            Log.i("forget_resp", resp);
                            ForgetPswRespBean forgetPswRespBean = JsonUtil.decodeByGson(resp, ForgetPswRespBean.class);
                            String respCode = forgetPswRespBean.getCode();
                            Log.i("forget_resp_code", respCode);

                            if (respCode != null && respCode.equals("1")) {
                                UnRepeatToast.showToast(getActivity(), "修改成功");
                                getActivity().finish();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            UnRepeatToast.showToast(getActivity(), "服务器不务正业中");
                        }
                    });

            //添加忘记密码请求到请求队列
            mRequestQueue.add(forgetPswRequest);
        }
    }

    /**
     * 用于校验获取验证码操作的数据
     *
     * @param phoneNumber 获取验证码的手机号码
     * @return 如果填写的数据其中一项或全部不正确，返回false，否则返回true
     */
    public Boolean checkForgetPswPhone(String phoneNumber) {
        if (phoneNumber.equals("")) {
            UnRepeatToast.showToast(getActivity(), "手机号码不能为空");
            return false;
        }

        if (phoneNumber.length() != 11) {
            UnRepeatToast.showToast(getActivity(), "手机号码长度应为11位");
            return false;
        }

        //校验成功后，保存手机号
        mUserPhoneBean = new UserPhoneBean(phoneNumber);
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
     * 用于校验忘记密码操作的数据
     *
     * @param userPhoneText 填写的手机号码
     * @param pswText       填写的新密码
     * @param cfmPswText    重复新密码
     * @return 如果填写的数据其中一项或全部不正确，返回false，否则返回true
     */
    public boolean checkForgetPswData(String userPhoneText,
                                      String pswText,
                                      String cfmPswText) {

        //校验手机号码
        if (!checkForgetPswPhone(userPhoneText)) {
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

        //校验成功后保存填写的用户名和密码和验证码
        mUserBean = new UserLoginBean(userPhoneText, pswText);
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

    //点击登陆导航时回调
    public interface OnLoginClickListener {
        void onLoginNavClick();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i(TAG, "UserVisible:" + isVisibleToUser);
    }

}
