package com.unm.unnouveaumonde;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.unm.rodolphe.unnouveaumonde.R;
import com.unm.unnouveaumonde.objects.Tarif;

import java.util.ArrayList;

public class TarifActivity extends AppCompatActivity {

    TextView jour;
    TextView jourR;
    TextView demiJour;
    TextView demiJourR;
    TextView semaine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarif);

        jour = (TextView) findViewById(R.id.JTarif);
        jourR = (TextView) findViewById(R.id.JRTarif);
        demiJour = (TextView) findViewById(R.id.DJTarif);
        demiJourR = (TextView) findViewById(R.id.DJRTarif);
        semaine = (TextView) findViewById(R.id.STarif);

        jour.setText(String.valueOf(Constants.tarif.getJournee()));
        jourR.setText(String.valueOf(Constants.tarif.getJourneeRepas()));
        demiJour.setText(String.valueOf(Constants.tarif.getDemiejournee()));
        demiJourR.setText(String.valueOf(Constants.tarif.getDemiejourneeRepas()));
        semaine.setText(String.valueOf(Constants.tarif.getSemaine()));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu, menu);

        return true;
    }

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
                Intent loginActivity = new Intent(TarifActivity.this, LoginActivity.class);
                startActivity(loginActivity);
                Constants.premiereConnection = true;
                Main.getInstance().finish();
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
