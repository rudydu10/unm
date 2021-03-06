package com.unm.unnouveaumonde;


import com.unm.unnouveaumonde.objects.Activite;
import com.unm.unnouveaumonde.objects.Enfant;
import com.unm.unnouveaumonde.objects.Tarif;

import java.util.ArrayList;
import java.util.List;

public class Constants {

    //SCRIPTS
    public static final String parent_PHP = "parent.php";
    public static final String enfant_PHP = "enfant.php";
    public static final String inscription_PHP = "inscription.php";
    public static final String activite_PHP = "activite.php";
    public static final String login_PHP = "login.php";
    public static final String inscriptions_PHP = "inscriptions.php";
    public static final String register_PHP = "register.php";
    public static final String coeff_PHP = "coeff.php";

    //CODES D'ERREUR
    public static final String CODE_ERROR = "42";
    public static final String CODE_OK = "200";
    public static final String CODE_MISSING = "201";
    public static final String CODE_ERROR_LOGIN = "203";
    public static final String CODE_ERROR_DUAL_ENTRY = "202";
    public static final String CODE_ERROR_DROIT_CONNECTION = "204";
    public static final String CODE_ERROR_SENPOST_NULL = "205";
    public static final String CODE_NO_DATA = "206";
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";


    //ADRESSES WEB
    public static final String server_ADDRESS = "http://www.un-nouveau-monde.fr/app/android/";
    public static final String webserver_ADDRESS = "http://www.un-nouveau-monde.fr/";
    public static final String programme_ADDRESS = "http://www.un-nouveau-monde.fr/app/android/programme/programme.html";
    public static final int timeout = 5;
    //VARIABLES
    public static List<Enfant> enfant = new ArrayList<>();
    public static List<Activite> activites = new ArrayList<>();
    public static boolean rp_srv_act = false;
    public static boolean rp_srv_enf = false;
    public static Boolean premiereConnection = true;
    public static String idParent;
    public static Tarif tarif = new Tarif(0, 0, 0, 0, 0);
}
