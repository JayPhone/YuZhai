package com.yuzhai.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuzhai.config.IPConfig;
import com.yuzhai.entry.requestBean.UserForgetPsw;
import com.yuzhai.entry.requestBean.UserPhone;
import com.yuzhai.entry.responseBean.ForgetPswRespBean;
import com.yuzhai.entry.responseBean.VerifyRespBean;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.ParamsGenerateUtil;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import java.util.Map;

/**
 * Created by Administrator on 2016/7/4.
 */
public class ForgetPswActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mBackImage;
    private EditText mUserPhoneEdit;
    private EditText mPswEdit;
    private EditText mCfmPswEdit;
    private EditText mCheckCodeEdit;
    private Button mCheckCodeButton;
    private Button mAlterBtn;

    private RequestQueue mRequestQueue;
    private UserForgetPsw mUserForget;
    private UserPhone mUserPhone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_psw);
        //获取请求队列
        mRequestQueue = RequestQueueSingleton.getInstance(this).getRequestQueue();
        //初始化组件
        initViews();
    }

    public void initViews() {
        //获取组件
        mBackImage = (ImageView) findViewById(R.id.back_image);
        mUserPhoneEdit = (EditText) findViewById(R.id.phone_num);
        mCheckCodeEdit = (EditText) findViewById(R.id.code_field);
        mCheckCodeButton = (Button) findViewById(R.id.code_button);
        mPswEdit = (EditText) findViewById(R.id.password);
        mCfmPswEdit = (EditText) findViewById(R.id.confirm_password);
        mAlterBtn = (Button) findViewById(R.id.change_button);

        //设置点击监听器
        mBackImage.setOnClickListener(this);
        mCheckCodeButton.setOnClickListener(this);
        mAlterBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击返回按钮
            case R.id.back_image:
                finish();
                break;

            //点击获取验证码按钮
            case R.id.code_button:
                //发送获取验证码请求
                sendVerifyRequest(mUserPhoneEdit.getText().toString());
                break;

            //点击修改密码按钮
            case R.id.change_button:
                //发送忘记密码请求
                sendForgetPswRequest(mUserPhoneEdit.getText().toString(),
                        mCheckCodeEdit.getText().toString(),
                        mPswEdit.getText().toString(),
                        mCfmPswEdit.getText().toString());
                break;
        }
    }

    /**
     * 发送获取验证码请求
     */
    public void sendVerifyRequest(String forgetPswPhone) {
        if (checkForgetPswPhone(forgetPswPhone)) {
            //生成获取验证码请求参数
            Map<String, String> params = ParamsGenerateUtil.generateVerifyParams(mUserPhone.getUserPhone());

            //创建获取验证码请求
            CommonRequest verifyRequest = new CommonRequest(IPConfig.verifyAddress,
                    null,
                    params,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String resp) {
                            Log.i("verify_resp", resp);
                            VerifyRespBean verifyRespBean = JsonUtil.decodeByGson(resp, VerifyRespBean.class);
                            String respCode = verifyRespBean.getCode();
                            Log.i("verify_resp_code", respCode);

                            if (respCode != null && respCode.equals("1")) {
                                UnRepeatToast.showToast(ForgetPswActivity.this, "验证码发射成功,请注意捕获");
                            }

                            if (respCode != null && respCode.equals("-1")) {
                                UnRepeatToast.showToast(ForgetPswActivity.this, "验证码发射失败,请稍后再来");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            UnRepeatToast.showToast(ForgetPswActivity.this, "服务器不务正业中");
                        }
                    });

            //添加获取验证码请求到请求队列
            mRequestQueue.add(verifyRequest);
        }
    }

    /**
     * 发送忘记密码请求
     *
     * @param userPhone 填写的忘记密码号码
     * @param checkCode 填写的验证码
     * @param psw       填写的密码
     * @param cfmPsw    重复密码
     */
    public void sendForgetPswRequest(String userPhone,
                                     String checkCode,
                                     String psw,
                                     String cfmPsw) {
        if (checkForgetPswData(userPhone,
                checkCode,
                psw,
                cfmPsw)) {

            //生成忘记密码请求参数
            Map<String, String> params = ParamsGenerateUtil.generateForgetPswParams(mUserForget.getUserPhone(),
                    mUserForget.getTemVerify(),
                    mUserForget.getUserPsw());

            //创建忘记密码请求
            CommonRequest forgetPswRequest = new CommonRequest(IPConfig.forgetPswAddress,
                    null,
                    params,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String resp) {
                            Log.i("forget_resp", resp);
                            ForgetPswRespBean forgetPswRespBean = JsonUtil.decodeByGson(resp, ForgetPswRespBean.class);
                            String respCode = forgetPswRespBean.getCode();
                            Log.i("forget_resp_code", respCode);

                            if (respCode != null && respCode.equals("0")) {
                                UnRepeatToast.showToast(ForgetPswActivity.this, "验证码错误");
                            }

                            if (respCode != null && respCode.equals("1")) {
                                UnRepeatToast.showToast(ForgetPswActivity.this, "修改成功");
                                finish();
                            }

                            if (respCode != null && respCode.equals("2")) {
                                UnRepeatToast.showToast(ForgetPswActivity.this, "验证码已过期，请重新获取");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            UnRepeatToast.showToast(ForgetPswActivity.this, "服务器不务正业中");
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
            UnRepeatToast.showToast(this, "手机号码不能为空");
            return false;
        }

        if (phoneNumber.length() != 11) {
            UnRepeatToast.showToast(this, "手机号码长度应为11位");
            return false;
        }

        //校验成功后，保存手机号
        mUserPhone = new UserPhone(phoneNumber);
        return true;
    }

    /**
     * 用于校验忘记密码操作的数据
     *
     * @param userPhoneText 填写的手机号码
     * @param checkCodeText 填写的验证码
     * @param pswText       填写的新密码
     * @param cfmPswText    重复新密码
     * @return 如果填写的数据其中一项或全部不正确，返回false，否则返回true
     */
    public boolean checkForgetPswData(String userPhoneText,
                                      String checkCodeText,
                                      String pswText,
                                      String cfmPswText) {

        //校验手机号码
        if (!checkForgetPswPhone(userPhoneText)) {
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
        mUserForget = new UserForgetPsw(userPhoneText, checkCodeText, pswText);
        return true;
    }
}
