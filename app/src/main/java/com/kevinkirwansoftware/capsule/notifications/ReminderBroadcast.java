package com.kevinkirwansoftware.capsule.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.kevinkirwansoftware.capsule.general.ApplicationTools;
import com.kevinkirwansoftware.capsule.notifications.NotificationHelper;

import java.util.Objects;

public class ReminderBroadcast extends BroadcastReceiver {
    private static String TAG = "ReminderBroadcast.java";
    private String notificationTitle;
    private String notificationDescription;


    @Override
    public void onReceive(Context context, Intent intent) {
        //displayNotification(context);
        String tag = intent.getStringExtra("tag");
        int code = intent.getIntExtra("code", 0);

        ApplicationTools.showNotification(Objects.requireNonNull(context), intent.getStringExtra("title"+tag), intent.getStringExtra("desc"+tag), tag, code);
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] vibePattern = {1000, 0, 1000, 0, 1000};
        VibrationEffect ve = VibrationEffect.createWaveform(vibePattern, 2);
        assert vibrator != null;
        vibrator.vibrate(ve);
        //ApplicationTools.showFsNotification(Objects.requireNonNull(context), intent.getStringExtra("title"+tag), intent.getStringExtra("desc"+tag), tag, intent.getIntExtra("code", 0));
        Log.d(TAG, "Received");
    }

    private void displayNotification(Context context){
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(1, nb.build());
    }
}
