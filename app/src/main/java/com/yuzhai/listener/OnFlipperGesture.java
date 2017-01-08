package com.yuzhai.listener;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.yuzhai.view.CustomViewFlipper;

/**
 * Created by Administrator on 2016/6/12.
 */
public class OnFlipperGesture implements GestureDetector.OnGestureListener {

    private CustomViewFlipper mCustomViewFlipper;

    public OnFlipperGesture(CustomViewFlipper customViewFlipper) {
        this.mCustomViewFlipper = customViewFlipper;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (distanceX >= distanceY) {

        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > 10) {
            mCustomViewFlipper.stopFlipping();
            mCustomViewFlipper.showNext();
        } else if (e2.getX() - e1.getX() > 10) {
            mCustomViewFlipper.stopFlipping();
            mCustomViewFlipper.showPrevious();
        }
        mCustomViewFlipper.startFlipping();
        return true;
    }
}
