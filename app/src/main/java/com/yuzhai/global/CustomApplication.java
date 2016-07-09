package com.yuzhai.global;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Administrator on 2016/7/9.
 */
public class CustomApplication extends Application {
    private RequestQueue mRequestQueue;
    private boolean LOGIN = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public boolean isLOGIN() {
        return LOGIN;
    }

    public void setLOGIN(boolean LOGIN) {
        this.LOGIN = LOGIN;
    }
}
