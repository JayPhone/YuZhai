package com.yuzhai.listener;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.yuzhai.view.PointViewFlipper;

/**
 * Created by Administrator on 2016/6/12.
 */
public class OnGestureBanner implements GestureDetector.OnGestureListener {

    private PointViewFlipper picturePanel;

    public OnGestureBanner(PointViewFlipper picturePanel) {
        this.picturePanel = picturePanel;
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
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > 50) {
            picturePanel.stopFlipping();
            picturePanel.showNext();
        } else if (e2.getX() - e1.getX() > 50) {
            picturePanel.stopFlipping();
            picturePanel.showPrevious();
        }
        picturePanel.startFlipping();
        return true;
    }
}
