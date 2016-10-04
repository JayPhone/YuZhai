package com.yuzhai.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuzhai.config.IPConfig;
import com.yuzhai.entry.requestBean.UserAlterPsw;
import com.yuzhai.entry.responseBean.AlterPswRespBean;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.ParamsGenerateUtil;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import java.util.HashMap;
import java.util.Map;

public class AlterPswActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mBackImage;
    private TextView mTitleText;
    private EditText mOldPswEdit;
    private EditText mNewPswEdit;
    private EditText mCfmPswEdit;
    private Button mAlterBtn;

    private CustomApplication customApplication;
    private RequestQueue requestQueue;
    private UserAlterPsw userAlterPsw;
    private final String REPLACE_MENU_FILETER = "yzgz.broadcast.replace.fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_psw);
        customApplication = (CustomApplication) getApplication();
        requestQueue = RequestQueueSingleton.getInstance(this).getRequestQueue();
        initViews();
    }

    public void initViews() {
        mBackImage = (ImageView) findViewById(R.id.back_image);
        if (mBackImage != null) {
            mBackImage.setImageResource(R.drawable.back);
        }

        mTitleText = (TextView) findViewById(R.id.title_text);
        if (mTitleText != null) {
            mTitleText.setText("修改密码");
        }
        mAlterBtn = (Button) findViewById(R.id.change_button);
        mOldPswEdit = (EditText) findViewById(R.id.password);
        mNewPswEdit = (EditText) findViewById(R.id.new_password);
        mCfmPswEdit = (EditText) findViewById(R.id.confirm_password);

        mBackImage.setOnClickListener(this);
        mAlterBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.back_image:
                finish();
                break;

            //点击修改按钮
            case R.id.change_button:
                //发送修改密码请求
                sendAlterPswRequest(mOldPswEdit.getText().toString(),
                        mNewPswEdit.getText().toString(),
                        mCfmPswEdit.getText().toString());
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
    public void sendAlterPswRequest(String oldPsw, String newPsw, String cfmPsw) {
        if (checkAlterPswData(oldPsw,
                newPsw,
                cfmPsw)) {

            //生成修改密码参数集
            Map<String, String> params = ParamsGenerateUtil.generateAlterPswParams(userAlterPsw.getOldPswd(),
                    userAlterPsw.getNewPswd());

            //创建修改密码请求
            CommonRequest alterPswRequest = new CommonRequest(IPConfig.alterPswAddress,
                    createHeaders(),
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
                                replaceMenuUI();
                                //启动主界面，由于设置了singleTask模式，上层的activity被弹出
                                Intent main = new Intent(AlterPswActivity.this, MainActivity.class);
                                startActivity(main);
                            }

                            if (respCode != null && respCode.equals("-1")) {
                                UnRepeatToast.showToast(AlterPswActivity.this, "原密码错误");
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
     * 替换侧滑菜单的UI为未登录状态
     */
    public void replaceMenuUI() {
        Intent replaceUserMenuUI = new Intent(REPLACE_MENU_FILETER);
        sendBroadcast(replaceUserMenuUI);
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
        userAlterPsw = new UserAlterPsw(oldPsw, newPsw);
        return true;
    }

    public Map<String, String> createHeaders() {
        //设置请求参数
        Map<String, String> headers = new HashMap<>();
        headers.put("cookie", customApplication.getCookie());
        return headers;
    }
}
