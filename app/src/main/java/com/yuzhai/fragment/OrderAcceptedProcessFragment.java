package com.yuzhai.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyachi.stepview.VerticalStepView;
import com.yuzhai.bean.responseBean.OrderAcceptedDetailBean;
import com.yuzhai.global.CustomApplication;
import com.yuzhai.util.JsonUtil;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/14.
 */

public class OrderAcceptedProcessFragment extends Fragment {
    private VerticalStepView mVerticalStepView;
    private OrderAcceptedDetailBean.OrderInfoBean mOrder;
    private static final String ORDER = "order";
    private List<String> list;

    public static OrderAcceptedProcessFragment newInstance(String order) {
        Bundle args = new Bundle();
        args.putString(ORDER, order);
        OrderAcceptedProcessFragment fragment = new OrderAcceptedProcessFragment();
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
        return inflater.inflate(R.layout.fragment_accepted_process, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
            mOrder = JsonUtil.decodeByGson(getArguments().getString(ORDER), OrderAcceptedDetailBean.class).getDetailedOrder();
            updateData(mOrder);
        } else {
            testMethod();
        }
    }

    private void updateData(OrderAcceptedDetailBean.OrderInfoBean order) {
        list = new ArrayList<>();
        if (mOrder.getStatus().equals("取消发布订单")) {
            list.add("订单接收成功");
            list.add("订单已被发布方取消");
            list.add("订单停止");
        } else {
            list.add("订单接收成功");
            list.add("订单开始");
            list.add("提交作品");
            list.add("订单完成");
        }

        mVerticalStepView.setStepViewTexts(list);//总步骤
        if (mOrder.getStatus().equals("取消发布订单")) {
            mVerticalStepView.setStepsViewIndicatorComplectingPosition(2);//设置完成的步数
        } else {
            mVerticalStepView.setStepsViewIndicatorComplectingPosition(1);
        }
    }
}
