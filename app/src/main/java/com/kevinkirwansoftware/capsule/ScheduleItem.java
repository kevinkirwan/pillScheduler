package com.kevinkirwansoftware.capsule;

import android.view.View;

import java.util.UUID;

public class ScheduleItem {
    private String mReminderName;
    private String mReminderDescription;
    private ReminderType mReminderType;
    private ActivationType mActivationType;
    private boolean menuVisible;
    private boolean isActivated;
    private String scheduleID;
    private int dbCode1;
    private int dbCode2;
    private int dbCode3;
    private int dbCode4;

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

    public int getReminderTypeInt(){
        if(mReminderType == ReminderType.ONE_TIME){
            return 0;
        } else if(mReminderType == ReminderType.RECURRING){
            return 1;
        } else{
            return -1;
        }
    }

    public int getActivationTypeInt(){
        if(mActivationType == ActivationType.NOT_ACTIVATED){
            return 0;
        } else if(mActivationType == ActivationType.ACTIVATED){
            return 1;
        } else{
            return -1;
        }

    }

    public String getScheduleID(){
        return scheduleID;
    }

    public int getDbCode1(){ return dbCode1;}

    public void setDbCode1(int code){
        dbCode1 = code;
    }

    public int getDbCode2(){ return dbCode2;}

    public void setDbCode2(int code){
        dbCode2 = code;
    }

    public int getDbCode3(){ return dbCode3;}

    public void setDbCode3(int code){
        dbCode3 = code;
    }

    public int getDbCode4(){ return dbCode4;}

    public void setDbCode4(int code){
        dbCode4 = code;
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

    public ActivationType getActivationType(){
        return mActivationType;
    }

    public void setActivationType(ActivationType activationType){
        if(activationType == ActivationType.ACTIVATED){
            isActivated = true;
        } else {
            isActivated = false;
        }
        mActivationType = activationType;
    }

    public void setActive(boolean isActive){
        if(isActive){
            isActivated = true;
            mActivationType = ActivationType.ACTIVATED;
        } else {
            isActivated = false;
            mActivationType = ActivationType.NOT_ACTIVATED;
        }
    }

    public boolean isActive(){
        return isActivated;
    }

    public String getActiveString(){
        if(isActivated){
            return "Active";
        } else {
            return "Inactive";
        }
    }

    public enum ActivationType{
        NOT_ACTIVATED,
        ACTIVATED
    }

    public enum ReminderType{
        ONE_TIME,
        RECURRING,
        NONE
    }






}
