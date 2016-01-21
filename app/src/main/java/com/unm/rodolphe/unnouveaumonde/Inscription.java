package com.unm.rodolphe.unnouveaumonde;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class Inscription extends Activity {

    Spinner spinnerEnfant;
    Spinner spinnerActivite;
    ListView listE;
    ListView listA;
    String idActivite = "0";
    String idEnfant = "0";
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
        spinnerEnfant.setVisibility(View.GONE);
        spinnerActivite.setVisibility(View.GONE);
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


        listE = (ListView)findViewById(R.id.ListView01);
        listE.setAdapter(adapterEnfant);

        listA = (ListView)findViewById(R.id.ListView02);
        listA.setAdapter(adapterActivite);

        listA.setVisibility(View.GONE);

        addListenerOnEnfant();
        addListenerOnActivite();
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

        //spinnerEnfant = (Spinner) findViewById(R.id.spinnerEnfant);
        buttonSubmit = (Button) findViewById(R.id.buttonValideInscription);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (idEnfant.length() > 0 && idActivite.length() > 0) {

                    try {
                        //idActivite = Methods.getActiviteId(String.valueOf(spinnerActivite.getSelectedItem()));
                        //idEnfant = Methods.JSONtoStringID(getEnfantId(String.valueOf(spinnerEnfant.getSelectedItem())));
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
                    System.out.println("Valeur enfant" + idEnfant.length() + " " + idActivite.length());
                    Toast.makeText(Inscription.this, "Erreur, champ vide.", Toast.LENGTH_LONG).show();
                    System.out.println("");
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
                    Toast.makeText(Inscription.this, idEnfant, Toast.LENGTH_LONG).show();
                    listA.setVisibility(View.VISIBLE);
                //try {
                    //texteDescription.setText(Methods.sendPOST(new URL(Constants.server_ADDRESS + Constants.activite_PHP), "activite", "description", "activite", String.valueOf(spinnerActivite.getSelectedItem())));
                /*} catch (IOException e) {
                    e.printStackTrace();
                }*/
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
                System.out.print(idActivite);
                Toast.makeText(Inscription.this, idActivite, Toast.LENGTH_LONG).show();
                try {
                texteDescription.setText(Methods.sendPOST(new URL(Constants.server_ADDRESS + Constants.activite_PHP), "activite", "description", "activite", String.valueOf(listA.getItemAtPosition(position))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        });


    }

}
