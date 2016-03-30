package com.unm.rodolphe.unnouveaumonde;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.unm.rodolphe.unnouveaumonde.Objects.Activite;
import com.unm.rodolphe.unnouveaumonde.Objects.Enfant;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        boutonRetour = (Button) findViewById(R.id.boutonRetour1);
        buttonCancel = (Button) findViewById(R.id.boutonAnnuler);
        buttonSubmit = (Button) findViewById(R.id.buttonValideInscription);
        List<Object> listEnfant = new ArrayList<>();
        List<Object> listActivite = new ArrayList<>();

        for (Enfant enfant : Constants.enfant)
            listEnfant.add(enfant.getEnfant());

        for (Activite activite : Constants.activites)
            listActivite.add(activite.getActivite());

        ArrayAdapter<Object> adapterEnfant = new ArrayAdapter<>(this, R.layout.listviewinscription, listEnfant);
        adapterEnfant.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        ArrayAdapter<Object> adapterActivite = new ArrayAdapter<>(this, R.layout.listviewinscription, listActivite);
        adapterActivite.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        listE = (ListView)findViewById(R.id.ListEnfant);
        listE.setAdapter(adapterEnfant);

        listA = (ListView)findViewById(R.id.ListActivite);
        listA.setAdapter(adapterActivite);

        listA.setVisibility(View.GONE);

        addListenerOnEnfant();
        addListenerOnActivite();

        buttonSubmit.setEnabled(false);
        buttonCancel.setEnabled(false);

        //Bouton Valider
        addListenerOnButton1();

        //Bouton Menu Principal
        addListenerOnButton2();

        //Bouton Annuler
        addListenerOnButton3();
    }

    //Bouton Valider
    private void addListenerOnButton1()
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
                                Toast.makeText(Inscription.this, getResources().getString(R.string.signingok), Toast.LENGTH_LONG).show();
                                finish();
                            } else if (response.contains(Constants.CODE_ERROR_DUAL_ENTRY)) {
                                Toast.makeText(Inscription.this, getResources().getString(R.string.errorchildsigning), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Inscription.this, getResources().getString(R.string.errorcontactadministrator), Toast.LENGTH_LONG).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(Inscription.this, getResources().getString(R.string.errorfieldempty), Toast.LENGTH_LONG).show();
                        System.out.println("");
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(Inscription.this, getResources().getString(R.string.errorconnexion), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    //Bouton Menu Principal
    private void addListenerOnButton2() {
        boutonRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //Bouton Annuler
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
                texteDescription.setText("");
            }
        });
    }

    private void addListenerOnEnfant()
    {
        texteDescription = (TextView) findViewById(R.id.texteDescription);
        listE.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for (Enfant enfant : Constants.enfant)
                    if (listE.getItemAtPosition(position).equals(enfant.getEnfant()))
                        idEnfant = String.valueOf(enfant.getId());
                listE.setVisibility(View.GONE);
                listA.setVisibility(View.VISIBLE);
                buttonCancel.setEnabled(true);

            }
        });
    }

    private void addListenerOnActivite()
    {
        texteDescription = (TextView) findViewById(R.id.texteDescription);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        texteDescription.setTextSize(22);
        texteDescription.setHeight(50);
        listA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for (Activite activite : Constants.activites)
                    if (listA.getItemAtPosition(position).equals(activite.getActivite()))
                        idActivite = String.valueOf(activite.getId());
                try {

                    //TODO integrer DateD et DateF sur le layout
                texteDescription.setText(Methods.sendPOST(new URL(Constants.server_ADDRESS + Constants.activite_PHP), "activite", "description", "activite", String.valueOf(listA.getItemAtPosition(position))));

                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    alertDialogBuilder.setMessage(Methods.sendPOST(new URL(Constants.server_ADDRESS + Constants.activite_PHP), "activite", "description", "activite", String.valueOf(listA.getItemAtPosition(position))))
                            .setTitle("Description :");

                    alertDialogBuilder.create();
                    alertDialogBuilder.show();

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
