package com.kevinkirwansoftware.capsule;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.SoundPool;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.kevinkirwansoftware.capsule.general.App;
import com.kevinkirwansoftware.capsule.general.MainActivity;
import com.kevinkirwansoftware.capsule.notifications.NotificationClickedBroadcast;

public class ThrowawayService extends Service {
    private final String TAG = "ThrowawayService";
    private SoundPool soundPool;
    //private int


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d("Kevin", "FS Started");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*
        Log.d(TAG, "showNotification() Notification displayed...");
        String input = intent.getStringExtra("inputExtra");
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        // Initialize Collapsed View
        RemoteViews collapsedView = new RemoteViews(getApplicationContext().getPackageName(),
                R.layout.collapsed_notification);
        collapsedView.setTextViewText(R.id.notificationText1, "Title 123");
        collapsedView.setTextViewText(R.id.notificationText2, "Desc 456");
        //collapsedView.setOnClickResponse(R.id.collapsedButton, RemoteViews.RemoteResponse.fromPendingIntent());

        RemoteViews expandedView = new RemoteViews(getApplicationContext().getPackageName(),
                R.layout.expanded_notification);

        Intent intent1 = new Intent("Notification deleted");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 5, intent1, 0);

        Notification notification = new NotificationCompat.Builder(getApplicationContext(), App.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_capsule)
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView)
                .setDeleteIntent(pendingIntent)
                .build();
        notificationManager.notify(5, notification);

         */


        String ACTION_SNOOZE = "sidwh";
        String EXTRA_NOTIFICATION_ID = "test";
        Intent snoozeIntent = new Intent(this, NotificationClickedBroadcast.class);
        snoozeIntent.setAction(ACTION_SNOOZE);
        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, "test123");
        PendingIntent toastPI =
                PendingIntent.getBroadcast(this, 30, snoozeIntent, 0);

        RemoteViews collapsedView = new RemoteViews(getApplicationContext().getPackageName(),
                R.layout.collapsed_notification);
        collapsedView.setTextViewText(R.id.notificationText1, "Title 123");
        collapsedView.setTextViewText(R.id.notificationText2, "Desc 456");

        Log.d("Kevin", "API "+ Build.VERSION.SDK_INT);

        if(Build.VERSION.SDK_INT >= 29){
            collapsedView.setOnClickResponse(R.id.collapsedButton, RemoteViews.RemoteResponse.fromPendingIntent(toastPI));
        }



        String input = intent.getStringExtra("inputExtra");
        Intent notificationIntent = new Intent(this, ThrowawayActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                34, notificationIntent, 0);

        Intent fullScreenIntent = new Intent(this, ThrowawayActivity.class);
        PendingIntent fullScreenPI = PendingIntent.getActivity(this, 33, fullScreenIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_ID)
                .setCustomContentView(collapsedView)
                .setSmallIcon(R.drawable.ic_capsule)
                .setContentIntent(pendingIntent)
                .setFullScreenIntent(fullScreenPI, true)
                .build();
        //do heavy work on a background thread
        //stopSelf();
        startForeground(33, notification);




        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
