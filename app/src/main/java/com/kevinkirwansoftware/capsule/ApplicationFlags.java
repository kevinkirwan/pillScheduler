package com.kevinkirwansoftware.capsule;

import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

public class ApplicationFlags {
    private static boolean ReminderDatasetItemChanged = false;
    private static boolean ReminderDatasetItemAdded = false;
    private static boolean ReminderDatasetItemRemoved = false;
    private static boolean ReminderDatasetNeedsUpdate = false;
    private static ArrayList<String> RemindersChangedList;
    private static ArrayList<String> RemindersAddedList;
    private static ArrayList<String> RemindersRemovedList;

    public static boolean GetReminderDatasetItemChanged(){
        return ReminderDatasetItemChanged;
    }

    public static boolean GetReminderDatasetItemAdded(){
        return ReminderDatasetItemAdded;
    }

    public static boolean GetReminderDatasetItemRemoved(){
        return ReminderDatasetItemRemoved;
    }

    public static boolean GetReminderDatasetNeedsUpdate(){
        return ReminderDatasetNeedsUpdate;
    }

    public static void SetReminderDatasetItemChangedFlag(String reminderID){
        ReminderDatasetItemChanged = true;
        ReminderDatasetNeedsUpdate = true;
        if(RemindersChangedList == null){
            RemindersChangedList = new ArrayList<>();
        }
        RemindersChangedList.add(reminderID);
    }

    public static void SetReminderDatasetItemAddedFlag(String reminderID){
        ReminderDatasetItemAdded = true;
        ReminderDatasetNeedsUpdate = true;
        if(RemindersAddedList == null){
            RemindersAddedList = new ArrayList<>();
        }
        RemindersAddedList.add(reminderID);
    }

    public static void SetReminderDatasetItemRemovedFlag(String reminderID){
        ReminderDatasetItemRemoved = true;
        ReminderDatasetNeedsUpdate = true;
        if(RemindersRemovedList == null){
            RemindersRemovedList = new ArrayList<>();
        }
        RemindersRemovedList.add(reminderID);
        Log.d("Kevin", "AppFlag removed: " + reminderID + " arraylist: " + RemindersRemovedList.get(RemindersRemovedList.size()-1));
    }

    public static void ResetReminderDatasetFlags(){
        ReminderDatasetItemChanged = false;
        ReminderDatasetItemRemoved = false;
        ReminderDatasetItemAdded = false;
        RemindersChangedList = null;
        RemindersRemovedList = null;
        RemindersAddedList = null;
    }

    public static ArrayList<String> GetRemindersChangedList(){
        return RemindersChangedList;
    }

    public static ArrayList<String> GetRemindersAddedList(){
        return RemindersAddedList;
    }

    public static ArrayList<String> GetRemindersRemovedList(){
        return RemindersRemovedList;
    }
}
