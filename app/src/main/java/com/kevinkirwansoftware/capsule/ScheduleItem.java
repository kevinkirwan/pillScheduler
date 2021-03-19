package com.kevinkirwansoftware.capsule;

import android.view.View;

import java.util.UUID;

public class ScheduleItem {
    private String mReminderName;
    private String mReminderDescription;
    private ReminderType mReminderType;
    private boolean menuVisible;
    private String scheduleID;

    public String getReminderName(){
        return mReminderName;
    }

    public String getReminderDescription(){
        return mReminderDescription;
    }

    public int[][] getMultiRemindersArray(){
        int[][] tempArray = new int[2][4];
        tempArray[0][0] = 1;
        tempArray[1][0] = 2;
        tempArray[0][1] = 3;
        tempArray[1][1] = 4;
        tempArray[0][2] = 5;
        tempArray[1][2] = 6;
        tempArray[0][3] = 7;
        tempArray[1][3] = 8;
        return tempArray;
    }

    public ReminderType getReminderType(){
        return mReminderType;
    }

    public int getTypeInt(){
        if(mReminderType == ReminderType.ONE_TIME){
            return 0;
        } else if(mReminderType == ReminderType.RECURRING){
            return 1;
        } else{
            return 2;
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
