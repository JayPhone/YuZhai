package com.yuzhai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuzhai.bean.innerBean.BaseUserInfoBean;
import com.yuzhai.bean.requestBean.UserAlterPswBean;
import com.yuzhai.bean.responseBean.AlterPswRespBean;
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

public class AlterPswActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mAlterPswToolbar;
    private EditText mOldPswEdit;
    private EditText mNewPswEdit;
    private EditText mCfmPswEdit;
    private Button mAlterBtn;

    private CustomApplication customApplication;
    private RequestQueue requestQueue;
    private UserAlterPswBean userAlterPswBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_psw);
        customApplication = (CustomApplication) getApplication();
        requestQueue = RequestQueueSingleton.getRequestQueue(this);
        initViews();
    }

    public void initViews() {
        mAlterPswToolbar = (Toolbar) findViewById(R.id.alter_psw_toolbar);
        mAlterPswToolbar.setNavigationIcon(R.drawable.back);
        mAlterPswToolbar.setTitle("修改密码");
        mAlterPswToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAlterBtn = (Button) findViewById(R.id.change_button);
        mOldPswEdit = (EditText) findViewById(R.id.password);
        mNewPswEdit = (EditText) findViewById(R.id.new_password);
        mCfmPswEdit = (EditText) findViewById(R.id.confirm_password);

        mAlterBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_button:
                //发送修改密码请求
                sendAlterPswRequest(mOldPswEdit.getText().toString(),
                        mNewPswEdit.getText().toString(),
                        mCfmPswEdit.getText().toString(),
                        customApplication.getToken());
                break;
        }
    }

    /**
     * 发送修改密码请求
     *
     * @param oldPsw 旧密码
     * @param newPsw 新密码
     * @param cfmPsw 重复密码
     */
    public void sendAlterPswRequest(String oldPsw, String newPsw, String cfmPsw, String token) {
        if (checkAlterPswData(oldPsw,
                newPsw,
                cfmPsw)) {

            //生成修改密码参数集
            Map<String, String> params = ParamsGenerateUtil.generateAlterPswParams(oldPsw,
                    newPsw, cfmPsw, token);

            //创建修改密码请求
            CommonRequest alterPswRequest = new CommonRequest(IPConfig.alterPswAddress,
                    null,
                    params,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String resp) {
                            Log.i("alter_psw_resp", resp);
                            AlterPswRespBean alterPswRespBean = JsonUtil.decodeByGson(resp, AlterPswRespBean.class);
                            String respCode = alterPswRespBean.getCode();
                            Log.i("alter_resp_code", respCode);

                            if (respCode != null && respCode.equals("1")) {
                                UnRepeatToast.showToast(AlterPswActivity.this, "密码修改成功,重新登陆");

                                //设置为没登录
                                customApplication.setLoginState(false);
                                //替换侧滑菜单界面为非登录界面
                                EventBus.getDefault().post(new BaseUserInfoBean(null, null, customApplication.isLogin()));
                                //启动主界面，由于设置了singleTask模式，上层的activity被弹出
                                Intent main = new Intent(AlterPswActivity.this, MainActivity.class);
                                startActivity(main);
                            }

                            if (respCode != null && respCode.equals("0")) {
                                UnRepeatToast.showToast(AlterPswActivity.this, "原密码错误");
                            }

                            if (respCode != null && respCode.equals("-1")) {
                                UnRepeatToast.showToast(AlterPswActivity.this, "两次密码不一致");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            UnRepeatToast.showToast(AlterPswActivity.this, "服务器不务正业中");
                        }
                    });

            //添加到请求队列
            requestQueue.add(alterPswRequest);
        }
    }

    /**
     * 用于校验修改密码操作的数据
     *
     * @param oldPsw 旧密码
     * @param newPsw 新密码
     * @param cfmPsw 重复密码
     * @return 如果填写的数据其中一项或全部不正确，返回false，否则返回true
     */
    public boolean checkAlterPswData(String oldPsw, String newPsw, String cfmPsw) {
        if (oldPsw.equals("")) {
            UnRepeatToast.showToast(this, "旧密码没有填上哦");
            return false;
        }

        if (newPsw.equals("")) {
            UnRepeatToast.showToast(this, "新密码没有填上哦");
            return false;
        }

        if (newPsw.length() < 6) {
            UnRepeatToast.showToast(this, "密码长度要大于6位");
            return false;
        }

        if (!cfmPsw.equals(newPsw)) {
            UnRepeatToast.showToast(this, "两次密码不一致");
            return false;
        }

        //校验成功后，保存旧密码，新密码
        userAlterPswBean = new UserAlterPswBean(oldPsw, newPsw);
        return true;
    }
}
