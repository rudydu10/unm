package com.unm.rodolphe.unnouveaumonde;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TarifActivity extends Activity {

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


}
