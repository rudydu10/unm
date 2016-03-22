package com.unm.rodolphe.unnouveaumonde;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class sendPost extends AsyncTask<String, Void, String>{

    @Override
    protected String doInBackground(String... urls)
    {

        try {


            URL obj = new URL(urls[0]);
            String variable = urls[1];
            String select = urls[2];
            String where = urls[3];
            String like = urls[4];

            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestProperty("Accept-Encoding", "");
            if (Build.VERSION.SDK_INT > 13) {
                connection.setRequestProperty("Connection", "close");
            }

            Uri.Builder builder1 = new Uri.Builder().appendQueryParameter(variable, select + ":" + where + ":" + like);
            System.out.println("Variable : " + variable + " " + select + " : " + where + " : " + like);
            String query1 = builder1.build().getEncodedQuery();

            connection.setFixedLengthStreamingMode(query1.getBytes().length);

            connection.setRequestMethod("POST");

            connection.setDoInput(true);
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query1);
            writer.flush();
            writer.close();
            os.close();
            connection.connect();

            int responceCode = connection.getResponseCode();

            if (responceCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();


                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println(response.toString());
                connection.disconnect();
                return response.toString();


            } else {
                connection.disconnect();
                return null;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }

}