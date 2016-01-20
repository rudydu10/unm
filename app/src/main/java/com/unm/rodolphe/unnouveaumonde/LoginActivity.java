package com.unm.rodolphe.unnouveaumonde;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

public class LoginActivity extends Activity {

    Button buttonSubmit;
    EditText Username;
    EditText Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addListenerOnButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    private void addListenerOnButton()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();

        buttonSubmit = (Button) findViewById(R.id.buttonSubmitLogin);
        Username = (EditText) findViewById(R.id.Username);
        Username.setText(preferences.getString("USERNAME", ""));
        Password = (EditText) findViewById(R.id.Password);
        Password.setText(preferences.getString("PASSWORD", ""));

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = Methods.login(Username.getText().toString(), Password.getText().toString());
                if (login.contains(Constants.CODE_OK)) {
                    editor.putString("USERNAME", Username.getText().toString());
                    editor.putString("PASSWORD", Password.getText().toString());
                    editor.commit();
                    Constants.idParent = Methods.getParentId(Username.getText().toString()).replaceAll("\t", "");
                    Intent MainActivite = new Intent(LoginActivity.this, Main.class);
                    startActivity(MainActivite);
                    finish();
                } else if (login.contains(Constants.CODE_ERROR_LOGIN)) {
                    Toast.makeText(LoginActivity.this, "Erreur, nom d'utilisateur ou mot de passe incorrect.", Toast.LENGTH_LONG).show();
                } else if (login.contains(Constants.CODE_ERROR_DROIT_CONNECTION)) {
                    Toast.makeText(LoginActivity.this, "Erreur, vous n'avez pas acces a l'application, contactez l'association.", Toast.LENGTH_LONG).show();
                } else if (login.contains(Constants.CODE_MISSING)) {
                    Toast.makeText(LoginActivity.this, "Erreur, nom d'utilisateur ou mot de passe non renseigne.", Toast.LENGTH_LONG).show();
                } else if (login.contains(Constants.CODE_ERROR)) {
                    Toast.makeText(LoginActivity.this, "Erreur", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Erreur", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

}
