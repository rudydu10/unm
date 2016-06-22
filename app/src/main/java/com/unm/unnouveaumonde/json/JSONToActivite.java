package com.unm.unnouveaumonde.json;

import android.os.AsyncTask;

import com.unm.unnouveaumonde.objects.Activite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONToActivite extends AsyncTask<String, Void, List<Activite>> {

    @Override
    protected List<Activite> doInBackground(String... strings) {
        List<Activite> list = new ArrayList<>(100);
        String string = strings[0];
        try {

            JSONArray jArray = new JSONArray(string);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                list.add(new Activite(json_data.getInt("id"), json_data.getString("activite")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

}