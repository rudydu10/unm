package com.unm.rodolphe.unnouveaumonde;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.ProgressBar;


public class SplashScreen extends Activity {

    ProgressBar progressBar;
    boolean login, enfant, activite, tarif = false;
    private int taille_max = 100;
    private int progression, nbact, nbenf, nbtar = 0;
    private int maxtry = 3;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

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
                    while (progression < taille_max) {

                        if (activite && enfant && tarif)
                            progression = (taille_max / 4) * 3;

                        if (!login) {

                            //Si les données de connection stockée sont vide, démarrer l'activité Login
                            if (preferences.getString("USERNAME", "").contentEquals("") || preferences.getString("PASSWORD", "").contentEquals("") || !Methods.isOnline()) {
                                Intent loginActivite = new Intent(SplashScreen.this, LoginActivity.class);
                                startActivity(loginActivite);
                                finish();
                                break;
                            }
                            // Sinon si le login réussi on stocke l'id Parent dans Constants.java
                            else if (Methods.login(preferences.getString("USERNAME", ""), preferences.getString("PASSWORD", "")).contains(Constants.CODE_OK)) {
                                Constants.idParent = preferences.getString("ID", "0");
                                progression += (int) Math.round(0.2 * taille_max);
                                login = true;
                                progression += taille_max / 4;
                            }

                        } else if (!enfant && nbenf < maxtry) {
                            Constants.enfant = Methods.JSONToEnfant(Methods.getEnfants(Constants.idParent));
                            if (!Constants.enfant.isEmpty()) {
                                progression += (int) Math.round(0.2 * taille_max);
                                enfant = true;
                                if (nbenf == 0)
                                    progression += taille_max / 4;
                                else
                                    progression += ((taille_max / 4) / maxtry) * (maxtry - nbenf);
                                if (progression >= taille_max)
                                    break;
                            } else {
                                nbenf++;
                                progression += (taille_max / 4) / maxtry;
                            }
                        }
                        else if (!activite && nbact < maxtry && !Constants.rp_srv_act) {
                            String activ = Methods.getAllActivites();
                            if (!Constants.rp_srv_act)
                                Constants.activites = Methods.JSONToActivite(activ);
                            if (!Constants.activites.isEmpty()) {
                                progression += (int) Math.round(0.2 * taille_max);
                                activite = true;
                                if (nbact == 0)
                                    progression += taille_max / 4;
                                else
                                    progression += ((taille_max / 4) / maxtry) * (maxtry - nbact);
                                if (progression >= taille_max)
                                    break;
                            } else {
                                nbact++;
                                progression += (taille_max / 4) / maxtry;
                            }

                        } else if (!tarif && nbtar < maxtry) {
                            Constants.tarif = Methods.getTarifs(Constants.idParent);
                            if (Constants.tarif.getJournee() > 0) {
                                progression += (int) Math.round(0.1 * taille_max);
                                tarif = true;
                                if (nbtar == 0)
                                    progression += taille_max / 4;
                                else
                                    progression += ((taille_max / 4) / maxtry) * (maxtry - nbtar);
                                if (progression >= taille_max)
                                    break;
                            } else {
                                nbtar++;
                                progression += (taille_max / 4) / maxtry;
                            }

                        }

                        // Update de la barre
                        handler.post(new Runnable() {
                            public void run() {
                                progressBar.setProgress(progression);
                            }
                        });

                        Thread.sleep(600);
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

    @Override
    protected void onPause() {
        super.onPause();
    }
}