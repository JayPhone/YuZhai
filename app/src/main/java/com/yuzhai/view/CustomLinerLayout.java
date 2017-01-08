package com.yuzhai.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.yuzhai.yuzhaiwork.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/27.
 */

public class CustomLinerLayout extends LinearLayout implements CustomViewFlipper.OnFlipListener {
    private Context mContext;
    private List<View> mIndicatorList;

    public CustomLinerLayout(Context context) {
        this(context, null);
    }

    public CustomLinerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public void setIndicator(CustomViewFlipper customViewFlipper, int indicatorCount) {
        customViewFlipper.setOnFlipListener(this);
        mIndicatorList = new ArrayList<>();
        for (int i = 0; i < indicatorCount; i++) {
            View view = new View(mContext);
            LayoutParams params = new LayoutParams(
                    (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics())),
                    (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics())));
            params.setMargins(0, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()), 0);
            view.setLayoutParams(params);
            if (i == 0) {
                view.setBackgroundResource(R.drawable.buttom_style_choose);
            } else {
                view.setBackgroundResource(R.drawable.buttom_style);
            }
            this.addView(view);
            mIndicatorList.add(view);
        }
    }

    @Override
    public void onShowPrevious(CustomViewFlipper flipper) {
        int id = flipper.getDisplayedChild();
        mIndicatorList.get(id).setBackgroundResource(R.drawable.buttom_style_choose);
        for (int i = 0; i < mIndicatorList.size(); i++) {
            if (id == i)
                continue;
            else
                mIndicatorList.get(i).setBackgroundResource(R.drawable.buttom_style);
        }
    }

    @Override
    public void onShowNext(CustomViewFlipper flipper) {
        int id = flipper.getDisplayedChild();
        mIndicatorList.get(id).setBackgroundResource(R.drawable.buttom_style_choose);
        for (int i = 0; i < mIndicatorList.size(); i++) {
            if (id == i)
                continue;
            else
                mIndicatorList.get(i).setBackgroundResource(R.drawable.buttom_style);
        }
    }
}
