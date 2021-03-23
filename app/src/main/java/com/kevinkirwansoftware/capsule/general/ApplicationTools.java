package com.kevinkirwansoftware.capsule.general;

import android.app.Notification;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.kevinkirwansoftware.capsule.R;
import com.kevinkirwansoftware.capsule.RecurringReminder;
import com.kevinkirwansoftware.capsule.database.RecurringReminderColumns;
import com.kevinkirwansoftware.capsule.SingleReminder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ApplicationTools {
    public static String TAG = "ApplicationTools.java";
    public static int MAX_REMINDER_NAME_STRING_LENGTH = 30;
    public static int MAX_REMINDER_DESC_STRING_LENGTH = 100;
    public static String CHANNEL_ID = "notification";
    private static String APPLICATION_ID = "com.kevinkirwansoftware.capsule";
    private static String WEATHER_URL = "http://api.openweathermap.org/";
    private static String POST_MOCK_URL_BOOKS = "https://www.googleapis.com/books/v1/";
    private static String POST_MOCK_URL_LATIN = "https://jsonplaceholder.typicode.com/";
    private static String POST_MOCK_URL_NEWS = "https://newsapi.org/v2/";
    private static String POST_NEWS_API_KEY = "0e340a8de1494e5f87634a3506ee3300";


    public static String getApplicationId(){
        return APPLICATION_ID;
    }

    public static String getWeatherUrl(){
        return WEATHER_URL;
    }

    public static String getMockLatinDataUrl(){
        return POST_MOCK_URL_LATIN;
    }

    public static String getMockDataUrl(){
        return POST_MOCK_URL_BOOKS;
    }

    public static String getNewsUrl(){
        return POST_MOCK_URL_NEWS;
    }

    public static String getNewsApiKey() {
        return POST_NEWS_API_KEY;
    }

    public static void showNotification(Context context){
        Log.d(TAG, "showNotification() Notification displayed...");
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        RemoteViews collapsedView = new RemoteViews(context.getPackageName(),
                R.layout.collapsed_notification);
        RemoteViews expandedView = new RemoteViews(context.getPackageName(),
                R.layout.expanded_notification);

        Notification notification = new NotificationCompat.Builder(context, App.CHANNEL_ID)
                .setSmallIcon(R.drawable.capsule)
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView)
                .build();
        notificationManager.notify(1, notification);
    }

    public static String getDateForApiCall(){
        SimpleDateFormat twoCharDateFormat = new SimpleDateFormat("YYYY-MM-dd");
        String day = twoCharDateFormat.format(new Date());
        Log.d("Kevin", "Date for API:" + day + ":");
        return day;
    }

    public static String getDateData(){
        SimpleDateFormat twoCharDateFormat = new SimpleDateFormat("MMMM dd");
        String day = twoCharDateFormat.format(new Date());
        return day;
    }

    public static String getTimeZoneData(){
        TimeZone zone = TimeZone.getDefault();
        String timeZoneRaw = zone.getID();
        String cityTemp = timeZoneRaw.substring(timeZoneRaw.lastIndexOf("/") + 1);
        cityTemp = cityTemp.replaceAll("_", " ");
        String displayNameTemp = zone.getDisplayName();
        String timeZoneFormatted = cityTemp + " - " + displayNameTemp;
        return timeZoneFormatted;
    }


    public static boolean isSQLiteStringValid(String inputString){
        String[] checker = getListOfInvalidSQLiteChars();
        for (int i = 0; i < checker.length; i++){
            if(inputString.contains(checker[i])){
                return false;
            }
        }
        return true;
    }

    public static String[] getListOfInvalidSQLiteChars(){
        String[] listOfChars = new String[] {"\\",  "/",  ":", "*",  "?", "\"", "<", ">", "|", "\'", "&"};
        return listOfChars;
    }

    public static ContentValues setRecurringReminderCV(RecurringReminder holderItem){
        ContentValues cv = new ContentValues();
        int[][] multiRemindersArray = holderItem.getMultiRemindersArray();
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_NAME, holderItem.getReminderName());
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DESCRIPTION, holderItem.getReminderDescription());
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_TYPE, holderItem.getTypeInt());
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DAILY_REMINDERS, holderItem.getNumDailyReminders());
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_HOUR_ONE, multiRemindersArray[0][0]);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_MINUTE_ONE, multiRemindersArray[1][0]);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_HOUR_TWO, multiRemindersArray[0][1]);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_MINUTE_TWO, multiRemindersArray[1][1]);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_HOUR_THREE, multiRemindersArray[0][2]);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_MINUTE_THREE, multiRemindersArray[1][2]);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_HOUR_FOUR, multiRemindersArray[0][3]);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_MINUTE_FOUR, multiRemindersArray[1][3]);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_YEAR, -1);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_MONTH, -1);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DAY, -1);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_HOUR, -1);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_MINUTE, -1);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_SCHEDULE_ID, holderItem.getScheduleID());
        return cv;
    }

    public static ContentValues setSingleReminderCV(SingleReminder holderItem){
        ContentValues cv = new ContentValues();
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_NAME, holderItem.getReminderName());
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DESCRIPTION, holderItem.getReminderDescription());
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_TYPE, holderItem.getTypeInt());
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DAILY_REMINDERS, -1);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_HOUR_ONE, -1);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_MINUTE_ONE, -1);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_HOUR_TWO, -1);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_MINUTE_TWO, -1);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_HOUR_THREE, -1);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_MINUTE_THREE, -1);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_HOUR_FOUR, -1);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_MINUTE_FOUR, -1);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_YEAR, holderItem.getReminderCalendar().get(Calendar.YEAR));
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_MONTH, holderItem.getReminderCalendar().get(Calendar.MONTH));
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DAY, holderItem.getReminderCalendar().get(Calendar.DAY_OF_MONTH));
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_HOUR, holderItem.getReminderCalendar().get(Calendar.HOUR_OF_DAY));
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_MINUTE, holderItem.getReminderCalendar().get(Calendar.MINUTE));
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_SCHEDULE_ID, holderItem.getScheduleID());
        return cv;
    }

}
