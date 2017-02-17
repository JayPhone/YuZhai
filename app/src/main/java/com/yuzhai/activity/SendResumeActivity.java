package com.yuzhai.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuzhai.bean.requestBean.SendResumeBean;
import com.yuzhai.bean.responseBean.DetailResumeBean;
import com.yuzhai.bean.responseBean.SendResumeRespBean;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.IPConfig;
import com.yuzhai.http.ParamsGenerateUtil;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/10.
 */

public class SendResumeActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private TextView mIsSendResumeText;
    private TextInputLayout mNameLayout;
    private TextInputEditText mNameEdit;
    private Spinner mSexSpinner;
    private Spinner mTypeSpinner;
    private Spinner mEducationSpinner;
    private TextInputLayout mTelLayout;
    private TextInputEditText mTelEdit;
    private TextInputEditText mEducationEdit;
    private TextInputEditText mSkillEdit;
    private TextInputEditText mWorkExperienceEdit;
    private TextInputEditText mSelfEvaluationEdit;
    private Button mSendResumeBtn;

    private CustomApplication mCustomApplication;
    private RequestQueue mRequestQueue;
    private static final String DATA = "data";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_resume);
        mCustomApplication = (CustomApplication) getApplication();
        mRequestQueue = RequestQueueSingleton.getRequestQueue(this);

        initViews();
        initData();
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.resume_toolbar);
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.setTitle("简历投放");

        mIsSendResumeText = (TextView) findViewById(R.id.send_resume_finish);
        mNameLayout = (TextInputLayout) findViewById(R.id.name_layout);
        mNameEdit = (TextInputEditText) findViewById(R.id.name);
        mSexSpinner = (Spinner) findViewById(R.id.sex_spinner);
        mTypeSpinner = (Spinner) findViewById(R.id.type_spinner);
        mEducationSpinner = (Spinner) findViewById(R.id.education_spinner);
        mTelLayout = (TextInputLayout) findViewById(R.id.tel_layout);
        mTelEdit = (TextInputEditText) findViewById(R.id.tel);
        mEducationEdit = (TextInputEditText) findViewById(R.id.educational);
        mSkillEdit = (TextInputEditText) findViewById(R.id.skill);
        mWorkExperienceEdit = (TextInputEditText) findViewById(R.id.work);
        mSelfEvaluationEdit = (TextInputEditText) findViewById(R.id.evaluation);
        mSendResumeBtn = (Button) findViewById(R.id.send_resume_btn);
        mSendResumeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_resume_btn:
                String sex = mSexSpinner.getSelectedItem().toString();
                String type = mTypeSpinner.getSelectedItem().toString();
                String education = mEducationSpinner.getSelectedItem().toString();

                SendResumeBean sendResumeBean = new SendResumeBean();
                sendResumeBean.setName(mNameEdit.getEditableText().toString());
                sendResumeBean.setSex(sex.substring(6, sex.length() - 1));
                sendResumeBean.setType(type.substring(6, type.length() - 1));
                sendResumeBean.setEducation(education.substring(6, education.length() - 1));
                sendResumeBean.setTel(mTelEdit.getEditableText().toString());
                sendResumeBean.setEducationalExperience(mEducationEdit.getEditableText().toString());
                sendResumeBean.setSkill(mSkillEdit.getEditableText().toString());
                sendResumeBean.setWorkExperience(mWorkExperienceEdit.getEditableText().toString());
                sendResumeBean.setSelfEvaluation(mSelfEvaluationEdit.getEditableText().toString());
                Log.i("data", sendResumeBean.toString());
                sendSendResumeRequest(sendResumeBean);
                break;
        }
    }

    private void initData() {
        String[] sexTexts = new String[]{"请选择您的性别", "男性", "女性"};
        String[] typeTexts = new String[]{"请选择投放板块", "软件IT", "音乐制作", "平面设计", "视频拍摄", "游戏研发", "文案撰写", "金融会计"};
        String[] educationTexts = new String[]{"请选择您的学历", "初中", "高中", "专科", "本科", "硕士", "博士"};

        mSexSpinner.setAdapter(generateAdapter(generateData(sexTexts), R.layout.resume_sex_spinner_item, R.id.sex_item));
        mTypeSpinner.setAdapter(generateAdapter(generateData(typeTexts), R.layout.resume_type_spinner_layout, R.id.type_item));
        mEducationSpinner.setAdapter(generateAdapter(generateData(educationTexts), R.layout.resume_education_item_layout, R.id.education_item));

        if (CustomApplication.isConnect) {
            sendPersonalDetailResumeRequest();
        }
    }

    /**
     * 生成List集合
     *
     * @param data
     * @return
     */
    private List<Map<String, String>> generateData(String[] data) {
        List<Map<String, String>> dataList = new ArrayList<>();
        Map<String, String> dataMap;

        for (int i = 0; i < data.length; i++) {
            dataMap = new HashMap<>();
            dataMap.put(DATA, data[i]);
            dataList.add(dataMap);
        }
        return dataList;
    }

    /**
     * 生成spinner的适配器
     *
     * @param dataList
     * @param layoutId
     * @param itemId
     * @return
     */
    private SimpleAdapter generateAdapter(List<Map<String, String>> dataList, int layoutId, int itemId) {
        return new SimpleAdapter(
                this,
                dataList,
                layoutId,
                new String[]{DATA},
                new int[]{itemId}
        );
    }

    public void sendSendResumeRequest(SendResumeBean sendResumeBean) {
        if (checkData(sendResumeBean)) {
            //生成投递简历请求的参数
            Map<String, String> param = ParamsGenerateUtil.generateSendResumeParam(sendResumeBean);
            Log.i("param", param.toString());

            //生成投递简历请求
            CommonRequest sendResumeRequest = new CommonRequest(this,
                    IPConfig.sendResumeAddress,
                    mCustomApplication.generateHeaderMap(),
                    param,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String resp) {
                            Log.i("send_resume_resp", resp);
                            if (JsonUtil.decodeByGson(resp, SendResumeRespBean.class).getCode().equals("1")) {
                                UnRepeatToast.showToast(SendResumeActivity.this, "简历投递成功");
                                clearAll();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            UnRepeatToast.showToast(SendResumeActivity.this, "服务器不务正业中");
                        }
                    });

            mRequestQueue.add(sendResumeRequest);
        }
    }

    /**
     * 发送查看详细简历请求
     */
    public void sendPersonalDetailResumeRequest() {
        //生成投递简历请求的参数
//        Map<String, String> param = ParamsGenerateUtil.generatePersonalResumeParams();
//        Log.i("param", param.toString());

        //生成查看详细简历请求
        CommonRequest sendPersonalDetailResumeRequest = new CommonRequest(this,
                IPConfig.personalResumeAddress,
                mCustomApplication.generateHeaderMap(),
                null,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resp) {
                        Log.i("personal_resume_resp", resp);
                        if (JsonUtil.decodeByJsonObject(resp, "detail_resume").equals("0")) {
                            mIsSendResumeText.setVisibility(View.GONE);
                        } else {
                            updateData(JsonUtil.decodeByGson(resp, DetailResumeBean.class).getDetailResume());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        UnRepeatToast.showToast(SendResumeActivity.this, "服务器不务正业中");
                    }
                });

        mRequestQueue.add(sendPersonalDetailResumeRequest);
    }

    private void clearAll() {
        mNameEdit.getText().clear();
        mSexSpinner.setSelection(0);
        mTypeSpinner.setSelection(0);
        mEducationSpinner.setSelection(0);
        mTelEdit.getText().clear();
        mEducationEdit.getText().clear();
        mSkillEdit.getText().clear();
        mWorkExperienceEdit.getText().clear();
        mSelfEvaluationEdit.getText().clear();
    }

    private void updateData(DetailResumeBean.ResumeBean resumeBean) {
        mSendResumeBtn.setText("修改简历");
        mNameEdit.setText(resumeBean.getName());
        mTelEdit.setText(resumeBean.getContactNumber());
        mEducationEdit.setText(resumeBean.getEducationExperience());
        mSkillEdit.setText(resumeBean.getSkill());
        mWorkExperienceEdit.setText(resumeBean.getWorkExperience());
        mSelfEvaluationEdit.setText(resumeBean.getSelfEvaluation());

        for (int i = 0; i < mSexSpinner.getAdapter().getCount(); i++) {
            String item = mSexSpinner.getAdapter().getItem(i).toString();
            if (item.substring(6, item.length() - 1).equals(resumeBean.getSex())) {
                mSexSpinner.setSelection(i);
            }
        }

        for (int i = 0; i < mTypeSpinner.getAdapter().getCount(); i++) {
            String item = mTypeSpinner.getAdapter().getItem(i).toString();
            if (item.substring(6, item.length() - 1).equals(resumeBean.getModule())) {
                mTypeSpinner.setSelection(i);
            }
        }

        for (int i = 0; i < mEducationSpinner.getAdapter().getCount(); i++) {
            String item = mEducationSpinner.getAdapter().getItem(i).toString();
            if (item.substring(6, item.length() - 1).equals(resumeBean.getEducation())) {
                mEducationSpinner.setSelection(i);
            }
        }
    }

    /**
     * 校验数据:
     * 如果填写的数据其中一项或全部不正确，返回false;
     * 如果填写的数据全部都正确，返回true.
     */
    private boolean checkData(SendResumeBean sendResumeBean) {
        if (sendResumeBean.getName().equals("")) {
            UnRepeatToast.showToast(this, "姓名不能为空");
            return false;
        }

        if (sendResumeBean.getSex().equals("请选择您的性别")) {
            UnRepeatToast.showToast(this, "请选择您的性别");
            return false;
        }

        if (sendResumeBean.getType().equals("请选择投放板块")) {
            UnRepeatToast.showToast(this, "请选择投放板块");
            return false;
        }

        if (sendResumeBean.getEducation().equals("请选择您的学历")) {
            UnRepeatToast.showToast(this, "请选择您的学历");
            return false;
        }

        if (sendResumeBean.getTel().equals("")) {
            UnRepeatToast.showToast(this, "手机号码不能为空");
            return false;
        }

        if (sendResumeBean.getTel().length() != 11) {
            UnRepeatToast.showToast(this, "手机号码长度应为11位");
            return false;
        }

        if (sendResumeBean.getEducationalExperience().equals("")) {
            UnRepeatToast.showToast(this, "教育经历不能为空");
            return false;
        }

        if (sendResumeBean.getSkill().equals("")) {
            UnRepeatToast.showToast(this, "专业技能不能为空");
            return false;
        }

        if (sendResumeBean.getWorkExperience().equals("")) {
            UnRepeatToast.showToast(this, "工作经验不能为空");
            return false;
        }

        if (sendResumeBean.getSelfEvaluation().equals("")) {
            UnRepeatToast.showToast(this, "自我评价不能为空");
            return false;
        }

        return true;
    }
}
