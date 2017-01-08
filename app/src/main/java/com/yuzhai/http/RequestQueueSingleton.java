package com.yuzhai.http;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Administrator on 2016/8/16.
 */
public class RequestQueueSingleton {
    private static volatile RequestQueue sRequestQueue = null;

    public static RequestQueue getRequestQueue(Context context) {
        if (null == sRequestQueue) {
            synchronized (RequestQueueSingleton.class) {
                if (null == sRequestQueue) {
                    sRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
                }
            }
        }
        return sRequestQueue;
    }
}
