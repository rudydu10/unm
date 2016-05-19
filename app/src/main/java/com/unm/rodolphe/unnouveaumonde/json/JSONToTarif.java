package com.unm.rodolphe.unnouveaumonde.json;

import android.os.AsyncTask;

import com.unm.rodolphe.unnouveaumonde.objects.Tarif;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONToTarif extends AsyncTask<String, Void, Tarif> {

    @Override
    protected Tarif doInBackground(String... strings) {
        Tarif tarif = new Tarif(0, 0, 0, 0, 0);
        String string = strings[0];

        try {

            JSONArray jArray = new JSONArray(string);
            JSONObject json_data = jArray.getJSONObject(0);
            tarif = new Tarif(json_data.getDouble("journee"), json_data.getDouble("journeeRepas"), json_data.getDouble("demiejournee"), json_data.getDouble("demiejourneeRepas"), json_data.getDouble("semaine"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tarif;
    }

}