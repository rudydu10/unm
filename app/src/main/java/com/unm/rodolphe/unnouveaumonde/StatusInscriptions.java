package com.unm.rodolphe.unnouveaumonde;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class StatusInscriptions extends Activity {
    Spinner spinnerenfant;
    Spinner spinneractivite;
    Hashtable htenfant = Methods.JSONToHashtable(Methods.getEnfants(Constants.idParent), "id", "enfant");
    Hashtable htactivite;

    Button boutonRetour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_inscriptions);
        spinnerenfant = (Spinner) findViewById(R.id.spinnerEnfant2);
        spinneractivite = (Spinner) findViewById(R.id.spinnerActivites2);
        List listenfant = new ArrayList();
        Enumeration e1 = htenfant.elements();
        boutonRetour = (Button) findViewById(R.id.boutonRetour2);

        addListenerOnSpinnerEnfant();
        addListenerOnSpinnerActivite();
        addListenerOnButton2();

        while (e1.hasMoreElements())
        {
            listenfant.add(e1.nextElement());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listenfant);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerenfant.setAdapter(adapter);
    }


    private void addListenerOnSpinnerEnfant()
    {
        spinnerenfant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    htactivite = Methods.JSONToHashtable(Methods.sendPOST(new URL(Constants.server_ADDRESS + Constants.inscriptions_PHP), "inscriptions", "idactivite", "idenfant", String.valueOf(spinnerenfant.getSelectedItem())), "id", "activite");
                    List listactivite = new ArrayList();
                    listactivite.clear();
                    Enumeration e2 = htactivite.elements();

                    while (e2.hasMoreElements()) {
                        listactivite.add(e2.nextElement());
                    }
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(StatusInscriptions.this, android.R.layout.simple_spinner_item, listactivite);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinneractivite.setAdapter(adapter2);
                } catch (IOException e) {
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
                        textStatus.setText("Inscription validée");
                    }
                    else if(valide.contains("0"))
                    {
                        textStatus.setVisibility(View.VISIBLE);
                        textStatus.setText("Inscription en attente");
                    }
                    else
                    {
                        textStatus.setVisibility(View.INVISIBLE);
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
}
