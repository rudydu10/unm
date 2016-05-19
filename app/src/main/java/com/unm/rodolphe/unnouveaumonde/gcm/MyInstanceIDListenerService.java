package com.unm.rodolphe.unnouveaumonde.gcm;


import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;


public class MyInstanceIDListenerService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        // Vérifier la mise a jour du token et en notifier le serveur d'application (si applicable).
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}