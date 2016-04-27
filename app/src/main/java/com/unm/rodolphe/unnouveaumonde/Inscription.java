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
    Button buttonSubmit;
    Button boutonRetour;
    Button buttonCancel;
    Button buttonDescription;
    TextView datedebut;
    TextView datefin;
    TextView datedebuttext;
    TextView datefintext;

    String listAselectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        boutonRetour = (Button) findViewById(R.id.boutonRetour1);
        buttonCancel = (Button) findViewById(R.id.boutonAnnuler);
        buttonSubmit = (Button) findViewById(R.id.buttonValideInscription);
        buttonDescription = (Button) findViewById(R.id.buttonDescription);

        datedebut = (TextView) findViewById(R.id.dateDebut);
        datefin = (TextView) findViewById(R.id.dateFin);
        datedebuttext = (TextView) findViewById(R.id.dateDebutText);
        datefintext = (TextView) findViewById(R.id.dateFinText);

        List<Object> listEnfant = new ArrayList<>();
        List<Object> listActivite = new ArrayList<>();

        datedebuttext.setVisibility(View.INVISIBLE);
        datefintext.setVisibility(View.INVISIBLE);
        datedebut.setVisibility(View.INVISIBLE);
        datefin.setVisibility(View.INVISIBLE);

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

        //Bouton Description
        addListenerOnButton4();
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
                                Constants.activites = Methods.JSONToActivite(Methods.getAllActivites());
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
                buttonDescription.setEnabled(false);
                listA.setVisibility(View.GONE);
                listE.setVisibility(View.VISIBLE);

                listAselectedItem = "";

                datedebut.setText("");
                datedebut.setVisibility(View.INVISIBLE);
                datefin.setVisibility(View.INVISIBLE);
                datefin.setText("");
            }
        });
    }

    //Bouton Description
    private void addListenerOnButton4() {
        buttonDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new AlertDialog.Builder(Inscription.this).setMessage(Methods.sendPOST(new URL(Constants.server_ADDRESS + Constants.activite_PHP), "activite", "description", "activite", listAselectedItem))
                            .setTitle("Description :").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create().show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addListenerOnEnfant()
    {
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
        listA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for (Activite activite : Constants.activites)
                    if (listA.getItemAtPosition(position).equals(activite.getActivite()))
                        idActivite = String.valueOf(activite.getId());
                try {
                    listAselectedItem = listA.getItemAtPosition(position).toString();
                    buttonDescription.setEnabled(true);
                    datedebut.setVisibility(View.VISIBLE);
                    datedebuttext.setVisibility(View.VISIBLE);
                    datefin.setVisibility(View.VISIBLE);
                    datefintext.setVisibility(View.VISIBLE);
                    datedebut.setText(Methods.sendPOST(new URL(Constants.server_ADDRESS + Constants.activite_PHP), "activite", "datedebut", "activite", String.valueOf(listA.getItemAtPosition(position))).replaceAll("\t", ""));
                    datefin.setText(Methods.sendPOST(new URL(Constants.server_ADDRESS + Constants.activite_PHP), "activite", "datefin", "activite", String.valueOf(listA.getItemAtPosition(position))).replaceAll("\t", ""));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                buttonSubmit.setEnabled(true);
            }
        });
    }
}
