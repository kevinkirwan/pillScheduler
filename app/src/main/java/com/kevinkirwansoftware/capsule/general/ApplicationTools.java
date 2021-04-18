package com.kevinkirwansoftware.capsule.general;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.common.api.Api;
import com.kevinkirwansoftware.capsule.R;
import com.kevinkirwansoftware.capsule.RecurringReminder;
import com.kevinkirwansoftware.capsule.ReminderCheckJobService;
import com.kevinkirwansoftware.capsule.WakeUpActivity;
import com.kevinkirwansoftware.capsule.database.RecurringReminderColumns;
import com.kevinkirwansoftware.capsule.SingleReminder;
import com.kevinkirwansoftware.capsule.notifications.ReminderBroadcast;
import com.kevinkirwansoftware.capsule.notifications.ThrowawayBroadcast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ApplicationTools {
    public static String TAG = "ApplicationTools.java";

    // Private static variables
    private static String WEATHER_URL = "https://api.openweathermap.org/data/2.5/";
    private static String POST_MOCK_URL_BOOKS = "https://www.googleapis.com/books/v1/";
    private static String POST_MOCK_URL_LATIN = "https://jsonplaceholder.typicode.com/";
    private static String POST_MOCK_URL_NEWS = "https://newsapi.org/v2/";
    private static final int SERVICE_ID = 444;

    // Public static variables
    public static int MAXIMUM_POSITIVE_INT = 2147483647;
    public static int MAX_REMINDER_NAME_STRING_LENGTH = 30;
    public static int MAX_REMINDER_DESC_STRING_LENGTH = 100;
    public static String CHANNEL_ID = "notification";


    public static String getWeatherApiKey(){
        return ApiKeys.POST_WEATHER_API_KEY;
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
        return ApiKeys.POST_NEWS_API_KEY;
    }

    public static void showNotification(Context context, String titleText, String descriptionText, String notificationTag, int code){
        Log.d(TAG, "showNotification() Notification displayed...");
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        Log.d("Kevin", "AppTools " + titleText);

        // Initialize Collapsed View
        RemoteViews collapsedView = new RemoteViews(context.getPackageName(),
                R.layout.collapsed_notification);
        collapsedView.setTextViewText(R.id.notificationText1, titleText);
        collapsedView.setTextViewText(R.id.notificationText2, descriptionText);

        RemoteViews expandedView = new RemoteViews(context.getPackageName(),
                R.layout.expanded_notification);

        Intent intent = new Intent("Notification deleted");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, code, intent, 0);

        Notification notification = new NotificationCompat.Builder(context, App.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_capsule)
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView)
                .setDeleteIntent(pendingIntent)
                .build();
        notificationManager.notify(code, notification);
    }

    public static void showFsNotification(Context context, String titleText, String descriptionText, String notificationTag, int code){
        Log.d(TAG, "showNotification() Notification displayed...");
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        Log.d("Kevin", "AppTools " + titleText);

        // Initialize Collapsed View
        RemoteViews collapsedView = new RemoteViews(context.getPackageName(),
                R.layout.collapsed_notification);
        collapsedView.setTextViewText(R.id.notificationText1, titleText);
        collapsedView.setTextViewText(R.id.notificationText2, descriptionText);

        RemoteViews expandedView = new RemoteViews(context.getPackageName(),
                R.layout.expanded_notification);

        Intent intent = new Intent("Notification deleted");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, code, intent, 0);

        Intent fullScreenIntent = new Intent(context, WakeUpActivity.class);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(context, App.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_capsule)
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView)
                .setDeleteIntent(pendingIntent)
                .setFullScreenIntent(fullScreenPendingIntent, true)
                .build();
        notificationManager.notify(code, notification);
    }

    public static void scheduleJobService(Context context){
        ComponentName componentName = new ComponentName(context, ReminderCheckJobService.class);
        JobInfo info = new JobInfo.Builder(SERVICE_ID, componentName)
                .setPersisted(true)
                .setPeriodic(15*60*1000)
                .build();
        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        assert scheduler != null;
        int resultCode = scheduler.schedule(info);
        if(resultCode == JobScheduler.RESULT_SUCCESS){
            Log.d(TAG, "ReminderCheckJobService started...");
        } else {
            Log.d(TAG, "ReminderCheckJobService failed.");
        }
    }

    public static void cancelJobService(Context context){
        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        assert scheduler != null;
        scheduler.cancel(SERVICE_ID);
        Log.d(TAG, "Job cancelled.");
    }

    private void startForeground(){

    }

    //TODO get icons from internet
    public static int getWeatherDrawable(String desc){
        if(desc.contains("cloud")){
            return R.drawable.ic_cloud;
        } else if(desc.contains("sun") || desc.contains("clear")){
            if((Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 20) || (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 6)){
                return R.drawable.ic_moon;
            } else {
                return R.drawable.ic_sun;
            }
        } else if(desc.contains("rain")){
            return R.drawable.ic_rain;
        } else if(desc.contains("snow")){
            return R.drawable.ic_snow;
        } else {
            return R.drawable.ic_cloud;
        }
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

    public static String weatherDescriptionFormatted(String lcString){
        char[] charArray = lcString.toCharArray();
        boolean foundSpace = true;

        for(int i = 0; i < charArray.length; i++) {

            if(Character.isLetter(charArray[i])) {
                if(foundSpace) {
                    charArray[i] = Character.toUpperCase(charArray[i]);
                    foundSpace = false;
                }
            }

            else {
                foundSpace = true;
            }
        }

        String ucString = String.valueOf(charArray);
        return ucString;
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

    public static Intent broadcastIntentGenerator(Context context,
                                                  String scheduleID,
                                                  String name,
                                                  String description,
                                                  int code){
        Intent intent = new Intent(context, ThrowawayBroadcast.class);
        String tag = scheduleID;
        intent.setAction(tag);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("tag", tag);
        intent.putExtra("title" + tag, name);
        intent.putExtra("desc" + tag, description);
        intent.putExtra("code", code);
        Log.d("Kevin", "Title: " + name + " Code: " + code);
        return intent;

    }

    public static Intent broadcastIntentGeneratorFs(Context context,
                                                  String scheduleID,
                                                  String name,
                                                  String description,
                                                  int code){
        Intent intent = new Intent(context, ThrowawayBroadcast.class);
        String tag = scheduleID;
        intent.setAction(tag);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("tag", tag);
        return intent;

    }

    public static ContentValues setRecurringReminderCV(RecurringReminder holderItem){
        ContentValues cv = new ContentValues();
        int[][] multiRemindersArray = holderItem.getMultiRemindersArray();
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_NAME, holderItem.getReminderName());
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DESCRIPTION, holderItem.getReminderDescription());
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_TYPE, holderItem.getReminderTypeInt());
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_ACTIVATED, holderItem.getActivationTypeInt());
        Log.d("Kevin", "setRecurringReminderCV: " + multiRemindersArray[1][0]);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DAILY_REMINDERS, holderItem.getNumDailyReminders());
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_HOUR_ONE, multiRemindersArray[0][0]);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_MINUTE_ONE, multiRemindersArray[1][0]+1);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_HOUR_TWO, multiRemindersArray[0][1]);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_MINUTE_TWO, multiRemindersArray[1][1]+1);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_HOUR_THREE, multiRemindersArray[0][2]);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_MINUTE_THREE, multiRemindersArray[1][2]+1);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_HOUR_FOUR, multiRemindersArray[0][3]);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_MINUTE_FOUR, multiRemindersArray[1][3]+1);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_YEAR, -1);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_MONTH, -1);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DAY, -1);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_HOUR, -1);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_MINUTE, -1);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_SCHEDULE_ID, holderItem.getScheduleID());
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DB_CODE_1, holderItem.getDbCode1());
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DB_CODE_2, holderItem.getDbCode2());
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DB_CODE_3, holderItem.getDbCode3());
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DB_CODE_4, holderItem.getDbCode4());
        return cv;
    }

    public static ContentValues setSingleReminderCV(SingleReminder holderItem){
        ContentValues cv = new ContentValues();
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_NAME, holderItem.getReminderName());
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DESCRIPTION, holderItem.getReminderDescription());
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_TYPE, holderItem.getReminderTypeInt());
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_ACTIVATED, holderItem.getActivationTypeInt());
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
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_MONTH, holderItem.getReminderCalendar().get(Calendar.MONTH)+1);
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DAY, holderItem.getReminderCalendar().get(Calendar.DAY_OF_MONTH));
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_HOUR, holderItem.getReminderCalendar().get(Calendar.HOUR_OF_DAY));
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_MINUTE, holderItem.getReminderCalendar().get(Calendar.MINUTE)+1);
        Log.d("Kevin", "setSingleReminderCV: " + holderItem.getReminderCalendar().get(Calendar.MINUTE));
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_SCHEDULE_ID, holderItem.getScheduleID());
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DB_CODE_1, holderItem.getDbCode1());
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DB_CODE_2, holderItem.getDbCode2());
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DB_CODE_3, holderItem.getDbCode3());
        cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DB_CODE_4, holderItem.getDbCode4());
        return cv;
    }



}
