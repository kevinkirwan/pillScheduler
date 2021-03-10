package com.kevinkirwansoftware.capsule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.kevinkirwansoftware.capsule.ReminderColumns.*;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "reminders.db";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_REMINDER_TABLE = "CREATE TABLE " +
                ReminderEntry.TABLE_NAME + " (" +
                ReminderEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ReminderEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                ReminderEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                ReminderEntry.COLUMN_TYPE + " INTEGER NOT NULL, " +
                ReminderEntry.COLUMN_DAILY_REMINDERS + " INTEGER NOT NULL, " +
                ReminderEntry.COLUMN_REMINDER_HOUR_ONE + " INTEGER NOT NULL, " +
                ReminderEntry.COLUMN_REMINDER_MINUTE_ONE + " INTEGER NOT NULL, " +
                ReminderEntry.COLUMN_REMINDER_HOUR_TWO + " INTEGER NOT NULL, " +
                ReminderEntry.COLUMN_REMINDER_MINUTE_TWO + " INTEGER NOT NULL, " +
                ReminderEntry.COLUMN_REMINDER_HOUR_THREE + " INTEGER NOT NULL, " +
                ReminderEntry.COLUMN_REMINDER_MINUTE_THREE + " INTEGER NOT NULL, " +
                ReminderEntry.COLUMN_REMINDER_HOUR_FOUR + " INTEGER NOT NULL, " +
                ReminderEntry.COLUMN_REMINDER_MINUTE_FOUR + " INTEGER NOT NULL, " +
                ReminderEntry.COLUMN_SCHEDULE_ID + " TEXT NOT NULL, " +
                ReminderEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

                db.execSQL(SQL_CREATE_REMINDER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ReminderEntry.TABLE_NAME);
        onCreate(db);
    }

    /*
    public boolean addData(String name, String desc, String type){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL0, name);
        contentValues.put(COL1, desc);
        contentValues.put(COL2, type);

        Log.d("Kevin", "addData: Adding " + name + " to " + TABLE_NAME );

        long result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1){
            return false;
        } else {
            return true;
        }


    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

     */


}
