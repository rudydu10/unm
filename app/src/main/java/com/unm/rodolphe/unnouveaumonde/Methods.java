package com.unm.rodolphe.unnouveaumonde;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;


public class Methods {

    public static String sendPOST(URL obj, String variable, String select, String where, String like) throws IOException
    {
        if(isOnline()) {
            String response = "";
            sendPost sendpost = new sendPost();
            String[] params = new String[5];
            params[0] = obj.toString();
            params[1] = variable;
            params[2] = select;
            params[3] = where;
            params[4] = like;
            sendpost.execute(params);
            try {
                response = sendpost.get(10, TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
                return Constants.CODE_ERROR_SENPOST_NULL;
            }
            System.out.println("sendPost = " + response);
            return response;
        }
        else
        {
            return Constants.CODE_ERROR_SENPOST_NULL;
        }
    }

    public static int countActivity() {
        String response = "";
        try {
            response = sendPOST(new URL(Constants.server_ADDRESS + Constants.count_PHP), "count", "id", "id", "*");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response.contains("Error")) {
            return 0;
        } else {
            return Integer.parseInt(response.substring(1, response.length()));
        }
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
        Hashtable response = new Hashtable();
        JSONToHashtable jsonToHashtable = new JSONToHashtable();
        String[] params = new String[3];
        params[0] = string;
        params[1] = getint;
        params[2] = getstring;
        jsonToHashtable.execute(params);
        try {
            response = jsonToHashtable.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
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

    public static String getParentId(String username)
    {
        try
        {
            String response = sendPOST(new URL(Constants.server_ADDRESS + Constants.parent_PHP), "parent", "id", "username", username);
            return response;

        }catch(IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static String getParentFirstName(String id)
    {
        try
        {
            String response = sendPOST(new URL(Constants.server_ADDRESS + Constants.parent_PHP), "parent", "prenom", "id", id);
            return response;

        }catch(IOException e)
        {
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
            return "0";
        }


    }

    public static String login(String username, String password)
    {
        try {
            String response = sendPOST(new URL(Constants.server_ADDRESS + Constants.login_PHP), "login", "password", "username", username + ":" + password);
            if(response.contains(encodeMD5(password)))
            {
                return Constants.CODE_OK;
            }
            else if(response.contains(Constants.CODE_ERROR_DROIT_CONNECTION))
            {
                return Constants.CODE_ERROR_DROIT_CONNECTION;
            }
            else if(response.contains(Constants.CODE_MISSING))
            {
                return Constants.CODE_MISSING;
            }
            else if(response.contains(Constants.CODE_ERROR_LOGIN))
            {
                return Constants.CODE_ERROR_LOGIN;
            }
            else
            {
                return Constants.CODE_ERROR;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Constants.CODE_ERROR_SENPOST_NULL;
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

    public static boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }

}
