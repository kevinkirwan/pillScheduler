package com.kevinkirwansoftware.capsule;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.kevinkirwansoftware.capsule.notifications.ThrowawayBroadcast;

public class ThrowawayActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "my_channel";
    public static final String FULL_SCREEN_ACTION = "full_screen_action";
    static final int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wake_up);
        createNotificationChannel(this);

        //set flags so activity is showed when phone is off (on lock screen)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * Use button to set alarm manager with a pending intent to create the full screen notification
     * after use has time to shut off device to test with the lock screen showing
     */
    public void buttonClick(View view) {
        Intent intent = new Intent(FULL_SCREEN_ACTION, null, this, WakeUpActivity.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 15000, pendingIntent);
        }

        NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID); //cancel last notification for repeated tests
    }

    public static void CreateFullScreenNotification(Context context) {
        Intent intent = new Intent(context, ThrowawayBroadcast.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("Full Screen Alarm Test")
                        .setContentText("This is a test")
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setCategory(NotificationCompat.CATEGORY_ALARM)
                        .setContentIntent(pendingIntent)
                        .setFullScreenIntent(pendingIntent, true);
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "channel_name", NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("channel_description");
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}