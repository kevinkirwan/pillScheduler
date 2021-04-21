package com.kevinkirwansoftware.capsule.notifications;

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
import com.kevinkirwansoftware.capsule.general.ApplicationTools;
import com.kevinkirwansoftware.capsule.general.ChannelGenerator;
import com.kevinkirwansoftware.capsule.notifications.NotificationHelper;
import com.kevinkirwansoftware.capsule.notifications.ReminderBroadcast;
import com.kevinkirwansoftware.capsule.notifications.ThrowawayBroadcast;

import java.util.Calendar;

public class ReminderCheckJobService extends JobService {
    private static final String TAG = "ReminderCheckJobService";
    private boolean jobCancelled = false;
    private SQLiteDatabase mDatabase;
    //Context context;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job started...");
        ChannelGenerator.createNotificationChannels(getApplicationContext());
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
                cursor.moveToFirst();
                for(int i = 0; i < cursor.getCount(); i++){
                    cursor.moveToPosition(i);
                    if(cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_ACTIVATED)) == 1){
                        // A value of 0 is one-time
                        if(cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_TYPE)) == 0){
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.MILLISECOND, 0);
                            calendar.set(Calendar.SECOND, 0);
                            calendar.set(Calendar.MINUTE, cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_MINUTE)) - 1);
                            calendar.set(Calendar.HOUR_OF_DAY, cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_HOUR)));
                            calendar.set(Calendar.DAY_OF_MONTH, cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DAY)));
                            calendar.set(Calendar.MONTH, cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_MONTH)) - 1);
                            calendar.set(Calendar.YEAR, cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_YEAR)));
                            Log.d("Kevin", "Code: " + cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DB_CODE_1)));
                            if(!calendar.before(Calendar.getInstance())) {
                                AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                                // PI Flag: PendingIntent.FLAG_UPDATE_CURRENT
                                int code = cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DB_CODE_1));
                                Intent intent = ApplicationTools.broadcastIntentGeneratorTest(getApplicationContext(),
                                        cursor.getString(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_SCHEDULE_ID)),
                                        cursor.getString(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_NAME)),
                                        cursor.getString(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DESCRIPTION)),
                                        code);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), code, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                assert alarmManager != null;
                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                            }
                        }
                        // Recurring Reminder is a value of 1
                        else if(cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_TYPE)) == 1) {
                            Calendar calendar = Calendar.getInstance();
                            int dailyReminders = cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DAILY_REMINDERS));
                            if(dailyReminders > 3) {
                                calendar.set(Calendar.MILLISECOND, 0);
                                calendar.set(Calendar.SECOND, 0);
                                calendar.set(Calendar.MINUTE, cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_MINUTE_FOUR)) - 1);
                                calendar.set(Calendar.HOUR_OF_DAY, cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_HOUR_FOUR)));
                                if (calendar.before(Calendar.getInstance())) {
                                    calendar.add(Calendar.DATE, 1);
                                }
                                AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                                int code = cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DB_CODE_4));
                                Intent intent = ApplicationTools.broadcastIntentGeneratorTest(getApplicationContext(),
                                        cursor.getString(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_SCHEDULE_ID)),
                                        cursor.getString(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_NAME)),
                                        cursor.getString(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DESCRIPTION)),
                                        code);
                                // PI Flag: PendingIntent.FLAG_UPDATE_CURRENT
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), code, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                assert alarmManager != null;
                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                            } if(dailyReminders > 2) {
                                calendar.set(Calendar.MILLISECOND, 0);
                                calendar.set(Calendar.SECOND, 0);
                                calendar.set(Calendar.MINUTE, cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_MINUTE_THREE)) - 1);
                                calendar.set(Calendar.HOUR_OF_DAY, cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_HOUR_THREE)));
                                if (calendar.before(Calendar.getInstance())) {
                                    calendar.add(Calendar.DATE, 1);
                                }
                                AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                                int code = cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DB_CODE_3));
                                Intent intent = ApplicationTools.broadcastIntentGeneratorTest(getApplicationContext(),
                                        cursor.getString(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_SCHEDULE_ID)),
                                        cursor.getString(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_NAME)),
                                        cursor.getString(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DESCRIPTION)),
                                        code);
                                // PI Flag: PendingIntent.FLAG_UPDATE_CURRENT
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), code, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                assert alarmManager != null;
                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                            } if (dailyReminders > 1) {
                                calendar.set(Calendar.MILLISECOND, 0);
                                calendar.set(Calendar.SECOND, 0);
                                calendar.set(Calendar.MINUTE, cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_MINUTE_TWO)) - 1);
                                calendar.set(Calendar.HOUR_OF_DAY, cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_HOUR_TWO)));
                                if (calendar.before(Calendar.getInstance())) {
                                    calendar.add(Calendar.DATE, 1);
                                }
                                AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                                int code = cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DB_CODE_2));
                                Intent intent = ApplicationTools.broadcastIntentGeneratorTest(getApplicationContext(),
                                        cursor.getString(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_SCHEDULE_ID)),
                                        cursor.getString(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_NAME)),
                                        cursor.getString(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DESCRIPTION)),
                                        code);
                                // PI Flag: PendingIntent.FLAG_UPDATE_CURRENT
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), code, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                assert alarmManager != null;
                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                                }
                                    // Runs every time
                            calendar.set(Calendar.MILLISECOND, 0);
                            calendar.set(Calendar.SECOND, 0);
                            calendar.set(Calendar.MINUTE, cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_MINUTE_ONE)) - 1);
                            calendar.set(Calendar.HOUR_OF_DAY, cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_HOUR_ONE)));
                            Log.d("Kevin", "Code: " + cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DB_CODE_1)));
                            if (calendar.before(Calendar.getInstance())) {
                                calendar.add(Calendar.DATE, 1);
                            }
                            AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                            int code = cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DB_CODE_1));
                            Intent intent = ApplicationTools.broadcastIntentGeneratorTest(getApplicationContext(),
                                    cursor.getString(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_SCHEDULE_ID)),
                                    cursor.getString(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_NAME)),
                                    cursor.getString(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DESCRIPTION)),
                                    code);
                            // PI Flag: PendingIntent.FLAG_UPDATE_CURRENT
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), code, intent, 0);
                            assert alarmManager != null;
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                        }
                    }
                    if(jobCancelled){
                        return;
                    }
                }
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
