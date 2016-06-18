package com.yuzhai.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ViewFlipper;

import com.yuzhai.listener.OnGestureBanner;

/**
 * Created by Administrator on 2016/6/11.
 */
public class PointViewFlipper extends ViewFlipper {
    private GestureDetector detectorBanner;
    private Context context;

    public PointViewFlipper(Context context) {
        super(context);
        this.context = context;
        detectorBanner = new GestureDetector(context, new OnGestureBanner(this));
    }

    public PointViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        detectorBanner = new GestureDetector(context, new OnGestureBanner(this));
    }

    private OnFlipListener onFlipListener;

    public interface OnFlipListener {
        void onShowPrevious(PointViewFlipper flipper);

        void onShowNext(PointViewFlipper flipper);
    }

    public void setOnFlipListener(
            OnFlipListener onFlipListener) {
        this.onFlipListener = onFlipListener;
    }

    @Override
    public void showPrevious() {
        super.showPrevious();
        if (hasFlipListener()) {
            onFlipListener.onShowPrevious(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return detectorBanner.onTouchEvent(ev);
    }

    @Override
    public void showNext() {
        super.showNext();
        if (hasFlipListener()) {
            onFlipListener.onShowNext(this);
        }
    }

    private boolean hasFlipListener() {
        return onFlipListener != null;
    }

}
