package com.yuzhai.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.yuzhai.bean.responseBean.DetailOrderBean;
import com.yuzhai.config.IPConfig;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.ParamsGenerateUtil;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.view.CircleImageView;
import com.yuzhai.view.IndicatedViewFlipper;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/1.
 */

public class WorkDetailActivity extends AppCompatActivity implements View.OnClickListener, Toolbar.OnMenuItemClickListener {
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;
    private TextView mTitle;
    private TextView mReward;
    private CircleImageView mUserHeader;
    private TextView mUserName;
    private TextView mState;
    private TextView mDeadline;
    private TextView mDate;
    private TextView mTel;
    private TextView mDescription;
    private IndicatedViewFlipper mImages;
    private LinearLayout mApplyUserLayout;
    private FloatingActionButton mApplyOrderFab;

    private CustomApplication mCustomApplication;
    private RequestQueue mRequestQueue;
    private String mType;
    private String mOrderId;
    private DetailOrderBean.OrderInfoBean mOrder;
    private List<Integer> imagesList = new ArrayList<>();
    private int[] imageId = new int[]{R.drawable.test1, R.drawable.test2, R.drawable.test3, R.drawable.test4, R.drawable.test1, R.drawable.test2};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_work_datail);
        mCustomApplication = (CustomApplication) getApplication();
        mRequestQueue = RequestQueueSingleton.getRequestQueue(this);

        //获取订单号
//        mOrderId = getIntent().getStringExtra(WorkFragment.ORDER_ID);

        imagesList.add(R.drawable.test1);
        imagesList.add(R.drawable.test2);
        imagesList.add(R.drawable.test3);
        imagesList.add(R.drawable.test4);

        initViews();
        initData();
    }

    /**
     * 获取组件引用
     */
    private void initViews() {
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.detail_order_menu);
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTitle = (TextView) findViewById(R.id.title);
        mUserHeader = (CircleImageView) findViewById(R.id.user_header);
        mUserName = (TextView) findViewById(R.id.user_name);
        mState = (TextView) findViewById(R.id.tel);
        mDeadline = (TextView) findViewById(R.id.deadline);
        mDate = (TextView) findViewById(R.id.date);
        mTel = (TextView) findViewById(R.id.tel);
        mDescription = (TextView) findViewById(R.id.description);
        mImages = (IndicatedViewFlipper) findViewById(R.id.image_flipper);
        mApplyUserLayout = (LinearLayout) findViewById(R.id.apply_user);

        mApplyOrderFab = (FloatingActionButton) findViewById(R.id.apply_order_fab);

        mApplyOrderFab.setOnClickListener(this);
    }

    /**
     * 初始化组件数据
     */
    private void initData() {
//        sendOrderDetailRequest(mOrderId, mCustomApplication.getToken());

        mImages.setFlipperImageResources(imagesList);
        mApplyUserLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int layoutWidth = mApplyUserLayout.getWidth();
                int circleImageWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, getResources().getDisplayMetrics());
                int circleImageHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, getResources().getDisplayMetrics());
                int marginLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
                int count = (layoutWidth + marginLeft) / (circleImageWidth + marginLeft);
                for (int i = 0; i < count; i++) {
                    CircleImageView circleImageView = new CircleImageView(WorkDetailActivity.this);
                    circleImageView.setBorderColor(Color.WHITE);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(circleImageWidth, circleImageHeight);
                    if (i != 0) {
                        params.setMargins(marginLeft, 0, 0, 0);
                    }
                    circleImageView.setLayoutParams(params);
                    circleImageView.setImageResource(imageId[i]);
                    circleImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent userData = new Intent(WorkDetailActivity.this, UserDataActivity.class);
                            startActivity(userData);
                        }
                    });
