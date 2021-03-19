package com.kevinkirwansoftware.capsule;

import android.util.Log;

import java.time.LocalTime;
import java.util.ArrayList;

public class RecurringReminder extends ScheduleItem {
    public boolean mDaily;
    private int mDailyReminders;
    private int[][] mMultiRemindersArray;

    public RecurringReminder(){
        super();
    }

    public int[][] getMultiRemindersArray(){
        return mMultiRemindersArray;
    }

    public int getNumDailyReminders(){
        return mDailyReminders;
    }

    public boolean isDaily(){
        return mDaily;
    }

    public void setDaily(boolean daily){
        mDaily = daily;
    }

    public void setDailyReminders(int reminders){
        mDailyReminders = reminders;
    }

    public void setMultiRemindersArray(int[][] remindersArray){
        Log.d("Kevin", "Length: " + remindersArray[0].length);
        for(int i = 0; i < remindersArray[0].length; i++){
            Log.d("Kevin", "i: " + i + " val: " + remindersArray[0][i]);
        }
    }

    enum FrequencyType{
        EVERY_DAY,
        WEEKDAY,
        WEEKEND,
        CUSTOM
    }


}
