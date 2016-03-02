package com.unm.rodolphe.unnouveaumonde;

import java.util.Date;

public class Constants {

    //SCRIPTS
    public static final String parent_PHP = "parent.php";
    public static final String enfant_PHP = "enfant.php";
    public static final String inscription_PHP = "inscription.php";
    public static final String activite_PHP = "activite.php";
    public static final String login_PHP = "login.php";
    public static final String inscriptions_PHP = "inscriptions.php";
    public static final String register_PHP = "register.php";
    public static final String count_PHP = "count.php";

    public static final String CODE_ERROR = "42";
    public static final String CODE_OK = "200";
    public static final String CODE_MISSING = "201";
    public static final String CODE_ERROR_LOGIN = "203";
    public static final String CODE_ERROR_DUAL_ENTRY = "202";
    public static final String CODE_ERROR_DROIT_CONNECTION = "204";
    public static final String CODE_ERROR_SENPOST_NULL = "205";
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String server_ADDRESS = "http://www.un-nouveau-monde.fr/app/android/";
    public static final String webserver_ADDRESS = "http://www.un-nouveau-monde.fr/";
    public static final String programme_ADDRESS = "http://www.un-nouveau-monde.fr/app/android/programme/programme.html";
    public static String idParent;

    //SERVER ADDRESS
    public static String enfant = "null";
    public static Boolean premiereConnection = true;
    public static Date date = new Date();
}
