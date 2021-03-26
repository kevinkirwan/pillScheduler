package com.kevinkirwansoftware.capsule;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;

public class SingleReminder extends ScheduleItem {
    private Calendar reminderCalendar;

    public SingleReminder(int year, int month, int day, int hour, int minute){
        reminderCalendar = Calendar.getInstance();
        reminderCalendar.set(Calendar.MINUTE, minute - 1);
        reminderCalendar.set(Calendar.HOUR_OF_DAY, hour);
        reminderCalendar.set(Calendar.DAY_OF_MONTH, day);
        reminderCalendar.set(Calendar.MONTH, month - 1);
        reminderCalendar.set(Calendar.YEAR, year);
    }

    public SingleReminder(Date date){
        reminderCalendar = Calendar.getInstance();
        reminderCalendar.setTime(date);
    }

    public int getMinute() {
        return reminderCalendar.get(Calendar.MINUTE);
    }

    public int getHour() {
        return reminderCalendar.get(Calendar.HOUR_OF_DAY);
    }

    public int getDay() {
        return reminderCalendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getMonth() {
        return reminderCalendar.get(Calendar.MONTH);
    }

    public int getYear() {
        return reminderCalendar.get(Calendar.YEAR);
    }

    public Calendar getReminderCalendar(){
        return reminderCalendar;
    }

    public String getDateSingleAsString(){
        Timestamp ts = new java.sql.Timestamp(reminderCalendar.getTimeInMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd");
        return formatter.format(ts);
    }

    public String getTimeSingleAsString(){
        boolean is24Hour = false;
        String returnString;
        Timestamp ts = new java.sql.Timestamp(reminderCalendar.getTimeInMillis());
        SimpleDateFormat formatter;
        if(is24Hour){
            formatter = new SimpleDateFormat("HH:mm");
            returnString = formatter.format(ts);
        } else {
            String amPm;
            if(reminderCalendar.get(Calendar.HOUR_OF_DAY) >= 12){
                amPm = " PM";
            } else {
                amPm = " AM";
            }
            formatter = new SimpleDateFormat("h:mm");
            returnString = formatter.format(ts) + amPm;
        }
        return returnString;
    }

    public String getTypeString(){
        return "One-Time";
    }
}








