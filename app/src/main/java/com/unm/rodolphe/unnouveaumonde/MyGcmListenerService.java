package com.unm.rodolphe.unnouveaumonde;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import java.util.Random;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    @Override
    public void onMessageReceived(String from, Bundle data) {

        String message = data.getString("message");
        String title = data.getString("title");

        Log.d(TAG, "De : " + from);
        Log.d(TAG, "Message : " + message);

        sendNotification(message, title);
    }

    private void sendNotification(String message, String title) {
        Intent intent = new Intent(this, Programme.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // On génère un nombre aléatoire pour pouvoir afficher plusieurs notifications
        notificationManager.notify(new Random().nextInt(9999), notificationBuilder.build());
    }
}