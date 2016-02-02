package com.unm.rodolphe.unnouveaumonde;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class Main extends AppCompatActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String UNREGISTER_URL = Constants.server_ADDRESS + Constants.unregister_PHP;
    private static final String REGISTER_URL = Constants.server_ADDRESS + Constants.register_PHP;

    private static final String TAG = "Main";

    Button bouton1;
    Button boutonStatus;
    Button boutonSiteweb;
    Button boutonProgramme;
    private BroadcastReceiver mRegistrationBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Actions a effectuer à la réception d'un message

            }
        };

        if(!preferences.getBoolean("registerServeur", false))
        {
            if (checkPlayServices()) {
                // Demarrer l'IntentService pour enregistrer l'application avec GCM.
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }
        }else
        {
            System.out.println("Test : " + preferences.getBoolean("serveurRegister", false));
            if(preferences.getBoolean("serveurRegister", false)) {
                Methods.sendUnregistrationToServer(this);
                preferences.edit().putBoolean("serveurRegister", false).apply();
            }
        }

        if (Methods.login(preferences.getString("USERNAME", ""), preferences.getString("PASSWORD", "")).contains(Constants.CODE_OK)) {
            Constants.idParent = preferences.getString("ID", "");
            if (Constants.premiereConnection) {
                Toast.makeText(Main.this, "Bonjour" + Methods.getParentFirstName(Constants.idParent), Toast.LENGTH_LONG).show();
                Constants.enfant = Methods.getEnfants(Constants.idParent);
                Constants.premiereConnection = false;
            }
        } else {
            Intent loginActivite = new Intent(Main.this, LoginActivity.class);
            startActivity(loginActivite);
            finish();
        }

        setContentView(R.layout.activity_main);

        bouton1 = (Button) findViewById(R.id.bouton1);
        boutonStatus = (Button) findViewById(R.id.boutonStatus);
        boutonSiteweb = (Button) findViewById(R.id.boutonSiteweb);
        boutonProgramme = (Button) findViewById(R.id.boutonProgramme);

        if(Constants.enfant.contains("null")){
            bouton1.setEnabled(false);
            boutonStatus.setEnabled(false);
        }

        bouton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent EnfantsActivite = new Intent(Main.this, Inscription.class);
                startActivity(EnfantsActivite);
            }
        });

        boutonStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent StatusActivite = new Intent(Main.this, StatusInscriptions.class);
                startActivity(StatusActivite);
            }
        });

        boutonSiteweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SiteWeb = new Intent(Main.this, web.class);
                startActivity(SiteWeb);
            }
        });

        boutonProgramme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Programme = new Intent(Main.this, Programme.class);
                startActivity(Programme);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constants.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu, menu);
        if(preferences.getBoolean("registerServeur", false))
        {
            menu.getItem(0).getSubMenu().setHeaderIcon(R.drawable.valide);
        }
        else {
            menu.getItem(0).getSubMenu().setHeaderIcon(R.drawable.invalide);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        switch (item.getItemId()) {
            case R.id.deconnection:

                final SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Intent loginActivity = new Intent(Main.this, LoginActivity.class);
                startActivity(loginActivity);
                Constants.premiereConnection = true;
                finish();
                return true;
            case R.id.notif:

                if(preferences.getBoolean("serverRegister", false))
                {
                    preferences.edit().putBoolean("registerServeur", false).apply();
                    Intent main = new Intent(this, Main.class);
                    startActivity(main);
                    finish();
                }
                else
                {
                    preferences.edit().putBoolean("registerServeur", true).apply();
                    Intent main = new Intent(this, Main.class);
                    startActivity(main);
                    finish();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Vérifier si notre utilisateur a l'application Google Play Service
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "Telephone non supporté.");
                finish();
            }
            return false;
        }
        return true;
    }

}
