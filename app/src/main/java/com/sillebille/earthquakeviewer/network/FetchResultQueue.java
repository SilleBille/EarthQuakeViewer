package com.sillebille.earthquakeviewer.network;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * A ResultQueue to process network requests via Volley library
 */
public class FetchResultQueue {
    private static FetchResultQueue mInstance;
    private Context mContext;
    private RequestQueue mRequestQueue;

    private FetchResultQueue(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    /**
     * Gets an instance of FetchResultQueue. Uses singleton pattern
     *
     * @param context the applicaiton context
     * @return the instance of FetchResultQueue
     */
    public static synchronized FetchResultQueue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new FetchResultQueue(context);
        }
        return mInstance;
    }

    /**
     * Create a new volley RequestQueue.
     *
     * @return the request queue
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }
}
