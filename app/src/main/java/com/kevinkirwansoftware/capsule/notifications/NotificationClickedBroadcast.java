package com.kevinkirwansoftware.capsule.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;

import com.kevinkirwansoftware.capsule.TimePair;
import com.kevinkirwansoftware.capsule.database.RecurringDbHelper;
import com.kevinkirwansoftware.capsule.database.RecurringReminderColumns;
import com.kevinkirwansoftware.capsule.general.ApplicationPreferences;
import com.kevinkirwansoftware.capsule.throwaway.ThrowawayService;

import java.util.Calendar;
import java.util.List;


public class NotificationClickedBroadcast extends BroadcastReceiver {
    SQLiteDatabase mRecurringDatabase;

    @Override
    public void onReceive(Context context, Intent intent) {
        //stopService(context);
        cancelNotification(intent.getIntExtra("code", 0), context);
        long time1 = intent.getLongExtra("time", 0);
        long time2 = Calendar.getInstance().getTimeInMillis();
        long deltaT =  time2 - time1;
        String tag = intent.getStringExtra("tag");
        List<TimePair> timeList = ApplicationPreferences.getLatencyList(context, tag);
        if(timeList != null){
            if(timeList.size() == 30){
                timeList.remove(0);
                timeList.add(new TimePair(time1, time2));
                ApplicationPreferences.setLatencyList(context, tag, timeList);
            } else {
                timeList.add(new TimePair(time1, time2));
                ApplicationPreferences.setLatencyList(context, tag, timeList);
            }
        }


        //LatencyDbHelper recurringDbHelper = new LatencyDbHelper(context);
        //mRecurringDatabase = recurringDbHelper.getWritableDatabase();


    }

    public static void stopService(Context context) {
        Intent serviceIntent = new Intent(context, ThrowawayService.class);
        context.stopService(serviceIntent);
    }

    public static void cancelNotification(int code, Context context){
        NotificationManagerCompat.from(context).cancel(code);
    }

    private Cursor getAllItems(){
        return mRecurringDatabase.query(
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

