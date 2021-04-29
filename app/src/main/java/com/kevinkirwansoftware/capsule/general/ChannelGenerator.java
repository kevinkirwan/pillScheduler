package com.kevinkirwansoftware.capsule.general;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import com.kevinkirwansoftware.capsule.R;
import com.kevinkirwansoftware.capsule.database.RecurringDbHelper;
import com.kevinkirwansoftware.capsule.database.RecurringReminderColumns;

import java.util.ArrayList;

public class ChannelGenerator extends Application {
    //private ArrayList<String> channelsIds;
    private static SQLiteDatabase mDatabase;
    public static final String CHANNEL_1 = "exampleChannel1";
    public static final String CHANNEL_2 = "exampleChannel2";
    public static long[] vibrationPattern = {100, 3000, 1000, 3000, 1000, 3000};
    public static String TAG = "ChannelGenerator";
    public static Uri soundUri;
    public static AudioAttributes audioAttributes;
    @Override
    public void onCreate() {
        super.onCreate();
        RecurringDbHelper recurringDbHelper = new RecurringDbHelper(this.getApplicationContext());
        mDatabase = recurringDbHelper.getWritableDatabase();
        createNotificationChannels(getApplicationContext());
         soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
         audioAttributes = new AudioAttributes.Builder()
                 .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                 .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                 .build();
    }
    public static void createNotificationChannels(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Cursor cursor = getAllItems();
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                NotificationChannel channel = new NotificationChannel(
                        cursor.getString(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_SCHEDULE_ID)),
                        cursor.getString(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_NAME)),
                        NotificationManager.IMPORTANCE_HIGH
                );
                channel.setVibrationPattern(vibrationPattern);
                channel.enableVibration(true);
                channel.enableLights(true);
                channel.setSound(soundUri, audioAttributes);
                NotificationManager manager = context.getSystemService(NotificationManager.class);
                assert manager != null;
                manager.createNotificationChannel(channel);
                Log.d(TAG, "creating notification channels...");
            }

            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1,
                    "test1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager1 = context.getSystemService(NotificationManager.class);
            assert manager1 != null;
            manager1.createNotificationChannel(channel1);

            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2,
                    "test2",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager2 = context.getSystemService(NotificationManager.class);
            assert manager2 != null;
            manager2.createNotificationChannel(channel2);
        }
    }

    private static Cursor getAllItems(){
        return mDatabase.query(
                RecurringReminderColumns.RecurringReminderEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                RecurringReminderColumns.RecurringReminderEntry.COLUMN_TIMESTAMP + " DESC"
        );

    }
}
