package com.kevinkirwansoftware.capsule.notifications;

import android.app.AlarmManager;
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

import com.kevinkirwansoftware.capsule.ThrowawayActivity;
import com.kevinkirwansoftware.capsule.WakeUpActivity;
import com.kevinkirwansoftware.capsule.general.ApplicationTools;
import com.kevinkirwansoftware.capsule.notifications.NotificationHelper;

import java.util.Objects;



public class ThrowawayBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ThrowawayActivity.FULL_SCREEN_ACTION.equals(intent.getAction()))
            ThrowawayActivity.CreateFullScreenNotification(context);
    }
}

