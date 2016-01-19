package com.unm.rodolphe.unnouveaumonde;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class Inscription extends Activity {

    Spinner spinnerEnfant;
    Spinner spinnerActivite;
    TextView texteDescription;
    Button buttonSubmit;
    Button boutonRetour;
    Hashtable ht1 = Methods.JSONToHashtable(Methods.getEnfants(Constants.idParent), "id", "enfant");
    Hashtable ht2 = Methods.JSONToHashtable(Methods.getAllActivites(), "id", "activite");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        boutonRetour = (Button) findViewById(R.id.boutonRetour1);

        spinnerEnfant = (Spinner) findViewById(R.id.spinnerEnfant);
        spinnerActivite = (Spinner) findViewById(R.id.spinnerActivite);
        List listEnfant = new ArrayList();
        List listActivite = new ArrayList();
        Enumeration e1 = ht1.elements();
        Enumeration e2 = ht2.elements();

        while (e1.hasMoreElements())
        {
            listEnfant.add(e1.nextElement());
        }

        while (e2.hasMoreElements())
        {
            listActivite.add(e2.nextElement());
        }



        ArrayAdapter<String> adapterEnfant = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listEnfant);
        adapterEnfant.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEnfant.setAdapter(adapterEnfant);

        ArrayAdapter<String> adapterActivite = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listActivite);
        adapterActivite.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerActivite.setAdapter(adapterActivite);

        addListenerOnSpinner();
        addListenerOnButton();
        addListenerOnButton2();
    }

    private void addListenerOnButton2() {
        boutonRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MainActivite = new Intent(Inscription.this, Main.class);
                startActivity(MainActivite);
                finish();
            }
        });
    }

    private void addListenerOnButton()
    {
        spinnerEnfant = (Spinner) findViewById(R.id.spinnerEnfant);
        buttonSubmit = (Button) findViewById(R.id.buttonValideInscription);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (String.valueOf(spinnerEnfant.getSelectedItem()).length() > 1) {

                    try {
                        String idActivite = Methods.getActiviteId(String.valueOf(spinnerActivite.getSelectedItem()));
                        String idEnfant = Methods.JSONtoStringID(getEnfantId(String.valueOf(spinnerEnfant.getSelectedItem())));
                        String response = Methods.sendPOST(new URL(Constants.server_ADDRESS + Constants.inscription_PHP), "inscription", "select", "where", idActivite + ":" + idEnfant);
                        if (response.contains(Constants.CODE_OK)) {
                            Toast.makeText(Inscription.this, "Inscription correctement enregistrée.", Toast.LENGTH_LONG).show();
                            Intent retourMenu = new Intent(Inscription.this, Main.class);
                            startActivity(retourMenu);
                            finish();
                        }
                        else if(response.contains(Constants.CODE_ERROR_DUAL_ENTRY))
                        {
                            Toast.makeText(Inscription.this, "Erreur, enfant deja inscrit", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(Inscription.this, "Erreur, contactez l'administrateur.", Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(Inscription.this, "Erreur, champ vide.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private String getEnfantId(String enfant)
    {
        try
        {
            String response = Methods.sendPOST(new URL(Constants.server_ADDRESS + Constants.enfant_PHP), "enfant", "id", "enfant", enfant);
            return response;

        }catch(IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private void addListenerOnSpinner()
    {
        texteDescription = (TextView) findViewById(R.id.texteDescription);
        spinnerActivite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    texteDescription.setText(Methods.sendPOST(new URL(Constants.server_ADDRESS + Constants.activite_PHP), "activite", "description", "activite", String.valueOf(spinnerActivite.getSelectedItem())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

}
