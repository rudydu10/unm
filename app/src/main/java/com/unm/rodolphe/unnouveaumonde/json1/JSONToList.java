package com.unm.rodolphe.unnouveaumonde.json1;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONToList extends AsyncTask<String, Void, List<Object>> {

    @Override
    protected List<Object> doInBackground(String... strings) {
        List<Object> list = new ArrayList<>();

        String string = strings[0];
        String getint = strings[1];
        String getstring = strings[2];

        try {

            JSONArray jArray = new JSONArray(string);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                list.add(json_data.getInt(getint), json_data.getString(getstring));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

}