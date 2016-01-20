package com.unm.rodolphe.unnouveaumonde;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Main extends AppCompatActivity {

    public Button bouton1;
    public Button boutonStatus;
    public Button boutonSiteweb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bouton1 = (Button) findViewById(R.id.bouton1);
        boutonStatus = (Button) findViewById(R.id.boutonStatus);
        boutonSiteweb = (Button) findViewById(R.id.boutonSiteweb);

        bouton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent EnfantsActivite = new Intent(Main.this, Inscription.class);
                startActivity(EnfantsActivite);
                finish();
            }
        });

        boutonStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent StatusActivite = new Intent(Main.this, StatusInscriptions.class);
                startActivity(StatusActivite);
                finish();
            }
        });

        boutonSiteweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SiteWeb = new Intent(Main.this, web.class);
                startActivity(SiteWeb);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
