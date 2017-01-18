package com.yuzhai.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.yuzhai.bean.responseBean.DetailResumeBean;
import com.yuzhai.fragment.ResumeFragment;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.IPConfig;
import com.yuzhai.http.ParamsGenerateUtil;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.view.CircleImageView;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import java.util.Map;

/**
 * Created by Administrator on 2017/1/11.
 */

public class ResumeDetailActivity extends AppCompatActivity {
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;
    private CircleImageView mUserHeader;
    private TextView mName;
    private TextView mSex;
    private TextView mEducation;
    private TextView mTel;
    private TextView mEducationalExperience;
    private TextView mSkill;
    private TextView mWorkExperience;
    private TextView mSelfEvaluation;

    private CustomApplication mCustomApplication;
    private RequestQueue mRequestQueue;
    private String mResumeId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_resume_detail);
        mCustomApplication = (CustomApplication) getApplication();
        mRequestQueue = RequestQueueSingleton.getRequestQueue(this);

        //获取简历标识号
        mResumeId = getIntent().getStringExtra(ResumeFragment.RESUME_ID);

        initViews();
        initData();
    }

    private void initViews() {
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mUserHeader = (CircleImageView) findViewById(R.id.user_header);
        mName = (TextView) findViewById(R.id.name);
        mSex = (TextView) findViewById(R.id.sex);
        mEducation = (TextView) findViewById(R.id.education);
        mTel = (TextView) findViewById(R.id.tel);
        mEducationalExperience = (TextView) findViewById(R.id.education_experience);
        mSkill = (TextView) findViewById(R.id.skill);
        mWorkExperience = (TextView) findViewById(R.id.work_experience);
        mSelfEvaluation = (TextView) findViewById(R.id.evaluation);
    }

    private void initData() {
        if (CustomApplication.isConnect) {
            sendDetailResumeRequest(mResumeId, mCustomApplication.getToken());
        }
    }

    private void updateData(DetailResumeBean.ResumeBean resumeBean) {
        mCollapsingToolbarLayout.setTitle(resumeBean.getUserName());
        mName.setText(resumeBean.getName());
        mSex.setText(resumeBean.getSex());
        mEducation.setText(resumeBean.getEducation());
        mTel.setText(resumeBean.getContactNumber());
        mEducationalExperience.setText(resumeBean.getEducationExperience());
        mSkill.setText(resumeBean.getSkill());
        mWorkExperience.setText(resumeBean.getWorkExperience());
        mSelfEvaluation.setText(resumeBean.getSelfEvaluation());
        Glide.with(this)
                .load(IPConfig.image_addressPrefix + "/" + resumeBean.getAvatar())
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(mUserHeader);
    }

    /**
     * 发送查看详细简历请求
     *
     * @param token
     */
    public void sendDetailResumeRequest(String resumeId, String token) {
        //生成投递简历请求的参数
        Map<String, String> param = ParamsGenerateUtil.generateDetailResumeParams(resumeId, token);
        Log.i("param", param.toString());

        //生成查看详细简历请求
        CommonRequest sendDetailResumeRequest = new CommonRequest(IPConfig.resumeDetailAddress,
                mCustomApplication.generateCookieMap(),
                param,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resp) {
                        Log.i("detail_resume_resp", resp);
                        updateData(JsonUtil.decodeByGson(resp, DetailResumeBean.class).getDetailResume());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        UnRepeatToast.showToast(ResumeDetailActivity.this, "服务器不务正业中");
                    }
                });

        mRequestQueue.add(sendDetailResumeRequest);
    }
}
