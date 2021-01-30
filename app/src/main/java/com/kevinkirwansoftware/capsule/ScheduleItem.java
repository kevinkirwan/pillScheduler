package com.kevinkirwansoftware.capsule;

import android.view.View;

import java.util.UUID;

public class ScheduleItem {
    private String mReminderName;
    private String mReminderDescription;
    private ReminderType mReminderType;
    private int mDailyReminders;
    private boolean menuVisible;
    private String scheduleID;

    public String getReminderName(){
        return mReminderName;
    }

    public String getReminderDescription(){
        return mReminderDescription;
    }

    public int getNumDailyReminders(){
        return mDailyReminders;
    }

    public ReminderType getReminderType(){
        return mReminderType;
    }

    public String getTypeString(){
        if(mReminderType == ReminderType.ONE_TIME){
            return "One-Time";
        } else if(mReminderType == ReminderType.RECURRING){
            return "Recurring";
        } else{
            return "None";
        }
    }

    public String getScheduleID(){
        return scheduleID;
    }

    public void setReminderName(String name){
        mReminderName = name;
    }

    public void setReminderDescription(String description){
        mReminderDescription = description;
    }

    public void setReminderType(ReminderType rt){
        mReminderType = rt;
    }

    public void setDailyReminders(int reminders){
        mDailyReminders = reminders;
    }


    public void setMenuVisible(boolean visible){
        menuVisible = visible;
    }

    public void setScheduleID(String id){
        scheduleID = id;
    }

    public boolean isMenuVisible(){
        return menuVisible;
    }

    public enum ReminderType{
        ONE_TIME,
        RECURRING,
        NONE
    }






}
