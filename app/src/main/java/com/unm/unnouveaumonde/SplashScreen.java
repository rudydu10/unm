package com.unm.unnouveaumonde;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;


public class SplashScreen extends AppCompatActivity {

    ProgressBar progressBar;
    TextView percent;

    boolean login, enfant, activite, tarif = false;
    private int taille_max = 100;
    private double progression = 0;
    private int nbact, nbenf, nbtar = 0;
    private int maxtry = 3;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        percent = (TextView) findViewById(R.id.percent);

        Constants.rp_srv_enf = false;
        Constants.rp_srv_act = false;

        if (Constants.tarif.getJournee() > 0)
            tarif = true;
        if (!Constants.activites.isEmpty())
            activite = true;
        if (!Constants.enfant.isEmpty())
            enfant = true;

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Démarrer l'initialisation en background
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {

                    if (activite) {
                        progression += (taille_max / 4);
                        // Update de la barre
                        handler.post(new Runnable() {
                            public void run() {
                                progressBar.setProgress((int) Math.round(progression));
                                percent.setText(String.format("%1$d %%", (int) Math.round(progression)));
                            }
                        });
                    }

                    if (enfant) {
                        progression += (taille_max / 4);
                        // Update de la barre
                        handler.post(new Runnable() {
                            public void run() {
                                progressBar.setProgress((int) Math.round(progression));
                                percent.setText(String.format("%1$d %%", (int) Math.round(progression)));
                            }
                        });
                    }

                    if (tarif) {
                        progression += (taille_max / 4);
                        // Update de la barre
                        handler.post(new Runnable() {
                            public void run() {
                                progressBar.setProgress((int) Math.round(progression));
                                percent.setText(String.format("%1$d %%", (int) Math.round(progression)));
                            }
                        });
                    }

                    while (progression < taille_max) {

                        if (!login) {

                            //Si les données de connection stockée sont vide, démarrer l'activité Login
                            if (preferences.getString("USERNAME", "").contentEquals("") || preferences.getString("PASSWORD", "").contentEquals("") || !Methods.isOnline() || !Methods.login(preferences.getString("USERNAME", ""), preferences.getString("PASSWORD", "")).contains(Constants.CODE_OK)) {
                                Intent loginActivite = new Intent(SplashScreen.this, LoginActivity.class);
                                startActivity(loginActivite);
                                finish();
                                break;
                            }
                            // Sinon si le login réussi on stocke l'id Parent dans Constants.java
                            else {
                                login = true;
                                progression = doProgress(progression, taille_max, maxtry, 0);
                            }

                        } else if (!enfant && nbenf < maxtry && !Constants.rp_srv_enf) {
                            String enf = Methods.getEnfants(Constants.idParent);
                            if (!Constants.rp_srv_enf) {
                                Constants.enfant = Methods.JSONToEnfant(enf);
                                if (!Constants.enfant.isEmpty()) {
                                    enfant = true;
                                    progression = doProgress(progression, taille_max, maxtry, 0);
                                    System.out.println("Enfant ok ");
                                } else {
                                    nbenf++;
                                    progression = doProgress(progression, taille_max, maxtry, nbenf);
                                    System.out.println("Enfant pas ok : " + nbenf);
                                }
                            } else
                                progression = doProgress(progression, taille_max, maxtry, 0);
                        } else if (!activite && nbact < maxtry && !Constants.rp_srv_act) {
                            String activ = Methods.getAllActivites();
                            if (!Constants.rp_srv_act) {
                                Constants.activites = Methods.JSONToActivite(activ);

                                if (!Constants.activites.isEmpty()) {
                                    activite = true;
                                    progression = doProgress(progression, taille_max, maxtry, 0);
                                    System.out.println("Activite ok ");
                                } else {
                                    nbact++;
                                    progression = doProgress(progression, taille_max, maxtry, nbact);
                                    System.out.println("Activite pas ok : " + nbact);
                                }
                            } else
                                progression = doProgress(progression, taille_max, maxtry, 0);

                        } else if (!tarif && nbtar < maxtry) {
                            Constants.tarif = Methods.getTarifs(Constants.idParent);
                            if (Constants.tarif.getJournee() > 0) {
                                tarif = true;
                                progression = doProgress(progression, taille_max, maxtry, 0);
                                System.out.println("Tarif ok ");
                            } else {
                                nbtar++;
                                progression = doProgress(progression, taille_max, maxtry, 0);
                                System.out.println("Tarif pas ok : " + nbtar);
                            }

                        }

                        // Update de la barre
                        handler.post(new Runnable() {
                            public void run() {
                                progressBar.setProgress((int) Math.round(progression));
                                percent.setText(String.format("%1$d %%", (int) Math.round(progression)));
                            }
                        });


                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);

                if (login) {
                    intent = new Intent(SplashScreen.this, Main.class);
                }

                startActivity(intent);
                finish();
            }
        });

        thread.start();
    }

    private double doProgress(double progression, int taillemax, int maxtry, int nb) {
        double result;

        if (nb != 0)
            result = (taillemax / 4) / maxtry * nb + progression;
        else
            result = taillemax / 4 + progression;
        return result;
    }
}