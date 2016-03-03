package com.unm.rodolphe.unnouveaumonde;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

public class JSONToHashtable extends AsyncTask<String, Void, Hashtable> {

    @Override
    protected Hashtable doInBackground(String... strings) {
        Hashtable ht = new Hashtable();

        String string = strings[0];
        String getint = strings[1];
        String getstring = strings[2];

        try {

            JSONArray jArray = new JSONArray(string);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);

                ht.put(json_data.getInt(getint), json_data.getString(getstring));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ht;
    }


}
