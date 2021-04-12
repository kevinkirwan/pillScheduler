package com.kevinkirwansoftware.capsule;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.kevinkirwansoftware.capsule.database.RecurringDbHelper;
import com.kevinkirwansoftware.capsule.database.RecurringReminderColumns;
import com.kevinkirwansoftware.capsule.general.ApplicationPreferences;
import com.kevinkirwansoftware.capsule.notifications.NotificationHelper;
import com.kevinkirwansoftware.capsule.notifications.ReminderBroadcast;

import java.util.Calendar;

public class ReminderCheckJobService extends JobService {
    private static final String TAG = "ReminderCheckJobService";
    private boolean jobCancelled = false;
    private SQLiteDatabase mDatabase;
    //Context context;

    /*
    ReminderCheckJobService(Context context){
        this.context = context;
    }

     */

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job started...");
        RecurringDbHelper recurringDbHelper = new RecurringDbHelper(this.getApplicationContext());
        mDatabase = recurringDbHelper.getWritableDatabase();
        checkForReminders(params);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Job cancelled before completion...");
        jobCancelled = true;
        return false;
    }

    private void checkForReminders(JobParameters params){
        new Thread((new Runnable() {
            @Override
            public void run() {
                Cursor cursor = getAllItems();
                for(int i = 0; i < cursor.getCount(); i++){
                    cursor.moveToPosition(i);
                    if(cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_ACTIVATED)) == 1){
                        // A value of 0 is one-time
                        if(cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_TYPE)) == 0){
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.MINUTE, cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_MINUTE)) - 1);
                            calendar.set(Calendar.HOUR_OF_DAY, cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_HOUR)));
                            calendar.set(Calendar.DAY_OF_MONTH, cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DAY)));
                            calendar.set(Calendar.MONTH, cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_MONTH)) - 1);
                            calendar.set(Calendar.YEAR, cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_YEAR)));
                            if(!calendar.before(Calendar.getInstance())) {
                                AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                                Intent intent = new Intent(getApplicationContext(), ReminderBroadcast.class);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, 0);
                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                            }
                        } else {

                        }
                    }

                    // TODO DELETE, this is temporary
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(jobCancelled){
                        return;
                    }

                }
                Log.d(TAG, "Job completed successfully");
                jobFinished(params, false);
            }
        })).start();
    }

    private void displayToast(final String message) {
        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void reminderSetUp(){
        //displayNotification(getContext());
        /*
        for (int i = 0; i < mScheduleItems.size(); i++){
            Log.d("Kevin", "Class" + mScheduleItems.get(i).getClass().toString());
            if(mScheduleItems.get(i) instanceof SingleReminder){
                Log.d("Kevin", "Single");
                SingleReminder singleReminder = (SingleReminder) mScheduleItems.get(i);
                Calendar c = singleReminder.getReminderCalendar();
                if(!c.before(Calendar.getInstance())){
                    AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(getContext(), ReminderBroadcast.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, 0);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
                    Calendar now = Calendar.getInstance();
                    long diff = (c.getTimeInMillis() - now.getTimeInMillis())/1000;
                    Log.d("Kevin", "Scheduled Year : " + c.get(Calendar.YEAR));
                    Log.d("Kevin", "Month : " + c.get(Calendar.MONTH));
                    Log.d("Kevin", "Day : " + c.get(Calendar.DAY_OF_MONTH));
                    Log.d("Kevin", "Hour : " + c.get(Calendar.HOUR_OF_DAY));
                    Log.d("Kevin", "Minute : " + c.get(Calendar.MINUTE));
                    Log.d("Kevin", "Current Year : " + now.get(Calendar.YEAR));
                    Log.d("Kevin", "Month : " + now.get(Calendar.MONTH));
                    Log.d("Kevin", "Day : " + now.get(Calendar.DAY_OF_MONTH));
                    Log.d("Kevin", "Hour : " + now.get(Calendar.HOUR_OF_DAY));
                    Log.d("Kevin", "Minute : " + now.get(Calendar.MINUTE));
                    Log.d("Kevin", "In seconds : " + diff);
                } else {
                    Log.d("Kevin", "Alarm b4");
                }




            } else if(mScheduleItems.get(i) instanceof RecurringReminder) {
                Log.d("Kevin", "Recurring");
            }
        }

         */


    }

    private void displayNotification(Context context){
        Log.d(TAG, "displayNotification() Notification displayed...");
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(1, nb.build());
    }

    private Cursor getAllItems(){
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
