package com.kevinkirwansoftware.capsule.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;

import com.kevinkirwansoftware.capsule.ThrowawayActivity;
import com.kevinkirwansoftware.capsule.ThrowawayService;
import com.kevinkirwansoftware.capsule.general.MainActivity;


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

