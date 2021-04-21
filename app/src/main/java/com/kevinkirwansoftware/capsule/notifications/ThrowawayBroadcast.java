package com.kevinkirwansoftware.capsule.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.kevinkirwansoftware.capsule.throwaway.ThrowawayService;


public class ThrowawayBroadcast extends BroadcastReceiver {
    private final String CHANNEL_ID = "channelid";
    @Override
    public void onReceive(Context context, Intent intent) {
        startService(context, intent);
        Log.d("Kevin", "Throwaway Broadcast received");
    }

    public void startService(Context context, Intent intentIn) {
        Intent serviceIntent = new Intent(context, ThrowawayService.class);
        String tag = intentIn.getStringExtra("tag");
        serviceIntent.putExtra("tag", tag);
        serviceIntent.putExtra("title" + tag, intentIn.getStringExtra("title" + tag));
        serviceIntent.putExtra("desc" + tag, intentIn.getStringExtra("desc" + tag));
        serviceIntent.putExtra("code", intentIn.getIntExtra("code", 0));

        serviceIntent.setAction(tag);
        serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if(Build.VERSION.SDK_INT >= 26){
            context.startForegroundService(serviceIntent);
        } else {
            // TODO Implement for API levels 21-25
            context.startService(serviceIntent);
        }
    }

    public static void stopService(Context context) {
        Intent serviceIntent = new Intent(context, ThrowawayService.class);
        context.stopService(serviceIntent);
    }


}

