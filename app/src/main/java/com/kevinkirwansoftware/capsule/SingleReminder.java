package com.kevinkirwansoftware.capsule;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.time.*;
import java.util.Calendar;
import java.util.Date;

public class SingleReminder extends ScheduleItem {
    private int hour, minute;
    private Date date;
    private Calendar reminderCalendar;

    SingleReminder(int minute, int hour, Date date){
        this.minute = minute;
        this.hour = hour;
        this.date = date;
        reminderCalendar = Calendar.getInstance();
    }

    public int getMinute(){
        return minute;
    }

    public int getHour(){
        return hour;
    }

    public Date getDate() {
        return date;
    }
}








