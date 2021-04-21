package com.kevinkirwansoftware.capsule.throwaway;

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
import androidx.core.app.NotificationCompat;

import com.kevinkirwansoftware.capsule.R;
import com.kevinkirwansoftware.capsule.general.ChannelGenerator;
import com.kevinkirwansoftware.capsule.notifications.NotificationClickedBroadcast;

public class ThrowawayService extends Service {
    private final String TAG = "ThrowawayService";
    private SoundPool soundPool;

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

        String ACTION_SNOOZE = "snooze";
        Intent snoozeIntent = new Intent(this, NotificationClickedBroadcast.class);
        snoozeIntent.setAction(ACTION_SNOOZE);

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

        assert tag != null;
        if(title.equals("q")){
            tag = ChannelGenerator.CHANNEL_1;
        } else {
            tag = ChannelGenerator.CHANNEL_2;
        }
        Notification notification = new NotificationCompat.Builder(this, tag)
                .setCustomContentView(collapsedView)
                .setSmallIcon(R.drawable.ic_capsule)
                .setContentIntent(pendingIntent)
                //.setFullScreenIntent(fullScreenPI, true)
                .build();
        startForeground(code, notification);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
