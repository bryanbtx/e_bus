package com.e_bus.e_bus;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton myInstance;
    private RequestQueue myRequestQueue;
    private static Context myCtx;

    private VolleySingleton(Context context) {
        myCtx = context;
        myRequestQueue = getRequestQueue();
    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (myInstance == null) {
            myInstance = new VolleySingleton(context);
        }
        return myInstance;
    }

    public RequestQueue getRequestQueue() {
        if (myRequestQueue == null) {
            myRequestQueue = Volley.newRequestQueue(myCtx.getApplicationContext());
        }
        return myRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
