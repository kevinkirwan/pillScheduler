package com.kevinkirwansoftware.capsule;

import android.app.Activity;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.kevinkirwansoftware.capsule.database.RecurringDbHelper;
import com.kevinkirwansoftware.capsule.database.RecurringReminderColumns;
import com.kevinkirwansoftware.capsule.general.ApplicationPreferences;

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
                    Calendar calendar = Calendar.getInstance();
                    //calendar.set();
                    int isOneTime;
                    isOneTime = cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_NAME));
                    String name = cursor.getString(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_NAME));
                    Log.d("Kevin", "index: " + i + " Name: " + name );
                    //displayToast(name);
                    try {
                        Thread.sleep(1000);
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
