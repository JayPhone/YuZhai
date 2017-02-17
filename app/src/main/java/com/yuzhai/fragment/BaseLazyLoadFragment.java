package com.yuzhai.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 35429 on 2017/2/14.
 */

public class BaseLazyLoadFragment extends Fragment {
    protected boolean isViewCreated = false;
    private static final String TAG = "BaseLazyLoadFragment";

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            lazyLoadData();
        }
    }

    /**
     * 懒加载方法
     */
    protected void lazyLoadData() {

    }
}
