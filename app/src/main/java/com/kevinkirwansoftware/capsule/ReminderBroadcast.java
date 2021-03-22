package com.kevinkirwansoftware.capsule;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcast extends BroadcastReceiver {
    private static String TAG = "ReminderBroadcast.java";

    @Override
    public void onReceive(Context context, Intent intent) {
        displayNotification(context);
        Log.d(TAG, "Received");
    }

    private void displayNotification(Context context){
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(1, nb.build());
    }
}
