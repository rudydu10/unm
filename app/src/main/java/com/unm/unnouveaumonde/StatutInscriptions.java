package com.unm.unnouveaumonde;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.unm.unnouveaumonde.objects.Activite;
import com.unm.unnouveaumonde.objects.Enfant;
import com.unm.unnouveaumonde.objects.Tarif;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StatutInscriptions extends AppCompatActivity {
    Spinner spinnerenfant;
    Spinner spinneractivite;

    Button boutonRetour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_inscriptions);
        spinnerenfant = (Spinner) findViewById(R.id.spinnerEnfant2);
        spinneractivite = (Spinner) findViewById(R.id.spinnerActivites2);
        boutonRetour = (Button) findViewById(R.id.boutonRetour2);

        addListenerOnSpinnerEnfant();
        addListenerOnSpinnerActivite();
        addListenerOnButton2();

        List<Object> listEnfant = new ArrayList<>();

        for (Enfant enfant : Constants.enfant)
            listEnfant.add(enfant.getEnfant());

        ArrayAdapter<Object> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listEnfant);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerenfant.setAdapter(adapter);
    }


    private void addListenerOnSpinnerEnfant()
    {
        spinnerenfant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //List<Object> activite = Methods.JSONToActivite(Methods.sendPOST(new URL(Constants.server_ADDRESS + Constants.inscriptions_PHP), "inscriptions", "idactivite", "idenfant", String.valueOf(spinnerenfant.getSelectedItem())), "id", "activite");
                //activite.clear();
                try{
                    List<Object> listActivite = new ArrayList<>();
                    for (Activite activite : Methods.JSONToActivite(Methods.sendPOST(new URL(Constants.server_ADDRESS + Constants.inscriptions_PHP), "inscriptions", "idactivite", "idenfant", String.valueOf(spinnerenfant.getSelectedItem()))))
                    listActivite.add(activite.getActivite());


                    ArrayAdapter<Object> adapter2 = new ArrayAdapter<>(StatutInscriptions.this, android.R.layout.simple_spinner_item, listActivite);
                    adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    spinneractivite.setAdapter(adapter2);
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void addListenerOnSpinnerActivite()
    {
        spinneractivite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    TextView textStatus = (TextView) findViewById(R.id.textStatus);
                    textStatus.setVisibility(View.INVISIBLE);
                    String valide = Methods.sendPOST(new URL(Constants.server_ADDRESS + Constants.inscriptions_PHP), "inscriptions", "valide", String.valueOf(spinneractivite.getSelectedItem()), String.valueOf(spinnerenfant.getSelectedItem()));
                    if(valide.contains("1"))
                    {
                        textStatus.setVisibility(View.VISIBLE);
                        textStatus.setText(R.string.inscriptionvalide);
                    }
                    else if(valide.contains("0"))
                    {
                        textStatus.setVisibility(View.VISIBLE);
                        textStatus.setText(R.string.inscriptionattente);
                    }
                    else
                    {
                        textStatus.setVisibility(View.VISIBLE);
                        textStatus.setText(R.string.pasinscription);
                    }
                }catch(IOException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void addListenerOnButton2() {
        boutonRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                Intent loginActivity = new Intent(StatutInscriptions.this, LoginActivity.class);
                startActivity(loginActivity);
                Constants.premiereConnection = true;
                Main.getInstance().finish();
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
