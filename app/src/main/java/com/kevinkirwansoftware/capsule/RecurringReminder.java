package com.kevinkirwansoftware.capsule;

import android.content.Context;
import android.util.Log;

import com.kevinkirwansoftware.capsule.general.ApplicationPreferences;
import com.kevinkirwansoftware.capsule.general.ApplicationTools;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class RecurringReminder extends ScheduleItem {
    public boolean mDaily;
    private int mDailyReminders;
    private int[][] mMultiRemindersArray;
    private Calendar calendar1, calendar2, calendar3, calendar4;

    public RecurringReminder(){
        super();
    }

    public int[][] getMultiRemindersArray(){
        return mMultiRemindersArray;
    }

    public int getNumDailyReminders(){
        return mDailyReminders;
    }

    public String getNumDailyRemindersString(){
        String returnString;
        switch (mDailyReminders){
            case 1:
                returnString  = "One";
                break;
            case 2:
                returnString  = "Two";
                break;
            case 3:
                returnString  = "Three";
                break;
            case 4:
                returnString  = "Four";
                break;
            default:
                returnString  = "Unknown";
        }
        return returnString + "\nDaily\nReminders";
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

    public void setMultiRemindersArray(int[][] remindersArray, boolean fromDb){
        mMultiRemindersArray = remindersArray;
        setCalendars(fromDb);
    }

    private void setCalendars(boolean fromDb){
        int offset = 0;
        if(fromDb){
            offset = -1;
        }
        calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.HOUR_OF_DAY, mMultiRemindersArray[0][0]);
        calendar1.set(Calendar.MINUTE, mMultiRemindersArray[1][0] + offset);
        calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.HOUR_OF_DAY, mMultiRemindersArray[0][1]);
        calendar2.set(Calendar.MINUTE, mMultiRemindersArray[1][1] + offset);
        calendar3 = Calendar.getInstance();
        calendar3.set(Calendar.HOUR_OF_DAY, mMultiRemindersArray[0][2]);
        calendar3.set(Calendar.MINUTE, mMultiRemindersArray[1][2] + offset);
        calendar4 = Calendar.getInstance();
        calendar4.set(Calendar.HOUR_OF_DAY, mMultiRemindersArray[0][3]);
        calendar4.set(Calendar.MINUTE, mMultiRemindersArray[1][3] + offset);
    }

    public Calendar getCalendar1(){
        return calendar1;
    }

    public Calendar getCalendar2(){
        return calendar2;
    }

    public Calendar getCalendar3(){
        return calendar3;
    }

    public Calendar getCalendar4(){
        return calendar4;
    }

    public String getTypeString(){
        return "Recurring";
    }

    public void recurringReminderInit(Context context){
        setScheduleID(UUID.randomUUID().toString());
        setDbCode1((int) Math.floor(Math.random() * ApplicationTools.MAXIMUM_POSITIVE_INT));
        setDbCode2((int) Math.floor(Math.random() * ApplicationTools.MAXIMUM_POSITIVE_INT));
        setDbCode3((int) Math.floor(Math.random() * ApplicationTools.MAXIMUM_POSITIVE_INT));
        setDbCode4((int) Math.floor(Math.random() * ApplicationTools.MAXIMUM_POSITIVE_INT));
        List<TimePair> timeList = new ArrayList<>();
        ApplicationPreferences.setLatencyList(context, getScheduleID(), timeList);

    }

    public String getFistTimeAsString(){
        boolean is24Hour = ApplicationPreferences.is24Hour();;
        String returnString;
        Timestamp ts = new java.sql.Timestamp(calendar1.getTimeInMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd");
        if(is24Hour){
            formatter = new SimpleDateFormat("HH:mm");
            returnString = formatter.format(ts);
        } else {
            String amPm;
            if(calendar1.get(Calendar.HOUR_OF_DAY) >= 12){
                amPm = " PM";
            } else {
                amPm = " AM";
            }
            formatter = new SimpleDateFormat("h:mm");
            returnString = formatter.format(ts) + amPm;
        }
        return returnString;
    }

    enum FrequencyType{
        EVERY_DAY,
        WEEKDAY,
        WEEKEND,
        CUSTOM
    }


}
