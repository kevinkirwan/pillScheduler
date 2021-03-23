package com.kevinkirwansoftware.capsule.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kevinkirwansoftware.capsule.database.SingleReminderColumns.*;

public class SingleDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "singleReminders.db";
    private static final int DATABASE_VERSION = 1;

    public SingleDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_REMINDER_TABLE = "CREATE TABLE " +
                SingleReminderEntry.TABLE_NAME + " (" +
                SingleReminderEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SingleReminderEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                SingleReminderEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                SingleReminderEntry.COLUMN_TYPE + " INTEGER NOT NULL, " +
                SingleReminderEntry.COLUMN_YEAR + " INTEGER NOT NULL, " +
                SingleReminderEntry.COLUMN_MONTH + " INTEGER NOT NULL, " +
                SingleReminderEntry.COLUMN_DAY + " INTEGER NOT NULL, " +
                SingleReminderEntry.COLUMN_HOUR + " INTEGER NOT NULL, " +
                SingleReminderEntry.COLUMN_MINUTE + " INTEGER NOT NULL, " +
                SingleReminderEntry.COLUMN_SCHEDULE_ID + " TEXT NOT NULL, " +
                SingleReminderEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

                db.execSQL(SQL_CREATE_REMINDER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SingleReminderEntry.TABLE_NAME);
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
