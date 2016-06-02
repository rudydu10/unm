package com.unm.unnouveaumonde.gcm;


import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.unm.unnouveaumonde.Methods;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    @Override
    public void onMessageReceived(String from, Bundle data) {

        String message = data.getString("message");
        String title = data.getString("title");

        Log.d(TAG, "De : " + from);
        Log.d(TAG, "Message : " + message);

        Methods.sendNotification(this, message, title);
    }
}