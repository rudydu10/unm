package com.unm.rodolphe.unnouveaumonde;

import com.unm.rodolphe.unnouveaumonde.JSON.JSONToActivite;
import com.unm.rodolphe.unnouveaumonde.JSON.JSONToEnfant;
import com.unm.rodolphe.unnouveaumonde.JSON.JSONToTarif;
import com.unm.rodolphe.unnouveaumonde.Objects.Activite;
import com.unm.rodolphe.unnouveaumonde.Objects.Enfant;
import com.unm.rodolphe.unnouveaumonde.Objects.Tarif;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class Methods {

    /**
     * Méthodes permettant l'envoi de requêtes POST
     * @param obj URL du script où envoyer la requête
     * @param variable nom de la variable POST
     * @param param1 paramètre 1 (Select)
     * @param param2 paramètre 2 (Where)
     * @param param3 paramètre 3 (Like)
     * @return contenu de la page du script
     */
    public static String sendPOST(URL obj, String variable, String param1, String param2, String param3) throws IOException
    {
        if(isOnline()) {
            sendPost sendpost = new sendPost();
            String[] params = new String[5];
            params[0] = obj.toString();
            params[1] = variable;
            params[2] = param1;
            params[3] = param2;
            params[4] = param3;
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

    /**
     * Méthodes permettant le cryptage en MD5
     * @param password Mot de passe
     * @return Mot de passe encrypté
     */
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

    /**
     * Méthodes permettant la convertion des tableaux JSON enfants en liste d'Enfants
     * @param string tableau JSON
     * @return Liste d'enfants
     */
    public static List<Enfant> JSONToEnfant(String string)
    {
        List<Enfant> response = new ArrayList<>();
        JSONToEnfant jsonToEnfant = new JSONToEnfant();
        String[] params = new String[1];
        params[0] = string;
        jsonToEnfant.execute(params);
        try {
            response = jsonToEnfant.get(Constants.timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Méthodes permettant la convertion des tableaux JSON Tarif en objet Tarif
     *
     * @param string tableau JSON
     * @return objet Tarif
     */
    public static Tarif JSONToTarif(String string) {
        Tarif tarif = new Tarif(0, 0, 0, 0, 0);
        JSONToTarif jsonToTarif = new JSONToTarif();
        String[] params = new String[1];
        params[0] = string;
        jsonToTarif.execute(params);
        try {
            tarif = jsonToTarif.get(Constants.timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tarif;
    }

    /**
     * Méthodes permettant la convertion des tableaux JSON activités en liste d'Activites
     * @param string tableau JSON
     * @return Liste d'activité
     */
    public static List<Activite> JSONToActivite(String string) {
        List<Activite> response = new ArrayList<>();
        JSONToActivite jsonToActivite = new JSONToActivite();
        String[] params = new String[1];
        params[0] = string;
        jsonToActivite.execute(params);
        try {
            response = jsonToActivite.get(Constants.timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Méthodes permettant la récupération des enfants d'un parent
     * @param idparent id du parent
     * @return Tableau JSON des enfants du parent avec leurs id
     */
    public static String getEnfants(String idparent) {
        try {
            return sendPOST(new URL(Constants.server_ADDRESS + Constants.enfant_PHP), "enfant", "id,enfant", "idparent", idparent);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Méthodes permettant la récupération des activités
     * @return Tableau JSON des activités non terminées dont il reste des places libre
     */
    public static String getAllActivites() {
        try {
            String response = sendPOST(new URL(Constants.server_ADDRESS + Constants.activite_PHP), "activite", "id,activite", "activite", "%");
            if (!response.replaceAll("\t", "").equals(Constants.CODE_NO_DATA)) {
                System.out.println("response" + response);
                return response;
            }
            else {
                Constants.rp_srv_act = true;
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Méthodes permettant la recuperation du prénom d'un parent
     * @param id id du Parent
     * @return Prénom du Parent
     */
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

    /**
     * Méthodes permettant la connexion au serveur
     * @param username nom d'utilisateur
     * @param password mot de passe
     * @return Code d'erreur
     */
    public static String login(String username, String password)
    {
        try {
            String response = sendPOST(new URL(Constants.server_ADDRESS + Constants.login_PHP), "login", "password", "username", username + ":" + password).replace("\t", "").replace("\r", "").replace("\n", "").replace("\f", "");
            String passwd = response;
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

    /**
     * Méthodes permettant la verification de la connection
     * @return Booléen de témoin de connection
     */
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

    /**
     * Méthode servant à récupérer un Objet tarif contenant tout les tarifs en fonction du coefficiant CAF du parent
     *
     * @param idparent id du Parent
     * @return Objet Tarif contenant les tarifs correspondants au coefficiant CAF du parent
     */

    public static Tarif getTarifs(String idparent) {
        String response;
        Tarif tarif = new Tarif(0, 0, 0, 0, 0);
        try {
            tarif = JSONToTarif(sendPOST(new URL(Constants.server_ADDRESS + Constants.coeff_PHP), "id", Constants.idParent, "", ""));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tarif;
    }

}