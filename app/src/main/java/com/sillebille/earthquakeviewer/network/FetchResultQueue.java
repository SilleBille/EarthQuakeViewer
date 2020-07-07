package com.sillebille.earthquakeviewer.network;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/*
Singleton class to instantiate Volley request queue
 */
public class FetchResultQueue {
    private static FetchResultQueue mInstance;
    private Context mContext;
    private RequestQueue mRequestQueue;

    private FetchResultQueue(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized FetchResultQueue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new FetchResultQueue(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }
}
