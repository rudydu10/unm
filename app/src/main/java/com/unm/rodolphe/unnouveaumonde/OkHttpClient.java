package com.unm.rodolphe.unnouveaumonde;

import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;


public class OkHttpClient extends AsyncTask<String, Void, String>{

    @Override

    protected String doInBackground(String... parameter)
    {
        com.squareup.okhttp.OkHttpClient client = new com.squareup.okhttp.OkHttpClient();

        String KEY_TOKEN = parameter[0];
        String TOKEN = parameter[1];
        String SERVER_ADDRESS = parameter[2];

        RequestBody requestBody = new FormEncodingBuilder()
                .add(KEY_TOKEN, TOKEN)
                .build();

        Request request = new Request.Builder()
                .url(SERVER_ADDRESS)
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
