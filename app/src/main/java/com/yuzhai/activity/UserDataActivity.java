package com.yuzhai.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
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
import com.yuzhai.bean.responseBean.ContactUserDataBean;
import com.yuzhai.bean.responseBean.ApplyUserDataBean;
import com.yuzhai.fragment.OrderPublishedProcessFragment;
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

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;

/**
 * Created by 35429 on 2017/2/26.
 */

public class UserDataActivity extends AppCompatActivity {
    private static final String TAG = "UserDataActivity";

    private CustomApplication mCustomApplication;
    private RequestQueue mRequestQueue;

    private Toolbar mUserDataToolbar;
    private AppBarLayout mAppBarLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private CircleImageView mUserAvatars;
    private TextView mUserName;
    private TextView mUserPhone;
    private TextView mUserRealName;
    private FloatingActionButton mContactFab;

    private ApplyUserDataBean mApplyUserDataBean;
    private BmobIMConversation mConversation;

    private String mAvatarText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mAvatarText = getIntent().getStringExtra(OrderPublishedProcessFragment.AVATAR);

        setContentView(R.layout.activity_user_data);
        mCustomApplication = (CustomApplication) getApplication();
        mRequestQueue = RequestQueueSingleton.getRequestQueue(this);

        initViews();
        initData();
    }

    private void initData() {
        sendApplyUserDataRequest(mAvatarText);
    }

    private void updateData(ApplyUserDataBean applyUserDataBean) {
        //获取头像
        Glide.with(this)
                .load(IPConfig.image_addressPrefix + applyUserDataBean.getAvatar())
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(mUserAvatars);
        mUserName.setText(applyUserDataBean.getUserName());
        mUserPhone.setText(applyUserDataBean.getUserPhone());
        mUserRealName.setText(applyUserDataBean.getAuthen());
    }

    private void initViews() {
        mUserDataToolbar = (Toolbar) findViewById(R.id.user_data_toolbar);
        mUserDataToolbar.setNavigationIcon(R.drawable.back);
        mUserDataToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        mUserName = (TextView) findViewById(R.id.user_name);
        mUserAvatars = (CircleImageView) findViewById(R.id.user_header);
        mUserPhone = (TextView) findViewById(R.id.status);
        mUserRealName = (TextView) findViewById(R.id.authen);

        mContactFab = (FloatingActionButton) findViewById(R.id.contact_fab);
        mContactFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //被聊天用户
                ContactUserDataBean contactUserDataBean = new ContactUserDataBean(
                        mApplyUserDataBean.getUserPhone(),
                        mApplyUserDataBean.getUserName(),
                        mApplyUserDataBean.getAvatar());

                BmobIMUserInfo bmobIMUserInfo =
                        new BmobIMUserInfo(contactUserDataBean.getUserId(), contactUserDataBean.getName(), contactUserDataBean.getAvatar());
                //开启对话
                mConversation = BmobIM.getInstance().startPrivateConversation(bmobIMUserInfo, null);
                //进入详细聊天界面
                Intent contactDetail = new Intent(UserDataActivity.this, ContactDetailActivity.class);
                contactDetail.putExtra("c", mConversation);
                startActivity(contactDetail);
            }
        });
    }

    /**
     * 发送查看申请用户信息请求
     */
    private void sendApplyUserDataRequest(String avatarText) {
        //获取查看申请接单用户数据的参数集
        Map<String, String> params = ParamsGenerateUtil.generateApplyUserDataParams(avatarText);

        //创建查看申请接单用户数据订单请求
        CommonRequest applyUserData = new CommonRequest(this,
                IPConfig.applyUserDataAddress,
                mCustomApplication.generateHeaderMap(),
                params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resp) {
                        Log.i(TAG, "apply_user_data:" + resp);
                        mApplyUserDataBean = JsonUtil.decodeByGson(resp, ApplyUserDataBean.class);
                        updateData(mApplyUserDataBean);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        UnRepeatToast.showToast(UserDataActivity.this, "服务器不务正业中");
                    }
                });

        //添加到请求队列
        mRequestQueue.add(applyUserData);
    }
}
