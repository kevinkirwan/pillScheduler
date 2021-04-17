package com.kevinkirwansoftware.capsule.notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.kevinkirwansoftware.capsule.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.kevinkirwansoftware.capsule.ThrowawayActivity;
import com.kevinkirwansoftware.capsule.ThrowawayService;
import com.kevinkirwansoftware.capsule.WakeUpActivity;
import com.kevinkirwansoftware.capsule.general.ApplicationTools;
import com.kevinkirwansoftware.capsule.notifications.NotificationHelper;

import java.util.Objects;



public class ThrowawayBroadcast extends BroadcastReceiver {
    private final String CHANNEL_ID = "channelid";
    @Override
    public void onReceive(Context context, Intent intent) {
        startService(context);
        Log.d("Kevin", "Throwaway Broadcast received");
    }

    public void startService(Context context) {
        /*
        Intent serviceIntent = new Intent(context, ThrowawayService.class);
        serviceIntent.putExtra("inputExtra", "test");
        context.startService(serviceIntent);
         */

        Intent serviceIntent = new Intent(context, ThrowawayService.class);
        serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT >= 26){
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }
    }

    public static void stopService(Context context) {
        Intent serviceIntent = new Intent(context, ThrowawayService.class);
        context.stopService(serviceIntent);
    }


}

