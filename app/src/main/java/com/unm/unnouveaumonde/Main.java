package com.unm.unnouveaumonde;

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
import com.unm.rodolphe.unnouveaumonde.R;
import com.unm.unnouveaumonde.gcm.RegistrationIntentService;
import com.unm.unnouveaumonde.objects.Tarif;

import java.util.ArrayList;


public class Main extends AppCompatActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    static Main main;
    Button bouton1;
    Button boutonStatus;
    Button boutonSiteweb;
    Button boutonProgramme;
    Button boutonTarif;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    public static Main getInstance() {
        return main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        main = this;

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


            //On verifie si c'est la première fois que l'utilisateur arrive sur Main.java depuis le lancement de l'application afin de lui dire bonjour
            if (Constants.premiereConnection) {
                Toast.makeText(Main.this, getResources().getString(R.string.hello) + Methods.getParentFirstName(String.valueOf(Constants.idParent)), Toast.LENGTH_LONG).show();
                Constants.premiereConnection = false;
            }

        setContentView(R.layout.activity_main);

        bouton1 = (Button) findViewById(R.id.bouton1);
        boutonStatus = (Button) findViewById(R.id.boutonStatus);
        boutonSiteweb = (Button) findViewById(R.id.boutonSiteweb);
        boutonProgramme = (Button) findViewById(R.id.boutonProgramme);
        boutonTarif = (Button) findViewById(R.id.boutonTarif);

        //Vérifier si les tableaux d'enfant et d'activités ne sont pas vide et bloquer les boutons les utilisants le cas échéant
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
                Intent StatusActivite = new Intent(Main.this, StatutInscriptions.class);
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

        boutonTarif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Tarif = new Intent(Main.this, TarifActivity.class);
                startActivity(Tarif);
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


    //Vérifier si notre utilisateur a l'application Google Play Service

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Création du menu de préférence pour la déconnection

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        switch (item.getItemId()) {
            case R.id.deconnection:

                final SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Constants.premiereConnection = true;
                Constants.enfant = new ArrayList<>();
                Constants.tarif = new Tarif(0, 0, 0, 0, 0);
                Constants.idParent = null;
                Intent loginActivity = new Intent(Main.this, LoginActivity.class);
                startActivity(loginActivity);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

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
