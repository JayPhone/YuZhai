package com.yuzhai.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyachi.stepview.VerticalStepView;
import com.yuzhai.bean.responseBean.OrderAppliedDetailBean;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 35429 on 2017/2/21.
 */

public class OrderAppliedProcessFragment extends Fragment {
    private VerticalStepView mVerticalStepView;
    private OrderAppliedDetailBean.OrderInfoBean mOrder;
    private static final String ORDER = "order";
    private List<String> list;
    private CustomApplication mCustomApplication;

    public static OrderAppliedProcessFragment newInstance(String order) {
        Bundle args = new Bundle();
        args.putString(ORDER, order);
        OrderAppliedProcessFragment fragment = new OrderAppliedProcessFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void testMethod() {
        mVerticalStepView
                .setStepsViewIndicatorComplectingPosition(1)//设置完成的步数
                .setStepViewTexts(list);//总步骤
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_applied_process, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mCustomApplication = (CustomApplication) getActivity().getApplication();
        initViews();
        initData();
    }

    private void initViews() {
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
            mOrder = JsonUtil.decodeByGson(getArguments().getString(ORDER), OrderAppliedDetailBean.class).getDetailedOrder();
            updateData(mOrder);
        } else {
            testMethod();
        }
    }

    private void updateData(OrderAppliedDetailBean.OrderInfoBean order) {
        list = new ArrayList<>();
        if (order.getStatus().equals("取消发布订单")) {//发布方取消订单
            list.add("发起订单申请");
            list.add("订单已被发布方取消");
            list.add("申请订单失败");
        } else {//订单状态正常
            if (order.getBidder().equals("0") || order.getBidder().equals(mCustomApplication.getUserPhone())) {//无接收者或接收者为自己
                list.add("发起订单申请");
                list.add("等待发布方确认");
                list.add("申请订单成功");
            } else if (!order.getBidder().equals(mCustomApplication.getUserPhone())) {//有接收者但不是自己
                list.add("发起订单申请");
                list.add("订单已被其他用户接收");
                list.add("申请订单失败");
            }
        }
        mVerticalStepView.setStepViewTexts(list);//总步骤

        if (order.getStatus().equals("取消发布订单")) {//发布方取消订单
            mVerticalStepView.setStepsViewIndicatorComplectingPosition(2);
        } else {
            if (order.getBidder().equals(mCustomApplication.getUserPhone())) {//接收者为自己
                mVerticalStepView.setStepsViewIndicatorComplectingPosition(2);
            } else if (order.getBidder().equals("0")) {//无接收者
                mVerticalStepView.setStepsViewIndicatorComplectingPosition(1);
            } else if (!order.getBidder().equals(mCustomApplication.getUserPhone())) {//接收者不是自己
                mVerticalStepView.setStepsViewIndicatorComplectingPosition(2);
            }
        }
    }
}
