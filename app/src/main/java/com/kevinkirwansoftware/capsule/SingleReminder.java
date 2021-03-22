package com.kevinkirwansoftware.capsule;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.time.*;
import java.util.Calendar;
import java.util.Date;

public class SingleReminder extends ScheduleItem {
    private Calendar reminderCalendar;

    SingleReminder(int year, int month, int day, int hour, int minute){
        reminderCalendar = Calendar.getInstance();
        reminderCalendar.set(Calendar.MINUTE, minute);
        reminderCalendar.set(Calendar.HOUR, hour);
        reminderCalendar.set(Calendar.DAY_OF_MONTH, day);
        reminderCalendar.set(Calendar.MONTH, month);
        reminderCalendar.set(Calendar.YEAR, year);
    }

    public Calendar getReminderCalendar(){
        return reminderCalendar;
    }
}








