package com.kevinkirwansoftware.capsule;

import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

public class ApplicationFlags {
    private static boolean reminderDatasetItemChanged = false;
    private static boolean reminderDatasetItemAdded = false;
    private static boolean reminderDatasetItemRemoved = false;
    private static boolean reminderDatasetNeedsUpdate = false;
    private static ArrayList<String> remindersChangedList;
    private static ArrayList<String> remindersAddedList;
    private static ArrayList<String> remindersRemovedList;

    public static boolean GetReminderDatasetItemChanged(){
        return reminderDatasetItemChanged;
    }

    public static boolean GetReminderDatasetItemAdded(){
        return reminderDatasetItemAdded;
    }

    public static boolean GetReminderDatasetItemRemoved(){
        return reminderDatasetItemRemoved;
    }

    public static boolean GetReminderDatasetNeedsUpdate(){
        return reminderDatasetNeedsUpdate;
    }

    public static void SetReminderDatasetItemChangedFlag(String reminderID){
        reminderDatasetItemChanged = true;
        reminderDatasetNeedsUpdate = true;
        if(remindersChangedList == null){
            remindersChangedList = new ArrayList<>();
        }
        remindersChangedList.add(reminderID);
    }

    public static void SetReminderDatasetItemAddedFlag(String reminderID){
        reminderDatasetItemAdded = true;
        reminderDatasetNeedsUpdate = true;
        if(remindersAddedList == null){
            remindersAddedList = new ArrayList<>();
        }
        remindersAddedList.add(reminderID);
    }

    public static void SetReminderDatasetItemRemovedFlag(String reminderID){
        reminderDatasetItemRemoved = true;
        reminderDatasetNeedsUpdate = true;
        if(remindersRemovedList == null){
            remindersRemovedList = new ArrayList<>();
        }
        remindersRemovedList.add(reminderID);
        Log.d("Kevin", "AppFlag removed: " + reminderID + " arraylist: " + remindersRemovedList.get(remindersRemovedList.size()-1));
    }

    public static void ResetReminderDatasetFlags(){
        reminderDatasetItemChanged = false;
        reminderDatasetItemRemoved = false;
        reminderDatasetItemAdded = false;
        remindersChangedList = null;
        remindersRemovedList = null;
        remindersAddedList = null;
    }

    public static ArrayList<String> GetRemindersChangedList(){
        return remindersChangedList;
    }

    public static ArrayList<String> GetRemindersAddedList(){
        return remindersAddedList;
    }

    public static ArrayList<String> GetRemindersRemovedList(){
        return remindersRemovedList;
    }
}
