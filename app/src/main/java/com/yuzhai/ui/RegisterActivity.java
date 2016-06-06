package com.yuzhai.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yuzhai.config.IPConfig;
import com.yuzhai.dao.JsonUtil;
import com.yuzhai.entry.UserPhone;
import com.yuzhai.entry.UserReg;
import com.yuzhai.http.HttpUtil;
import com.yuzhai.util.CheckData;
import com.yuzhai.util.InputStreamToString;
import com.yuzhai.yuzhaiwork.R;

import java.io.InputStream;

/**
 * Created by Administrator on 2016/5/23.
 */
public class RegisterActivity extends AppCompatActivity {
    //组件引用
    private EditText phoneNumEdit;
    private EditText passwordEdit;
    private EditText confirmPswdEdit;
    private EditText codeEdit;
    private Button codeButton;
    private Button registerButton;
    private TextView loginTextView;

    //其他引用
    private String paramPhone = null;
    private String params = null;
    private InputStream returnStream = null;
    private String jsonString;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Toast.makeText(RegisterActivity.this, "获取验证码成功，请注意查收", Toast.LENGTH_SHORT).show();
            } else if (msg.what == -1) {
                Toast.makeText(RegisterActivity.this, "获取验证码失败", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 0) {
                Toast.makeText(RegisterActivity.this, "网络出现异常", Toast.LENGTH_SHORT).show();
            } else if (msg.what == -21) {
                Toast.makeText(RegisterActivity.this, "注册失败，请稍后再试", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 21) {
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //加载界面的各个组件
        inflate_phoneNum_EditText();
        inflate_code_EditText();
        inflate_password_EditText();
        inflate_confirmPswd_EditText();
        inflate_code_button(RegisterActivity.this);
        inflate_register_button(RegisterActivity.this);
        inflate_buttom_textview();
    }

    public void inflate_code_EditText() {
        codeEdit = (EditText) findViewById(R.id.code_field);
    }

    public void inflate_phoneNum_EditText() {
        phoneNumEdit = (EditText) findViewById(R.id.phone_num);
    }

    public void inflate_password_EditText() {
        passwordEdit = (EditText) findViewById(R.id.password);
    }

    public void inflate_confirmPswd_EditText() {
        confirmPswdEdit = (EditText) findViewById(R.id.affirmPsw);
    }

    public void inflate_code_button(final Context context) {
        codeButton = (Button) findViewById(R.id.code_button);
        codeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //验证手机号码合法性
                paramPhone = checkPhoneNUm(context);
                if (paramPhone != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            returnStream = HttpUtil.doPost(IPConfig.verifyAddress, 5000, paramPhone);
                            if (returnStream != null) {
                                //将返回的输入流解析成jsonString
                                jsonString = InputStreamToString.praseToString(returnStream);
                                //获取返回的状态码
                                String code = JsonUtil.decodeJson(jsonString, "code");
                                if (code.equals("-1")) {
                                    handler.sendEmptyMessage(-1);
                                } else {
                                    handler.sendEmptyMessage(1);
                                }
                            } else {
                                handler.sendEmptyMessage(0);
                            }
                        }
                    }).start();
                }
            }
        });
    }

    public void inflate_register_button(final Context context) {
        registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params = checkData(context);
                if (params != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            returnStream = HttpUtil.doPost(IPConfig.registerAddress, 5000, params);
                            if (returnStream != null) {
                                jsonString = InputStreamToString.praseToString(returnStream);
                                String code = JsonUtil.decodeJson(jsonString, "code");
                                if (code.equals(-1)) {
                                    handler.sendEmptyMessage(-21);
                                } else if (code.equals(1)) {
                                    handler.sendEmptyMessage(21);
                                }
                            } else {
                                handler.sendEmptyMessage(0);
                            }
                        }
                    }).start();
                }
            }
        });
    }

    //校验数据
    public String checkPhoneNUm(Context context) {
        //获取手机号码
        String userName_str = phoneNumEdit.getText().toString();
        if (userName_str.equals("")) {
            Toast.makeText(context, "手机号码不能为空", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (userName_str.length() < 11) {
            Toast.makeText(context, "手机号码长度应为11位", Toast.LENGTH_SHORT).show();
            return null;
        }

        //获取手机验证码的手机号，生成Json数据
        UserPhone userPhone = new UserPhone(userName_str);
        String jsonString = JsonUtil.gsonToString(userPhone);
        return jsonString;
    }

    //校验数据
    public String checkData(Context context) {
        //获取账号和密码
        String userName_str = phoneNumEdit.getText().toString();
        String identCode_str = codeEdit.getText().toString();
        String pswd_str = passwordEdit.getText().toString();
        String confirmPswd_str = confirmPswdEdit.getText().toString();
        //校验手机号码
        if (CheckData.isEmpty(userName_str)) {
            Toast.makeText(context, "手机号码不能为空", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (!CheckData.matchLength(userName_str, 11)) {
            Toast.makeText(context, "手机号码格式错误", Toast.LENGTH_SHORT).show();
            return null;
        }

        //校验验证码
        if (CheckData.isEmpty(identCode_str)) {
            Toast.makeText(context, "验证码不能为空", Toast.LENGTH_SHORT).show();
        }

        if (!CheckData.matchLength(identCode_str, 6)) {
            Toast.makeText(context, "验证码格式错误", Toast.LENGTH_SHORT).show();
            return null;
        }

        //校验密码
        if (CheckData.isEmpty(pswd_str)) {
            Toast.makeText(context, "密码不能为空", Toast.LENGTH_SHORT).show();
            return null;
        }

        if (CheckData.lessLength(pswd_str, 6)) {
            Toast.makeText(context, "密码长度应为6-16位", Toast.LENGTH_SHORT).show();
            return null;
        }

        //校验第二次密码
        if (!CheckData.matchString(pswd_str, confirmPswd_str)) {
            Toast.makeText(context, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return null;
        }

        UserReg userReg = new UserReg(userName_str, identCode_str, pswd_str);
        String jsonString = JsonUtil.gsonToString(userReg);
        return jsonString;
    }

    public void inflate_buttom_textview() {
        loginTextView = (TextView) findViewById(R.id.register_login);
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
