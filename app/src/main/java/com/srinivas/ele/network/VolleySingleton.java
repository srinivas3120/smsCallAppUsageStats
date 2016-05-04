package com.srinivas.ele.network;


import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.srinivas.ele.utils.MyApplication;

/**
 * Created by Mudavath Srinivas on 19-11-2015.
 */
public class VolleySingleton {

    private static VolleySingleton sInstance=null;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private VolleySingleton() {
        mRequestQueue = new RequestQueue(new DiskBasedCache(MyApplication.getAppContext().getCacheDir(), ((1024 * 1024))), new BasicNetwork(new HurlStack()));
        mRequestQueue.start();
        mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache(){}) {
        };

    }

    public static VolleySingleton getInstance(){
        if(sInstance==null){
            sInstance=new VolleySingleton();
        }
        return sInstance;
    }

    public RequestQueue getRequestQueue(){
        return mRequestQueue;
    }

    public ImageLoader getImageLoader(){
        return mImageLoader;
    }

    public void addToRequestQueue(Request req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? MyApplication.TAG : tag);
        getRequestQueue().add(req);
    }

    public void addToRequestQueue(Request req) {
        req.setTag(MyApplication.TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
