package com.kevinkirwansoftware.capsule.database;

import android.provider.BaseColumns;

public class RecurringReminderColumns {

    private RecurringReminderColumns(){}

    public static final class RecurringReminderEntry implements BaseColumns{
        public static final String TABLE_NAME = "recurringReminderList";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_TYPE = "type";

        // Only recurring reminders
        public static final String COLUMN_DAILY_REMINDERS = "daily_reminder";
        public static final String COLUMN_REMINDER_HOUR_ONE = "reminder_hour_one";
        public static final String COLUMN_REMINDER_MINUTE_ONE = "reminder_minute_one";
        public static final String COLUMN_REMINDER_HOUR_TWO = "reminder_hour_two";
        public static final String COLUMN_REMINDER_MINUTE_TWO = "reminder_minute_two";
        public static final String COLUMN_REMINDER_HOUR_THREE = "reminder_hour_three";
        public static final String COLUMN_REMINDER_MINUTE_THREE = "reminder_minute_three";
        public static final String COLUMN_REMINDER_HOUR_FOUR = "reminder_hour_four";
        public static final String COLUMN_REMINDER_MINUTE_FOUR = "reminder_minute_four";

        // Only single reminders
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_MONTH = "month";
        public static final String COLUMN_DAY = "day";
        public static final String COLUMN_HOUR = "hour";
        public static final String COLUMN_MINUTE = "minute";

        public static final String COLUMN_ACTIVATED = "activated";

        public static final String COLUMN_SCHEDULE_ID = "schedule_id";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
