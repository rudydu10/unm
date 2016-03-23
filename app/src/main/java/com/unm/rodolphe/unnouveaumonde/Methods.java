package com.unm.rodolphe.unnouveaumonde;

import com.unm.rodolphe.unnouveaumonde.JSON.JSONToActivite;
import com.unm.rodolphe.unnouveaumonde.JSON.JSONToEnfant;
import com.unm.rodolphe.unnouveaumonde.Objects.Activite;
import com.unm.rodolphe.unnouveaumonde.Objects.Enfant;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class Methods {

    public static String sendPOST(URL obj, String variable, String select, String where, String like) throws IOException
    {
        if(isOnline()) {
            sendPost sendpost = new sendPost();
            String[] params = new String[5];
            params[0] = obj.toString();
            params[1] = variable;
            params[2] = select;
            params[3] = where;
            params[4] = like;
            sendpost.execute(params);
            try {
                String response = sendpost.get(10, TimeUnit.SECONDS);
                if (response == null) {
                    return Constants.CODE_ERROR_SENPOST_NULL;
                }
                return response;
            } catch (Exception e) {
                e.printStackTrace();
                return Constants.CODE_ERROR_SENPOST_NULL;
            }
        }
        else
        {
            return Constants.CODE_ERROR_SENPOST_NULL;
        }
    }

    public static String encodeMD5(String password)
    {
        byte[] uniqueKey = password.getBytes();
        byte[] hash;

        try
        {
            hash = MessageDigest.getInstance("MD5").digest(uniqueKey);
        }
        catch(NoSuchAlgorithmException e)
        {
            throw new Error("Erreur d'encodage MD5");
        }

        StringBuilder hashString = new StringBuilder();

        for (Byte iterator : hash)
        {
            String hex = Integer.toHexString(iterator);
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

    public static List<Enfant> JSONToEnfant(String string)
    {
        List<Enfant> response = new ArrayList<>();
        JSONToEnfant jsonToEnfant = new JSONToEnfant();
        String[] params = new String[1];
        params[0] = string;
        jsonToEnfant.execute(params);
        try {
            response = jsonToEnfant.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static List<Activite> JSONToActivite(String string) {
        List<Activite> response = new ArrayList<>();
        JSONToActivite jsonToActivite = new JSONToActivite();
        String[] params = new String[3];
        params[0] = string;
        jsonToActivite.execute(params);
        try {
            response = jsonToActivite.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static String getEnfants(String idparent) {
        try {
            return sendPOST(new URL(Constants.server_ADDRESS + Constants.enfant_PHP), "enfant", "id,enfant", "idparent", idparent);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getAllActivites() {
        try {
            return sendPOST(new URL(Constants.server_ADDRESS + Constants.activite_PHP), "activite", "id,activite", "activite", "%");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getParentFirstName(String id)
    {
        try
        {
            return sendPOST(new URL(Constants.server_ADDRESS + Constants.parent_PHP), "parent", "prenom", "id", id);
        }catch(IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static String login(String username, String password)
    {
        try {
            String response = sendPOST(new URL(Constants.server_ADDRESS + Constants.login_PHP), "login", "password", "username", username + ":" + password).replace("\t", "").replace("\r", "").replace("\n", "").replace("\f", "");
            String passwd = "";
            if (response.length() >= 33) {
                passwd = response.substring(0, 32);
                Constants.idParent = response.substring(32);
            }

            if (passwd.contains(encodeMD5(password)))
            {
                return Constants.CODE_OK;
            } else if (passwd.contains(Constants.CODE_ERROR_DROIT_CONNECTION))
            {
                return Constants.CODE_ERROR_DROIT_CONNECTION;
            } else if (passwd.contains(Constants.CODE_MISSING))
            {
                return Constants.CODE_MISSING;
            } else if (passwd.contains(Constants.CODE_ERROR_LOGIN))
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

    public static boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

}
