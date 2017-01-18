package com.yuzhai.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.yuzhai.yuzhaiwork.R;

import java.util.List;

/**
 * Created by Administrator on 2016/10/27.
 */

public class IndicatedViewFlipper extends FrameLayout {
    private CustomViewFlipper mCustomViewFlipper;
    private CustomLinerLayout mIndicator;
    private Context mContext;
    private boolean mIsShowIndicator;

    public IndicatedViewFlipper(Context context) {
        this(context, null);
    }

    public IndicatedViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatedViewFlipper);
        mIsShowIndicator = typedArray.getBoolean(R.styleable.IndicatedViewFlipper_showIndicator, true);
        typedArray.recycle();

        View view = LayoutInflater.from(context).inflate(R.layout.indicated_view_flipper_layout, this);

        mCustomViewFlipper = (CustomViewFlipper) view.findViewById(R.id.flipper);
        setFlipperInAnimation(android.R.anim.fade_in);
        setFlipperOutAnimation(android.R.anim.fade_out);

        mIndicator = (CustomLinerLayout) view.findViewById(R.id.indicator);
    }

    /**
     * 设置子项点击监听
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(CustomViewFlipper.OnItemClickListener onItemClickListener) {
        mCustomViewFlipper.setOnItemClickListener(onItemClickListener);
    }

    public void setFlipperInAnimation(int inAnimationId) {
        mCustomViewFlipper.setInAnimation(mContext, inAnimationId);
    }

    public void setFlipperOutAnimation(int outAnimationId) {
        mCustomViewFlipper.setOutAnimation(mContext, outAnimationId);
    }

    //设置禁止阻拦的事件的组件
    public void setDisallowInterceptViewGroup(ViewGroup disallowInterceptViewGroup) {
        mCustomViewFlipper.setDisallowInterceptViewGroup(disallowInterceptViewGroup);
    }

    public void setFlipperImageResources(List<Integer> imagesList) {
        mCustomViewFlipper.setImageResources(imagesList);
        if (mIsShowIndicator) {
            generateIndicator(mCustomViewFlipper, imagesList.size());
        }
    }

    public void setFlipperImageUrls(String prefix, List<String> imageUrlsList) {
        mCustomViewFlipper.setImageUrls(prefix, imageUrlsList);
        if (mIsShowIndicator) {
            generateIndicator(mCustomViewFlipper, imageUrlsList.size());
        }
    }

    public void generateIndicator(CustomViewFlipper customViewFlipper, int indicatorCount) {
        mIndicator.setIndicator(customViewFlipper, indicatorCount);
    }
}
