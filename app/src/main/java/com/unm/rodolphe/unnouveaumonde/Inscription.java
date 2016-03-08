package com.unm.rodolphe.unnouveaumonde;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class Inscription extends Activity {

    ListView listE;
    ListView listA;
    String idActivite = "0";
    String idEnfant = "0";
    TextView texteDescription;
    Button buttonSubmit;
    Button boutonRetour;
    Button buttonCancel;
    Hashtable ht1 = Methods.JSONToHashtable(Constants.enfant, "id", "enfant");
    Hashtable ht2 = Methods.JSONToHashtable(Methods.getAllActivites(), "id", "activite");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        boutonRetour = (Button) findViewById(R.id.boutonRetour1);
        buttonCancel = (Button) findViewById(R.id.boutonAnnuler);

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


        ArrayAdapter<String> adapterActivite = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listActivite);
        adapterActivite.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        listE = (ListView)findViewById(R.id.ListEnfant);
        listE.setAdapter(adapterEnfant);

        listA = (ListView)findViewById(R.id.ListActivite);
        listA.setAdapter(adapterActivite);

        listA.setVisibility(View.GONE);

        addListenerOnEnfant();
        addListenerOnActivite();
        addListenerOnButton();
        buttonSubmit.setEnabled(false);
        buttonCancel.setEnabled(false);
        addListenerOnButton2();
        addListenerOnButton3();
    }

    private void addListenerOnButton2() {
        boutonRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addListenerOnButton3() {
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listE.clearChoices();
                listA.clearChoices();

                buttonSubmit.setEnabled(false);
                buttonCancel.setEnabled(false);
                listA.setVisibility(View.GONE);
                listE.setVisibility(View.VISIBLE);
            }
        });
    }

    private void addListenerOnButton()
    {
        buttonSubmit = (Button) findViewById(R.id.buttonValideInscription);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {


                    if (idEnfant.length() > 0 && idActivite.length() > 0) {

                        try {
                            String response = Methods.sendPOST(new URL(Constants.server_ADDRESS + Constants.inscription_PHP), "inscription", "select", "where", idActivite + ":" + idEnfant);
                            if (response.contains(Constants.CODE_OK)) {
                                Toast.makeText(Inscription.this, "Inscription correctement enregistrée.", Toast.LENGTH_LONG).show();
                                finish();
                            } else if (response.contains(Constants.CODE_ERROR_DUAL_ENTRY)) {
                                Toast.makeText(Inscription.this, "Erreur, enfant deja inscrit", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Inscription.this, "Erreur, contactez l'administrateur.", Toast.LENGTH_LONG).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Valeur enfant" + idEnfant.length() + " " + idActivite.length());
                        Toast.makeText(Inscription.this, "Erreur, champ vide.", Toast.LENGTH_LONG).show();
                        System.out.println("");
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(Inscription.this, "Erreur de connexion.", Toast.LENGTH_LONG).show();
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

    private void addListenerOnEnfant()
    {
        texteDescription = (TextView) findViewById(R.id.texteDescription);
        listE.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                idEnfant = Methods.JSONtoStringID(getEnfantId(String.valueOf(listE.getItemAtPosition(position))));
                System.out.print(idEnfant);
                listE.setVisibility(View.GONE);
                listA.setVisibility(View.VISIBLE);
                buttonCancel.setEnabled(true);

            }
        });
    }

    private void addListenerOnActivite()
    {
        texteDescription = (TextView) findViewById(R.id.texteDescription);
        listA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                idActivite = Methods.getActiviteId(String.valueOf((listA.getItemAtPosition(position))));
                try {

                    //TODO integrer DateD et DateF sur le layout
                texteDescription.setText(Methods.sendPOST(new URL(Constants.server_ADDRESS + Constants.activite_PHP), "activite", "description", "activite", String.valueOf(listA.getItemAtPosition(position))));
                    String DateD = Methods.sendPOST(new URL(Constants.server_ADDRESS + Constants.activite_PHP), "activite", "datedebut", "activite", String.valueOf(listA.getItemAtPosition(position)));
                    String DateF = Methods.sendPOST(new URL(Constants.server_ADDRESS + Constants.activite_PHP), "activite", "datefin", "activite", String.valueOf(listA.getItemAtPosition(position)));
                    System.out.println("Depart : " + DateD + " Fin : " + DateF);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                buttonSubmit.setEnabled(true);
            }
        });
    }
}
