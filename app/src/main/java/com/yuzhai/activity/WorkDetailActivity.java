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
import com.yuzhai.bean.innerBean.PayRoleBean;
import com.yuzhai.bean.responseBean.ApplyOrderRespBean;
import com.yuzhai.bean.responseBean.DetailOrderBean;
import com.yuzhai.fragment.WorkFragment;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.CommonRequest;
import com.yuzhai.http.IPConfig;
import com.yuzhai.http.ParamsGenerateUtil;
import com.yuzhai.http.RequestQueueSingleton;
import com.yuzhai.view.CircleImageView;
import com.yuzhai.view.CustomViewFlipper;
import com.yuzhai.view.IndicatedViewFlipper;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.icu.util.HebrewCalendar.AV;
import static com.yuzhai.fragment.OrderPublishedProcessFragment.PRICE;
import static com.yuzhai.util.JsonUtil.decodeByGson;

/**
 * Created by Administrator on 2016/11/1.
 */

public class WorkDetailActivity extends AppCompatActivity implements
        View.OnClickListener, Toolbar.OnMenuItemClickListener {
    private static final String TAG = "WorkDetailActivity";
    public static final String IMAGE_URL = "image_url";
    private static final String PAY_ROLE = "pay_role";
    private static final String PRICE = "price";
    private static final String PAY_DESCRIPTION = "pay_description";
    public static final String APPLY_ROLE = "apply_role";
    private static final String AVATAR = "avatar";

    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;
    private TextView mTitle;
    private CircleImageView mUserHeader;
    private TextView mUserName;
    private TextView mID;
    private TextView mStatus;
    private TextView mDeadline;
    private TextView mDate;
    private TextView mTel;
    private TextView mDescription;
    private IndicatedViewFlipper mImageFlipper;
    private LinearLayout mApplyUserLayout;
    private FloatingActionButton mApplyOrderFab;

    private CustomApplication mCustomApplication;
    private RequestQueue mRequestQueue;
    private DetailOrderBean.OrderInfoBean mOrder;
    private String mOrderId;
    private CommonRequest mOrderDetailRequest;

    /**
     * 测试数据
     */
    private int[] imageId = new int[]{R.drawable.test1, R.drawable.test2, R.drawable.test3, R.drawable.test4, R.drawable.test2};
    private List<Integer> testImagesList = new ArrayList<>();

    private void testMethod() {
        for (int i = 0; i < imageId.length; i++) {
            testImagesList.add(imageId[i]);
        }
        mImageFlipper.setFlipperImageResources(testImagesList);

        mApplyUserLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int circleImageWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, getResources().getDisplayMetrics());
                int circleImageHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, getResources().getDisplayMetrics());
                int marginLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());

                for (int i = 0; i < testImagesList.size(); i++) {
                    CircleImageView circleImageView = new CircleImageView(WorkDetailActivity.this);
                    circleImageView.setBorderColor(Color.WHITE);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(circleImageWidth, circleImageHeight);
                    if (i != 0) {
                        params.setMargins(marginLeft, 0, 0, 0);
                    }

                    circleImageView.setLayoutParams(params);
                    circleImageView.setImageResource(testImagesList.get(i));
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_work_datail);
        mCustomApplication = (CustomApplication) getApplication();
        mRequestQueue = RequestQueueSingleton.getRequestQueue(this);

        //获取订单号
        mOrderId = getIntent().getStringExtra(WorkFragment.ORDER_ID);

        initViews();
        initData();
    }

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
        mUserHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userData = new Intent(WorkDetailActivity.this, UserDataActivity.class);
                userData.putExtra(AVATAR, mOrder.getPublisherAvatar());
                startActivity(userData);
            }
        });

        mUserName = (TextView) findViewById(R.id.user_name);
        mID = (TextView) findViewById(R.id.id);
        mStatus = (TextView) findViewById(R.id.status);
        mDeadline = (TextView) findViewById(R.id.deadline);
        mDate = (TextView) findViewById(R.id.date);
        mTel = (TextView) findViewById(R.id.tel);
        mDescription = (TextView) findViewById(R.id.description);
        mImageFlipper = (IndicatedViewFlipper) findViewById(R.id.image_flipper);
        mImageFlipper.setOnItemClickListener(new CustomViewFlipper.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (CustomApplication.isConnect) {
                    Intent showImage = new Intent(WorkDetailActivity.this, ShowImageActivity.class);
                    showImage.putExtra(IMAGE_URL, mOrder.getPictures().get(position).getImage());
                    Log.i(TAG, "pictures_url:" + mOrder.getPictures().get(position).getImage());
                    startActivity(showImage);
                }
            }
        });

        mApplyUserLayout = (LinearLayout) findViewById(R.id.apply_user);
        mApplyOrderFab = (FloatingActionButton) findViewById(R.id.apply_order_fab);
        mApplyOrderFab.setOnClickListener(this);
    }

    /**
     * 初始化组件数据
     */
    private void initData() {
        if (CustomApplication.isConnect) {
            sendOrderDetailRequest(mOrderId);
        } else {
            testMethod();
        }
    }

    public void updateData(final DetailOrderBean.OrderInfoBean detailOrder) {
        //设置订单标题
        mTitle.setText(detailOrder.getTitle());
        //设置订单金额
        mCollapsingToolbarLayout.setTitle("￥" + detailOrder.getReward());
        //设置发布用户名
        mUserName.setText(detailOrder.getPublisher());
        //设置订单Id
        mID.setText(detailOrder.getOrderID());
        //设置订单状态
        mStatus.setText(detailOrder.getStatus());
        //设置订单期限
        mDeadline.setText(detailOrder.getDeadline());
        //设置订单发布日期
        mDate.setText(detailOrder.getDate());
        //设置发布方联系电话
        mTel.setText(detailOrder.getTel());
        //设置订单详细描述
        mDescription.setText(detailOrder.getDescription());
        //设置订单图片
        setFlipperImages(detailOrder.getPictures());
        //设置用户头像
        Glide.with(this)
                .load(IPConfig.image_addressPrefix + "/" + detailOrder.getPublisherAvatar())
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(mUserHeader);
        Log.i(TAG, "applicant_avatar:" + detailOrder.getApplicantAvatars().toString());

        //设置申请订单的用户头像
        mApplyUserLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (null != mOrder.getApplicantAvatars() && mOrder.getApplicantAvatars().size() > 0) {
                    int layoutWidth = mApplyUserLayout.getWidth();
                    int circleImageWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, getResources().getDisplayMetrics());
                    int circleImageHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, getResources().getDisplayMetrics());
                    int marginLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());

                    int hasCount = detailOrder.getApplicantAvatars().size();
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
                        Log.i("AVATAR", detailOrder.getApplicantAvatars().get(i).getApplicantAvatar());
                        Glide.with(WorkDetailActivity.this)
                                .load(IPConfig.image_addressPrefix + "/" + detailOrder.getApplicantAvatars().get(i).getApplicantAvatar())
                                .placeholder(R.drawable.default_image)
                                .error(R.drawable.default_image)
                                .into(circleImageView);

                        mApplyUserLayout.addView(circleImageView);
                    }
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
                    Snackbar.make(v, "你尚未登陆，请登录后再接单", Snackbar.LENGTH_SHORT).show();
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
        builder.setMessage("申请接单需要缴纳违约金(订单金额30%)，违约金将会在订单完成之后返还到您的账户");
        builder.setPositiveButton("申请并支付", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (CustomApplication.isConnect) {
//                    Intent pay = new Intent(WorkDetailActivity.this, PayActivity.class);
//                    //计算支付金额
//                    double rewardPrice = Double.parseDouble(mOrder.getReward());
//                    double penalty = rewardPrice * 0.3;
//                    pay.putExtra(PAY_ROLE, APPLY_ROLE);
//                    pay.putExtra(PRICE, String.valueOf(penalty));
//                    pay.putExtra(PAY_DESCRIPTION, "支付金额为您申请接收的订单违约金(订单金额的30%),订单违约金会在发布方确认完成订单后退还到您的账户。");
//                    startActivity(pay);
                    sendApplyOrderRequest(mOrder.getOrderID());
                }
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    /**
     * 发送查看详细订单的请求
     *
     * @param orderId
     */
    public void sendOrderDetailRequest(String orderId) {
        //生成详细订单的参数集
        Map<String, String> params = ParamsGenerateUtil.generateOrderDetailParam(orderId);
        Log.i(TAG, "order_detail_params:" + params.toString());

        mOrderDetailRequest = new CommonRequest(this,
                IPConfig.orderDetailAddress,
                mCustomApplication.generateHeaderMap(),
                params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resp) {
                        Log.i(TAG, "order_detail_resp:" + resp);
                        mOrder = decodeByGson(resp, DetailOrderBean.class).getDetailedOrder();
                        //更新数据
                        updateData(mOrder);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        UnRepeatToast.showToast(WorkDetailActivity.this, "服务器不务正业中");
                    }
                });

        //添加到请求队列
        mRequestQueue.add(mOrderDetailRequest);
    }

    /**
     * 发送申请接收订单请求
     *
     * @param orderId 订单ID
     */
    public void sendApplyOrderRequest(String orderId) {
        //生成发送申请接收订单的参数集
        Map<String, String> params = ParamsGenerateUtil.generateApplyOrderParams(orderId);

        //创建发送申请接收订单的参数集
        CommonRequest applyOrderRequest = new CommonRequest(this,
                IPConfig.applyOrderAddress,
                mCustomApplication.generateHeaderMap(),
                params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resp) {
                        Log.i(TAG, "apply_order_resp:" + resp);
                        String code = decodeByGson(resp, ApplyOrderRespBean.class).getCode();
                        //订单申请接收成功
                        if (code.equals("1")) {
                            UnRepeatToast.showToast(WorkDetailActivity.this, "申请接收订单成功");
                        }
                        //订单重复申请接收
                        else if (code.equals("2")) {
                            UnRepeatToast.showToast(WorkDetailActivity.this, "你已成功申请，请不要重复申请");
                        }
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

    /**
     * 设置imageFlipper的图片
     *
     * @param imageList
     */
    private void setFlipperImages(List<DetailOrderBean.OrderInfoBean.PicturesBean> imageList) {
        List<String> imagePaths = new ArrayList<>();
        for (int i = 0; i < imageList.size(); i++) {
            imagePaths.add(imageList.get(i).getImage());
        }
        mImageFlipper.setFlipperImageUrls(IPConfig.image_addressPrefix, imagePaths);
    }

    //支付成功后调用
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventPayActivity(PayRoleBean payRoleBean) {
        if (payRoleBean.getPayRole().equals(APPLY_ROLE)) {
            sendApplyOrderRequest(mOrder.getOrderID());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
