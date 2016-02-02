package com.unm.rodolphe.unnouveaumonde;


import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;


public class RegistrationIntentService extends IntentService {

    public static final String TAG = "RegIntentService";


    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        try {

            synchronized (TAG) {
                // [DEMARRER enregistrement pour gcm]
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                InstanceID instanceID = InstanceID.getInstance(this);

                String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

                Log.i(TAG, "GCM Registration Token: " + token);

                sharedPreferences.edit().putString("token", token);

                // Si le token a déjà été enregistre pas la peine de le renvoyer
                if (!sharedPreferences.getBoolean(Constants.SENT_TOKEN_TO_SERVER, false))
                    Methods.sendRegistrationToServer(this);

                    sharedPreferences.edit().putBoolean(Constants.SENT_TOKEN_TO_SERVER, true).apply();
                    sharedPreferences.edit().putBoolean("serveurRegister", true).apply();

            }
        } catch (Exception e) {
            Log.d(TAG, "Echec de renouvellement du Token", e);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            sharedPreferences.edit().putBoolean(Constants.SENT_TOKEN_TO_SERVER, false).apply();
        }

        Intent registrationComplete = new Intent(Constants.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

}