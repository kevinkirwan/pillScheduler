package com.kevinkirwansoftware.capsule;

import com.kevinkirwansoftware.capsule.general.ApplicationPreferences;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimePair {
    long scheduledTime;
    long dismissedTime;

    public TimePair(long scheduledTime, long dismissedTime){
        this.scheduledTime = scheduledTime;
        this.dismissedTime = dismissedTime;
    }

    public long getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(long scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public long getDismissedTime() {
        return dismissedTime;
    }

    public void setDismissedTime(long dismissedTime) {
        this.dismissedTime = dismissedTime;
    }

    public Calendar getScheduledTimeCalendar(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(scheduledTime);
        return calendar;
    }

    public Calendar getDismissedTimeCalendar(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dismissedTime);
        return calendar;
    }

    public String getDismissedTimeString(){
        Timestamp ts = new java.sql.Timestamp(getDismissedTimeCalendar().getTimeInMillis());
        SimpleDateFormat timeFormat;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");
        String returnString;
        if(ApplicationPreferences.is24Hour()){
            timeFormat = new SimpleDateFormat("HH:mm");
            returnString = dateFormat.format(ts) + " " + timeFormat.format(ts);
        } else {
            String amPm;
            if(getDismissedTimeCalendar().get(Calendar.HOUR_OF_DAY) >= 12){
                amPm = " PM";
            } else {
                amPm = " AM";
            }
            timeFormat = new SimpleDateFormat("h:mm");
            returnString = dateFormat.format(ts) + " " + timeFormat.format(ts) + amPm;
        }
        return returnString;
    }

    public String getScheduledTimeString(){
        Timestamp ts = new java.sql.Timestamp(getScheduledTimeCalendar().getTimeInMillis());
        SimpleDateFormat timeFormat;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");
        String returnString;
        if(ApplicationPreferences.is24Hour()){
            timeFormat = new SimpleDateFormat("HH:mm");
            returnString = dateFormat.format(ts) + " " + timeFormat.format(ts);
        } else {
            String amPm;
            if(getScheduledTimeCalendar().get(Calendar.HOUR_OF_DAY) >= 12){
                amPm = " PM";
            } else {
                amPm = " AM";
            }
            timeFormat = new SimpleDateFormat("h:mm");
            returnString = dateFormat.format(ts) + " " + timeFormat.format(ts) + amPm;
        }
        return returnString;
    }

    public int getTimeDifferenceMinutes(){
        return (int) Math.floor((dismissedTime-scheduledTime)/(1000.0*60));
    }


    //public
}
