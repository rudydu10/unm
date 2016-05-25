package com.unm.rodolphe.unnouveaumonde.json;

import android.os.AsyncTask;

import com.unm.rodolphe.unnouveaumonde.objects.Enfant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONToEnfant extends AsyncTask<String, Void, List<Enfant>> {

    @Override
    protected List<Enfant> doInBackground(String... strings) {
        List<Enfant> list = new ArrayList<>();

        String string = strings[0];

        try {

            JSONArray jArray = new JSONArray(string);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                list.add(new Enfant(json_data.getInt("id"), json_data.getString("enfant")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

}