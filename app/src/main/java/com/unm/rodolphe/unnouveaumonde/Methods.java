package com.unm.rodolphe.unnouveaumonde;

import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;


public class Methods {

    public static String sendPOST(URL obj, String variable, String select, String where, String like) throws IOException
    {
        String response = sendPOSTrequest(obj, variable, select, where, like);

        for(int i = 0; i <= 4; i++)
        {

            if (response.isEmpty())
            {
                response = sendPOSTrequest(obj,variable, select, where, like);

            } else {
                break;
            }
            System.out.println("Boucle FOR : i = " + i + " sendPOST response = " + response);
        }
        return response;

    }

    private static String sendPOSTrequest(URL obj, String variable, String select, String where, String like) throws IOException {

        enableStrictMode();

        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestProperty("Accept-Encoding", "");
        if (Build.VERSION.SDK_INT > 13) {
            connection.setRequestProperty("Connection", "close");
        }

        Uri.Builder builder1 = new Uri.Builder().appendQueryParameter(variable, select+":"+where+":"+like);

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

        int responceCode = 0;
        responceCode = connection.getResponseCode();
        System.out.println("POST Response Code :: " + responceCode);
        System.out.println("Variable : " + variable + " valeur : " + select+":"+where+":"+like);

        if (responceCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();


            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println(response.toString());
            connection.disconnect();
            return response.toString();


        } else{
            System.out.println("POST Dommage");
            connection.disconnect();
            return null;
        }



    }

    private static void enableStrictMode()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public static String encodeMD5(String password)
    {
        byte[] uniqueKey = password.getBytes();
        byte[] hash = null;

        try
        {
            hash = MessageDigest.getInstance("MD5").digest(uniqueKey);
        }
        catch(NoSuchAlgorithmException e)
        {
            throw new Error("Erreur d'encodage MD5");
        }

        StringBuilder hashString = new StringBuilder();
        for (int i = 0; i < hash.length; i++)
        {
            String hex = Integer.toHexString(hash[i]);
            if(hex.length() == 1)
            {
                hashString.append('0');
                hashString.append(hex.charAt(hex.length() - 1));
            }
            else
            {
                hashString.append(hex.substring(hex.length() - 2));
            }
        }
        return hashString.toString();
    }

    public static Hashtable JSONToHashtable(String string, String getint, String getstring)
    {
        Hashtable ht = new Hashtable();
        try {

            JSONArray jArray = new JSONArray(string);
            for(int i=0;i< jArray.length();i++){
                JSONObject json_data = jArray.getJSONObject(i);

                ht.put(json_data.getInt(getint), json_data.getString(getstring));
            }

        }catch(JSONException e){

        }
        return ht;
    }

    public static String getEnfants(String idparent) {
        try {
            String response = sendPOST(new URL(Constants.server_ADDRESS + Constants.enfant_PHP), "enfant", "id,enfant", "idparent", idparent);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getAllActivites() {
        try {
            String response = sendPOST(new URL(Constants.server_ADDRESS + Constants.activite_PHP), "activite", "id,activite", "activite", "%");
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getActiviteId(String activite) {
        try
        {
            String response = sendPOST(new URL(Constants.server_ADDRESS + Constants.activite_PHP), "activite", "id, activite", "activite", activite);
            response = JSONtoStringID(response);
            return response;

        }catch(IOException e)
        {
            e.printStackTrace();
            return null;
        }


    }

    public static String JSONtoStringID(String string) {
        String id = null;
        try {

            JSONArray jArray = new JSONArray(string);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                id = Integer.toString(json_data.getInt("id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return id;
    }

}
