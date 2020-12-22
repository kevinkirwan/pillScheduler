package com.kevinkirwansoftware.capsule;

import java.time.LocalTime;
import java.util.ArrayList;

public class RecurringReminder extends ScheduleItem {
    boolean mDaily;
    int mTimesDay;
    ArrayList<LocalTime> dailyAlarms;

    public RecurringReminder(String name, String description, boolean daily, int timesDay){
        super();
        mTimesDay = timesDay;
        mDaily = daily;
    }

    enum FrequencyType{
        EVERY_DAY,
        WEEKDAY,
        WEEKEND,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY,
        CUSTOM
    }


}
