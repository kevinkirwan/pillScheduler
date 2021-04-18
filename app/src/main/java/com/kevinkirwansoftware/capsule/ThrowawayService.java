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
        String tag = intent.getStringExtra("tag");
        String title = intent.getStringExtra("title" + tag);
        String desc = intent.getStringExtra("desc" + tag);
        int code = intent.getIntExtra("code", 0);
        Log.d("Kevin", "Code: " + code);

        String ACTION_SNOOZE = "sidwh";
        String EXTRA_NOTIFICATION_ID = "test";
        Intent snoozeIntent = new Intent(this, NotificationClickedBroadcast.class);
        snoozeIntent.setAction(ACTION_SNOOZE);
        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, "test123");
        /*
        snoozeIntent.putExtra("tag", tag);
        snoozeIntent.putExtra("title" + tag, title);
        snoozeIntent.putExtra("desc" + tag, desc);
        snoozeIntent.putExtra("code" + tag, code);

         */
        PendingIntent toastPI =
                PendingIntent.getBroadcast(this, code, snoozeIntent, 0);

        RemoteViews collapsedView = new RemoteViews(getApplicationContext().getPackageName(),
                R.layout.collapsed_notification);
        collapsedView.setTextViewText(R.id.notificationText1, title);
        collapsedView.setTextViewText(R.id.notificationText2, desc);

        if(Build.VERSION.SDK_INT >= 29){
            collapsedView.setOnClickResponse(R.id.collapsedButton, RemoteViews.RemoteResponse.fromPendingIntent(toastPI));
        } else {
            //TODO Implement on SDK levels 21-28
            //collapsedView.setOnClickResponse(R.id.collapsedButton, RemoteViews.RemoteResponse.fromPendingIntent(toastPI));
        }

        Intent notificationIntent = new Intent(this, ThrowawayActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                code, notificationIntent, 0);

        /*
        Intent fullScreenIntent = new Intent(this, ThrowawayActivity.class);
        PendingIntent fullScreenPI = PendingIntent.getActivity(this, 33, fullScreenIntent, 0);
         */

        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_ID)
                .setCustomContentView(collapsedView)
                .setSmallIcon(R.drawable.ic_capsule)
                .setContentIntent(pendingIntent)
                //.setFullScreenIntent(fullScreenPI, true)
                .build();
        //do heavy work on a background thread
        //stopSelf();
        startForeground(code, notification);




        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
