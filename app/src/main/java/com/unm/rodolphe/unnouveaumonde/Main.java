package com.unm.rodolphe.unnouveaumonde;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.unm.rodolphe.unnouveaumonde.com.unm.rodolphe.unnouveaumonde.GCM.RegistrationIntentService;


public class Main extends AppCompatActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    Button bouton1;
    Button boutonStatus;
    Button boutonSiteweb;
    Button boutonProgramme;
    private BroadcastReceiver mRegistrationBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Actions a effectuer à la réception d'un message

            }
        };

        if (checkPlayServices()) {
                // Demarrer l'IntentService pour enregistrer l'application avec GCM.
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }

        if (preferences.getString("USERNAME", "").contentEquals("") || preferences.getString("PASSWORD", "").contentEquals("")) {
            Intent loginActivite = new Intent(Main.this, LoginActivity.class);
            startActivity(loginActivite);
            finish();
        }
        else if (Methods.login(preferences.getString("USERNAME", ""), preferences.getString("PASSWORD", "")).contains(Constants.CODE_OK)) {
            Constants.idParent = preferences.getString("ID", "0");
            if (Constants.premiereConnection) {
                Toast.makeText(Main.this, getResources().getString(R.string.hello) + Methods.getParentFirstName(String.valueOf(Constants.idParent)), Toast.LENGTH_LONG).show();
                    Constants.enfant = Methods.JSONToEnfant(Methods.getEnfants(Constants.idParent));
                    Constants.activites = Methods.JSONToActivite(Methods.getAllActivites());
                Constants.premiereConnection = false;
            }
        }
        setContentView(R.layout.activity_main);

        bouton1 = (Button) findViewById(R.id.bouton1);
        boutonStatus = (Button) findViewById(R.id.boutonStatus);
        boutonSiteweb = (Button) findViewById(R.id.boutonSiteweb);
        boutonProgramme = (Button) findViewById(R.id.boutonProgramme);

        if (Constants.enfant.isEmpty() || Constants.activites.isEmpty()) {
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
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.webserver_ADDRESS));
                startActivity(intent);
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
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu, menu);

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
                finish();
            }
            return false;
        }
        return true;
    }

}
