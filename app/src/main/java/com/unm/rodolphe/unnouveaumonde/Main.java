package com.unm.rodolphe.unnouveaumonde;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Main extends AppCompatActivity {

    Button bouton1;
    Button boutonStatus;
    Button boutonSiteweb;
    Button boutonProgramme;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if(Methods.login(preferences.getString("USERNAME", ""), preferences.getString("PASSWORD", "")).contains(Constants.CODE_OK))
        {
            Constants.idParent = preferences.getString("ID","");
            if(Constants.premiereConnection)
            {
                Toast.makeText(Main.this, "Bonjour" + Methods.getParentFirstName(Constants.idParent), Toast.LENGTH_LONG).show();
                Constants.premiereConnection = false;
            }
        }
        else
        {
            Intent loginActivite = new Intent(Main.this, LoginActivity.class);
            startActivity(loginActivite);
            finish();
        }

        setContentView(R.layout.activity_main);

        bouton1 = (Button) findViewById(R.id.bouton1);
        boutonStatus = (Button) findViewById(R.id.boutonStatus);
        boutonSiteweb = (Button) findViewById(R.id.boutonSiteweb);
        boutonProgramme = (Button) findViewById(R.id.boutonProgramme);

        bouton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent EnfantsActivite = new Intent(Main.this, Inscription.class);
                startActivity(EnfantsActivite);
            }
        });

        boutonStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent StatusActivite = new Intent(Main.this, StatusInscriptions.class);
                startActivity(StatusActivite);
            }
        });

        boutonSiteweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SiteWeb = new Intent(Main.this, web.class);
                startActivity(SiteWeb);
            }
        });

        boutonProgramme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Programme = new Intent(Main.this, Programme.class);
                startActivity(Programme);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.deconnection:
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                final SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Intent loginActivity = new Intent(Main.this, LoginActivity.class);
                startActivity(loginActivity);
                Constants.premiereConnection = true;
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