//                    ImageLoader imageLoader = RequestQueueSingleton.getRequestQueue(WorkDetailActivity.this).getImageLoader();
//                    ImageLoader.ImageListener listener = ImageLoader.getImageListener(circleImageView, 0, 0);
//                    imageLoader.get(null, listener, circleImageWidth, circleImageHeight);
                    mApplyUserLayout.addView(circleImageView);
                }
                mApplyUserLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

    }

    public void updateData(final DetailOrderBean.OrderInfoBean detailOrder) {
        mTitle.setText(detailOrder.getTitle());
        mReward.setText(detailOrder.getReward());
        mUserName.setText(detailOrder.getPublisher().getName());
        mState.setText(detailOrder.getState());
        mDeadline.setText(detailOrder.getDeadline());
        mDate.setText(detailOrder.getDate());
        mTel.setText(detailOrder.getTel());
        mDescription.setText(detailOrder.getDescription());
        mImages.setFlipperImageUrls(IPConfig.image_addressPrefix, detailOrder.getPicture());

        //获取用户头像
        Glide.with(this)
                .load(IPConfig.image_addressPrefix + "/" + detailOrder.getPublisher().getAvatar())
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(mUserHeader);

        //获取申请订单用户
        mApplyUserLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int layoutWidth = mApplyUserLayout.getWidth();
                int circleImageWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, getResources().getDisplayMetrics());
                int circleImageHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, getResources().getDisplayMetrics());
                int marginLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());

                int hasCount = detailOrder.getApplicant().size();
                int maxCount = (layoutWidth + marginLeft) / (circleImageWidth + marginLeft);
                maxCount = hasCount >= maxCount ? maxCount : hasCount;

                for (int i = 0; i < maxCount; i++) {
                    CircleImageView circleImageView = new CircleImageView(WorkDetailActivity.this);
                    circleImageView.setBorderColor(Color.WHITE);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(circleImageWidth, circleImageHeight);
                    if (i != 0) {
                        params.setMargins(marginLeft, 0, 0, 0);
                    }

                    circleImageView.setLayoutParams(params);

                    Glide.with(WorkDetailActivity.this)
                            .load(IPConfig.image_addressPrefix + "/" + detailOrder.getApplicant().get(i).getApplicantAvatar())
                            .placeholder(R.drawable.default_image)
                            .error(R.drawable.default_image)
                            .into(circleImageView);

                    circleImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UnRepeatToast.showToast(WorkDetailActivity.this, "你点击了用户头像");
                        }
                    });

                    mApplyUserLayout.addView(circleImageView);
                }
                mApplyUserLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.apply_order_fab:
                if (!mCustomApplication.isLogin()) {
                    Snackbar.make(v, "你尚未登陆，点击登陆", Snackbar.LENGTH_INDEFINITE).setAction("登录", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent login = new Intent(WorkDetailActivity.this, LoginActivity.class);
                            startActivity(login);
                        }
                    }).show();
                } else {
                    showApplyOrderDialog();
                }
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.star:
                Snackbar sb = Snackbar.make(mApplyOrderFab, "收藏成功，点击浏览查看收藏列表", Snackbar.LENGTH_SHORT);
                sb.show();
                sb.setAction("浏览", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent collection = new Intent(WorkDetailActivity.this, CollectionActivity.class);
                        startActivity(collection);
                    }
                });
                break;
            case R.id.call:
                break;
        }
        return false;
    }

    /**
     * 显示申请订单对话框
     */
    public void showApplyOrderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("申请接单");
        builder.setMessage("申请接单后，等待发布方的回复，如果发布方同意您的申请，你将成功接单");
        builder.setPositiveButton("申请", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                sendApplyOrderRequest(mOrder.getPublish().getPublishId(), mCustomApplication.getToken());
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    public void sendOrderDetailRequest(String orderId, String token) {
        //生成详细订单的参数集
        Map<String, String> params = ParamsGenerateUtil.generateOrderDetailParam(orderId, token);
        Log.i("order_detail_params", params.toString());

        CommonRequest orderDetailRequest = new CommonRequest(IPConfig.orderDetailAddress,
                null,
                params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resp) {
                        Log.i("order_detail_resp", resp);
                        //更新数据
                        updateData(JsonUtil.decodeByGson(resp, DetailOrderBean.class).getOrder());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        UnRepeatToast.showToast(WorkDetailActivity.this, "服务器不务正业中");
                    }
                });

        //添加到请求队列
        mRequestQueue.add(orderDetailRequest);
    }

    /**
     * 发送申请接收订单请求
     *
     * @param orderId 订单ID
     */
    public void sendApplyOrderRequest(String orderId, String token) {
        //生成发送申请接收订单的参数集
        Map<String, String> params = ParamsGenerateUtil.generateApplyOrderParams(orderId, token);

        //创建发送申请接收订单的参数集
        CommonRequest applyOrderRequest = new CommonRequest(IPConfig.applyOrderAddress,
                null,
                params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resp) {
                        Log.i("apply_order_resp", resp);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        UnRepeatToast.showToast(WorkDetailActivity.this, "服务器不务正业中");
                    }
                });

        //添加到请求队列
        mRequestQueue.add(applyOrderRequest);
    }
}
