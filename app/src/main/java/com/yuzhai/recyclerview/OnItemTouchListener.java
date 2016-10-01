package com.yuzhai.recyclerview;

/**
 * Created by Administrator on 2016/9/29.
 */
public interface OnItemTouchListener {
    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
