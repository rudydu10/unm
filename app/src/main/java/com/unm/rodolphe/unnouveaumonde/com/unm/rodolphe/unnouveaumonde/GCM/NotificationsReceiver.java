package com.unm.rodolphe.unnouveaumonde.com.unm.rodolphe.unnouveaumonde.GCM;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.unm.rodolphe.unnouveaumonde.Programme;
import com.unm.rodolphe.unnouveaumonde.R;

public class NotificationsReceiver extends C2DMBroadcastReceiver {

    @Override
    protected void onError(Context context, String error) {
        // traitement de l'erreur
    }

    @Override
    protected void onRegistration(Context context, String registrationId) {
        // envoi du registrationId au serveur d'application
    }

    @Override
    protected void onUnregistration(Context context) {
        // traitement du désabonnement
    }

    @Override
    protected void onMessageReceived(Context context, Intent intent) {

        // création de l'activité à démarrer lors du clic :
        Intent notifIntent = new Intent(context.getApplicationContext(), Programme.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notifIntent, 0);

        // création de la notification :
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(context)
                .setContentTitle("Un Nouveau Monde")
                .setContentText("Le nouveau programme est sortit !").setSmallIcon(R.drawable.notification)
                .setContentIntent(contentIntent).setVibrate(new long[] {0,200,100,200,100,200}).build();

        // affichage de la notification dans le menu déroulant :
        notification.flags |= Notification.FLAG_AUTO_CANCEL; // la notification disparaitra une fois cliquée

        // lancement de la notification :
        notificationManager.notify(1, notification);
    }
}
