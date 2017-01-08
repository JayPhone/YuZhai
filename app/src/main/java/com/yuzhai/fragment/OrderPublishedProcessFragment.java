package com.yuzhai.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.baoyachi.stepview.VerticalStepView;
import com.yuzhai.view.CircleImageView;
import com.yuzhai.view.UnRepeatToast;
import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/14.
 */

public class OrderPublishedProcessFragment extends Fragment {
    private VerticalStepView mVerticalStepView;
    private LinearLayout mApplyUserLayout;
    private int[] imageId = new int[]{R.drawable.test1, R.drawable.test2, R.drawable.test3, R.drawable.test4, R.drawable.test1, R.drawable.test2};

    public static OrderPublishedProcessFragment newInstance() {
//        Bundle args = new Bundle();
        OrderPublishedProcessFragment fragment = new OrderPublishedProcessFragment();
//        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_published_process, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mApplyUserLayout = (LinearLayout) getView().findViewById(R.id.apply_user);
        mApplyUserLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int layoutWidth = mApplyUserLayout.getWidth();
                int circleImageWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, getResources().getDisplayMetrics());
                int circleImageHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, getResources().getDisplayMetrics());
                int marginLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
                int count = (layoutWidth + marginLeft) / (circleImageWidth + marginLeft);
                for (int i = 0; i < count; i++) {
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
                            UnRepeatToast.showToast(getActivity(),"点击了头像");
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
