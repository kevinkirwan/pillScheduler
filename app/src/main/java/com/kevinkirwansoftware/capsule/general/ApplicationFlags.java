package com.kevinkirwansoftware.capsule.general;

import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

public class ApplicationFlags {
    // Reminder Items database update flags
    private static boolean reminderDatasetItemChanged = false;
    private static boolean reminderDatasetItemAdded = false;
    private static boolean reminderDatasetItemRemoved = false;
    private static boolean reminderDatasetNeedsUpdate = false;
    private static ArrayList<String> remindersChangedList;
    private static ArrayList<String> remindersAddedList;
    private static ArrayList<String> remindersRemovedList;

    // Units changed flags
    private static boolean unitPreferencesChangedFlag = false;

    public static boolean getReminderDatasetItemChanged(){
        return reminderDatasetItemChanged;
    }

    public static boolean getReminderDatasetItemAdded(){
        return reminderDatasetItemAdded;
    }

    public static boolean getReminderDatasetItemRemoved(){
        return reminderDatasetItemRemoved;
    }

    public static boolean getReminderDatasetNeedsUpdate(){
        return reminderDatasetNeedsUpdate;
    }

    public static void setReminderDatasetItemChangedFlag(String reminderID){
        reminderDatasetItemChanged = true;
        reminderDatasetNeedsUpdate = true;
        if(remindersChangedList == null){
            remindersChangedList = new ArrayList<>();
        }
        remindersChangedList.add(reminderID);
    }

    public static void setReminderDatasetItemAddedFlag(String reminderID){
        reminderDatasetItemAdded = true;
        reminderDatasetNeedsUpdate = true;
        if(remindersAddedList == null){
            remindersAddedList = new ArrayList<>();
        }
        remindersAddedList.add(reminderID);
    }

    public static void setReminderDatasetItemRemovedFlag(String reminderID){
        reminderDatasetItemRemoved = true;
        reminderDatasetNeedsUpdate = true;
        if(remindersRemovedList == null){
            remindersRemovedList = new ArrayList<>();
        }
        remindersRemovedList.add(reminderID);
        Log.d("Kevin", "AppFlag removed: " + reminderID + " arraylist: " + remindersRemovedList.get(remindersRemovedList.size()-1));
    }

    public static void resetReminderDatasetFlags(){
        reminderDatasetItemChanged = false;
        reminderDatasetItemRemoved = false;
        reminderDatasetItemAdded = false;
        remindersChangedList = null;
        remindersRemovedList = null;
        remindersAddedList = null;
    }

    public static ArrayList<String> getRemindersChangedList(){
        return remindersChangedList;
    }

    public static ArrayList<String> getRemindersAddedList(){
        return remindersAddedList;
    }

    public static ArrayList<String> getRemindersRemovedList(){
        return remindersRemovedList;
    }

    public static void setUnitPreferencesChangedFlag(){
        unitPreferencesChangedFlag = true;
    }

    public static void resetUnitPreferencesChangedFlag(){
        unitPreferencesChangedFlag = false;
    }

    public static boolean isUnitPreferencesChangedFlagSet(){
        return unitPreferencesChangedFlag;
    }
}
