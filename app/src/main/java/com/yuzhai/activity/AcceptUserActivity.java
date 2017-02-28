package com.yuzhai.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.yuzhai.bean.innerBean.PayRoleBean;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;

/**
 * Created by Administrator on 2017/1/8.
 */

public class AcceptUserActivity extends AppCompatActivity {
    private static final String TAG = "AcceptUserActivity";
    private static final String PRICE = "price";
    private static final String PAY_DESCRIPTION = "pay_description";
    private static final String PAY_ROLE = "pay_role";
    public static final String PUBLISH_ROLE = "publish_role";

    private CustomApplication mCustomApplication;
    private RequestQueue mRequestQueue;

    private Toolbar mUserDataToolbar;
    private AppBarLayout mAppBarLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private CircleImageView mUserAvatars;
    private TextView mUserName;
    private TextView mUserPhone;
    private TextView mUserRealName;
    private TextView mAgreeAccept;
    private FloatingActionButton mContactFab;

    private ApplyUserDataBean mApplyUserDataBean;
    private BmobIMConversation mConversation;

    private String mAvatarText;
    private String mOrderId;
    private String mPrice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mPrice = getIntent().getStringExtra(OrderPublishedProcessFragment.PRICE);
        mOrderId = getIntent().getStringExtra(OrderPublishedProcessFragment.ORDER);
        mAvatarText = getIntent().getStringExtra(OrderPublishedProcessFragment.AVATAR);

        setContentView(R.layout.activity_accept_user);
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
        Glide.with(AcceptUserActivity.this)
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

        mAgreeAccept = (TextView) findViewById(R.id.agree_accept_text);
        mAgreeAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPayDialog();
            }
        });

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
                Intent contactDetail = new Intent(AcceptUserActivity.this, ContactDetailActivity.class);
                contactDetail.putExtra("c", mConversation);
                startActivity(contactDetail);
            }
        });
    }

    private void showPayDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("同意申请");
        builder.setMessage("同意申请者接单，你需要缴纳订单金额，保证金(订单金额30%)及服务费(订单金额3%)，缴纳成功后，订单开始");
        builder.setPositiveButton("我要支付", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Intent pay = new Intent(AcceptUserActivity.this, PayActivity.class);
//                pay.putExtra(PAY_ROLE, PUBLISH_ROLE);
//                pay.putExtra(PRICE, mPrice);
//                pay.putExtra(PAY_DESCRIPTION, "支付金额为您的订单金额加上订单保证金(订单金额的30%)加上订单服务费(订单金额的3%),订单保证金会在接收方完成后退还到您的账户，同时，如果申请人在24小时内没有确认订单，将退还订单金额到您的账户");
//                startActivity(pay);
                sendAgreeApplyOrderRequest(mOrderId, mApplyUserDataBean.getUserPhone());
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
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
                        UnRepeatToast.showToast(AcceptUserActivity.this, "服务器不务正业中");
                    }
                });

        //添加到请求队列
        mRequestQueue.add(applyUserData);
    }

    /**
     * 发送查看申请用户信息请求
     */
    private void sendAgreeApplyOrderRequest(String orderId, String bidderId) {
        //获取同意用户申请订单的参数集
        Map<String, String> params = ParamsGenerateUtil.generateAgreeApplyOrderParams(orderId, bidderId);

        //创建同意用户申请订单请求
        CommonRequest agressApplyOrder = new CommonRequest(this,
                IPConfig.decideBidderAddress,
                mCustomApplication.generateHeaderMap(),
                params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resp) {
                        Log.i(TAG, "agree_apply_order:" + resp);
                        UnRepeatToast.showToast(AcceptUserActivity.this, "同意申请订单成功");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        UnRepeatToast.showToast(AcceptUserActivity.this, "服务器不务正业中");
                    }
                });

        //添加到请求队列
        mRequestQueue.add(agressApplyOrder);
    }

    //支付成功后调用
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventPayActivity(PayRoleBean payRoleBean) {
        if (payRoleBean.getPayRole().equals(PUBLISH_ROLE)) {
            sendAgreeApplyOrderRequest(mOrderId, mApplyUserDataBean.getUserPhone());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
