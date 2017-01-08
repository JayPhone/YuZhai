package com.yuzhai.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyachi.stepview.VerticalStepView;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/14.
 */

public class OrderAcceptedProcessFragment extends Fragment {
    private VerticalStepView mVerticalStepView;

    public static OrderAcceptedProcessFragment newInstance() {
//        Bundle args = new Bundle();
        OrderAcceptedProcessFragment fragment = new OrderAcceptedProcessFragment();
//        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accepted_process, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mVerticalStepView = (VerticalStepView) getView().findViewById(R.id.step_view);
        List<String> list = new ArrayList<>();
        list.add("订单发布");
        list.add("等待用户申请接收订单");
        list.add("同意用户接收订单");
        list.add("接收方提交作品");
        list.add("支付订单金额");
        list.add("双方评价");
        list.add("订单完成");

        mVerticalStepView.setStepsViewIndicatorComplectingPosition(1)//设置完成的步数
                .setStepViewTexts(list)//总步骤
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(getActivity(), R.color.mainColor))//设置StepsViewIndicator完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(getActivity(), R.color.color_BDBDBD))//设置StepsViewIndicator未完成线的颜色
                .setStepViewComplectedTextColor(ContextCompat.getColor(getActivity(), R.color.color_757575))//设置StepsView text完成线的颜色
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(getActivity(), R.color.color_BDBDBD))//设置StepsView text未完成线的颜色
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(getActivity(), R.drawable.complted))//设置StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(getActivity(), R.drawable.complted_no))//设置StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(getActivity(), R.drawable.attention))//设置StepsViewIndicator
                .reverseDraw(false);
    }
}
