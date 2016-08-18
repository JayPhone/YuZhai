package com.yuzhai.http;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.yuzhai.util.BitmapCache;

/**
 * Created by Administrator on 2016/8/16.
 */
public class RequestQueueSingleton {
    private static RequestQueueSingleton mInstance;
    private RequestQueue mRequestQueue;
    private static Context mContext;
    private ImageLoader mImageLoader;

    private RequestQueueSingleton(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
        mImageLoader = new ImageLoader(mRequestQueue, new BitmapCache());
    }

    public static synchronized RequestQueueSingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RequestQueueSingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            //确保 RequestQueue 在app的生命周期中一直存活，不会因为 activity 的重新创建而被重新创建
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

    public ImageLoader getmImageLoader() {
        return mImageLoader;
    }
}
