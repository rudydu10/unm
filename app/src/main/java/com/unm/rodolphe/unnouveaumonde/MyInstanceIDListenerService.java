package com.unm.rodolphe.unnouveaumonde;


import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;


public class MyInstanceIDListenerService extends InstanceIDListenerService {

    private static final String TAG = "MyInstanceIDLS";

    @Override
    public void onTokenRefresh() {
        // Vérifier la mise a jour du token et en notifier le serveur d'application (si applicable).
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}