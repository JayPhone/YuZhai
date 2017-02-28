package com.yuzhai.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.baoyachi.stepview.VerticalStepView;
import com.bumptech.glide.Glide;
import com.yuzhai.activity.AcceptUserActivity;
import com.yuzhai.bean.responseBean.OrderPublishedDetailBean;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.http.IPConfig;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.view.CircleImageView;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/14.
 */

public class OrderPublishedProcessFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "OrderPublishedProcessFr";
    public static final String ORDER = "order";
    public static final String AVATAR = "avatar";
    public static final String PRICE = "price";

    private VerticalStepView mVerticalStepView;
    private LinearLayout mApplyUserLayout;
    private OrderPublishedDetailBean.OrderInfoBean mOrder;
    private List<String> list;
    private List<CircleImageView> mImageViewList;
    private CustomApplication mCustomApplication;

    public static OrderPublishedProcessFragment newInstance(String order) {
        Bundle args = new Bundle();
        args.putString(ORDER, order);
        OrderPublishedProcessFragment fragment = new OrderPublishedProcessFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 测试方法
     */
    private void testMethod() {
        final int[] imageId = new int[]{R.drawable.test1, R.drawable.test2, R.drawable.test3, R.drawable.test4, R.drawable.test2};
        mApplyUserLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int circleImageWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, getResources().getDisplayMetrics());
                int circleImageHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, getResources().getDisplayMetrics());
                int marginLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
                for (int i = 0; i < imageId.length; i++) {
                    CircleImageView circleImageView = new CircleImageView(getActivity());
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
                            Intent userData = new Intent(getActivity(), AcceptUserActivity.class);
                            startActivity(userData);
                        }
                    });
                    mApplyUserLayout.addView(circleImageView);
                }
                mApplyUserLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }

        });

        //设置进度流程
        mVerticalStepView
                .setStepViewTexts(list)//总步骤
                .setStepsViewIndicatorComplectingPosition(1);//设置完成的步数
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_published_process, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCustomApplication = (CustomApplication) getActivity().getApplication();

        initViews();
        initData();
    }

    private void initViews() {
        mApplyUserLayout = (LinearLayout) getView().findViewById(R.id.apply_user);
        mVerticalStepView = (VerticalStepView) getView().findViewById(R.id.step_view);
        mVerticalStepView
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(getActivity(), R.color.mainColor))//设置StepsViewIndicator完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(getActivity(), R.color.color_BDBDBD))//设置StepsViewIndicator未完成线的颜色
                .setStepViewComplectedTextColor(ContextCompat.getColor(getActivity(), R.color.color_757575))//设置StepsView text完成线的颜色
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(getActivity(), R.color.color_BDBDBD))//设置StepsView text未完成线的颜色
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(getActivity(), R.drawable.complted))//设置StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(getActivity(), R.drawable.complted_no))//设置StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(getActivity(), R.drawable.attention))//设置StepsViewIndicator
                .reverseDraw(false);
    }

    private void initData() {
        if (CustomApplication.isConnect) {
            mOrder = JsonUtil.decodeByGson(getArguments().getString(ORDER), OrderPublishedDetailBean.class).getDetailedOrder();
            Log.i("order", mOrder.toString());
            updateData(mOrder);
        } else {
            testMethod();
        }
    }

    private void updateData(final OrderPublishedDetailBean.OrderInfoBean order) {
        mApplyUserLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (null != mOrder.getApplicantAvatars() && mOrder.getApplicantAvatars().size() > 0) {
                    mImageViewList = new ArrayList<>();
                    int layoutWidth = mApplyUserLayout.getWidth();
                    int circleImageWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, getResources().getDisplayMetrics());
                    int circleImageHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, getResources().getDisplayMetrics());
                    int marginLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());

                    int hasCount = order.getApplicantAvatars().size();
                    int maxCount = (layoutWidth + marginLeft) / (circleImageWidth + marginLeft);
                    maxCount = hasCount >= maxCount ? maxCount : hasCount;

                    for (int i = 0; i < maxCount; i++) {
                        CircleImageView circleImageView = new CircleImageView(getActivity());
                        circleImageView.setBorderColor(Color.WHITE);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(circleImageWidth, circleImageHeight);
                        if (i != 0) {
                            params.setMargins(marginLeft, 0, 0, 0);
                        }
                        circleImageView.setLayoutParams(params);
                        //获取头像
                        Glide.with(OrderPublishedProcessFragment.this)
                                .load(IPConfig.image_addressPrefix + mOrder.getApplicantAvatars().get(i).getApplicantAvatar())
                                .placeholder(R.drawable.default_image)
                                .error(R.drawable.default_image)
                                .into(circleImageView);
                        circleImageView.setOnClickListener(OrderPublishedProcessFragment.this);
                        circleImageView.setTag(i);
                        mImageViewList.add(circleImageView);
                        mApplyUserLayout.addView(circleImageView);
                    }
                    mApplyUserLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        //初始化进度数据
        list = new ArrayList<>();
        list.add("订单发布");
        list.add("等待用户申请接收订单");
        list.add("同意用户接收订单");
        list.add("订单开始");
        list.add("确认收货");
        list.add("订单完成");
        //设置进度流程
        mVerticalStepView.setStepViewTexts(list);//总步骤

        if (!order.getBidder().equals("0")) {//有接收者
            mVerticalStepView.setStepsViewIndicatorComplectingPosition(3);
        } else {//没有接收者
            mVerticalStepView.setStepsViewIndicatorComplectingPosition(1);
        }
        //设置订单进度
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, String.valueOf(v.getTag()));
        Log.i(TAG, mImageViewList.toString());
        for (int id = 0; id < mImageViewList.size(); id++) {
            Object imageTag = mImageViewList.get(id).getTag();
            if (imageTag == v.getTag()) {
                Intent userData = new Intent(getActivity(), AcceptUserActivity.class);
                //计算支付金额
                double rewardPrice = Double.parseDouble(mOrder.getReward());
                double cashDeposit = rewardPrice * 0.3;
                double serviceCharge = rewardPrice * 0.03;
                double allPrice = rewardPrice + cashDeposit + serviceCharge;
                userData.putExtra(PRICE, String.valueOf(allPrice));
                userData.putExtra(ORDER, mOrder.getOrderID());
                userData.putExtra(AVATAR, mOrder.getApplicantAvatars().get(id).getApplicantAvatar());
                getActivity().startActivity(userData);
            }
        }
    }
}
