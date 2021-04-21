package com.kevinkirwansoftware.capsule.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import com.kevinkirwansoftware.capsule.throwaway.ThrowawayService;


public class NotificationClickedBroadcast extends BroadcastReceiver {
    private final String CHANNEL_ID = "channelid";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Kevin", "Notif Clicked Broadcast received");
        //stopService(context);
        cancelNotification(intent.getIntExtra("code", 0), context);
    }

    public static void stopService(Context context) {
        Intent serviceIntent = new Intent(context, ThrowawayService.class);
        context.stopService(serviceIntent);
    }

    public static void cancelNotification(int code, Context context){
        NotificationManagerCompat.from(context).cancel(code);
    }
}

